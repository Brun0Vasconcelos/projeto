package br.com.oficina.orcamento.controller;

import br.com.oficina.orcamento.dto.ServicoDTO;
import br.com.oficina.orcamento.model.Servico;
import br.com.oficina.orcamento.service.ServicoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/veiculos/{veiculoId}/servicos")
@RequiredArgsConstructor
@Tag(name = "Serviços", description = "Gerencia os serviços de manutenção dos veículos")
public class ServicoController {

    private final ServicoService service;

    @Operation(summary = "Lista todos os serviços de um veículo")
    @GetMapping
    public ResponseEntity<List<Servico>> listarPorVeiculo(@PathVariable Long veiculoId) {
        List<Servico> lista = service.listarPorVeiculo(veiculoId);
        return ResponseEntity.ok(lista);
    }

    @Operation(summary = "Busca um serviço por ID")
    @GetMapping("/{id}")
    public ResponseEntity<Servico> buscarPorId(
            @PathVariable Long veiculoId,
            @PathVariable Long id) {
        Servico serv = service.buscarPorId(id);
        return ResponseEntity.ok(serv);
    }

    @Operation(summary = "Cadastra um novo serviço em um veículo")
    @PostMapping
    public ResponseEntity<Servico> criar(
            @PathVariable Long veiculoId,
            @RequestBody @Valid ServicoDTO dto) {
        Servico criado = service.adicionarAoVeiculo(veiculoId, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(criado);
    }

    @Operation(summary = "Atualiza um serviço existente")
    @PutMapping("/{id}")
    public ResponseEntity<Servico> atualizar(
            @PathVariable Long veiculoId,
            @PathVariable Long id,
            @RequestBody @Valid ServicoDTO dto) {
        Servico atualizado = service.atualizar(id, dto);
        return ResponseEntity.ok(atualizado);
    }

    @Operation(summary = "Remove um serviço")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(
            @PathVariable Long veiculoId,
            @PathVariable Long id) {
        service.remover(id);
        return ResponseEntity.noContent().build();
    }
}
