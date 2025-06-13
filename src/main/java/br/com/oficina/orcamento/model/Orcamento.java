package br.com.oficina.orcamento.model;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orcamento")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Orcamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Quando o orçamento foi gerado */
    @Column(nullable = false)
    private LocalDate data;

    /** Total bruto (soma dos serviços) */
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal totalBruto;

    /** Percentual de desconto (ex: 10 para 10%) */
    @Column(nullable = false)
    private Integer descontoPercentual;

    /** Total líquido (após desconto) */
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal totalLiquido;

    /** Número de parcelas, se houver */
    @Column(nullable = false)
    private Integer parcelas;

    /** Relação com veículo */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "veiculo_id", nullable = false)
    private Veiculo veiculo;

    /** Relação com cliente (via veículo) */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    /**
     * Lista de IDs dos serviços que entraram neste orçamento.
     * Armazenado em tabela de coleção separada.
     */
    @ElementCollection
    @CollectionTable(
            name = "orcamento_servicos",
            joinColumns = @JoinColumn(name = "orcamento_id")
    )
    @Column(name = "servico_id", nullable = false)
    private List<Long> servicosIds = new ArrayList<>();

    /**
     * Adiciona um serviço ao orçamento (somente o ID).
     */
    public void addServicoId(Long servicoId) {
        this.servicosIds.add(servicoId);
    }

    /**
     * Remove um serviço do orçamento (pelo ID).
     */
    public void removeServicoId(Long servicoId) {
        this.servicosIds.remove(servicoId);
    }

    /**
     * Getter explícito (Lombok já gera, mas deixado aqui por completude).
     */
    public List<Long> getServicosIds() {
        return servicosIds;
    }
}
