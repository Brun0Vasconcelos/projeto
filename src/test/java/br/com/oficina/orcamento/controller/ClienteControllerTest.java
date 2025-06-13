package br.com.oficina.orcamento.controller;

import br.com.oficina.orcamento.dto.ClienteDTO;
import br.com.oficina.orcamento.enums.FormaPagamento;
import br.com.oficina.orcamento.model.Cliente;
import br.com.oficina.orcamento.service.ClienteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ClienteController.class)
public class ClienteControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ClienteService service;

    @Autowired
    private ObjectMapper mapper;

    @Test
    void deveCriarClienteComSucesso() throws Exception {
        // DTO agora tem 6 componentes: id (null), nome, email, telefone, cpf, formaPagamento
        var dto = new ClienteDTO(
                null,
                "Joao Silva",
                "joao@teste.com",
                "11999998888",
                "12345678901",
                FormaPagamento.PIX
        );

        var cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNome(dto.nome());
        cliente.setEmail(dto.email());
        cliente.setTelefone(dto.telefone());
        cliente.setCpf(dto.cpf());
        cliente.setFormaPagamentoPreferida(dto.formaPagamentoPreferida());

        when(service.salvar(Mockito.any(ClienteDTO.class))).thenReturn(cliente);

        mvc.perform(post("/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("Joao Silva"));
    }

    @Test
    void deveListarClientes() throws Exception {
        var c1 = new Cliente(); c1.setId(1L); c1.setNome("A");
        var c2 = new Cliente(); c2.setId(2L); c2.setNome("B");
        when(service.listar()).thenReturn(List.of(c1, c2));

        mvc.perform(get("/clientes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void deveBuscarPorId() throws Exception {
        var c = new Cliente(); c.setId(5L); c.setNome("X");
        when(service.buscarPorId(5L)).thenReturn(c);

        mvc.perform(get("/clientes/5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("X"));
    }

    @Test
    void deveRetornar404SeIdInexistente() throws Exception {
        when(service.buscarPorId(99L))
                .thenThrow(new EntityNotFoundException("Cliente não encontrado"));

        mvc.perform(get("/clientes/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.mensagem", containsString("Cliente não encontrado")));
    }

    @Test
    void deveRemoverCliente() throws Exception {
        doNothing().when(service).remover(3L);

        mvc.perform(delete("/clientes/3"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deveAtualizarCliente() throws Exception {
        // Mesma mudança aqui: id = null no DTO
        var dto = new ClienteDTO(
                null,
                "N",
                "e@mail",
                "1199999999",
                "12345678901",
                FormaPagamento.DEBITO
        );
        var updated = new Cliente();
        updated.setId(7L);
        updated.setNome("N");

        // especifique o tipo no any para evitar ambiguidade
        when(service.atualizar(eq(7L), Mockito.any(ClienteDTO.class)))
                .thenReturn(updated);

        mvc.perform(put("/clientes/7")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(7))
                .andExpect(jsonPath("$.nome").value("N"));
    }
}
