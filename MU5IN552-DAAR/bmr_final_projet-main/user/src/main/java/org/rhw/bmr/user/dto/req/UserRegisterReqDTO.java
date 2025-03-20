package org.rhw.bmr.user.dto.req;

import lombok.Data;

@Data
public class UserRegisterReqDTO {
    /**
     * 用户ID
     */
    private long id;
    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    private String password;
    /**
     * 邮箱地址
     */
    private String mail;
    /**
     * 手机号码
     */
    private String phone;
}
