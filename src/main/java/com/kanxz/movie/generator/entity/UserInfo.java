package com.kanxz.movie.generator.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <p>
 * 
 * </p>
 *
 * @author kanxz
 * @since 2022-05-05
 */
@Getter
@Setter
@ToString
@TableName("user_info")
public class UserInfo {

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * user的ID
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 邮箱
     */
    @TableField("email")
    private String email;

    /**
     * 性别
     */
    @TableField("gender")
    private String gender;

    /**
     * 年龄
     */
    @TableField("age")
    private Integer age;


}
