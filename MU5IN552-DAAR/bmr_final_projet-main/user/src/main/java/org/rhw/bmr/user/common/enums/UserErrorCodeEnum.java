package org.rhw.bmr.user.common.enums;

import org.rhw.bmr.user.common.convention.errorcode.IErrorCode;

public enum UserErrorCodeEnum implements IErrorCode {

    USER_NULL("B000200", "User information does not exist"),
    USER_NAME_EXIST("B000201", "User already exists"),
    USER_SAVE_ERROR("B000202", "User creation failed"),
    USER_REGISTER_RATE_ERROR("B000203", "The maximum number of parallel registrations has been reached."),
    USER_LOGIN_REPEAT("B000204", "User repeatedly logs in"),
    USER_NOT_ONLINE("B000205", "The user is not logged in."),
    USER_TOKEN_FAIL("A00200", "User token verification failed");

    private final String code;
    private final String message;

    UserErrorCodeEnum(String code, String message) {
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
