package io.github.Erissonteixeira.api_ecommerce.exception;

import io.swagger.v3.oas.annotations.media.Schema;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import java.time.OffsetDateTime;
import java.util.List;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(name = "ErrorResponse")
public class ErrorResponse {

    @Schema(description = "Instante do erro")
    private OffsetDateTime timestamp;

    @Schema(description = "Status HTTP", example = "400")
    private Integer status;

    @Schema(description = "Nome do erro HTTP", example = "Bad Request")
    private String error;

    @Schema(description = "Mensagem amigável", example = "Dados inválidos")
    private String message;

    @Schema(description = "Caminho do endpoint", example = "/produtos/10")
    private String path;

    @Schema(description = "Erros de validação (quando houver)")
    private List<FieldErrorResponse> fieldErrors;
}
