package com.kanxz.movie.service.impl;

import com.kanxz.movie.generator.entity.RolePermission;
import com.kanxz.movie.generator.mapper.RolePermissionMapper;
import com.kanxz.movie.service.RolePermissionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 角色-权限关系表 服务实现类
 * </p>
 *
 * @author kanxz
 * @since 2022-04-01
 */
@Service
public class RolePermissionServiceImpl extends ServiceImpl<RolePermissionMapper, RolePermission> implements RolePermissionService {

}
