package com.kanxz.movie.service.impl;

import com.kanxz.movie.generator.entity.UserComment;
import com.kanxz.movie.generator.mapper.UserCommentMapper;
import com.kanxz.movie.service.UserCommentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户-评论关系表 服务实现类
 * </p>
 *
 * @author kanxz
 * @since 2022-04-01
 */
@Service
public class UserCommentServiceImpl extends ServiceImpl<UserCommentMapper, UserComment> implements UserCommentService {

}
