package br.com.oficina.orcamento.service;

import br.com.oficina.orcamento.dto.ServicoDTO;
import br.com.oficina.orcamento.model.Servico;
import br.com.oficina.orcamento.model.Veiculo;
import br.com.oficina.orcamento.repository.ServicoRepository;
import br.com.oficina.orcamento.repository.VeiculoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ServicoService {

    private final ServicoRepository servicoRepository;
    private final VeiculoRepository veiculoRepository;

    /**
     * Cadastra um serviço vinculado a um veículo
     */
    @Transactional
    public Servico adicionarAoVeiculo(Long veiculoId, ServicoDTO dto) {
        Veiculo veiculo = veiculoRepository.findById(veiculoId)
                .orElseThrow(() -> new EntityNotFoundException("Veículo não encontrado com ID: " + veiculoId));

        Servico servico = new Servico();
        servico.setDescricao(dto.getDescricao());
        servico.setDataEntrada(dto.getDataEntrada());
        servico.setDataEntrega(dto.getDataEntrega());
        servico.setValor(dto.getValor());
        servico.setFormaPagamento(dto.getFormaPagamento());
        servico.setLinkNotaFiscal(dto.getLinkNotaFiscal());
        servico.setVeiculo(veiculo);

        return servicoRepository.save(servico);
    }

    /**
     * Lista todos os serviços
     */
    @Transactional(readOnly = true)
    public List<Servico> listar() {
        return servicoRepository.findAll();
    }

    /**
     * Busca serviço por ID
     */
    @Transactional(readOnly = true)
    public Servico buscarPorId(Long id) {
        return servicoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Serviço não encontrado com ID: " + id));
    }

    /**
     * Lista serviços vinculados a um veículo
     */
    @Transactional(readOnly = true)
    public List<Servico> listarPorVeiculo(Long veiculoId) {
        return servicoRepository.findByVeiculoId(veiculoId);
    }

    /**
     * Atualiza um serviço
     */
    @Transactional
    public Servico atualizar(Long id, ServicoDTO dto) {
        Servico servico = servicoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Serviço não encontrado com ID: " + id));

        servico.setDescricao(dto.getDescricao());
        servico.setDataEntrada(dto.getDataEntrada());
        servico.setDataEntrega(dto.getDataEntrega());
        servico.setValor(dto.getValor());
        servico.setFormaPagamento(dto.getFormaPagamento());
        servico.setLinkNotaFiscal(dto.getLinkNotaFiscal());

        return servicoRepository.save(servico);
    }

    /**
     * Remove um serviço
     */
    @Transactional
    public void remover(Long id) {
        Servico servico = servicoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Serviço não encontrado com ID: " + id));
        servicoRepository.delete(servico);
    }
}
