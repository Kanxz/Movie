package com.kanxz.movie.service.impl;

import com.kanxz.movie.generator.entity.UserRole;
import com.kanxz.movie.generator.mapper.UserRoleMapper;
import com.kanxz.movie.service.UserRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户-角色关系表 服务实现类
 * </p>
 *
 * @author kanxz
 * @since 2022-04-01
 */
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements UserRoleService {

}
