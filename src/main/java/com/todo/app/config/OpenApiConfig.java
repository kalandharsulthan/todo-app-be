package com.todo.app.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI todoOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Todo REST API")
                        .description("A RESTful API for managing Todo items. Built with Spring Boot 3.5, " +
                                "backed by Supabase PostgreSQL, and deployed on Railway.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Kalandhar")
                                .email("s.kalandhar@gmail.com"))
                        .license(new License().name("MIT")))
                .servers(List.of(new Server().url("/").description("Current server")));
    }
}
