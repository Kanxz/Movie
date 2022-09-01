package com.kanxz.movie.generator.mapper;

import com.kanxz.movie.generator.entity.RolePermission;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 角色-权限关系表 Mapper 接口
 * </p>
 *
 * @author kanxz
 * @since 2022-04-01
 */
@Mapper
public interface RolePermissionMapper extends BaseMapper<RolePermission> {

}
