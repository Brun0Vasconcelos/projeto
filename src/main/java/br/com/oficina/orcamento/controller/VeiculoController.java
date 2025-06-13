package br.com.oficina.orcamento.controller;

import br.com.oficina.orcamento.dto.VeiculoDTO;
import br.com.oficina.orcamento.model.Veiculo;
import br.com.oficina.orcamento.service.VeiculoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/veiculos")
@RequiredArgsConstructor
@Tag(name = "Veículos", description = "Gerencia os veículos dos clientes")
public class VeiculoController {

    private final VeiculoService service;

    @Operation(summary = "Cadastra um veículo para um cliente")
    @PostMapping("/clientes/{clienteId}/veiculo")
    public ResponseEntity<Veiculo> criar(
            @PathVariable Long clienteId,
            @RequestBody @Valid VeiculoDTO dto
    ) {
        Veiculo salvo = service.adicionarAoCliente(clienteId, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
    }

    @Operation(summary = "Lista todos os veículos")
    @GetMapping
    public ResponseEntity<List<Veiculo>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @Operation(summary = "Busca veículo por ID")
    @GetMapping("/{id}")
    public ResponseEntity<Veiculo> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @Operation(summary = "Atualiza um veículo existente")
    @PutMapping("/{id}")
    public ResponseEntity<Veiculo> atualizar(
            @PathVariable Long id,
            @RequestBody @Valid VeiculoDTO dto
    ) {
        return ResponseEntity.ok(service.atualizar(id, dto));
    }

    @Operation(summary = "Remove um veículo")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        service.remover(id);
        return ResponseEntity.noContent().build();
    }
}
