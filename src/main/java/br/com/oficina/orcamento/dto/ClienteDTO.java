package br.com.oficina.orcamento.dto;

import br.com.oficina.orcamento.enums.FormaPagamento;
import br.com.oficina.orcamento.model.Cliente;

public record ClienteDTO(
        Long id,
        String nome,
        String email,
        String telefone,
        String cpf,
        FormaPagamento formaPagamentoPreferida
) {
        public ClienteDTO(Cliente cliente) {
                this(
                        cliente.getId(),
                        cliente.getNome(),
                        cliente.getEmail(),
                        cliente.getTelefone(),
                        cliente.getCpf(),
                        cliente.getFormaPagamentoPreferida()
                );
        }
}
