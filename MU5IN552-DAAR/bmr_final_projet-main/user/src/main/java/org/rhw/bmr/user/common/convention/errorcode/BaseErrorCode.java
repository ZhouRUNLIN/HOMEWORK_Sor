package org.rhw.bmr.user.common.convention.errorcode;

/**
 * 基础错误码定义
 */
public enum BaseErrorCode implements IErrorCode {

    // ========== 一级宏观错误码 客户端错误 ==========
    CLIENT_ERROR("A000001", "client error"),

    // ========== 二级宏观错误码 用户注册错误 ==========
    USER_REGISTER_ERROR("A000100", "User registration error"),
    USER_NAME_VERIFY_ERROR("A000110", "User name verification failed"),
    USER_NAME_EXIST_ERROR("A000111", "Username already exists"),
    USER_NAME_SENSITIVE_ERROR("A000112", "Username contains sensitive words"),
    USER_NAME_SPECIAL_CHARACTER_ERROR("A000113", "Username contains special characters"),
    PASSWORD_VERIFY_ERROR("A000120", "Password verification failed"),
    PASSWORD_SHORT_ERROR("A000121", "Password is not long enough"),
    PHONE_VERIFY_ERROR("A000151", "Failed to verify phone format"),

    // ========== 二级宏观错误码 系统请求缺少幂等Token ==========
    IDEMPOTENT_TOKEN_NULL_ERROR("A000200", "The idempotent token is empty."),
    IDEMPOTENT_TOKEN_DELETE_ERROR("A000201", "The idempotent token has already been used or has expired."),

    // ========== 一级宏观错误码 系统执行出错 ==========
    SERVICE_ERROR("B000001", "System execution error"),
    // ========== 二级宏观错误码 系统执行超时 ==========
    SERVICE_TIMEOUT_ERROR("B000100", "system execution timeout"),

    // ========== 一级宏观错误码 调用第三方服务出错 ==========
    REMOTE_ERROR("C000001", "Error calling third-party service"),

    // ========== 二级宏观错误码 系统请求操作频繁 ==========
    FLOW_LIMIT_ERROR("A000300", "The system is currently busy. Please try again later.");

    private final String code;

    private final String message;

    BaseErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String code() {
        return code;
    }

    @Override
    public String message() {
        return message;
    }
}