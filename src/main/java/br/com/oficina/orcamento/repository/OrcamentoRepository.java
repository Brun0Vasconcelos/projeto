package br.com.oficina.orcamento.repository;

import br.com.oficina.orcamento.model.Orcamento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrcamentoRepository extends JpaRepository<Orcamento, Long> {
    List<Orcamento> findByClienteId(Long clienteId);
    List<Orcamento> findByVeiculoId(Long veiculoId);
}
