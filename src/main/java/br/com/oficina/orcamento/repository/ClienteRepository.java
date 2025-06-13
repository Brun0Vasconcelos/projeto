package br.com.oficina.orcamento.repository;

import br.com.oficina.orcamento.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositório Spring‐Data para Cliente.
 * Basta essa interface para fornecer CRUD básico e
 * permitir que o Mockito a “mockeie” nos seus testes.
 */
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
}
