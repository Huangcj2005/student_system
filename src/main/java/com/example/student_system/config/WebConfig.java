package com.example.student_system.config;

import com.example.student_system.filter.JwtFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private JwtFilter jwtFilter;

    // TODO: 这里的过滤需要做一定的更改，控制哪些才是需要授权的
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtFilter)
                .addPathPatterns("/**") // 拦截所有请求
                .excludePathPatterns(
                        "/user/verification-codes", // 验证码接口
                        "/user/login",    // 登录接口
                        "/user/register", // 注册接口
                        "/error",             // 错误页面
                        "/favicon.ico"        // 网站图标
                );
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry){
        registry.addResourceHandler("/avatar/**")
                .addResourceLocations("file:" + System.getProperty("user.dir") + "/avatar/");
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
