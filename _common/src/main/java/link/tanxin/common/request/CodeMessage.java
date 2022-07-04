package link.tanxin.common.request;

/**
 * @author tan
 */

public enum CodeMessage implements CodeEnum {
    /**
     * 通用枚举，各个系统模块可
     */
    SUCCESS(0, "成功"),
    LOGIN_FAILED(210, "登录失败"),
    SERVER_ERROR(500, "服务器内部错误");
    private final int code;
    private final String message;

    CodeMessage(int code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
