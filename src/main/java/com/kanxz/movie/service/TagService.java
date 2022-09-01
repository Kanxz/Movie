package com.kanxz.movie.service;

import com.kanxz.movie.generator.entity.Tag;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 标签表 服务类
 * </p>
 *
 * @author kanxz
 * @since 2022-04-01
 */
public interface TagService extends IService<Tag> {

    /**
     * 获取所有标签
     *
     * @return 标签list
     */
    List<String> getAllTags();

}
