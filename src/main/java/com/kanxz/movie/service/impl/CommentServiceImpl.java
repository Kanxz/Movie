package com.kanxz.movie.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.kanxz.movie.dto.CommentDTO;
import com.kanxz.movie.generator.entity.Comment;
import com.kanxz.movie.generator.mapper.CommentMapper;
import com.kanxz.movie.service.CommentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 评论表 服务实现类
 * </p>
 *
 * @author kanxz
 * @since 2022-04-01
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Override
    public IPage<CommentDTO> getAllComments(IPage<CommentDTO> page, String name) {
        return commentMapper.getBackendCommentList(page, name);
    }
}
