package com.kanxz.movie.generator.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.kanxz.movie.dto.MovieDTO;
import com.kanxz.movie.generator.entity.Movie;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 电影表 Mapper 接口
 * </p>
 *
 * @author kanxz
 * @since 2022-04-01
 */
@Mapper
public interface MovieMapper extends BaseMapper<Movie> {

    /**
     * 获取电影列表
     *
     * @param page     分页
     * @param language 语言
     * @param tag      标签
     * @return 查询到的分页
     */
    IPage<Movie> getMovieList(IPage<Movie> page, @Param("lan") String language, @Param("tag") String tag);

    /**
     * 获取排序后的电影列表
     *
     * @param page     分页
     * @param language 语言
     * @param tag      标签
     * @param sortFn   排序方法
     * @return 查询到的分页
     */
    IPage<Movie> getMovieListWithSort(IPage<Movie> page, @Param("Lan") String language,
                                      @Param("Tag") String tag, @Param("sortFn") String sortFn);

    /**
     * 获取电影信息列表
     *
     * @param page 分页
     * @param name 要筛选的电影名
     * @return 电影列表
     */
    IPage<MovieDTO> getAllMovieInfo(IPage<MovieDTO> page, @Param("name") String name);

}
