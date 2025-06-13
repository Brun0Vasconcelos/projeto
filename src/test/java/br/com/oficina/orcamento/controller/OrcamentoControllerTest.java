// src/test/java/br/com/oficina/orcamento/controller/OrcamentoControllerTest.java
package br.com.oficina.orcamento.controller;

import br.com.oficina.orcamento.dto.OrcamentoDTO;
import br.com.oficina.orcamento.service.OrcamentoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrcamentoController.class)
class OrcamentoControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private OrcamentoService service;

    @Autowired
    private ObjectMapper mapper;

    @Test
    @DisplayName("POST /veiculos/{id}/orcamentos → 201 + body")
    void deveGerarOrcamento() throws Exception {
        OrcamentoDTO dto = new OrcamentoDTO(
                1L,
                LocalDate.now(),
                BigDecimal.valueOf(500),
                10,
                BigDecimal.valueOf(450),
                3,
                42L,
                7L,
                List.of(100L,101L)
        );

        Mockito.when(service.gerarPorVeiculo(eq(42L), eq(10), eq(3)))
                .thenReturn(dto);

        mvc.perform(post("/veiculos/42/orcamentos")
                        .param("descontoPercentual","10")
                        .param("parcelas","3"))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.totalBruto", is(500)))
                .andExpect(jsonPath("$.descontoPercentual", is(10)))
                .andExpect(jsonPath("$.parcelas", is(3)))
                .andExpect(jsonPath("$.veiculoId", is(42)))
                .andExpect(jsonPath("$.servicosIds", containsInAnyOrder(100,101)));
    }

    @Test
    @DisplayName("GET /clientes/{id}/orcamentos → 200 + lista")
    void deveListarPorCliente() throws Exception {
        OrcamentoDTO a = new OrcamentoDTO(1L, LocalDate.now(), BigDecimal.ONE, 0, BigDecimal.ONE, 1, 1L, 1L, List.of());
        OrcamentoDTO b = new OrcamentoDTO(2L, LocalDate.now(), BigDecimal.TEN, 5, BigDecimal.valueOf(9.5), 2, 1L, 1L, List.of());
        Mockito.when(service.listarPorCliente(1L)).thenReturn(List.of(a,b));

        mvc.perform(get("/clientes/1/orcamentos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].descontoPercentual", is(5)));
    }

    @Test
    @DisplayName("GET /veiculos/{id}/orcamentos → 200 + lista")
    void deveListarPorVeiculo() throws Exception {
        OrcamentoDTO o = new OrcamentoDTO(3L, LocalDate.now(), BigDecimal.valueOf(200), 0, BigDecimal.valueOf(200), 1, 5L, 2L, List.of());
        Mockito.when(service.listarPorVeiculo(5L)).thenReturn(List.of(o));

        mvc.perform(get("/veiculos/5/orcamentos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(3)))
                .andExpect(jsonPath("$[0].veiculoId", is(5)));
    }

    @Test
    @DisplayName("PUT /orcamentos/{id} → 200 + body")
    void deveAtualizarOrcamento() throws Exception {
        OrcamentoDTO dto = new OrcamentoDTO(4L, LocalDate.now(), BigDecimal.valueOf(300), 15, BigDecimal.valueOf(255), 4, 6L, 3L, List.of());
        Mockito.when(service.atualizar(eq(4L), eq(15), eq(4))).thenReturn(dto);

        mvc.perform(put("/orcamentos/4")
                        .param("descontoPercentual","15")
                        .param("parcelas","4"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(4)))
                .andExpect(jsonPath("$.totalLiquido", is(255)))
                .andExpect(jsonPath("$.parcelas", is(4)));
    }

    @Test
    @DisplayName("DELETE /orcamentos/{id} → 204")
    void deveRemoverOrcamento() throws Exception {
        mvc.perform(delete("/orcamentos/99"))
                .andExpect(status().isNoContent());

        Mockito.verify(service).remover(99L);
    }
}
