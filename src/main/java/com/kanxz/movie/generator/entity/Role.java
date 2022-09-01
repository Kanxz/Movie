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
 * 角色表
 * </p>
 *
 * @author kanxz
 * @since 2022-04-01
 */
@Getter
@Setter
@TableName("role")
@ToString
public class Role {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 角色名
     */
    @TableField("name")
    private String name;

    /**
     * 角色字符串
     */
    @TableField("role_key")
    private String roleKey;

    /**
     * 角色状态（1正常 0删除）
     */
    @TableField("status")
    private String status;

    /**
     * 备注
     */
    @TableField("remark")
    private String remark;


}
