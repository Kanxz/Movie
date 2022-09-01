package com.kanxz.movie.generator.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * <p>
 * 评论表
 * </p>
 *
 * @author kanxz
 * @since 2022-04-01
 */
@Getter
@Setter
@TableName("comment")
public class Comment {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID
     */
    @TableField("user_id")
    private Long  userId;

    /**
     * 用户名
     */
    @TableField("username")
    private String username;

    /**
     * 电影ID
     */
    @TableField("movie_id")
    private Long  movieId;

    /**
     * 评论
     */
    @TableField("comments")
    private String comments;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;


}
