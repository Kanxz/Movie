package com.kanxz.movie.service.impl;

import com.kanxz.movie.generator.entity.UserScore;
import com.kanxz.movie.generator.mapper.UserScoreMapper;
import com.kanxz.movie.service.UserScoreService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author kanxz
 * @since 2022-05-31
 */
@Service
public class UserScoreServiceImpl extends ServiceImpl<UserScoreMapper, UserScore> implements UserScoreService {

}
