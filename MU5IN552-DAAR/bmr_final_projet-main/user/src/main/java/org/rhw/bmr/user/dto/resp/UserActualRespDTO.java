package org.rhw.bmr.user.dto.resp;

import lombok.Data;

@Data
public class UserActualRespDTO {
    /**
     * 用户ID
     */
    private long id;
    /**
     * 用户名
     */
    private String username;

    /**
     * 邮箱地址
     */
    private String mail;
    /**
     * 手机号码
     */
    private String phone;
}
