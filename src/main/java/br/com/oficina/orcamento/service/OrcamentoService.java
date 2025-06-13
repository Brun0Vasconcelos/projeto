// src/main/java/br/com/oficina/orcamento/service/OrcamentoService.java
package br.com.oficina.orcamento.service;

import br.com.oficina.orcamento.dto.OrcamentoDTO;
import br.com.oficina.orcamento.model.Orcamento;
import br.com.oficina.orcamento.repository.OrcamentoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrcamentoService {

    private final OrcamentoRepository orcRepo;

    /**
     * 1) Gera (e persiste) um orçamento para um veículo
     */
    @Transactional
    public OrcamentoDTO gerarPorVeiculo(Long veiculoId, int descontoPercentual, int parcelas) {
        // ... sua implementação anterior de gerarPorVeiculo ...
        throw new UnsupportedOperationException("implemente aqui");
    }

    /**
     * 2) Lista todos os orçamentos de um cliente (persistidos)
     */
    @Transactional(readOnly = true)
    public List<OrcamentoDTO> listarPorCliente(Long clienteId) {
        // ... sua implementação anterior de listarPorCliente ...
        throw new UnsupportedOperationException("implemente aqui");
    }

    /**
     * 3) Lista todos os orçamentos de um veículo (persistidos)
     */
    @Transactional(readOnly = true)
    public List<OrcamentoDTO> listarPorVeiculo(Long veiculoId) {
        // ... sua implementação anterior de listarPorVeiculo ...
        throw new UnsupportedOperationException("implemente aqui");
    }

    /**
     * 4) Busca um orçamento pelo ID sem alterar nada (para exportação)
     */
    @Transactional(readOnly = true)
    public OrcamentoDTO buscarPorId(Long id) {
        Orcamento o = orcRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Orçamento não encontrado"));
        return new OrcamentoDTO(
                o.getId(),
                o.getData(),
                o.getTotalBruto(),
                o.getDescontoPercentual(),
                o.getTotalLiquido(),
                o.getParcelas(),
                o.getVeiculo().getId(),
                o.getCliente().getId(),
                o.getServicosIds()
        );
    }

    /**
     * 5) Atualiza descontos/parcelas de um orçamento existente
     */
    @Transactional
    public OrcamentoDTO atualizar(Long id, Integer novoDescontoPercentual, Integer novasParcelas) {
        Orcamento existente = orcRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Orçamento não encontrado"));

        BigDecimal bruto = existente.getTotalBruto();
        if (novoDescontoPercentual != null) {
            existente.setDescontoPercentual(novoDescontoPercentual);
            BigDecimal desconto = bruto
                    .multiply(BigDecimal.valueOf(novoDescontoPercentual))
                    .divide(BigDecimal.valueOf(100));
            existente.setTotalLiquido(bruto.subtract(desconto));
        }
        if (novasParcelas != null) {
            existente.setParcelas(novasParcelas);
        }

        Orcamento salvo = orcRepo.save(existente);
        return new OrcamentoDTO(
                salvo.getId(),
                salvo.getData(),
                salvo.getTotalBruto(),
                salvo.getDescontoPercentual(),
                salvo.getTotalLiquido(),
                salvo.getParcelas(),
                salvo.getVeiculo().getId(),
                salvo.getCliente().getId(),
                salvo.getServicosIds()
        );
    }

    /**
     * 6) Remove um orçamento
     */
    @Transactional
    public void remover(Long id) {
        Orcamento existente = orcRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Orçamento não encontrado"));
        orcRepo.delete(existente);
    }
}
