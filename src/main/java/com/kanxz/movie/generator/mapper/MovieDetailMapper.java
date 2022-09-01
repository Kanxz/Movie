package com.kanxz.movie.generator.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kanxz.movie.generator.entity.MovieDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * <p>
 * 电影详细信息表 Mapper 接口
 * </p>
 *
 * @author kanxz
 * @since 2022-04-01
 */
@Mapper
public interface MovieDetailMapper extends BaseMapper<MovieDetail> {

    List<String> getAllLanguage();

    /**
     * 根据电影ID获取电影标签
     *
     * @param id 电影ID
     * @return 电影标签List
     */
    List<String> getTagsById(@Param("id") long id);
}
