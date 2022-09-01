package com.kanxz.movie.service.impl;

import com.kanxz.movie.generator.entity.UserInfo;
import com.kanxz.movie.generator.mapper.UserInfoMapper;
import com.kanxz.movie.service.UserInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author kanxz
 * @since 2022-05-05
 */
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {

}
