package com.kanxz.movie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kanxz.movie.entity.UserDetail;
import com.kanxz.movie.generator.entity.User;
import com.kanxz.movie.generator.mapper.PermissionMapper;
import com.kanxz.movie.generator.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private PermissionMapper permissionMapper;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        //查询用户信息
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("username", username);
        User user = userMapper.selectOne(wrapper);

        if (Objects.isNull(user)) {
            throw new UsernameNotFoundException("用户名密码错误");
        }

        //查询用户权限
        List<String> permissionKeyList = permissionMapper.selectPermsByUserId(user.getId());
        log.debug("权限列表：{}", permissionKeyList);
        return new UserDetail(user, permissionKeyList);
    }
}
