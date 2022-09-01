package com.kanxz.movie.generator.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 电影表
 * </p>
 *
 * @author kanxz
 * @since 2022-04-01
 */
@Getter
@Setter
@TableName("movie")
public class Movie {

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 豆瓣ID
     */
    @TableField("douban_id")
    private String doubanId;

    /**
     * 电影名
     */
    @TableField("name")
    private String name;

    /**
     * 评分
     */
    @TableField("score")
    private Float score;

    /**
     * 电影封面
     */
    @TableField("pic_url")
    private String picUrl;


}
