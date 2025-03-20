package org.rhw.bmr.user.config;


import org.rhw.bmr.user.common.biz.user.UserFlowRiskControlFilter;
import org.rhw.bmr.user.common.biz.user.UserTransmitFilter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * User configuration automatic assembly
 * */

@Configuration
public class UserConfiguration {


    @Bean
    public FilterRegistrationBean<UserTransmitFilter> globalUserTransmitFilter() {

        FilterRegistrationBean<UserTransmitFilter> registration = new FilterRegistrationBean<>();


        registration.setFilter(new UserTransmitFilter());

        registration.addUrlPatterns("/*");

        registration.setOrder(0);

        return registration;
    }


    @Bean
    @ConditionalOnProperty(name = "bmr.flow-limit.enable", havingValue = "true")
    public FilterRegistrationBean<UserFlowRiskControlFilter> globalUserFlowRiskControlFilter(
            StringRedisTemplate stringRedisTemplate,
            UserFlowRiskControlConfiguration userFlowRiskControlConfiguration
    ) {

        FilterRegistrationBean<UserFlowRiskControlFilter> registration = new FilterRegistrationBean<>();

        registration.setFilter(new UserFlowRiskControlFilter(stringRedisTemplate, userFlowRiskControlConfiguration));

        registration.addUrlPatterns("/*");

        registration.setOrder(10);

        return registration;
    }
}
