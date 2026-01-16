package io.github.Erissonteixeira.api_ecommerce.exception;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.OffsetDateTime;
import java.util.List;

@Schema(name = "ErrorResponse")
public class OpenApiErrorSchemas {

    @Schema(description = "Instante do erro")
    public OffsetDateTime timestamp;

    @Schema(description = "Status HTTP", example = "400")
    public Integer status;

    @Schema(description = "Nome do erro HTTP", example = "Bad Request")
    public String error;

    @Schema(description = "Mensagem amigável", example = "Dados inválidos")
    public String message;

    @Schema(description = "Caminho do endpoint", example = "/produtos/10")
    public String path;

    @Schema(description = "Erros de validação (quando houver)")
    public List<FieldError> fieldErrors;

    @Schema(name = "FieldError")
    public static class FieldError {
        @Schema(example = "nome")
        public String field;

        @Schema(example = "não deve estar em branco")
        public String message;
    }
}
