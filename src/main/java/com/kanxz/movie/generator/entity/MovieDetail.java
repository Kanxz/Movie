package com.kanxz.movie.generator.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/**
 * <p>
 * 电影详细信息表
 * </p>
 *
 * @author kanxz
 * @since 2022-04-01
 */
@Getter
@Setter
@TableName("movie_detail")
public class MovieDetail {

    /**
     * 电影id
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
     * 导演
     */
    @TableField("director")
    private String director;

    /**
     * 语言
     */
    @TableField("language")
    private String language;

    /**
     * 上映日期
     */
    @TableField("release_time")
    private LocalDate releaseTime;

    /**
     * 片长（分钟）
     */
    @TableField("length")
    private Integer length;

    /**
     * 电影简介
     */
    @TableField("intro")
    private String intro;


}
