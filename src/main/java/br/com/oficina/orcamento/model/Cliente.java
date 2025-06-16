package br.com.oficina.orcamento.model;

import br.com.oficina.orcamento.enums.FormaPagamento;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 255)
    private String nome;

    private String email;

    @NotBlank
    @Size(min = 10, max = 15)
    private String telefone;

    @NotBlank
    @Size(min = 11, max = 14)
    private String cpf;

    @Enumerated(EnumType.STRING)
    private FormaPagamento formaPagamentoPreferida;

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Veiculo> veiculos = new ArrayList<>();
}
