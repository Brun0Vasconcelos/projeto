// src/main/java/br/com/oficina/orcamento/dto/VeiculoDTO.java
package br.com.oficina.orcamento.dto;

import br.com.oficina.orcamento.model.Veiculo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record VeiculoDTO(
        Long id,  // agora temos o id
        @NotBlank(message = "Placa é obrigatória")
        @Size(min = 4, max = 10, message = "Placa deve ter entre 4 e 10 caracteres")
        String placa,
        String marca,
        String modelo,
        @NotNull(message = "Ano é obrigatório")
        Integer ano,
        String cor
) {
    // construtor de conversão de entidade → DTO
    public VeiculoDTO(Veiculo v) {
        this(
                v.getId(),
                v.getPlaca(),
                v.getMarca(),
                v.getModelo(),
                v.getAno(),
                v.getCor()
        );
    }
}
