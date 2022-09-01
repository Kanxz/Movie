package com.kanxz.movie;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class MovieApplication {

    //TODO 设计并实现影视推荐的基本功能
    // 会员注册登录
    // 角色权限管理
    // 影视发布及管理
    // 热门影视分类导航及精准搜索
    // 信息查询修改删除
    // 后台管理
    // 用户评论等基本的交互功能及一些安全保护措施

    public static void main(String[] args) {
        SpringApplication.run(MovieApplication.class, args);
    }

}
