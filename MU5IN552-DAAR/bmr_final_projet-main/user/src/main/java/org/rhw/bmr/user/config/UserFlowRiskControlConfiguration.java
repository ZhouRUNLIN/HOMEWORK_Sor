package org.rhw.bmr.user.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * User operation traffic risk control profile
 */
@Data
@Component
@ConfigurationProperties(prefix = "bmr.flow-limit")
public class UserFlowRiskControlConfiguration {


    private Boolean enable;


    private String timeWindow;

    private Long maxAccessCount;
}