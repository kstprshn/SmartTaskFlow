package ru.java.teamProject.SmartTaskFlow.config;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .servers(
                        List.of(new Server().url("http://localhost:8080"))
                ).info(
                        new Info().title("SmartTaskFlow")
                                .version("1.0")
                                .description("A system for joint project management in kanban format")
                );
    }
}
