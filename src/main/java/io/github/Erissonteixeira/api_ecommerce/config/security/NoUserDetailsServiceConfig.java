package io.github.Erissonteixeira.api_ecommerce.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;

@Configuration
public class NoUserDetailsServiceConfig {

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            throw new UnsupportedOperationException(
                    "UserDetailsService ainda não implementado. Use /auth/login quando a Fase 2 estiver pronta."
            );
        };
    }
}