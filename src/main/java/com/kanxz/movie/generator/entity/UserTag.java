package com.kanxz.movie.generator.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author kanxz
 * @since 2022-05-31
 */
@Getter
@Setter
@TableName("user_tag")
@ToString
public class UserTag {

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 标签名
     */
    @TableField("tag_name")
    private String tagName;

    /**
     * 权重
     */
    @TableField("weight")
    private Double weight;

    /**
     * 最后修改时间
     */
    @TableField("edit_time")
    private LocalDateTime editTime;

    /**
     * 点击次数
     */
    @TableField("click_times")
    private Integer clickTimes;

    /**
     * 评分
     */
    @TableField("score")
    private Integer score;


}
