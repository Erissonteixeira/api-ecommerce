package io.github.Erissonteixeira.api_ecommerce.domain.auth.dto.response;

public class TokenResponseDto {

    private String token;
    private String type = "Bearer";

    public TokenResponseDto() {
    }

    public TokenResponseDto(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}