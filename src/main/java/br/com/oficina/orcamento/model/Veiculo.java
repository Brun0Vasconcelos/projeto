// src/main/java/br/com/oficina/orcamento/model/Veiculo.java
package br.com.oficina.orcamento.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "veiculo")
@Data
@NoArgsConstructor
public class Veiculo {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Placa é obrigatória")
    @Column(nullable = false, unique = true)
    private String placa;

    private String marca;
    private String modelo;
    private Integer ano;
    private String cor;

    /**
     * Cada veículo pertence a um único cliente.
     * @JsonBackReference indica ao Jackson que,
     * ao serializar Veiculo, ele não deve voltar
     * para o objeto Cliente (fecha o ciclo).
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cliente_id", nullable = false)
    @JsonBackReference
    private Cliente cliente;

    /**
     * Serviços associados a este veículo.
     * (Use anotação semelhante se quiser expor no JSON)
     */
    @OneToMany(mappedBy = "veiculo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Servico> servicos = new ArrayList<>();

    /** Construtor de conveniência (sem id) */
    public Veiculo(String placa, String marca, String modelo, Integer ano, String cor, Cliente cliente) {
        this.placa = placa;
        this.marca = marca;
        this.modelo = modelo;
        this.ano = ano;
        this.cor = cor;
        this.cliente = cliente;
    }
}
