package org.rhw.bmr.project.common.convention.errorcode;

/**
 * Basic error code definition
 */
public enum BaseErrorCode implements IErrorCode {

    CLIENT_ERROR("A000001", "client error"),

    USER_REGISTER_ERROR("A000100", "User registration error"),
    USER_NAME_VERIFY_ERROR("A000110", "User name verification failed"),
    USER_NAME_EXIST_ERROR("A000111", "Username already exists"),
    USER_NAME_SENSITIVE_ERROR("A000112", "Username contains sensitive words"),
    USER_NAME_SPECIAL_CHARACTER_ERROR("A000113", "Username contains special characters"),
    PASSWORD_VERIFY_ERROR("A000120", "Password verification failed"),
    PASSWORD_SHORT_ERROR("A000121", "Password is not long enough"),
    PHONE_VERIFY_ERROR("A000151", "Failed to verify phone format"),

    IDEMPOTENT_TOKEN_NULL_ERROR("A000200", "The idempotent token is empty."),
    IDEMPOTENT_TOKEN_DELETE_ERROR("A000201", "The idempotent token has already been used or has expired."),

    SERVICE_ERROR("B000001", "System execution error"),

    SERVICE_TIMEOUT_ERROR("B000100", "system execution timeout"),

    REMOTE_ERROR("C000001", "Error calling third-party service");

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
