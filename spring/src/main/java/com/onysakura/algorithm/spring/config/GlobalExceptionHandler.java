package com.onysakura.algorithm.spring.config;

import com.onysakura.algorithm.spring.common.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {

    @ExceptionHandler({Exception.class})
    public Result<?> exception(Exception e, HttpServletRequest request, HttpServletResponse response) {
        log.warn("GlobalException: {}", e.getMessage());
        return Result.fail();
    }
}