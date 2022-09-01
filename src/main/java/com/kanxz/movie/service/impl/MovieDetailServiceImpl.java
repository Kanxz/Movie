package com.kanxz.movie.service.impl;

import com.kanxz.movie.generator.entity.MovieDetail;
import com.kanxz.movie.generator.mapper.MovieDetailMapper;
import com.kanxz.movie.service.MovieDetailService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 电影详细信息表 服务实现类
 * </p>
 *
 * @author kanxz
 * @since 2022-04-01
 */
@Service
public class MovieDetailServiceImpl extends ServiceImpl<MovieDetailMapper, MovieDetail> implements MovieDetailService {

    @Autowired
    private MovieDetailMapper movieDetailMapper;

    @Override
    public List<String> getAllLanguages() {
        List<String> allLanguage = movieDetailMapper.getAllLanguage();
        allLanguage.add(0, "全部");
        return allLanguage;
    }

    @Override
    public List<String> getTagsById(Long id) {
        List<String> tags = movieDetailMapper.getTagsById(id);
        return tags;
    }
}
