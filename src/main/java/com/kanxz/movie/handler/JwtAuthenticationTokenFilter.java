package com.kanxz.movie.handler;


import com.kanxz.movie.common.utils.JwtUtil;
import com.kanxz.movie.common.utils.RedisCache;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

@Component
@Slf4j
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Autowired
    private RedisCache redisCache;
    @Autowired
    private UserDetailsService userDetailsService;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //获取Token
//        String token = request.getHeader("token");
        String token = request.getHeader("Authorization");
        if (!StringUtils.hasText(token)) {
            //放行
            filterChain.doFilter(request, response);
            return;
        }

        token = token.substring(7);
        log.debug(token);

        //解析Token
        String username;
        try {
            Claims claims = JwtUtil.parseJWT(token);
            username = claims.getSubject();
        } catch (Exception e) {
            log.error("token错误");
//            throw new RuntimeException("token非法");
            filterChain.doFilter(request, response);
            return;
        }

        //从redis中获取用户信息
//        String redisKey = "login:" + userId;
//        UserDetail userDetail = redisCache.getCacheObject(redisKey);

        // 获取UserDetail
        UserDetails userDetail = userDetailsService.loadUserByUsername(username);

        if (Objects.isNull(userDetail)) {
            log.error("用户未登录");
//            throw new RuntimeException("用户未登录");
            filterChain.doFilter(request, response);
            return;
        }

        //存入SecurityContextHolder
        //获取权限信息封装到Authentication中
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userDetail, null, userDetail.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        //放行
        filterChain.doFilter(request, response);
    }
}
