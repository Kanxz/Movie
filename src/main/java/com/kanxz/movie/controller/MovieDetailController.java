package com.kanxz.movie.controller;


import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kanxz.movie.common.api.CommonResult;
import com.kanxz.movie.dto.UserTagDTO;
import com.kanxz.movie.generator.entity.*;
import com.kanxz.movie.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 电影详细信息表 前端控制器
 * </p>
 *
 * @author kanxz
 * @since 2022-04-01
 */
@RestController
@RequestMapping("/movie-detail")
@Slf4j
public class MovieDetailController {

    @Autowired
    private MovieDetailService movieDetailService;
    @Autowired
    private TagService tagService;
    @Autowired
    private MovieService movieService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private UserScoreService scoreService;
    @Autowired
    private UserTagService userTagService;


    /**
     * 获取筛选框数据
     */
    @GetMapping("/filter")
    public CommonResult getFilter() {
        List<String> allLanguages = movieDetailService.getAllLanguages();
        JSONObject result = JSONUtil.createObj();
        result.putOnce("language", allLanguages);

        List<String> allTags = tagService.getAllTags();
        result.putOnce("tag", allTags);

        return CommonResult.success(result);
    }

    @GetMapping("/getOne")
    public CommonResult getMovieDetailByDoubanId(@RequestParam("doubanId") String doubanId) {
        // 获取电影详情
        QueryWrapper<MovieDetail> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("douban_id", doubanId);
        MovieDetail movieDetail = movieDetailService.getOne(queryWrapper);

        // 获取电影ID
        QueryWrapper<Movie> movieQueryWrapper = new QueryWrapper<>();
        movieQueryWrapper.eq("douban_id", doubanId);
        Movie movie = movieService.getOne(movieQueryWrapper);
        Long movieId = movie.getId();

        // 获取标签信息
        List<String> tags = movieDetailService.getTagsById(movieId);

        // 封装成Json数据
        JSONObject result = JSONUtil.createObj();
        result.putOnce("movie", movie);
        result.putOnce("movieDetail", movieDetail);
        result.putOnce("tags", tags);

        return CommonResult.success(result);
    }


    /**
     * 用户 获取评分
     */
    @PostMapping("/getScore")
    @PreAuthorize("hasAuthority('user')")
    public CommonResult getScore(@RequestBody UserTagDTO dto) {
        log.debug(dto.toString());

        QueryWrapper<UserScore> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", dto.getUserId()).eq("movie_id", dto.getMovieId());
        UserScore result = scoreService.getOne(wrapper);

        if (result != null) {
            return CommonResult.success(result);
        } else {
            return CommonResult.failed();
        }
    }

    /**
     * 用户 设置评分
     */
    @PostMapping("/setScore")
    @PreAuthorize("hasAuthority('user')")
    public CommonResult setScore(@RequestBody UserTagDTO dto) {
//        log.debug("设置评分：" + dto.toString());
        UserScore userScore = new UserScore();
        userScore.setUserId(dto.getUserId());
        userScore.setMovieId(dto.getMovieId());
        userScore.setScore(dto.getScore());
        scoreService.save(userScore);

        List<String> tags = dto.getTags();
        for (String tag : tags) {
            if (tag.equals("电影") || tag.equals("电视剧")) continue;
            QueryWrapper<UserTag> wrapper = new QueryWrapper<>();
            wrapper.eq("user_id", dto.getUserId()).eq("tag_name", tag);
            UserTag one = userTagService.getOne(wrapper);

            UserTag userTag = new UserTag();
            Long id = one.getId();
            userTag.setId(id);

            int score = dto.getScore();
            Integer clickTimes = one.getClickTimes();
            double weight = score * 20 + clickTimes;
            userTag.setScore(score);
            userTag.setWeight(weight);
            userTag.setEditTime(LocalDateTime.now());

            userTagService.updateById(userTag);
        }

        return CommonResult.success(null);
    }

    /**
     * 用户 点击次数修改
     */
    @PostMapping("/click")
    @PreAuthorize("hasAuthority('user')")
    public CommonResult changeClickTimes(@RequestBody UserTagDTO dto) {
//        log.debug("点击次数：" + dto.toString());

        List<String> tags = dto.getTags();
        for (String tag : tags) {
            if (tag.equals("电影") || tag.equals("电视剧")) continue;
            UserTag userTag = new UserTag();
            userTag.setUserId(dto.getUserId());
            userTag.setTagName(tag);

            // 查询是否存在
            QueryWrapper<UserTag> wrapper = new QueryWrapper<>();
            wrapper.eq("user_id", dto.getUserId()).eq("tag_name", tag);
            UserTag one = userTagService.getOne(wrapper);
            if (one == null) {
                userTag.setClickTimes(1);
                userTag.setEditTime(LocalDateTime.now());
                userTag.setWeight(1.0);
                userTagService.save(userTag);
            } else {
                Long id = one.getId();
                int clickTimes = one.getClickTimes() + 1;
                if (clickTimes > 100) clickTimes = 100;
                Integer score = one.getScore();
                double weight = score * 20 + clickTimes;

                userTag.setId(id);
                userTag.setClickTimes(clickTimes);
                userTag.setEditTime(LocalDateTime.now());
                userTag.setWeight(weight);
                userTagService.updateById(userTag);
            }
        }

        return CommonResult.success(null);
    }


    /**
     * 用户 评论
     */
    @PostMapping("/setComment")
    @PreAuthorize("hasAuthority('user')")
    public CommonResult setComment(@RequestBody Map param) {
        log.debug(String.valueOf(param));
        String comment = (String) param.get("comment");
        String userId = (String) param.get("userId");
        String doubanId = (String) param.get("doubanId");
        String username = (String) param.get("username");

        QueryWrapper<Movie> movieQueryWrapper = new QueryWrapper<>();
        movieQueryWrapper.eq("douban_id", doubanId);
        Long movieId = movieService.getOne(movieQueryWrapper).getId();

        LocalDateTime now = LocalDateTime.now();
        Comment comment1 = new Comment();
        comment1.setUserId(Long.valueOf(userId));
        comment1.setMovieId(movieId);
        comment1.setUsername(username);
        comment1.setComments(comment);
        comment1.setCreateTime(now);
        commentService.save(comment1);

        return CommonResult.success(null);
    }


    /**
     * 获取全部评论
     */
    @GetMapping("/getComments")
    public CommonResult getComments(@RequestParam("doubanId") String doubanId) {
        log.debug(doubanId);
        QueryWrapper<Movie> movieQueryWrapper = new QueryWrapper<>();
        movieQueryWrapper.eq("douban_id", doubanId);
        Long movieId = movieService.getOne(movieQueryWrapper).getId();

        QueryWrapper<Comment> commentQueryWrapper = new QueryWrapper<Comment>().eq("movie_id", movieId);
        List<Comment> comments = commentService.list(commentQueryWrapper);

        return CommonResult.success(comments);
    }
}
