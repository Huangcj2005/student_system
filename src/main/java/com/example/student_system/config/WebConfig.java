package com.example.student_system.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer
{
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry)
    {
        // 将 域名/files/... 映射到 对应地址获取文件
        registry.addResourceHandler("/files/**")
                .addResourceLocations("file:D:/uploads");
    }
}
