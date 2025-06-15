package br.com.oficina.orcamento.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ItemOrcamentoDTO(
        Long id,
        String descricao,
        LocalDate dataEntrada,
        LocalDate dataEntrega,
        BigDecimal valor
) {}
