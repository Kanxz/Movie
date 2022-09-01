package com.kanxz.movie.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kanxz.movie.common.api.CommonResult;
import com.kanxz.movie.dto.UserDTO;
import com.kanxz.movie.generator.entity.User;

import java.util.List;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author kanxz
 * @since 2022-04-01
 */
public interface UserService extends IService<User> {


    /**
     * 普通用户登录
     *
     * @param user 用户信息
     */
    CommonResult login(User user);

    CommonResult adminLogin(User user);

    CommonResult logout();

    List<String> getUserAuthorities(String userId);

    /**
     * 获取用户列表
     *
     * @param page 分页
     * @return 用户列表
     */
    IPage<UserDTO> getAllUser(IPage<UserDTO> page, String name);
}
