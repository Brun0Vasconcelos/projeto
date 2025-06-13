package br.com.oficina.orcamento.service;

import br.com.oficina.orcamento.dto.ServicoDTO;
import br.com.oficina.orcamento.enums.FormaPagamento;
import br.com.oficina.orcamento.model.Servico;
import br.com.oficina.orcamento.model.Veiculo;
import br.com.oficina.orcamento.repository.ServicoRepository;
import br.com.oficina.orcamento.repository.VeiculoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ServicoServiceTest {

    @InjectMocks
    private ServicoService service;

    @Mock
    private ServicoRepository servRepo;

    @Mock
    private VeiculoRepository veicRepo;

    private Veiculo veiculo;
    private ServicoDTO dto;

    @BeforeEach
    void setUp() {
        veiculo = new Veiculo();
        veiculo.setId(42L);

        dto = new ServicoDTO();
        dto.setDescricao("Troca de óleo");
        dto.setDataEntrada(LocalDate.now());
        dto.setDataEntrega(LocalDate.now().plusDays(1));
        dto.setValor(BigDecimal.valueOf(150.00));
        dto.setFormaPagamento(FormaPagamento.PIX);
        dto.setLinkNotaFiscal("http://nfse/123");
    }

    @Test
    void deveAdicionarAoVeiculoComSucesso() {
        when(veicRepo.findById(42L)).thenReturn(Optional.of(veiculo));
        Servico salvo = new Servico(
                dto.getDescricao(),
                dto.getDataEntrada(),
                dto.getDataEntrega(),
                dto.getValor(),
                dto.getFormaPagamento(),
                dto.getLinkNotaFiscal(),
                veiculo
        );
        when(servRepo.save(any(Servico.class))).thenReturn(salvo);

        Servico result = service.adicionarAoVeiculo(42L, dto);

        assertNotNull(result);
        assertEquals("Troca de óleo", result.getDescricao());
        verify(servRepo).save(any());
    }

    @Test
    void deveLancarSeVeiculoNaoExistir() {
        when(veicRepo.findById(1L)).thenReturn(Optional.empty());
        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class,
                () -> service.adicionarAoVeiculo(1L, dto));
        assertEquals("Veículo não encontrado", ex.getMessage());
    }

    @Test
    void deveListarServicosPorVeiculo() {
        Servico s1 = new Servico("A", LocalDate.now(), LocalDate.now(), BigDecimal.ONE, FormaPagamento.DINHEIRO, null, veiculo);
        Servico s2 = new Servico("B", LocalDate.now(), LocalDate.now(), BigDecimal.TEN, FormaPagamento.DEBITO, null, veiculo);

        when(veicRepo.existsById(42L)).thenReturn(true);
        when(servRepo.findByVeiculoId(42L)).thenReturn(List.of(s1, s2));

        List<Servico> lista = service.listarPorVeiculo(42L);
        assertEquals(2, lista.size());
        assertTrue(lista.contains(s1));
    }

    @Test
    void deveAtualizarServico() {
        Servico existente = new Servico("X", LocalDate.now(), LocalDate.now(), BigDecimal.ONE, FormaPagamento.PIX, null, veiculo);
        existente.setId(99L);
        when(servRepo.findById(99L)).thenReturn(Optional.of(existente));
        when(servRepo.save(any())).thenAnswer(inv -> inv.getArgument(0));

        dto.setDescricao("X Modificado");
        Servico atualizado = service.atualizar(99L, dto);

        assertEquals("X Modificado", atualizado.getDescricao());
        verify(servRepo).save(existente);
    }

    @Test
    void deveRemoverServico() {
        Servico existente = new Servico("Y", LocalDate.now(), LocalDate.now(), BigDecimal.ONE, FormaPagamento.PIX, null, veiculo);
        existente.setId(77L);
        when(servRepo.findById(77L)).thenReturn(Optional.of(existente));

        assertDoesNotThrow(() -> service.remover(77L));
        verify(servRepo).delete(existente);
    }
}
