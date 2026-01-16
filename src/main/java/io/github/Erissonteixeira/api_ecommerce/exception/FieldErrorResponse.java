package io.github.Erissonteixeira.api_ecommerce.exception;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FieldErrorResponse {
    private String field;
    private String message;
}
