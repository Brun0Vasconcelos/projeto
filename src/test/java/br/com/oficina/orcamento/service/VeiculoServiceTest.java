package br.com.oficina.orcamento.service;

import br.com.oficina.orcamento.dto.VeiculoDTO;
import br.com.oficina.orcamento.model.Cliente;
import br.com.oficina.orcamento.model.Veiculo;
import br.com.oficina.orcamento.repository.ClienteRepository;
import br.com.oficina.orcamento.repository.VeiculoRepository;
import br.com.oficina.orcamento.service.VeiculoService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VeiculoServiceTest {

    @InjectMocks
    private VeiculoService service;

    @Mock
    private VeiculoRepository veiculoRepo;

    @Mock
    private ClienteRepository clienteRepo;

    private VeiculoDTO dto;
    private Cliente clienteEntity;

    @BeforeEach
    void setUp() {
        // instanciando o record diretamente (id null pois é gerado pelo JPA)
        dto = new VeiculoDTO(
                null,
                "ABC1234",
                "MarcaX",
                "ModeloY",
                2020,
                null
        );

        clienteEntity = new Cliente();
        clienteEntity.setId(1L);
    }

    @Test
    void deveSalvarVeiculoComSucesso() {
        when(clienteRepo.findById(1L)).thenReturn(Optional.of(clienteEntity));
        when(veiculoRepo.findByPlaca("ABC1234")).thenReturn(Optional.empty());
        when(veiculoRepo.save(any(Veiculo.class))).thenAnswer(inv -> inv.getArgument(0));

        Veiculo salvo = service.adicionarAoCliente(1L, dto);

        assertNotNull(salvo);
        assertEquals("ABC1234", salvo.getPlaca());
        assertEquals("MarcaX",    salvo.getMarca());
        assertEquals("ModeloY",   salvo.getModelo());
        assertEquals(2020,        salvo.getAno());
        assertEquals(clienteEntity, salvo.getCliente());
        verify(veiculoRepo).save(any(Veiculo.class));
    }

    @Test
    void deveImpedirDuplicataDePlacaEmOutroCliente() {
        Veiculo existente = new Veiculo();
        existente.setId(2L);
        Cliente outro = new Cliente(); outro.setId(2L);
        existente.setCliente(outro);

        when(clienteRepo.findById(1L)).thenReturn(Optional.of(clienteEntity));
        when(veiculoRepo.findByPlaca("ABC1234")).thenReturn(Optional.of(existente));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> service.adicionarAoCliente(1L, dto));
        assertEquals("Essa placa já está registrada para outro cliente.", ex.getMessage());
    }

    @Test
    void deveAtualizarListaDeVeiculosAoListar() {
        Veiculo v1 = new Veiculo(); v1.setId(1L);
        Veiculo v2 = new Veiculo(); v2.setId(2L);
        when(veiculoRepo.findAll()).thenReturn(List.of(v1, v2));

        // agora chama listar(), não listarTodos()
        List<Veiculo> todos = service.listar();

        assertEquals(2, todos.size());
        assertEquals(1L, todos.get(0).getId());
        verify(veiculoRepo).findAll();
    }

    @Test
    void deveBuscarPorIdComSucesso() {
        Veiculo v = new Veiculo(); v.setId(5L);
        when(veiculoRepo.findById(5L)).thenReturn(Optional.of(v));

        Veiculo encontrado = service.buscarPorId(5L);

        assertNotNull(encontrado);
        assertEquals(5L, encontrado.getId());
    }

    @Test
    void deveLancarAoBuscarIdInexistente() {
        when(veiculoRepo.findById(99L)).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class,
                () -> service.buscarPorId(99L));
        assertEquals("Veículo não encontrado", ex.getMessage());
    }

    @Test
    void deveRemoverComSucesso() {
        Veiculo v = new Veiculo(); v.setId(3L);
        when(veiculoRepo.findById(3L)).thenReturn(Optional.of(v));
        doNothing().when(veiculoRepo).delete(v);

        assertDoesNotThrow(() -> service.remover(3L));
        verify(veiculoRepo).delete(v);
    }

    @Test
    void deveLancarAoRemoverInexistente() {
        when(veiculoRepo.findById(77L)).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class,
                () -> service.remover(77L));
        assertEquals("Veículo não encontrado", ex.getMessage());
    }
}
