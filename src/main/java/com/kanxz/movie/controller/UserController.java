package com.kanxz.movie.controller;


import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kanxz.movie.common.api.CommonResult;
import com.kanxz.movie.dto.UserDTO;
import com.kanxz.movie.generator.entity.Role;
import com.kanxz.movie.generator.entity.User;
import com.kanxz.movie.generator.entity.UserInfo;
import com.kanxz.movie.generator.entity.UserRole;
import com.kanxz.movie.service.RoleService;
import com.kanxz.movie.service.UserInfoService;
import com.kanxz.movie.service.UserRoleService;
import com.kanxz.movie.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author kanxz
 * @since 2022-04-01
 */
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private UserRoleService userRoleService;

    /**
     * 用户登录
     */
    @PostMapping("/login")
    public CommonResult login(@RequestBody User user) {
        log.debug(user.toString());
        return userService.login(user);
    }

    /**
     * 管理员登录
     */
    @PostMapping("/admin/login")
    public CommonResult adminLogin(@RequestBody User user) {
        log.debug(user.toString());
        return userService.adminLogin(user);
    }

    /**
     * 用户注册
     */
    @PostMapping("/register")
    public CommonResult register(@RequestBody UserDTO userDTO) {
        log.debug(userDTO.toString());

        // 判断用户名是否已经存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>().eq("username", userDTO.getUsername());
        User one = userService.getOne(queryWrapper);
        if (one != null) {
            return CommonResult.failed("用户名已存在");
        }

        User user = new User();
        user.setUsername(userDTO.getUsername());
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encode = passwordEncoder.encode(userDTO.getPassword());
        user.setPassword(encode);
        userService.save(user);

        Long userId = userService.getOne(queryWrapper).getId();

        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(userId);
        userInfo.setGender(userDTO.getGender());
        userInfo.setEmail(userDTO.getEmail());
        userInfo.setAge(userDTO.getAge());
        userInfoService.save(userInfo);

        QueryWrapper<Role> wrapper = new QueryWrapper<Role>().eq("role_key", "user");
        Role role = roleService.getOne(wrapper);
        Long roleId = role.getId();
        UserRole userRole = new UserRole();
        userRole.setUserId(userId);
        userRole.setRoleId(roleId);
        userRoleService.save(userRole);

        return CommonResult.success(null);
    }

    //    @GetMapping("/logout")
    public CommonResult logout() {
        return userService.logout();
    }

    /**
     * 用户 获取个人信息
     */
    @GetMapping("/getById")
    @PreAuthorize("hasAuthority('user')")
    public CommonResult getUserById(@RequestParam("id") String id) {
        User user = userService.getById(id);
        QueryWrapper<UserInfo> wrapper = new QueryWrapper<UserInfo>().eq("user_id", user.getId());
        UserInfo userInfo = userInfoService.getOne(wrapper);

        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setAge(userInfo.getAge());
        userDTO.setEmail(userInfo.getEmail());
        userDTO.setGender(userInfo.getGender());

        return CommonResult.success(userDTO);
    }

    /**
     * 用户 修改个人信息
     */
    @PostMapping("/editById")
    @PreAuthorize("hasAuthority('user')")
    public CommonResult editUserInfo(@RequestBody UserDTO userDTO) {
        log.debug(userDTO.toString());

        QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<UserInfo>().eq("user_id", userDTO.getId());
        UserInfo one = userInfoService.getOne(queryWrapper);
        Long userInfoId = one.getId();

        UserInfo userInfo = new UserInfo();
        userInfo.setId(userInfoId);
        userInfo.setEmail(userDTO.getEmail());
        userInfo.setAge(userDTO.getAge());
        userInfo.setGender(userDTO.getGender());
        userInfoService.updateById(userInfo);

//        log.debug(userInfo.toString());

        return CommonResult.success(null);
    }

    /**
     * 用户 修改密码
     */
    @PostMapping("/editPassword")
    public  CommonResult changePassword(@RequestBody UserDTO userDTO) {
        log.debug(userDTO.toString());

        User user = new User();
        user.setId(userDTO.getId());
        String rawPd = userDTO.getPassword();
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String password = encoder.encode(rawPd);
        user.setPassword(password);

        userService.updateById(user);

        return CommonResult.success(null);
    }


    /**
     * 后台管理 获取管理员权限信息
     */
    @GetMapping("/admin/getAuthority")
    @PreAuthorize("hasAuthority('movie')")
    public CommonResult getAuthority(@RequestParam("userId") String userId) {
        List<String> userAuthorities = userService.getUserAuthorities(userId);
        return CommonResult.success(userAuthorities);
    }

    /**
     * 后台管理 获取人员信息
     */
    @PostMapping("/getAll")
    @PreAuthorize("hasAuthority('super_admin')")
    public CommonResult getAllUser(@RequestBody Map queryForm) {
        String name = (String) queryForm.get("query");
        int pageNum = (int) queryForm.get("pageNum");
        if (name.equals("")) name = ".*";
//        log.debug(name + "---" + pageNum);
        Page<UserDTO> page = new Page<>(pageNum, 10);
        IPage<UserDTO> iPage = userService.getAllUser(page, name);
        List<UserDTO> userDTOList = iPage.getRecords();
        long total = iPage.getTotal();

        JSONObject result = JSONUtil.createObj();
        result.putOnce("userList", userDTOList);
        result.putOnce("total", total);
        return CommonResult.success(result);
    }

    /**
     * 后台管理 编辑人员信息
     */
    @PostMapping("/editOne")
    @PreAuthorize("hasAuthority('super_admin')")
    public CommonResult editOne(@RequestBody Map params) {
        log.debug(String.valueOf(params));
        int id = (int) params.get("id");
        String username = (String) params.get("username");
        int age = Integer.parseInt((String) params.get("age"));
        String gender = (String) params.get("gender");
        String email = (String) params.get("email");
        String roleKey = (String) params.get("roleKey");
        boolean status = (boolean) params.get("status");

        User user = new User();
        user.setId((long) id);
        user.setUsername(username);
        user.setStatus(status ? "1" : "0");
        boolean userUpdate = userService.updateById(user);

        UserInfo userInfo = new UserInfo();
        userInfo.setUserId((long) id);
        userInfo.setAge(age);
        userInfo.setEmail(email);
        userInfo.setGender(gender);
        LambdaUpdateWrapper<UserInfo> userInfoUpdateWrapper = new LambdaUpdateWrapper<>();
        userInfoUpdateWrapper.eq(UserInfo::getUserId, id);
        boolean userInfoUpdate = userInfoService.update(userInfo, userInfoUpdateWrapper);

        LambdaQueryWrapper<Role> roleLambdaQueryWrapper = new LambdaQueryWrapper<>();
        roleLambdaQueryWrapper.eq(Role::getRoleKey, roleKey);
        Role role = roleService.getOne(roleLambdaQueryWrapper);
        log.debug(role.toString());
        Long roleId = role.getId();

        UserRole userRole = new UserRole();
        userRole.setUserId((long) id);
        userRole.setRoleId(roleId);
        UpdateWrapper<UserRole> userRoleUpdateWrapper = new UpdateWrapper<>();
        userRoleUpdateWrapper.eq("user_id", id);
        boolean userRoleUpdate = userRoleService.update(userRole, userRoleUpdateWrapper);

        if (userUpdate && userInfoUpdate && userRoleUpdate) {
            return CommonResult.success(null);
        } else return CommonResult.failed();
    }

    /**
     * 后台管理 添加人员
     */
    @PostMapping("addOne")
    @PreAuthorize("hasAuthority('super_admin')")
    public CommonResult addOne(@RequestBody Map params) {
        log.debug(String.valueOf(params));

        String username = (String) params.get("username");
        int age = Integer.parseInt((String) params.get("age"));
        String gender = (String) params.get("gender");
        String password = (String) params.get("password");
        String email = (String) params.get("email");
        String roleKey = (String) params.get("roleKey");
        boolean status = (boolean) params.get("status");

        // 添加User
        User user = new User();
        user.setUsername(username);
        user.setStatus(status ? "1" : "0");
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encode = passwordEncoder.encode(password);
        user.setPassword(encode);
        try {
            log.debug(user.toString());
            userService.save(user);
        } catch (Exception e) {
            log.error(e.getMessage());
            return CommonResult.failed("用户名已存在");
        }
        // 获取UserID
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("username", username);
        User one = userService.getOne(userQueryWrapper);
        Long userId = one.getId();

        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(userId);
        userInfo.setAge(age);
        userInfo.setEmail(email);
        userInfo.setGender(gender);
        log.debug(userInfo.toString());
        userInfoService.save(userInfo);


        // 获取role ID
        LambdaQueryWrapper<Role> roleLambdaQueryWrapper = new LambdaQueryWrapper<>();
        roleLambdaQueryWrapper.eq(Role::getRoleKey, roleKey);
        Role role = roleService.getOne(roleLambdaQueryWrapper);
        log.debug(role.toString());
        Long roleId = role.getId();

        // 插入UserRole
        UserRole userRole = new UserRole();
        userRole.setUserId(userId);
        userRole.setRoleId(roleId);
        log.debug(userRole.toString());
        userRoleService.save(userRole);

        return CommonResult.success(null);
    }

    /**
     * 后台管理 切换人员状态
     */
    @GetMapping("/setStatus")
    @PreAuthorize("hasAuthority('super_admin')")
    public CommonResult setUserStatus(@RequestParam("id") String id, @RequestParam("status") boolean status) {
        log.debug("id --- " + id + " === status --- " + status);
        User user = new User();
        user.setId(Long.valueOf(id));
        user.setStatus(status ? "1" : "0");
        userService.updateById(user);
        return CommonResult.success(null);
    }

    /**
     * 后台管理 删除人员
     */
    @GetMapping("/deleteById")
    @PreAuthorize("hasAuthority('super_admin')")
    public CommonResult deleteById(@RequestParam("id") String id) {
        userService.removeById(id);

        QueryWrapper<UserInfo> userInfoQueryWrapper = new QueryWrapper<>();
        userInfoQueryWrapper.eq("user_id", id);
        userInfoService.remove(userInfoQueryWrapper);

        QueryWrapper<UserRole> userRoleQueryWrapper = new QueryWrapper<>();
        userRoleQueryWrapper.eq("user_id", id);
        userRoleService.remove(userRoleQueryWrapper);

        return CommonResult.success(null);
    }

}
