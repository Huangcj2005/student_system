package com.example.student_system.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
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

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")  // 允许所有路径进行跨域
                .allowedOrigins("http://localhost:3000")  // 允许来自前端地址的请求
                .allowedMethods("GET", "POST", "PUT", "DELETE")  // 允许的请求方法
                .allowedHeaders("*")  // 允许所有请求头
                .allowCredentials(true)  // 允许携带凭证（cookies）
                .maxAge(3600);  // 预检请求的缓存时间，单位秒
    }
}
