package com.kanxz.movie.generator.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.kanxz.movie.dto.CommentDTO;
import com.kanxz.movie.generator.entity.Comment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 评论表 Mapper 接口
 * </p>
 *
 * @author kanxz
 * @since 2022-04-01
 */
@Mapper
public interface CommentMapper extends BaseMapper<Comment> {

    /**
     * 查询某用户的全部评论
     *
     * @param page 分页
     * @param name 用户名
     * @return 评论列表
     */
    IPage<CommentDTO> getBackendCommentList(IPage<CommentDTO> page, @Param("name") String name);
}
