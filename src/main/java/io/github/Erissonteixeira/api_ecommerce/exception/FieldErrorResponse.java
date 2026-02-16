package io.github.Erissonteixeira.api_ecommerce.exception;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(name = "FieldError")
public class FieldErrorResponse {

    @Schema(example = "nome")
    private String field;

    @Schema(example = "não deve estar em branco")
    private String message;
}
