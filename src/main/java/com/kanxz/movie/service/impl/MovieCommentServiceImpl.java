package com.kanxz.movie.service.impl;

import com.kanxz.movie.generator.entity.MovieComment;
import com.kanxz.movie.generator.mapper.MovieCommentMapper;
import com.kanxz.movie.service.MovieCommentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 电影-评论关系表 服务实现类
 * </p>
 *
 * @author kanxz
 * @since 2022-04-01
 */
@Service
public class MovieCommentServiceImpl extends ServiceImpl<MovieCommentMapper, MovieComment> implements MovieCommentService {

}
