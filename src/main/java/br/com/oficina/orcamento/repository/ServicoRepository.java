package br.com.oficina.orcamento.repository;

import br.com.oficina.orcamento.model.Servico;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ServicoRepository extends JpaRepository<Servico, Long> {
    List<Servico> findByVeiculoId(Long veiculoId);
}
