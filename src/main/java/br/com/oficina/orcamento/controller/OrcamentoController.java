// src/main/java/br/com/oficina/orcamento/controller/OrcamentoController.java
package br.com.oficina.orcamento.controller;

import br.com.oficina.orcamento.dto.OrcamentoDTO;
import br.com.oficina.orcamento.service.OrcamentoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "Orçamentos", description = "Opera sobre orçamentos")
public class OrcamentoController {

    private final OrcamentoService service;

    @Operation(summary = "Gera um novo orçamento para um veículo")
    @PostMapping("/veiculos/{veiculoId}/orcamentos")
    public ResponseEntity<OrcamentoDTO> gerarPorVeiculo(
            @PathVariable Long veiculoId,
            @RequestParam @Min(0) int descontoPercentual,
            @RequestParam @Min(1) int parcelas
    ) {
        OrcamentoDTO dto = service.gerarPorVeiculo(veiculoId, descontoPercentual, parcelas);
        return ResponseEntity.status(201).body(dto);
    }

    @Operation(summary = "Lista todos os orçamentos de um cliente")
    @GetMapping("/clientes/{clienteId}/orcamentos")
    public List<OrcamentoDTO> listarPorCliente(@PathVariable Long clienteId) {
        return service.listarPorCliente(clienteId);
    }

    @Operation(summary = "Lista todos os orçamentos de um veículo")
    @GetMapping("/veiculos/{veiculoId}/orcamentos")
    public List<OrcamentoDTO> listarPorVeiculo(@PathVariable Long veiculoId) {
        return service.listarPorVeiculo(veiculoId);
    }

    @Operation(summary = "Atualiza desconto e/ou parcelas de um orçamento")
    @PutMapping("/orcamentos/{id}")
    public OrcamentoDTO atualizar(
            @PathVariable Long id,
            @RequestParam(required = false) Integer descontoPercentual,
            @RequestParam(required = false) Integer parcelas
    ) {
        return service.atualizar(id, descontoPercentual, parcelas);
    }

    @Operation(summary = "Remove um orçamento")
    @DeleteMapping("/orcamentos/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        service.remover(id);
        return ResponseEntity.noContent().build();
    }
}
