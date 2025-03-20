package org.rhw.bmr.user.service;


import com.baomidou.mybatisplus.extension.service.IService;
import org.rhw.bmr.user.dto.req.UserLoginReqDTO;
import org.rhw.bmr.user.dto.req.UserRegisterReqDTO;
import org.rhw.bmr.user.dto.req.UserUpdateReqDTO;
import org.rhw.bmr.user.dto.resp.UserLoginRespDTO;
import org.rhw.bmr.user.dto.resp.UserRespDTO;
import org.rhw.bmr.user.dao.entity.UserDO;

public interface UserService extends IService<UserDO> {

    UserRespDTO getUserByUsername(String username);

    Boolean availableUsername(String username);

    void register(UserRegisterReqDTO requestParam);

    UserLoginRespDTO login(UserLoginReqDTO requestParam);

    Boolean checkLogin(String username, String token);

    void update(UserUpdateReqDTO requestParam);

    void logout(String username, String token);
}
