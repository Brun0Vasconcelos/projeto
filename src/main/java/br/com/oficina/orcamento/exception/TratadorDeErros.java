// src/main/java/br/com/oficina/orcamento/exception/TratadorDeErros.java
package br.com.oficina.orcamento.exception;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class TratadorDeErros {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroValidacao> tratarErroValidacao(MethodArgumentNotValidException ex) {
        Map<String, String> erros = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        FieldError::getDefaultMessage
                ));
        return ResponseEntity
                .badRequest()
                .body(new ErroValidacao("Campos inválidos", erros));
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErroSimples> tratarErro404(EntityNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErroSimples(ex.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErroSimples> tratarErroDeNegocio(IllegalArgumentException ex) {
        return ResponseEntity
                .badRequest()
                .body(new ErroSimples("Erro de negócio: " + ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErroSimples> tratarErroGeral(Exception ex) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErroSimples("Erro interno do servidor: " + ex.getMessage()));
    }

    public record ErroSimples(String mensagem) {}
}
