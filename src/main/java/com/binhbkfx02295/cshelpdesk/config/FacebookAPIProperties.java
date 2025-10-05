package com.binhbkfx02295.cshelpdesk.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "facebook.api")
public class FacebookAPIProperties {
    private String appId;
    private String appSecret;
    private String pageId;
    private String defaultShortToken;
}