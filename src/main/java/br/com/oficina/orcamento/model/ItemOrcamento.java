// src/main/java/br/com/oficina/orcamento/model/ItemOrcamento.java
package br.com.oficina.orcamento.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "orcamento_servicos") // ou "item_orcamento", conforme seu schema
@Getter
@Setter
public class ItemOrcamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "orcamento_id", nullable = false)
    private Orcamento orcamento;

    @ManyToOne(optional = false)
    @JoinColumn(name = "servico_id", nullable = false)
    private Servico servico;

    @Column(nullable = false)
    private LocalDate dataEntrada;

    @Column(nullable = false)
    private LocalDate dataEntrega;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal valor;
}
