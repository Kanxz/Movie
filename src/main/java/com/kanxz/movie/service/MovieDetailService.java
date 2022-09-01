package com.kanxz.movie.service;

import com.kanxz.movie.generator.entity.MovieDetail;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 电影详细信息表 服务类
 * </p>
 *
 * @author kanxz
 * @since 2022-04-01
 */
public interface MovieDetailService extends IService<MovieDetail> {

    /**
     * 获取所有语言
     *
     * @return 语言list
     */
    List<String> getAllLanguages();

    /**
     * 根据电影ID获取电影标签
     *
     * @param id 电影ID
     * @return 电影标签List
     */
    List<String> getTagsById(Long id);

}
