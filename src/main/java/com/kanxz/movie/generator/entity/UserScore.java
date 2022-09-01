package com.kanxz.movie.generator.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

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
@TableName("user_score")
public class UserScore {

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
     * 影视ID
     */
    @TableField("movie_id")
    private Long movieId;

    /**
     * 评分
     */
    @TableField("score")
    private Integer score;


}
