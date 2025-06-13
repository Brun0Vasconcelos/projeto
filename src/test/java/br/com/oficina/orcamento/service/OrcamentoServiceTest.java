package br.com.oficina.orcamento.service;

import br.com.oficina.orcamento.dto.OrcamentoDTO;
import br.com.oficina.orcamento.model.Cliente;
import br.com.oficina.orcamento.model.Servico;
import br.com.oficina.orcamento.model.Veiculo;
import br.com.oficina.orcamento.repository.ClienteRepository;
import br.com.oficina.orcamento.repository.ServicoRepository;
import br.com.oficina.orcamento.repository.VeiculoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import jakarta.persistence.EntityNotFoundException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrcamentoServiceTest {

    @InjectMocks
    private OrcamentoService service;

    @Mock
    private ClienteRepository clienteRepo;
    @Mock
    private VeiculoRepository veiculoRepo;
    @Mock
    private ServicoRepository servicoRepo;

    private Cliente cliente;
    private Veiculo v1, v2;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        cliente = new Cliente(); cliente.setId(10L); cliente.setNome("Maria");
        v1 = new Veiculo(); v1.setId(1L); v1.setPlaca("AAA-1111"); v1.setCliente(cliente);
        v2 = new Veiculo(); v2.setId(2L); v2.setPlaca("BBB-2222"); v2.setCliente(cliente);
    }

    @Test
    void porClienteSucesso() {
        when(clienteRepo.findById(10L)).thenReturn(Optional.of(cliente));
        when(veiculoRepo.findByClienteId(10L)).thenReturn(List.of(v1, v2));
        when(servicoRepo.findByVeiculoId(1L)).thenReturn(List.of(
                new Servico("X", null, null, BigDecimal.valueOf(100), null, null, v1)
        ));
        when(servicoRepo.findByVeiculoId(2L)).thenReturn(List.of(
                new Servico("Y", null, null, BigDecimal.valueOf(200), null, null, v2)
        ));

        List<OrcamentoDTO> orcs = service.porCliente(10L);
        assertEquals(2, orcs.size());
        assertEquals(BigDecimal.valueOf(100), orcs.get(0).totalServicos());
        assertEquals(BigDecimal.valueOf(200), orcs.get(1).totalServicos());
    }

    @Test
    void porClienteNaoEncontrado() {
        when(clienteRepo.findById(5L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> service.porCliente(5L));
    }

    @Test
    void porVeiculoSucesso() {
        when(veiculoRepo.findById(1L)).thenReturn(Optional.of(v1));
        when(servicoRepo.findByVeiculoId(1L)).thenReturn(List.of(
                new Servico("Z", null, null, BigDecimal.valueOf(300), null, null, v1)
        ));

        OrcamentoDTO dto = service.porVeiculo(1L);
        assertEquals(BigDecimal.valueOf(300), dto.totalServicos());
    }

    @Test
    void porVeiculoNaoEncontrado() {
        when(veiculoRepo.findById(99L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> service.porVeiculo(99L));
    }
}


//package br.com.oficina.orcamento.service;
//
//import br.com.oficina.orcamento.dto.OrcamentoDTO;
//import br.com.oficina.orcamento.model.Cliente;
//import br.com.oficina.orcamento.model.Servico;
//import br.com.oficina.orcamento.model.Veiculo;
//import br.com.oficina.orcamento.repository.ClienteRepository;
//import br.com.oficina.orcamento.repository.ServicoRepository;
//import br.com.oficina.orcamento.repository.VeiculoRepository;
//import jakarta.persistence.EntityNotFoundException;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.*;
//import java.math.BigDecimal;
//import java.util.List;
//import java.util.Optional;
//
//import static org.assertj.core.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//class OrcamentoServiceTest {
//
//    @InjectMocks
//    OrcamentoService service;
//
//    @Mock
//    ServicoRepository servRepo;
//    @Mock
//    VeiculoRepository veicRepo;
//    @Mock
//    ClienteRepository cliRepo;
//
//    Cliente cliente;
//    Veiculo veiculoA, veiculoB;
//    Servico s1, s2, s3;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//
//        cliente = new Cliente();
//        cliente.setId(10L);
//        cliente.setNome("Fulano");
//
//        veiculoA = new Veiculo();
//        veiculoA.setId(1L);
//        veiculoA.setCliente(cliente);
//
//        veiculoB = new Veiculo();
//        veiculoB.setId(2L);
//        veiculoB.setCliente(cliente);
//
//        s1 = new Servico(); s1.setValor(BigDecimal.valueOf(100)); s1.setVeiculo(veiculoA);
//        s2 = new Servico(); s2.setValor(BigDecimal.valueOf(200)); s2.setVeiculo(veiculoA);
//        s3 = new Servico(); s3.setValor(BigDecimal.valueOf(300)); s3.setVeiculo(veiculoB);
//    }
//
//    @Test
//    void calculaPorVeiculo() {
//        when(veicRepo.findById(1L)).thenReturn(Optional.of(veiculoA));
//        when(servRepo.findByVeiculoId(1L)).thenReturn(List.of(s1, s2));
//
//        OrcamentoDTO dto = service.calcularPorVeiculo(
//                1L,
//                BigDecimal.valueOf(10),   // 10%
//                BigDecimal.valueOf(5)     // 5%
//        );
//
//        // total 300, desconto 30, acrescimo 15, final 285
//        assertThat(dto.totalServicos()).isEqualByComparingTo("300");
//        assertThat(dto.desconto()).isEqualByComparingTo("30");
//        assertThat(dto.acrescimo()).isEqualByComparingTo("15");
//        assertThat(dto.valorFinal()).isEqualByComparingTo("285");
//    }
//
//    @Test
//    void erroVeiculoNaoEncontrado() {
//        when(veicRepo.findById(99L)).thenReturn(Optional.empty());
//        assertThatThrownBy(() -> service.calcularPorVeiculo(99L, BigDecimal.ZERO, BigDecimal.ZERO))
//                .isInstanceOf(EntityNotFoundException.class)
//                .hasMessage("Veículo não encontrado");
//    }
//
//    @Test
//    void calculaPorCliente() {
//        when(cliRepo.findById(10L)).thenReturn(Optional.of(cliente));
//        when(veicRepo.findByClienteId(10L)).thenReturn(List.of(veiculoA, veiculoB));
//        when(servRepo.findByVeiculoId(1L)).thenReturn(List.of(s1, s2));
//        when(servRepo.findByVeiculoId(2L)).thenReturn(List.of(s3));
//
//        OrcamentoDTO dto = service.calcularPorCliente(
//                10L,
//                BigDecimal.ZERO,
//                BigDecimal.ZERO
//        );
//
//        // total 100+200+300 = 600
//        assertThat(dto.totalServicos()).isEqualByComparingTo("600");
//        assertThat(dto.desconto()).isZero();
//        assertThat(dto.acrescimo()).isZero();
//        assertThat(dto.valorFinal()).isEqualByComparingTo("600");
//    }
//
//    @Test
//    void erroClienteNaoEncontrado() {
//        when(cliRepo.findById(99L)).thenReturn(Optional.empty());
//        assertThatThrownBy(() -> service.calcularPorCliente(99L, BigDecimal.ZERO, BigDecimal.ZERO))
//                .isInstanceOf(EntityNotFoundException.class)
//                .hasMessage("Cliente não encontrado");
//    }
//}
