package com.kanxz.movie.service.impl;

import cn.hutool.core.map.MapUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kanxz.movie.common.utils.RedisCache;
import com.kanxz.movie.dto.MovieDTO;
import com.kanxz.movie.generator.entity.Movie;
import com.kanxz.movie.generator.entity.UserTag;
import com.kanxz.movie.generator.mapper.MovieMapper;
import com.kanxz.movie.service.MovieDetailService;
import com.kanxz.movie.service.MovieService;
import com.kanxz.movie.service.UserTagService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 电影表 服务实现类
 * </p>
 *
 * @author kanxz
 * @since 2022-04-01
 */
@Service
@Slf4j
public class MovieServiceImpl extends ServiceImpl<MovieMapper, Movie> implements MovieService {

    @Autowired
    private MovieMapper movieMapper;
    @Autowired
    private RedisCache redisCache;
    @Autowired
    private UserTagService userTagService;
    @Autowired
    private MovieDetailService movieDetailService;

    @Override
    public JSONObject getMovieList(IPage<Movie> page, String language, String tag) {
        IPage<Movie> movieIPage = movieMapper.getMovieList(page, language, tag);
        List<Movie> movieList = movieIPage.getRecords();
        JSONObject result = JSONUtil.createObj();
        result.putOnce("counts", movieIPage.getTotal());  // 总条数
        result.putOnce("pageSize", movieIPage.getSize()); // 每页显示条数
        result.putOnce("pages", movieIPage.getPages()); // 当前分页总页数
        result.putOnce("curPage", movieIPage.getCurrent()); // 当前页
        result.putOnce("movieList", movieList);

        return result;
    }

    @Override
    public JSONObject getMovieListWithSort(IPage<Movie> page, String language, String tag, String sortFn) {
        IPage<Movie> movieIPage = movieMapper.getMovieListWithSort(page, language, tag, sortFn);
        List<Movie> movieList = movieIPage.getRecords();
        JSONObject result = JSONUtil.createObj();
        result.putOnce("counts", movieIPage.getTotal());  // 总条数
        result.putOnce("pageSize", movieIPage.getSize()); // 每页显示条数
        result.putOnce("pages", movieIPage.getPages()); // 当前分页总页数
        result.putOnce("curPage", movieIPage.getCurrent()); // 当前页
        result.putOnce("movieList", movieList);

        return result;
    }

    @Override
    public IPage<MovieDTO> getMovieInfoList(IPage<MovieDTO> page, String name) {
        return movieMapper.getAllMovieInfo(page, name);
    }

    @Override
    public JSONObject getRecommendMovieList(Long userId, int pageNum) {
        Map<String, Double> userMap = getUserMap(userId);
        JSONObject jsonObject = new JSONObject();

        if (userMap.size() == 0) {
            Page<Movie> page = new Page<>(pageNum, 12);
            jsonObject = this.getMovieListWithSort(page, ".*", ".*", "score DESC");
            return jsonObject;
        }
        HashMap<Long, Double> similarityMap = new HashMap<>();
        List<Movie> movies = this.list();
        for (Movie movie : movies) {
            Long id = movie.getId();
            List<String> tags = movieDetailService.getTagsById(id);
            tags.remove("电影");
            tags.remove("电视剧");
            double similarity = getSimilarity(userMap, tags);
            log.debug("{} 的相似度: {} ", movie.getName(), similarity);
            similarityMap.put(id, similarity);
        }
        Map<Long, Double> resultMap = MapUtil.sortByValue(similarityMap, true);
        int st = 1 + (pageNum - 1) * 12;
        int ed = st + 12;
        int index = 0;
        ArrayList<MovieDTO> result = new ArrayList<>();
        for (Map.Entry<Long, Double> entry : resultMap.entrySet()) {
            index++;
            if (index >= ed) break;
            if (index >= st) {
                Long movieId = entry.getKey();
                Movie movie = this.getById(movieId);
                MovieDTO movieDTO = new MovieDTO();
                movieDTO.setId(movie.getId());
                movieDTO.setDoubanId(movie.getDoubanId());
                movieDTO.setName(movie.getName());
                movieDTO.setScore(String.valueOf(movie.getScore()));
                movieDTO.setPicUrl(movie.getPicUrl());
                List<String> tags = movieDetailService.getTagsById(movieId);
                tags.remove("电影");
                tags.remove("电视剧");
                String collect = String.join(" ", tags);
                movieDTO.setTags(collect);
                result.add(movieDTO);
            }
        }

        jsonObject.putOnce("movieList", result);

        return jsonObject;
    }

    /**
     * 余弦相似度算法计算相似度
     *
     * @param userMap 用户矩阵
     * @param tags    电影标签
     * @return 相似度
     */
    private double getSimilarity(Map<String, Double> userMap, List<String> tags) {
        double member = 0;
        double denominator = 0;
        for (String tag : tags) {
            Double weight = userMap.get(tag);
            if (weight != null) {
                member += weight;
            }
        }
        for (Map.Entry<String, Double> entry : userMap.entrySet()) {
            double value = entry.getValue();
            denominator += value * value;
        }
        int size = tags.size();
        denominator = Math.sqrt(denominator) * Math.sqrt(size);
        if (denominator == 0) return 0;
        return member / denominator;
    }

    /**
     * 获取用户矩阵
     *
     * @param userId 用户ID
     */
    private Map<String, Double> getUserMap(Long userId) {
        QueryWrapper<UserTag> wrapper = new QueryWrapper<UserTag>().eq("user_id", userId);
        List<UserTag> list = userTagService.list(wrapper);
        HashMap<String, Double> userMap = new HashMap<>();

        for (UserTag userTag : list) {
            String tagName = userTag.getTagName();
            double weight = userTag.getWeight();
            LocalDateTime editTime = userTag.getEditTime();
            LocalDateTime now = LocalDateTime.now();
            long days = Duration.between(editTime, now).toDays();
            if (days > 30) {
                weight = weight / 2;
            } else if (days > 7) {
                weight = weight / 1.2;
            }
            userMap.put(tagName, weight);
        }


        return userMap;
    }
}
