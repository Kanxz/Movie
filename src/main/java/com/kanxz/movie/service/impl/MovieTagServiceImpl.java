package com.kanxz.movie.service.impl;

import com.kanxz.movie.generator.entity.MovieTag;
import com.kanxz.movie.generator.mapper.MovieTagMapper;
import com.kanxz.movie.service.MovieTagService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 电影-标签关系表 服务实现类
 * </p>
 *
 * @author kanxz
 * @since 2022-04-01
 */
@Service
public class MovieTagServiceImpl extends ServiceImpl<MovieTagMapper, MovieTag> implements MovieTagService {

}
