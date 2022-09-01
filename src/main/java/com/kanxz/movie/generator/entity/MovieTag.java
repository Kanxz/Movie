package com.kanxz.movie.generator.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 电影-标签关系表
 * </p>
 *
 * @author kanxz
 * @since 2022-04-01
 */
@Getter
@Setter
@TableName("movie_tag")
public class MovieTag {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("tag_id")
    private Long tagId;

    @TableField("movie_id")
    private Long movieId;


}
