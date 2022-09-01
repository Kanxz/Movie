package com.kanxz.movie.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kanxz.movie.dto.CommentDTO;
import com.kanxz.movie.generator.entity.Comment;

/**
 * <p>
 * 评论表 服务类
 * </p>
 *
 * @author kanxz
 * @since 2022-04-01
 */
public interface CommentService extends IService<Comment> {


    /**
     * 根据用户名获取所有评论
     *
     * @param page 分页
     * @param name 用户名
     * @return 评论列表
     */
    IPage<CommentDTO> getAllComments(IPage<CommentDTO> page, String name);

}
