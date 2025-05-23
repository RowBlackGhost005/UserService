package com.marin.UserService.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *  Setup of the OpenAPI to set the information to be shown in Swagger
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI(){
        return new OpenAPI().info(
                new Info()
                        .title("Secure Restful API")
                        .version("1.0")
                        .description("Small Rest API secured by Spring Security using JWT"));
    }
}
