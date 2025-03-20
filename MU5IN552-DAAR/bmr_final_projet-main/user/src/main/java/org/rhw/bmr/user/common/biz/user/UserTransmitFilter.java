package org.rhw.bmr.user.common.biz.user;

import cn.hutool.core.util.StrUtil;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.rhw.bmr.user.common.biz.user.UserContext;
import org.rhw.bmr.user.common.biz.user.UserInfoDTO;

/**
 * User information transmission filter
 * Extract user information (from the request header).
 * Bind user information to the thread context.
 * Clean up the thread context to ensure thread safety.
 * */

@RequiredArgsConstructor

public class UserTransmitFilter implements Filter {


    @SneakyThrows
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) {

        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;


        String username = httpServletRequest.getHeader("username");


        if (StrUtil.isNotBlank(username)) {
            String userId = httpServletRequest.getHeader("userId");
            String token = httpServletRequest.getHeader("token");

            UserInfoDTO userInfoDTO = new UserInfoDTO(userId, username, token);

            UserContext.setUser(userInfoDTO);
        }

        try {
            filterChain.doFilter(servletRequest, servletResponse);
        } finally {
            UserContext.removeUser();
        }
    }
}
