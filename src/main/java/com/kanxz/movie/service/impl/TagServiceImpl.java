package com.kanxz.movie.service.impl;

import com.kanxz.movie.generator.entity.Tag;
import com.kanxz.movie.generator.mapper.TagMapper;
import com.kanxz.movie.service.TagService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 标签表 服务实现类
 * </p>
 *
 * @author kanxz
 * @since 2022-04-01
 */
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {

    @Autowired
    private TagMapper tagMapper;

    @Override
    public List<String> getAllTags() {
        List<String> allTagName = tagMapper.getAllTagName();
        allTagName.add(0, "全部");
        return allTagName;
    }
}
