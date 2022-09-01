package com.kanxz.movie.generator.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 电影-评论关系表
 * </p>
 *
 * @author kanxz
 * @since 2022-04-01
 */
@Getter
@Setter
@TableName("movie_comment")
public class MovieComment {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("movie_id")
    private Long movieId;

    @TableField("comment_id")
    private Long commentId;


}
