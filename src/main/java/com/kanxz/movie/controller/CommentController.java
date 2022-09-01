package com.kanxz.movie.controller;


import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kanxz.movie.common.api.CommonResult;
import com.kanxz.movie.dto.CommentDTO;
import com.kanxz.movie.service.CommentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 评论表 前端控制器
 * </p>
 *
 * @author kanxz
 * @since 2022-04-01
 */
@RestController
@RequestMapping("/comment")
@Slf4j
public class CommentController {

    @Autowired
    private CommentService commentService;

    /**
     * 管理员 获取所有评论
     */
    @PostMapping("/getAll")
    @PreAuthorize("hasAuthority('comment')")
    public CommonResult getAllComment(@RequestBody Map queryForm) {
        String name = (String) queryForm.get("query");
        int pageNum = (int) queryForm.get("pageNum");
        if (name.equals("")) name = ".*";

        Page<CommentDTO> page = new Page<>(pageNum, 10);
        IPage<CommentDTO> iPage = commentService.getAllComments(page, name);

        List<CommentDTO> commentList = iPage.getRecords();
        long total = iPage.getTotal();

        JSONObject result = JSONUtil.createObj();
        result.putOnce("commentList", commentList);
        result.putOnce("total", total);
        return CommonResult.success(result);
    }

    /**
     * 管理员 删除评论
     */
    @GetMapping("/deleteByIdAdmin")
    @PreAuthorize("hasAuthority('comment')")
    public CommonResult adminDeleteComment(@RequestParam("id") Long id) {
        log.debug(id.toString());
        commentService.removeById(id);
        return CommonResult.success(null);
    }


    /**
     * 用户 获取个人评论
     */
    @PostMapping("/getById")
    @PreAuthorize("hasAuthority('user')")
    public CommonResult getUserComments(@RequestBody Map param) {
        log.debug(param.toString());
        String username = (String) param.get("username");
        int pageNum = (int) param.get("pageNum");

        Page<CommentDTO> page = new Page<>(pageNum, 10);
        IPage<CommentDTO> iPage = commentService.getAllComments(page, username);

        List<CommentDTO> commentList = iPage.getRecords();
        long total = iPage.getTotal();

        JSONObject result = JSONUtil.createObj();
        result.putOnce("commentList", commentList);
        result.putOnce("total", total);
        return CommonResult.success(result);
    }

    /**
     * 用户 删除个人评论
     */
    @GetMapping("/deleteById")
    @PreAuthorize("hasAuthority('user')")
    public CommonResult deleteUserComment(@RequestParam("id") Long id) {
        log.debug(String.valueOf(id));
        commentService.removeById(id);
        return CommonResult.success(null);
    }
}
