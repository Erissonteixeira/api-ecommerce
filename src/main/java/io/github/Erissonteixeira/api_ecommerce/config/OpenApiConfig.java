package io.github.Erissonteixeira.api_ecommerce.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

        private static final String GROUP_USUARIOS = "Usuarios";
        private static final String GROUP_PRODUTOS = "Produtos";
        private static final String GROUP_CARRINHOS = "Carrinhos";

        private static final String SECURITY_SCHEME_NAME = "bearerAuth";

        @Bean
        public OpenAPI openAPI() {
                return new OpenAPI()

                        .info(new Info()
                                .title("API E-commerce")
                                .description("API REST para gestão de usuários, produtos, carrinho e pedidos.")
                                .version("v1")
                                .contact(new Contact()
                                        .name("Erisson Teixeira")
                                        .email("seu-email@exemplo.com")
                                )
                                .license(new License()
                                        .name("MIT")
                                        .url("https://opensource.org/licenses/MIT")
                                )
                        )

                        .addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME_NAME))

                        .components(new Components()
                                .addSecuritySchemes(
                                        SECURITY_SCHEME_NAME,
                                        new SecurityScheme()
                                                .name("Authorization")
                                                .type(SecurityScheme.Type.HTTP)
                                                .scheme("bearer")
                                                .bearerFormat("JWT")
                                )
                        );
        }

        @Bean
        public GroupedOpenApi usuariosApi() {
                return GroupedOpenApi.builder()
                        .group(GROUP_USUARIOS)
                        .pathsToMatch("/usuarios/**")
                        .build();
        }

        @Bean
        public GroupedOpenApi produtosApi() {
                return GroupedOpenApi.builder()
                        .group(GROUP_PRODUTOS)
                        .pathsToMatch("/produtos/**")
                        .build();
        }

        @Bean
        public GroupedOpenApi carrinhosApi() {
                return GroupedOpenApi.builder()
                        .group(GROUP_CARRINHOS)
                        .pathsToMatch("/carrinhos/**")
                        .build();
        }
}