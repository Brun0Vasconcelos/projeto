package br.com.oficina.orcamento.repository;

import br.com.oficina.orcamento.model.Veiculo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VeiculoRepository extends JpaRepository<Veiculo, Long> {

    Optional<Veiculo> findByPlaca(String placa);

    List<Veiculo> findByPlacaContainingIgnoreCase(String placa);

    /**
     * Necessário para o método porCliente(...)
     */
    List<Veiculo> findByClienteId(Long clienteId);
}
