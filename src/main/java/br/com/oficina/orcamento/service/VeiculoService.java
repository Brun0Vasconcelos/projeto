package br.com.oficina.orcamento.service;

import br.com.oficina.orcamento.dto.VeiculoDTO;
import br.com.oficina.orcamento.model.Cliente;
import br.com.oficina.orcamento.model.Veiculo;
import br.com.oficina.orcamento.repository.ClienteRepository;
import br.com.oficina.orcamento.repository.VeiculoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VeiculoService {

    private final VeiculoRepository veiculoRepo;
    private final ClienteRepository clienteRepo;

    @Transactional
    public Veiculo adicionarAoCliente(Long clienteId, VeiculoDTO dto) {
        Cliente cliente = clienteRepo.findById(clienteId)
                .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado"));

        // checa duplicata de placa
        veiculoRepo.findByPlaca(dto.placa())
                .ifPresent(ex -> {
                    if (!ex.getCliente().getId().equals(clienteId)) {
                        throw new IllegalArgumentException("Essa placa já está registrada para outro cliente.");
                    }
                });

        Veiculo veiculo = new Veiculo(
                dto.placa(),
                dto.marca(),
                dto.modelo(),
                dto.ano(),
                dto.cor(),
                cliente
        );

        return veiculoRepo.save(veiculo);
    }

    /**
     * retorna todos os veículos — usado por listarTodos() no Controller e no Test
     */
    @Transactional(readOnly = true)
    public List<Veiculo> listar() {
        return veiculoRepo.findAll();
    }

    @Transactional(readOnly = true)
    public Veiculo buscarPorId(Long id) {
        return veiculoRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Veículo não encontrado"));
    }

    @Transactional
    public Veiculo atualizar(Long id, VeiculoDTO dto) {
        Veiculo existente = buscarPorId(id);

        existente.setPlaca(dto.placa());
        existente.setMarca(dto.marca());
        existente.setModelo(dto.modelo());
        existente.setAno(dto.ano());
        existente.setCor(dto.cor());

        return veiculoRepo.save(existente);
    }

    @Transactional
    public void remover(Long id) {
        Veiculo v = buscarPorId(id);
        veiculoRepo.delete(v);
    }
}
