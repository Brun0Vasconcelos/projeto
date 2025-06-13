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

    private final ServicoRepository servicoRepo;
    private final VeiculoRepository veiculoRepo;

    /**
     * adiciona um serviço a um veículo existente
     */
    @Transactional
    public Servico adicionarAoVeiculo(Long veiculoId, ServicoDTO dto) {
        Veiculo veiculo = veiculoRepo.findById(veiculoId)
                .orElseThrow(() -> new EntityNotFoundException("Veículo não encontrado"));

        Servico servico = new Servico(
                dto.getDescricao(),
                dto.getDataEntrada(),
                dto.getDataEntrega(),
                dto.getValor(),
                dto.getFormaPagamento(),
                dto.getLinkNotaFiscal(),
                veiculo
        );

        return servicoRepo.save(servico);
    }

    /**
     * lista todos os serviços de um mesmo veículo
     */
    @Transactional(readOnly = true)
    public List<Servico> listarPorVeiculo(Long veiculoId) {
        // garante que o veículo exista
        if (!veiculoRepo.existsById(veiculoId)) {
            throw new EntityNotFoundException("Veículo não encontrado");
        }
        return servicoRepo.findByVeiculoId(veiculoId);
    }

    /**
     * busca serviço por ID
     */
    @Transactional(readOnly = true)
    public Servico buscarPorId(Long id) {
        return servicoRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Serviço não encontrado"));
    }

    /**
     * atualiza um serviço existente
     */
    @Transactional
    public Servico atualizar(Long id, ServicoDTO dto) {
        Servico existente = buscarPorId(id);

        existente.setDescricao(dto.getDescricao());
        existente.setDataEntrada(dto.getDataEntrada());
        existente.setDataEntrega(dto.getDataEntrega());
        existente.setValor(dto.getValor());
        existente.setFormaPagamento(dto.getFormaPagamento());
        existente.setLinkNotaFiscal(dto.getLinkNotaFiscal());
        // veiculo não muda aqui

        return servicoRepo.save(existente);
    }

    /**
     * remove um serviço
     */
    @Transactional
    public void remover(Long id) {
        Servico existente = buscarPorId(id);
        servicoRepo.delete(existente);
    }
}
