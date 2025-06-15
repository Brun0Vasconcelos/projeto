// src/main/java/br/com/oficina/orcamento/service/OrcamentoService.java
package br.com.oficina.orcamento.service;

import br.com.oficina.orcamento.dto.ItemOrcamentoDTO;
import br.com.oficina.orcamento.dto.OrcamentoDTO;
import br.com.oficina.orcamento.model.ItemOrcamento;
import br.com.oficina.orcamento.model.Orcamento;
import br.com.oficina.orcamento.model.Servico;
import br.com.oficina.orcamento.model.Veiculo;
import br.com.oficina.orcamento.repository.OrcamentoRepository;
import br.com.oficina.orcamento.repository.ServicoRepository;
import br.com.oficina.orcamento.repository.VeiculoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrcamentoService {

    private final OrcamentoRepository orcamentoRepository;
    private final VeiculoRepository veiculoRepository;
    private final ServicoRepository servicoRepository;

    /**
     * Busca um orçamento por ID e converte para DTO.
     */
    @Transactional(readOnly = true)
    public OrcamentoDTO buscarPorId(Long id) {
        Orcamento orc = orcamentoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Orçamento não encontrado: " + id));
        return mapToDTO(orc);
    }

    /**
     * Gera um novo orçamento para o veículo, inclui todos os serviços daquele veículo,
     * calcula bruto e líquido, salva e retorna o DTO.
     */
    @Transactional
    public OrcamentoDTO gerarPorVeiculo(Long veiculoId, int descontoPercentual, int parcelas) {
        Veiculo veiculo = veiculoRepository.findById(veiculoId)
                .orElseThrow(() -> new EntityNotFoundException("Veículo não encontrado: " + veiculoId));

        List<Servico> servicos = servicoRepository.findByVeiculoId(veiculoId);

        BigDecimal totalBruto = servicos.stream()
                .map(Servico::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal fatorDesconto = BigDecimal.valueOf(100 - descontoPercentual)
                .divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP);
        BigDecimal totalLiquido = totalBruto.multiply(fatorDesconto);

        Orcamento orc = new Orcamento();
        orc.setVeiculo(veiculo);
        orc.setCliente(veiculo.getCliente());
        orc.setData(LocalDate.now());
        orc.setDescontoPercentual(descontoPercentual);
        orc.setParcelas(parcelas);
        orc.setTotalBruto(totalBruto);
        orc.setTotalLiquido(totalLiquido);

        List<ItemOrcamento> itens = servicos.stream()
                .map(s -> {
                    ItemOrcamento item = new ItemOrcamento();
                    item.setOrcamento(orc);
                    item.setServico(s);
                    item.setDataEntrada(s.getDataEntrada());
                    item.setDataEntrega(s.getDataEntrega());
                    item.setValor(s.getValor());
                    return item;
                })
                .collect(Collectors.toList());
        orc.setItens(itens);

        Orcamento salvo = orcamentoRepository.save(orc);
        return mapToDTO(salvo);
    }

    @Transactional(readOnly = true)
    public List<OrcamentoDTO> listarPorCliente(Long clienteId) {
        return orcamentoRepository.findByClienteId(clienteId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<OrcamentoDTO> listarPorVeiculo(Long veiculoId) {
        return orcamentoRepository.findByVeiculoId(veiculoId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrcamentoDTO atualizar(Long id, Integer descontoPercentual, Integer parcelas) {
        Orcamento orc = orcamentoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Orçamento não encontrado: " + id));

        if (descontoPercentual != null) {
            orc.setDescontoPercentual(descontoPercentual);
        }
        if (parcelas != null) {
            orc.setParcelas(parcelas);
        }

        BigDecimal fator = BigDecimal.valueOf(100 - orc.getDescontoPercentual())
                .divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP);
        orc.setTotalLiquido(orc.getTotalBruto().multiply(fator));

        Orcamento atualizado = orcamentoRepository.save(orc);
        return mapToDTO(atualizado);
    }

    @Transactional
    public void remover(Long id) {
        if (!orcamentoRepository.existsById(id)) {
            throw new EntityNotFoundException("Orçamento não encontrado: " + id);
        }
        orcamentoRepository.deleteById(id);
    }

    private OrcamentoDTO mapToDTO(Orcamento orc) {
        List<ItemOrcamentoDTO> itensDTO = orc.getItens().stream()
                .map(item -> new ItemOrcamentoDTO(
                        item.getId(),
                        item.getServico().getDescricao(),
                        item.getDataEntrada(),
                        item.getDataEntrega(),
                        item.getValor()
                ))
                .collect(Collectors.toList());

        return new OrcamentoDTO(
                orc.getId(),
                orc.getData(),
                orc.getTotalBruto(),
                orc.getDescontoPercentual(),
                orc.getTotalLiquido(),
                orc.getParcelas(),
                orc.getCliente().getId(),
                orc.getCliente().getNome(),
                orc.getVeiculo().getId(),
                orc.getVeiculo().getPlaca(),
                orc.getVeiculo().getModelo(),
                itensDTO
        );
    }
}
