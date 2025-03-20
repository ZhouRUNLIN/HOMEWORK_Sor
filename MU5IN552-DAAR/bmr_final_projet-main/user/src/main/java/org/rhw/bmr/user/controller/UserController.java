package org.rhw.bmr.user.controller;

import cn.hutool.core.bean.BeanUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.rhw.bmr.user.common.convention.result.Result;
import org.rhw.bmr.user.common.convention.result.Results;
import org.rhw.bmr.user.dto.req.UserLoginReqDTO;
import org.rhw.bmr.user.dto.req.UserRegisterReqDTO;
import org.rhw.bmr.user.dto.req.UserUpdateReqDTO;
import org.rhw.bmr.user.dto.resp.UserActualRespDTO;
import org.rhw.bmr.user.dto.resp.UserLoginRespDTO;
import org.rhw.bmr.user.dto.resp.UserRespDTO;
import org.rhw.bmr.user.service.UserService;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;


    @GetMapping("/api/bmr/user/v1/user/{username}")
    public Result<UserRespDTO> getUserByUsername(@PathVariable("username") String username) {
        UserRespDTO result =  userService.getUserByUsername(username);
        return Results.success(result);
    }

    @GetMapping("/api/bmr/user/v1/actual/user/{username}")
    public Result<UserActualRespDTO> getActualUserByUsername(@PathVariable("username") String username) {
        return Results.success(BeanUtil.toBean(userService.getUserByUsername(username), UserActualRespDTO.class));
    }


    @GetMapping("/api/bmr/user/v1/user/has-username")
    public Result<Boolean> hasUsername(@RequestParam("username") String username) {
        return Results.success(userService.availableUsername(username));
    }


    @PostMapping("/api/bmr/user/v1/user")
    public Result<Void> register(@RequestBody UserRegisterReqDTO requestParam){
        userService.register(requestParam);
        return Results.success();
    }

    @PostMapping("/api/bmr/user/v1/user/login")
    public Result<UserLoginRespDTO> login(@RequestBody UserLoginReqDTO requestParam){
        UserLoginRespDTO result = userService.login(requestParam);
        return Results.success(result);
    }


    @GetMapping("/api/bmr/user/v1/user/check-login")
    public Result<Boolean> checkLogin(@RequestParam("username") String username, @RequestParam("token") String token){

        return Results.success(userService.checkLogin(username, token));
    }


    @PutMapping("/api/bmr/user/v1/user")
    public Result<Void> update(@RequestBody UserUpdateReqDTO requestParam){
        userService.update(requestParam);
        return Results.success();
    }

    @DeleteMapping("/api/bmr/user/v1/user/logout")
    public Result<Void> logout(@RequestParam("username") String username, @RequestParam("token") String token){

        userService.logout(username, token);
        return Results.success();
    }
}
