package com.kanxz.movie.common.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 处理空指针的异常
     */
    @ExceptionHandler(value = NullPointerException.class)
    @ResponseBody
    public CommonResult exceptionHandler(NullPointerException e) {
        log.error("发生空指针异常！原因是:" + e.getMessage());
        return CommonResult.failed(ResultCode.VALIDATE_FAILED);
    }

    /**
     * 无权限访问异常
     */
    @ExceptionHandler(value = AccessDeniedException.class)
    @ResponseBody
    public CommonResult exceptionHandler(AccessDeniedException e) {
        log.error("没有访问权限：{}", e.getMessage());
        return CommonResult.forbidden(null);
    }


    /**
     * 处理其他异常
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public CommonResult exceptionHandler(Exception e) {
        log.error("未知异常！原因是:" + e);
        return CommonResult.failed(ResultCode.FAILED);
    }
}
