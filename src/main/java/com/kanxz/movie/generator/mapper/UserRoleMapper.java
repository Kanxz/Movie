package com.kanxz.movie.generator.mapper;

import com.kanxz.movie.generator.entity.UserRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 用户-角色关系表 Mapper 接口
 * </p>
 *
 * @author kanxz
 * @since 2022-04-01
 */
@Mapper
public interface UserRoleMapper extends BaseMapper<UserRole> {

}
