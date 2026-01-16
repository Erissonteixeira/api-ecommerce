package io.github.Erissonteixeira.api_ecommerce.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

        @Bean
        public OpenAPI openAPI() {
                return new OpenAPI()
                        .info(new Info()
                                .title("API E-commerce")
                                .description("API REST para gestão de produtos e carrinho de compras.")
                                .version("v1")
                                .contact(new Contact()
                                        .name("Erisson Teixeira")
                                        .email("seu-email@exemplo.com")
                                )
                                .license(new License()
                                        .name("MIT")
                                        .url("https://opensource.org/licenses/MIT")
                                )
                        );
        }

        @Bean
        public GroupedOpenApi produtosApi() {
                return GroupedOpenApi.builder()
                        .group("Produtos")
                        .pathsToMatch("/produtos/**")
                        .build();
        }

        @Bean
        public GroupedOpenApi carrinhosApi() {
                return GroupedOpenApi.builder()
                        .group("Carrinhos")
                        .pathsToMatch("/carrinhos/**")
                        .build();
        }
}
