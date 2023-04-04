package com.example.mall_study.config;


import org.springframework.boot.context.properties.ConfigurationProperties;


@ConfigurationProperties("secure.ignored")
public class IgnoreUrlsConfig {
    private String[] urls;

    public String[] getUrls() {
        return urls;
    }

    public void setUrls(String[] urls) {
        this.urls = urls;
    }
}
