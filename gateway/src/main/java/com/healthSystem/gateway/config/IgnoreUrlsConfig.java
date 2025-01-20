package com.healthSystem.gateway.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * 白名单资源路径配置
 */

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "secure.ignored")
public class IgnoreUrlsConfig {

    private List<String> urls = new ArrayList<>();
    public List<String> getUrls(){
        return urls;
    }
    public void setUrls(List<String> urls) {
        this.urls = urls;
    }
}
