package com.kanxz.movie.generator.mapper;

import com.kanxz.movie.generator.entity.Tag;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * 标签表 Mapper 接口
 * </p>
 *
 * @author kanxz
 * @since 2022-04-01
 */
@Mapper
public interface TagMapper extends BaseMapper<Tag> {

    List<String> getAllTagName();
}
