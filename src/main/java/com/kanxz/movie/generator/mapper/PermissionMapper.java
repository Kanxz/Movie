package com.kanxz.movie.generator.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kanxz.movie.generator.entity.Permission;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * 权限表 Mapper 接口
 * </p>
 *
 * @author kanxz
 * @since 2022-04-01
 */
@Mapper
public interface PermissionMapper extends BaseMapper<Permission> {

    List<String> selectPermsByUserId(Long userid);
}
