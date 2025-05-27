package com.marin.UserService.config;

import com.marin.UserService.interceptor.RateLimitInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Establishes the configuration of interceptors to be used for rate limiting of request for non-authenticated users
 * in the /auth endpoints
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final RateLimitInterceptor rateLimitInterceptor;

    @Autowired
    public WebConfig(RateLimitInterceptor limitInterceptor){
        this.rateLimitInterceptor = limitInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(rateLimitInterceptor)
                .addPathPatterns("/auth/**")
                .excludePathPatterns("/users/**");
    }
}
