package com.kanxz.movie.service.impl;

import com.kanxz.movie.generator.entity.Role;
import com.kanxz.movie.generator.mapper.RoleMapper;
import com.kanxz.movie.service.RoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 角色表 服务实现类
 * </p>
 *
 * @author kanxz
 * @since 2022-04-01
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

}
