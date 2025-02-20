package com.xx.xiuxianserver.Config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Swagger 配置类
 * @author  jiangzk
 * 2025/1/24 22:02
*/
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(info())
                .servers(servers())
                .components(components())
                .addSecurityItem(securityRequirement());
    }

    /**
     * Swagger基本信息
     */
    private Info info() {
        return new Info()
                .title("文字修仙接口文档")
                .description("测试API文档")
                .version("1.0")
                .contact(new Contact()
                        .name("【欢迎来访】")
                        .url("https://github.com/Bron-ya/xiuxian"));
    }

    /**
     * 设置多地址下的访问
     */
    private List<Server> servers() {
        return List.of(
                new Server().url("http://localhost:8081/").description("本地环境")
                // new Server()...
        );
    }

    /**
     * 集成认证信息，在页面中能进行登录认证（与 SwaggerJwtAuthenticationFilter 相配合）
     */
    private Components components() {
        return new Components()
                .addSecuritySchemes("Bearer Authorization",
                        new SecurityScheme()
                                .name("Bearer 认证")
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .in(SecurityScheme.In.HEADER)
                );
    }

    private SecurityRequirement securityRequirement() {
        return new SecurityRequirement().addList("Bearer Authorization");
    }
}
