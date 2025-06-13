// src/main/java/br/com/oficina/orcamento/exception/ErroValidacao.java
package br.com.oficina.orcamento.exception;

import java.util.Map;

public record ErroValidacao(
        String mensagem,
        Map<String, String> erros
) {}
