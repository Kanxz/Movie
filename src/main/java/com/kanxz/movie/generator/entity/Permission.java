package com.kanxz.movie.generator.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 权限表
 * </p>
 *
 * @author kanxz
 * @since 2022-04-01
 */
@Getter
@Setter
@TableName("permission")
public class Permission {

    @TableId("id")
    private Long id;

    /**
     * 权限名
     */
    @TableField("name")
    private String name;

    /**
     * 权限
     */
    @TableField("perm_key")
    private String permKey;

    /**
     * 状态（0正常 1删除）
     */
    @TableField("status")
    private String status;

    /**
     * 备注
     */
    @TableField("remark")
    private String remark;


}
