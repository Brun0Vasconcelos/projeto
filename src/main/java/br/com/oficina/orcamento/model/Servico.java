package br.com.oficina.orcamento.model;

import br.com.oficina.orcamento.enums.FormaPagamento;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "servico")
@Data
@NoArgsConstructor
public class Servico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Descrição é obrigatória")
    private String descricao;

    @NotNull(message = "Data de entrada é obrigatória")
    private LocalDate dataEntrada;

    @NotNull(message = "Data de saída é obrigatória")
    private LocalDate dataEntrega;

    @NotNull(message = "Valor é obrigatório")
    private BigDecimal valor;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Forma de pagamento é obrigatória")
    private FormaPagamento formaPagamento;

    private String linkNotaFiscal;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "veiculo_id", nullable = false)
    private Veiculo veiculo;

    /**
     * Construtor de conveniência para criar já atrelado a um veículo.
     */
    public Servico(String descricao,
                   LocalDate dataEntrada,
                   LocalDate dataEntrega,
                   BigDecimal valor,
                   FormaPagamento formaPagamento,
                   String linkNotaFiscal,
                   Veiculo veiculo) {
        this.descricao = descricao;
        this.dataEntrada = dataEntrada;
        this.dataEntrega = dataEntrega;
        this.valor = valor;
        this.formaPagamento = formaPagamento;
        this.linkNotaFiscal = linkNotaFiscal;
        this.veiculo = veiculo;
    }
}
