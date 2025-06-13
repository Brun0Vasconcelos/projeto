package br.com.oficina.orcamento.service;

import br.com.oficina.orcamento.dto.ClienteDTO;
import br.com.oficina.orcamento.enums.FormaPagamento;
import br.com.oficina.orcamento.model.Cliente;
import br.com.oficina.orcamento.repository.ClienteRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClienteServiceTest {

    @InjectMocks
    private ClienteService clienteService;

    @Mock
    private ClienteRepository clienteRepository;

    private ClienteDTO dto;

    @BeforeEach
    void setUp() {
        // Agora passamos 'null' como id no DTO
        dto = new ClienteDTO(
                null,
                "Joao Silva",
                "joao@email.com",
                "11999999999",
                "12345678901",
                FormaPagamento.PIX
        );
    }

    @Test
    void deveSalvarClienteComSucesso() {
        Cliente mock = new Cliente();
        mock.setId(1L);
        mock.setNome(dto.nome());
        mock.setEmail(dto.email());
        mock.setTelefone(dto.telefone());
        mock.setCpf(dto.cpf());
        mock.setFormaPagamentoPreferida(dto.formaPagamentoPreferida());

        when(clienteRepository.save(any(Cliente.class)))
                .thenReturn(mock);

        Cliente salvo = clienteService.salvar(dto);

        assertNotNull(salvo);
        assertEquals(1L, salvo.getId());
        assertEquals("Joao Silva", salvo.getNome());
        verify(clienteRepository).save(any(Cliente.class));
    }

    @Test
    void deveBuscarPorIdComSucesso() {
        Cliente c = new Cliente();
        c.setId(1L);
        c.setNome("Teste");
        when(clienteRepository.findById(1L))
                .thenReturn(Optional.of(c));

        Cliente resultado = clienteService.buscarPorId(1L);

        assertEquals(1L, resultado.getId());
        assertEquals("Teste", resultado.getNome());
    }

    @Test
    void deveLancarAoBuscarIdInexistente() {
        when(clienteRepository.findById(99L))
                .thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(
                EntityNotFoundException.class,
                () -> clienteService.buscarPorId(99L)
        );
        assertEquals("Cliente não encontrado", ex.getMessage());
    }

    @Test
    void deveListarTodos() {
        Cliente c1 = new Cliente(); c1.setId(1L); c1.setNome("A");
        Cliente c2 = new Cliente(); c2.setId(2L); c2.setNome("B");
        when(clienteRepository.findAll())
                .thenReturn(List.of(c1, c2));

        // Método listar() continua o mesmo
        var lista = clienteService.listar();

        assertEquals(2, lista.size());
        assertEquals("A", lista.get(0).getNome());
        verify(clienteRepository).findAll();
    }

    @Test
    void deveAtualizarComSucesso() {
        Cliente existente = new Cliente();
        existente.setId(1L);
        when(clienteRepository.findById(1L))
                .thenReturn(Optional.of(existente));
        when(clienteRepository.save(any(Cliente.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        Cliente atualizado = clienteService.atualizar(1L, dto);

        assertEquals("Joao Silva", atualizado.getNome());
        verify(clienteRepository).save(existente);
    }

    @Test
    void deveRemoverComSucesso() {
        Cliente existente = new Cliente();
        existente.setId(1L);
        when(clienteRepository.findById(1L))
                .thenReturn(Optional.of(existente));
        doNothing().when(clienteRepository).delete(existente);

        assertDoesNotThrow(() -> clienteService.remover(1L));
        verify(clienteRepository).delete(existente);
    }
}
