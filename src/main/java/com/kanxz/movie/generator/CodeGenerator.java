package com.kanxz.movie.generator;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.util.Collections;

/**
 * 代码生成器
 *
 * @see <a href="https://baomidou.com/pages/981406/">代码生成器</a>
 */
public class CodeGenerator {
    public static void main(String[] args) {
        String projectPath = System.getProperty("user.dir");

        FastAutoGenerator.create("jdbc:mysql://localhost:3306/movie?useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai",
                        "root", "347866")
                .globalConfig(builder -> {
                    builder.author("kanxz") // 设置作者
//                            .enableSwagger() // 开启 swagger 模式
//                            .fileOverride() // 覆盖已生成文件
                            .disableOpenDir() //禁止打开输出目录
                            .outputDir(projectPath + "/src/main/java"); // 指定输出目录
                })
                .packageConfig(builder -> {
                    builder.parent("com.kanxz.movie") // 设置父包名
//                            .moduleName("generator") // 设置父包模块名
                            .entity("generator.entity")
                            .mapper("generator.mapper")
                            .pathInfo(Collections.singletonMap(OutputFile.mapperXml,
                                    projectPath + "/src/main/resources/generator/mapper")); // 设置mapperXml生成路径
                })
                .strategyConfig(builder -> {
                    builder
//                            .addInclude("carousel")
//                            .addInclude("movie_detail")// 设置需要生成的表名
//                            .addInclude("comment")
//                            .addInclude("movie_comment")
//                            .addInclude("movie")
//                            .addInclude("movie_tag")
//                            .addInclude("permission")
//                            .addInclude("role")
//                            .addInclude("role_permission")
//                            .addInclude("tag")
//                            .addInclude("user_info")
                            .addInclude("user_tag")
                            .addInclude("user_score")
//                            .addInclude("user_comment")
//                            .addInclude("user_role")
                            //.addTablePrefix("t_", "c_"); // 设置过滤表前缀
                            .entityBuilder()
                            .enableLombok()
                            .disableSerialVersionUID() // 禁用生成 serialVersionUID
                            .enableTableFieldAnnotation() // 开启生成实体时生成字段注解
                            //.idType(IdType.AUTO)
                            .controllerBuilder()
                            .enableRestStyle()
                            .serviceBuilder()
                            .formatServiceFileName("%sService") // 格式化Service接口文件名称
                            .formatServiceImplFileName("%sServiceImpl") // 格式化 ServiceImpl 实现类文件名称
                            .mapperBuilder()
                            .enableMapperAnnotation(); // 开启 @Mapper 注解
                })
                .templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .execute();

    }
}
