package io.github.Erissonteixeira.api_ecommerce.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

        private static final String GROUP_USUARIOS = "Usuarios";
        private static final String GROUP_PRODUTOS = "Produtos";
        private static final String GROUP_CARRINHOS = "Carrinhos";
        private static final String GROUP_AUTH = "Auth";
        private static final String GROUP_PEDIDOS = "Pedidos";

        private static final String SECURITY_SCHEME_NAME = "bearerAuth";

        @Bean
        public OpenAPI openAPI() {
                return new OpenAPI()
                        .info(new Info()
                                .title("API E-commerce")
                                .description("API REST para gestão de usuários, produtos, carrinho e pedidos.")
                                .version("v1")
                                .contact(new Contact().name("Erisson Teixeira").email("seu-email@exemplo.com"))
                                .license(new License().name("MIT").url("https://opensource.org/licenses/MIT"))
                        );
        }

        @Bean
        public OpenApiCustomizer jwtSecurityCustomizer() {
                return openApi -> {
                        if (openApi.getComponents() == null) {
                                openApi.setComponents(new Components());
                        }

                        if (openApi.getComponents().getSecuritySchemes() == null
                                || !openApi.getComponents().getSecuritySchemes().containsKey(SECURITY_SCHEME_NAME)) {

                                openApi.getComponents().addSecuritySchemes(
                                        SECURITY_SCHEME_NAME,
                                        new SecurityScheme()
                                                .type(SecurityScheme.Type.HTTP)
                                                .scheme("bearer")
                                                .bearerFormat("JWT")
                                                .name("Authorization")
                                );
                        }

                        if (openApi.getSecurity() == null
                                || openApi.getSecurity().stream().noneMatch(s -> s.containsKey(SECURITY_SCHEME_NAME))) {
                                openApi.addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME_NAME));
                        }
                };
        }

        @Bean
        public GroupedOpenApi usuariosApi(OpenApiCustomizer jwtSecurityCustomizer) {
                return GroupedOpenApi.builder()
                        .group(GROUP_USUARIOS)
                        .pathsToMatch("/usuarios/**")
                        .addOpenApiCustomizer(jwtSecurityCustomizer)
                        .build();
        }

        @Bean
        public GroupedOpenApi produtosApi(OpenApiCustomizer jwtSecurityCustomizer) {
                return GroupedOpenApi.builder()
                        .group(GROUP_PRODUTOS)
                        .pathsToMatch("/produtos/**")
                        .addOpenApiCustomizer(jwtSecurityCustomizer)
                        .build();
        }

        @Bean
        public GroupedOpenApi carrinhosApi(OpenApiCustomizer jwtSecurityCustomizer) {
                return GroupedOpenApi.builder()
                        .group(GROUP_CARRINHOS)
                        .pathsToMatch("/carrinho/**")
                        .addOpenApiCustomizer(jwtSecurityCustomizer)
                        .build();
        }

        @Bean
        public GroupedOpenApi authApi(OpenApiCustomizer jwtSecurityCustomizer) {
                return GroupedOpenApi.builder()
                        .group(GROUP_AUTH)
                        .pathsToMatch("/auth/**")
                        .addOpenApiCustomizer(jwtSecurityCustomizer)
                        .build();
        }

        @Bean
        public GroupedOpenApi pedidosApi(OpenApiCustomizer jwtSecurityCustomizer) {
                return GroupedOpenApi.builder()
                        .group(GROUP_PEDIDOS)
                        .pathsToMatch("/pedidos/**")
                        .addOpenApiCustomizer(jwtSecurityCustomizer)
                        .build();
        }
}