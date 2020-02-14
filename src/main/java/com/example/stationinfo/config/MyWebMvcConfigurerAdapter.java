package com.example.stationinfo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
@Configuration
public class MyWebMvcConfigurerAdapter implements WebMvcConfigurer {
    //获取拦截器的Bean
    @Bean
    public HandlerInterceptor getBackInterceptor() {
        return new BackInterceptor();
    }

    /**
     * 重写addInterceptors方法
     * addPathPatterns：需要拦截的访问路径
     * excludePathPatterns：不需要拦截的路径，
     * String数组类型可以写多个用","分割
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(getBackInterceptor()).addPathPatterns("/**").excludePathPatterns("/index.html", "/css/**","/images/**","/js/**","/lib/**","/login");
    }

}