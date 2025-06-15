package br.com.oficina.orcamento.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record OrcamentoDTO(
        Long id,
        LocalDate data,
        BigDecimal totalBruto,
        Integer descontoPercentual,
        BigDecimal totalLiquido,
        Integer parcelas,
        Long clienteId,
        String nomeCliente,
        Long veiculoId,
        String placaVeiculo,
        String modeloVeiculo,
        List<ItemOrcamentoDTO> itens
) {}
