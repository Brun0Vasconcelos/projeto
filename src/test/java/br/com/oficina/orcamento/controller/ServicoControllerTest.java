package br.com.oficina.orcamento.controller;

import br.com.oficina.orcamento.dto.ServicoDTO;
import br.com.oficina.orcamento.model.Servico;
import br.com.oficina.orcamento.service.ServicoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ServicoController.class)
class ServicoControllerTest {

    @Autowired
    private MockMvc mvc;
    @MockBean
    private ServicoService service;
    @Autowired
    private ObjectMapper mapper;

    @Test
    void criaServico() throws Exception {
        var dto = new ServicoDTO(null, "Limpeza", LocalDate.now(), LocalDate.now().plusDays(1),
                BigDecimal.valueOf(80), null, "http://nf/1");
        var serv = new Servico(dto.getDescricao(), dto.getDataEntrada(), dto.getDataEntrega(),
                dto.getValor(), dto.getFormaPagamento(), dto.getLinkNotaFiscal(), null);
        serv.setId(100L);

        when(service.adicionarAoVeiculo(2L, any())).thenReturn(serv);

        mvc.perform(post("/veiculos/{veiculoId}/servicos", 2L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(100))
                .andExpect(jsonPath("$.descricao").value("Limpeza"));
    }

    @Test
    void listaPorVeiculo() throws Exception {
        var s1 = new Servico("A", LocalDate.now(), LocalDate.now(), BigDecimal.ONE, null, null, null);
        var s2 = new Servico("B", LocalDate.now(), LocalDate.now(), BigDecimal.TEN, null, null, null);
        when(service.listarPorVeiculo(3L)).thenReturn(List.of(s1, s2));

        mvc.perform(get("/veiculos/{veiculoId}/servicos", 3L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void buscaPorId() throws Exception {
        var s = new Servico("X", LocalDate.now(), LocalDate.now(), BigDecimal.ONE, null, null, null);
        s.setId(5L);
        when(service.buscarPorId(5L)).thenReturn(s);

        mvc.perform(get("/veiculos/{veiculoId}/servicos/{id}", 3L, 5L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(5));
    }

    @Test
    void removeServico() throws Exception {
        mvc.perform(delete("/veiculos/{veiculoId}/servicos/{id}", 3L, 9L))
                .andExpect(status().isNoContent());
    }
}
