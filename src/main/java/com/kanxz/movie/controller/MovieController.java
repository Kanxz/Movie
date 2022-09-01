package com.kanxz.movie.controller;


import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kanxz.movie.common.api.CommonResult;
import com.kanxz.movie.dto.MovieDTO;
import com.kanxz.movie.generator.entity.Movie;
import com.kanxz.movie.generator.entity.MovieDetail;
import com.kanxz.movie.generator.entity.MovieTag;
import com.kanxz.movie.generator.entity.Tag;
import com.kanxz.movie.service.MovieDetailService;
import com.kanxz.movie.service.MovieService;
import com.kanxz.movie.service.MovieTagService;
import com.kanxz.movie.service.TagService;
import com.kanxz.movie.spider.MovieSpider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 电影表 前端控制器
 * </p>
 *
 * @author kanxz
 * @since 2022-04-01
 */
@RestController
@RequestMapping("/movie")
@Slf4j
public class MovieController {
    @Autowired
    private MovieSpider movieSpider;
    @Autowired
    private MovieService movieService;
    @Autowired
    private MovieDetailService movieDetailService;
    @Autowired
    private TagService tagService;
    @Autowired
    private MovieTagService movieTagService;

    //    @GetMapping("/spider")
    public CommonResult doSpider() {
        movieSpider.doSpider();
        return CommonResult.success(null);
    }

    /**
     * 首页的最新电影
     */
    @GetMapping("/home/new")
    public CommonResult getHomeMovieNew() {
        Page<Movie> moviePage = new Page<>(1, 8);
        JSONObject movieList = movieService.getMovieListWithSort(moviePage, ".*", ".*", "release_time DESC");
        return CommonResult.success(movieList, "查询成功");
    }

    @GetMapping("/home/score")
    public CommonResult getHomeMovieScore() {
        Page<Movie> moviePage = new Page<>(1, 8);
        JSONObject movieList = movieService.getMovieListWithSort(moviePage, ".*", ".*", "score DESC");
        return CommonResult.success(movieList, "查询成功");
    }

    /**
     * 电影分类列表数据获取
     */
    @PostMapping("/category/movies")
    public CommonResult getCategoryMovies(@RequestBody Map reqParams) {
        int curPage = (int) reqParams.get("page");
        int pageSize = (int) reqParams.get("pageSize");
        String sortField = (String) reqParams.get("sortField");
        String sortMethod = (String) reqParams.get("sortMethod");
        String selectedTag = (String) reqParams.get("selectedTag");
        String selectedLan = (String) reqParams.get("selectedLan");

        if ("全部".equals(selectedLan)) selectedLan = ".*";
        if ("全部".equals(selectedTag)) selectedTag = ".*";

        Page<Movie> moviePage = new Page<>(curPage, pageSize);

        JSONObject result = null;
        // 默认排序
        if (sortField == null && sortMethod == null) {
            result = movieService.getMovieList(moviePage, selectedLan, selectedTag);

            // 发布时间排序
        } else if ("releaseTime".equals(sortField) && sortMethod == null) {
            result = movieService.getMovieListWithSort(moviePage, selectedLan, selectedTag,
                    "`release_time` DESC");

            // 评分排序
        } else if ("score".equals(sortField)) {
            result = movieService.getMovieListWithSort(moviePage, selectedLan, selectedTag,
                    "`score` " + sortMethod);
        }
        return CommonResult.success(result);
    }

    @GetMapping("/search")
    public CommonResult searchByName(@RequestParam("name") String name) {
        QueryWrapper<Movie> movieQueryWrapper = new QueryWrapper<>();
        movieQueryWrapper.like("name", name);
        List<Movie> movieList = movieService.list(movieQueryWrapper);
        return CommonResult.success(movieList);
    }

    /**
     * 猜你喜欢 需要登录
     */
    @PostMapping("/recommend")
    @PreAuthorize("hasAuthority('user')")
    public CommonResult getRecommendMovies(@RequestBody Map param) {
        log.debug(param.toString());
        String userId = (String) param.get("userId");
        int pageNum = (int) param.get("pageNum");
        JSONObject movieList = movieService.getRecommendMovieList(Long.valueOf(userId), pageNum);
        return CommonResult.success(movieList);
    }

    @PostMapping("/admin/getAll")
    @PreAuthorize("hasAuthority('movie')")
    public CommonResult getAllMovies(@RequestBody Map params) {
        String name = (String) params.get("query");
        int pageNum = (int) params.get("pageNum");
        if (name.equals("")) name = ".*";

        Page<MovieDTO> page = new Page<>(pageNum, 10);
        IPage<MovieDTO> iPage = movieService.getMovieInfoList(page, name);
        List<MovieDTO> movieDTOList = iPage.getRecords();
        long total = iPage.getTotal();

        JSONObject result = JSONUtil.createObj();
        result.putOnce("movieList", movieDTOList);
        result.putOnce("total", total);
        return CommonResult.success(result);
    }

    @GetMapping("/admin/getById")
    @PreAuthorize("hasAuthority('movie')")
    public CommonResult getDataById(@RequestParam("id") String id) {
        Movie movie = movieService.getById(id);
        String doubanId = movie.getDoubanId();

        QueryWrapper<MovieDetail> movieDetailQueryWrapper = new QueryWrapper<>();
        movieDetailQueryWrapper.eq("douban_id", doubanId);
        MovieDetail movieDetail = movieDetailService.getOne(movieDetailQueryWrapper);

        List<String> tags = movieDetailService.getTagsById(Long.valueOf(id));

        JSONObject result = JSONUtil.createObj();
        result.putOnce("movie", movie);
        result.putOnce("movieDetail", movieDetail);
        result.putOnce("tags", tags);

        return CommonResult.success(result);
    }

    @GetMapping("/admin/deleteById")
    @PreAuthorize("hasAuthority('movie')")
    public CommonResult deleteById(@RequestParam("id") String id) {
        String doubanId = movieService.getById(id).getDoubanId();
        movieService.removeById(id);

        QueryWrapper<MovieDetail> queryWrapper = new QueryWrapper<MovieDetail>().eq("douban_id", doubanId);
        movieDetailService.remove(queryWrapper);

        QueryWrapper<MovieTag> movieTagQueryWrapper = new QueryWrapper<>();
        movieTagQueryWrapper.eq("movie_id", id);
        movieTagService.remove(movieTagQueryWrapper);

        return CommonResult.success(null);
    }

    @PostMapping("/admin/add")
    @PreAuthorize("hasAuthority('movie')")
    public CommonResult addMovie(@RequestBody Map param) {
        String name = (String) param.get("name");
        String score = (String) param.get("score");
        String doubanId = (String) param.get("doubanId");
        String director = (String) param.get("director");
        String language = (String) param.get("language");
        String releaseTime = (String) param.get("releaseTime");
        String length = (String) param.get("length");
        String picUrl = (String) param.get("picUrl");
        String intro = (String) param.get("intro");
        List<String> newTags = (List<String>) param.get("newTags");

        // 插入movie
        Movie movie = new Movie();
        movie.setDoubanId(doubanId);
        movie.setName(name);
        movie.setPicUrl(picUrl);
        movie.setScore(Float.valueOf(score));
        movieService.save(movie);

        // 获取movieId
        QueryWrapper<Movie> queryWrapper = new QueryWrapper<Movie>().eq("douban_id", doubanId);
        Long movieId = movieService.getOne(queryWrapper).getId();

        // 插入movieDetail
        MovieDetail movieDetail = new MovieDetail();
        movieDetail.setDoubanId(doubanId);
        movieDetail.setName(name);
        movieDetail.setDirector(director);
        movieDetail.setLanguage(language);
        movieDetail.setReleaseTime(LocalDate.parse(releaseTime));
        movieDetail.setLength(Integer.valueOf(length));
        movieDetail.setIntro(intro);
        movieDetailService.save(movieDetail);

        // 获取所有标签
        List<String> allTags = tagService.getAllTags();

        // 插入标签
        for (String tagName : newTags) {
            Tag tag = new Tag();
            tag.setTagName(tagName);
            // 标签已经存在
            if (allTags.contains(tagName)) {
                QueryWrapper<Tag> tagQueryWrapper = new QueryWrapper<Tag>().eq("tag_name", tagName);
                Long tagId = tagService.getOne(tagQueryWrapper).getId();
                MovieTag movieTag = new MovieTag();
                movieTag.setMovieId(movieId);
                movieTag.setTagId(tagId);
                movieTagService.save(movieTag);
            } else { // 标签不存在
                tagService.save(tag);
                QueryWrapper<Tag> tagQueryWrapper = new QueryWrapper<Tag>().eq("tag_name", tagName);
                Long tagId = tagService.getOne(tagQueryWrapper).getId();
                MovieTag movieTag = new MovieTag();
                movieTag.setMovieId(movieId);
                movieTag.setTagId(tagId);
                movieTagService.save(movieTag);
            }
        }

        return CommonResult.success(null);
    }

    @PostMapping("/admin/edit")
    @PreAuthorize("hasAuthority('movie')")
    public CommonResult editMovie(@RequestBody Map param) {
        log.debug(String.valueOf(param));
        int id = (int) param.get("id");
        String name = (String) param.get("name");
        String score = (String) param.get("score");
        String doubanId = (String) param.get("doubanId");
        String director = (String) param.get("director");
        String language = (String) param.get("language");
        String releaseTime = (String) param.get("releaseTime");
        String length = (String) param.get("length");
        String picUrl = (String) param.get("picUrl");
        String intro = (String) param.get("intro");
        List<String> newTags = (List<String>) param.get("newTags");
        List<String> deletedTags = (List<String>) param.get("deletedTags");

        // 获取原本的豆瓣id
        String rawDoubanId = movieService.getById(id).getDoubanId();

        // 更新movie
        Movie movie = new Movie();
        movie.setDoubanId(doubanId);
        movie.setName(name);
        movie.setPicUrl(picUrl);
        movie.setScore(Float.valueOf(score));
        movie.setId((long) id);
        movieService.updateById(movie);


        // 更新movie-detail
        MovieDetail movieDetail = new MovieDetail();
        movieDetail.setDoubanId(doubanId);
        movieDetail.setName(name);
        movieDetail.setDirector(director);
        movieDetail.setLanguage(language);
        movieDetail.setReleaseTime(LocalDate.parse(releaseTime));
        movieDetail.setLength(Integer.valueOf(length));
        movieDetail.setIntro(intro);
        UpdateWrapper<MovieDetail> movieDetailUpdateWrapper = new UpdateWrapper<>();
        movieDetailUpdateWrapper.eq("douban_id", rawDoubanId);
        movieDetailService.update(movieDetail, movieDetailUpdateWrapper);

        // 获取所有标签
        List<String> allTags = tagService.getAllTags();

        // 更新tag
        for (String tagName : newTags) {
            Tag tag = new Tag();
            tag.setTagName(tagName);
            // 标签已经存在
            if (allTags.contains(tagName)) {
                QueryWrapper<Tag> tagQueryWrapper = new QueryWrapper<Tag>().eq("tag_name", tagName);
                Long tagId = tagService.getOne(tagQueryWrapper).getId();
                MovieTag movieTag = new MovieTag();
                movieTag.setMovieId((long) id);
                movieTag.setTagId(tagId);
                try {
                    movieTagService.save(movieTag);
                } catch (Exception e) {
                    log.debug(e.getMessage());
                }
            } else { // 标签不存在
                tagService.save(tag);
                QueryWrapper<Tag> tagQueryWrapper = new QueryWrapper<Tag>().eq("tag_name", tagName);
                Long tagId = tagService.getOne(tagQueryWrapper).getId();
                MovieTag movieTag = new MovieTag();
                movieTag.setMovieId((long) id);
                movieTag.setTagId(tagId);
                movieTagService.save(movieTag);
            }
        }

        // 删除tag
        for (String tagName : deletedTags) {
            if (allTags.contains(tagName)) {
                QueryWrapper<Tag> tagQueryWrapper = new QueryWrapper<Tag>().eq("tag_name", tagName);
                Long tagId = tagService.getOne(tagQueryWrapper).getId();

                QueryWrapper<MovieTag> movieTagQueryWrapper = new QueryWrapper<>();
                movieTagQueryWrapper.eq("tag_id", tagId).eq("movie_id", id);
                movieTagService.remove(movieTagQueryWrapper);
            }
        }

        return CommonResult.success(null);
    }
}
