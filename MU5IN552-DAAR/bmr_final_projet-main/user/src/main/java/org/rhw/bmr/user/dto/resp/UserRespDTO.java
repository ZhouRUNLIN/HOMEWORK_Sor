package org.rhw.bmr.user.dto.resp;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import org.rhw.bmr.user.common.serialize.PhoneDesensitizationSerializer;

/**
 * 用户返回参数响应
 */
@Data
public class UserRespDTO {
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
    @JsonSerialize(using = PhoneDesensitizationSerializer.class)
    private String phone;
}
