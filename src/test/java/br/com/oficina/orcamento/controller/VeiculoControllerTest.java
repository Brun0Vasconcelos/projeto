package br.com.oficina.orcamento.controller;

import br.com.oficina.orcamento.dto.VeiculoDTO;
import br.com.oficina.orcamento.model.Veiculo;
import br.com.oficina.orcamento.service.VeiculoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(VeiculoController.class)
public class VeiculoControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private VeiculoService service;

    @Autowired
    private ObjectMapper mapper;

    @Test
    void deveAdicionarVeiculoAoCliente() throws Exception {
        long clienteId = 5L;
        VeiculoDTO dto = new VeiculoDTO(
                null,
                "ABC1234",
                "VW",
                "Gol",
                2010,
                null
        );

        Veiculo salvo = new Veiculo();
        salvo.setId(99L);
        salvo.setPlaca(dto.placa());
        salvo.setMarca(dto.marca());
        salvo.setModelo(dto.modelo());
        salvo.setAno(dto.ano());
        salvo.setCor(dto.cor());

        when(service.adicionarAoCliente(eq(clienteId), any(VeiculoDTO.class)))
                .thenReturn(salvo);

        mvc.perform(post("/veiculos/clientes/{clienteId}/veiculo", clienteId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(99))
                .andExpect(jsonPath("$.placa").value("ABC1234"))
                .andExpect(jsonPath("$.marca").value("VW"));
    }

    @Test
    void deveListarVeiculos() throws Exception {
        Veiculo v1 = new Veiculo(); v1.setId(1L); v1.setPlaca("XAAA1");
        Veiculo v2 = new Veiculo(); v2.setId(2L); v2.setPlaca("XBBB2");

        when(service.listar()).thenReturn(List.of(v1, v2));

        mvc.perform(get("/veiculos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].placa").value("XAAA1"))
                .andExpect(jsonPath("$[1].placa").value("XBBB2"));
    }

    @Test
    void deveBuscarVeiculoPorId() throws Exception {
        Veiculo v = new Veiculo();
        v.setId(11L);
        v.setPlaca("XYZ999");

        when(service.buscarPorId(11L)).thenReturn(v);

        mvc.perform(get("/veiculos/{id}", 11L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(11))
                .andExpect(jsonPath("$.placa").value("XYZ999"));
    }

    @Test
    void deveRemoverVeiculo() throws Exception {
        doNothing().when(service).remover(7L);

        mvc.perform(delete("/veiculos/{id}", 7L))
                .andExpect(status().isNoContent());
    }
}
