package br.com.oficina.orcamento.dto;

import br.com.oficina.orcamento.model.Veiculo;

public record VeiculoDTO(
        Long id,
        String placa,
        String marca,
        String modelo,
        Integer ano,
        String cor,
        Long clienteId
) {
    public VeiculoDTO(Veiculo v) {
        this(
                v.getId(),
                v.getPlaca(),
                v.getMarca(),
                v.getModelo(),
                v.getAno(),
                v.getCor(),
                v.getCliente() != null ? v.getCliente().getId() : null
        );
    }
}
