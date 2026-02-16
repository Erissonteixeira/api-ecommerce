package io.github.Erissonteixeira.api_ecommerce.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.OffsetDateTime;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<ErrorResponse> tratarRecursoNaoEncontrado(
            RecursoNaoEncontradoException ex,
            HttpServletRequest request
    ) {
        return build(HttpStatus.NOT_FOUND, ex.getMessage(), request.getRequestURI(), null);
    }

    @ExceptionHandler(NegocioException.class)
    public ResponseEntity<ErrorResponse> tratarNegocio(
            NegocioException ex,
            HttpServletRequest request
    ) {
        return build(HttpStatus.BAD_REQUEST, ex.getMessage(), request.getRequestURI(), null);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> tratarValidacaoBody(
            MethodArgumentNotValidException ex,
            HttpServletRequest request
    ) {
        List<FieldErrorResponse> fieldErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(this::toFieldError)
                .toList();

        return build(HttpStatus.BAD_REQUEST, "Dados inválidos", request.getRequestURI(), fieldErrors);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> tratarValidacaoParametros(
            ConstraintViolationException ex,
            HttpServletRequest request
    ) {
        List<FieldErrorResponse> fieldErrors = ex.getConstraintViolations()
                .stream()
                .map(this::toConstraintFieldError)
                .toList();

        return build(HttpStatus.BAD_REQUEST, "Dados inválidos", request.getRequestURI(), fieldErrors);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> tratarJsonInvalido(
            HttpMessageNotReadableException ex,
            HttpServletRequest request
    ) {
        return build(HttpStatus.BAD_REQUEST, "JSON inválido", request.getRequestURI(), null);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> tratarViolacaoIntegridade(
            DataIntegrityViolationException ex,
            HttpServletRequest request
    ) {
        return build(HttpStatus.CONFLICT, "Conflito de dados", request.getRequestURI(), null);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> tratarErroGenerico(
            Exception ex,
            HttpServletRequest request
    ) {
        return build(HttpStatus.INTERNAL_SERVER_ERROR, "Erro interno inesperado", request.getRequestURI(), null);
    }

    private FieldErrorResponse toFieldError(FieldError fe) {
        return FieldErrorResponse.builder()
                .field(fe.getField())
                .message(fe.getDefaultMessage())
                .build();
    }

    private FieldErrorResponse toConstraintFieldError(ConstraintViolation<?> cv) {
        String field = cv.getPropertyPath() != null ? cv.getPropertyPath().toString() : "param";
        return FieldErrorResponse.builder()
                .field(field)
                .message(cv.getMessage())
                .build();
    }

    private ResponseEntity<ErrorResponse> build(
            HttpStatus status,
            String message,
            String path,
            List<FieldErrorResponse> fieldErrors
    ) {
        ErrorResponse body = ErrorResponse.builder()
                .timestamp(OffsetDateTime.now())
                .status(status.value())
                .error(status.getReasonPhrase())
                .message(message)
                .path(path)
                .fieldErrors(fieldErrors == null || fieldErrors.isEmpty() ? null : fieldErrors)
                .build();

        return ResponseEntity.status(status).body(body);
    }
}
