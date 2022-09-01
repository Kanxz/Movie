package com.kanxz.movie.generator.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.kanxz.movie.dto.UserDTO;
import com.kanxz.movie.generator.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @author kanxz
 * @since 2022-04-01
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
    /**
     * 查询用户列表
     *
     * @param page 分页信息
     * @return 用户列表
     */
    IPage<UserDTO> getBackendUserList(IPage<UserDTO> page, @Param("name") String name);
}
