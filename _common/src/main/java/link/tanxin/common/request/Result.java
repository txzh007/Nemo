package link.tanxin.common.request;

import lombok.Data;

/**
 * 返回值
 *
 * @author tan
 */
@Data
public class Result<T> {
    /**
     * 响应码
     */
    private int code;
    /**
     * 响应消息
     */
    private String message;
    /**
     * 数据单元
     */
    private T data;

    public Result(T data) {
        this.data = data;
    }

    public Result(int code, String message) {
        this.code = code;
        this.message = message;
    }


    /**
     * 成功时候的调用
     */

    public static <T> Result<T> succeeded() {
        return succeeded(CodeMessage.SUCCESS.getCode(), null, CodeMessage.SUCCESS.getMessage());
    }

    public static <T> Result<T> succeeded(T data) {
        return succeeded(CodeMessage.SUCCESS.getCode(), data, CodeMessage.SUCCESS.getMessage());
    }

    public static <T> Result<T> succeeded(T data, String message) {
        Result<T> result = new Result<>(data);
        result.setCode(CodeMessage.SUCCESS.getCode());
        result.setMessage(message);
        return result;
    }

    public static <T> Result<T> succeeded(int code, T data, String message) {
        Result<T> result = new Result<>(data);
        result.setCode(code);
        result.setMessage(message);
        return result;
    }


    /**
     * 失败时候的调用
     */
    public static <T> Result<T> failed() {
        return failed(CodeMessage.SERVER_ERROR);
    }

    public static <T> Result<T> failed(CodeMessage codeMessage) {
        return new Result<>(codeMessage.getCode(), codeMessage.getMessage());
    }

    public static <T> Result<T> failed(String message) {
        return new Result<>(CodeMessage.SERVER_ERROR.getCode(), message);
    }

    public static <T> Result<T> failed(int code, String message) {
        return new Result<>(code, message);
    }


}
