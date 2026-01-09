package io.github.Erissonteixeira.api_ecommerce.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "API E-commerce",
                version = "v1",
                description = "API do projeto E-commerce"
        )
)
public class OpenApiConfig {
}