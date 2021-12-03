package fun.onysakura.algorithm.spring.common.result;

import fun.onysakura.algorithm.spring.common.constants.ResultCode;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
public class Result<T> implements Serializable {

    private int code;
    private String message;
    private T data;

    public Result(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public Result(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public Result(ResultCode resultCode) {
        this.code = resultCode.getCode();
        this.message = resultCode.getMessage();
    }

    public Result(ResultCode resultCode, T data) {
        this.code = resultCode.getCode();
        this.message = resultCode.getMessage();
        this.data = data;
    }

    public static <T> Result<T> ok() {
        return new Result<>(ResultCode.OK);
    }

    public static <T> Result<T> ok(T t) {
        return new Result<>(ResultCode.OK, t);
    }

    public static <T> Result<T> fail() {
        return new Result<>(ResultCode.FAIL);
    }
}
