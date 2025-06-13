// src/main/java/br/com/oficina/orcamento/dto/OrcamentoDTO.java
package br.com.oficina.orcamento.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * DTO de orçamento, com helper totalServicos() == totalBruto.
 */
public record OrcamentoDTO(
        Long id,
        LocalDate data,
        BigDecimal totalBruto,
        Integer descontoPercentual,
        BigDecimal totalLiquido,
        Integer parcelas,
        Long veiculoId,
        Long clienteId,
        List<Long> servicosIds
) {
    /**
     * Para os testes: soma dos valores dos serviços (= totalBruto).
     */
    public BigDecimal totalServicos() {
        return totalBruto;
    }
}
