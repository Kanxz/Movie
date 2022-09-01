package com.kanxz.movie.service.impl;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kanxz.movie.common.api.CommonResult;
import com.kanxz.movie.common.utils.JwtUtil;
import com.kanxz.movie.common.utils.RedisCache;
import com.kanxz.movie.dto.UserDTO;
import com.kanxz.movie.entity.UserDetail;
import com.kanxz.movie.generator.entity.User;
import com.kanxz.movie.generator.mapper.PermissionMapper;
import com.kanxz.movie.generator.mapper.UserMapper;
import com.kanxz.movie.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author kanxz
 * @since 2022-04-01
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private RedisCache redisCache;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private PermissionMapper permissionMapper;

    @Override
    public CommonResult login(User user) {
        //AuthenticationManager进行用户认证
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
        Authentication authenticate;
        try {
            authenticate = authenticationManager.authenticate(authenticationToken);
        } catch (AuthenticationException e) {
            log.warn("登录异常: {}", e.getMessage());
            return CommonResult.validateFailed(e.getMessage());
        }

        //认证成功
        UserDetail userDetail = (UserDetail) authenticate.getPrincipal();
        String userID = userDetail.getUser().getId().toString();
        String username = userDetail.getUser().getUsername();
        String jwt = JwtUtil.createJWT(username);

//        redisCache.setCacheObject("login:" + userID, userDetail);

        JSONObject result = JSONUtil.createObj();
        result.putOnce("userID", userID);
        result.putOnce("username", username);
        result.putOnce("token", jwt);

        return CommonResult.success(result, "登录成功");
    }

    @Override
    public CommonResult adminLogin(User user) {
        //AuthenticationManager进行用户认证
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
        Authentication authenticate;
        try {
            authenticate = authenticationManager.authenticate(authenticationToken);
        } catch (AuthenticationException e) {
            log.warn("登录异常: {}", e.getMessage());
            return CommonResult.validateFailed(e.getMessage());
        }

        //认证成功
        UserDetail userDetail = (UserDetail) authenticate.getPrincipal();

        boolean admin = userDetail.getPermissions().contains("admin_login");
        if (!admin) {
            return CommonResult.forbidden(null);
        }

        String userID = userDetail.getUser().getId().toString();
        String username = userDetail.getUser().getUsername();
        String jwt = JwtUtil.createJWT(username);

//        redisCache.setCacheObject("login:" + userID, userDetail);

        JSONObject result = JSONUtil.createObj();
        result.putOnce("userID", userID);
        result.putOnce("username", username);
        result.putOnce("token", jwt);

        return CommonResult.success(result, "登录成功");
    }

    @Override
    public CommonResult logout() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetail userDetail = (UserDetail) authentication.getPrincipal();
        Long userID = userDetail.getUser().getId();
//        redisCache.deleteObject("login:" + userID);
        return CommonResult.success(null, "退出成功");
    }

    @Override
    public List<String> getUserAuthorities(String userId) {
        List<String> perms = permissionMapper.selectPermsByUserId(Long.valueOf(userId));
        return perms;
    }

    @Override
    public IPage<UserDTO> getAllUser(IPage<UserDTO> page, String name) {
        return userMapper.getBackendUserList(page, name);
    }

}
