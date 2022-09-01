package com.kanxz.movie.service;

import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kanxz.movie.dto.MovieDTO;
import com.kanxz.movie.generator.entity.Movie;

/**
 * <p>
 * 电影表 服务类
 * </p>
 *
 * @author kanxz
 * @since 2022-04-01
 */
public interface MovieService extends IService<Movie> {

    /**
     * 获取电影列表
     *
     * @param page     分页
     * @param language 语言
     * @param tag      标签
     * @return 电影列表的 Json 数据
     */
    JSONObject getMovieList(IPage<Movie> page, String language, String tag);

    /**
     * 获取排序后的电影列表
     *
     * @param page     分页
     * @param language 语言
     * @param tag      标签
     * @param sortFn   排序方法
     * @return 电影列表的 Json 数据
     */
    JSONObject getMovieListWithSort(IPage<Movie> page, String language, String tag, String sortFn);

    /**
     * 根据电影名获取电影信息列表
     *
     * @param page 分页
     * @param name 电影名
     * @return 电影信息列表
     */
    IPage<MovieDTO> getMovieInfoList(IPage<MovieDTO> page, String name);


    /**
     * 获取推荐电影列表
     *
     * @param userId  用户ID
     * @param pageNum 页数
     * @return 电影列表
     */
    JSONObject getRecommendMovieList(Long userId, int pageNum);
}
