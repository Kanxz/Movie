package com.kanxz.movie.service.impl;

import com.kanxz.movie.generator.entity.Permission;
import com.kanxz.movie.generator.mapper.PermissionMapper;
import com.kanxz.movie.service.PermissionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 权限表 服务实现类
 * </p>
 *
 * @author kanxz
 * @since 2022-04-01
 */
@Service
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements PermissionService {

}
