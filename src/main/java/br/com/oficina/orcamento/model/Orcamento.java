package br.com.oficina.orcamento.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orcamento")
@Getter
@Setter
public class Orcamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate data;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal totalBruto;

    @Column(nullable = false)
    private Integer descontoPercentual;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal totalLiquido;

    @Column(nullable = false)
    private Integer parcelas;

    @ManyToOne(optional = false)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @ManyToOne(optional = false)
    @JoinColumn(name = "veiculo_id", nullable = false)
    private Veiculo veiculo;

    @OneToMany(
            mappedBy = "orcamento",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<ItemOrcamento> itens = new ArrayList<>();
}
