package com.onysakura.algorithm.core.mode.strategy.dto.base;

public enum ErrorCode {

    SUCCESS("10000", "请求成功"),
    OPERATION_ERROR("20001", "业务处理失败");

    private final String code;
    private final String msg;

    ErrorCode(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
