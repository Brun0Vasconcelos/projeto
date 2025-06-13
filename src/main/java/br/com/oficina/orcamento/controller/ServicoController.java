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
    public ResponseEntity<List<ServicoDTO>> listarPorVeiculo(@PathVariable Long veiculoId) {
        var dtos = service.listarPorVeiculo(veiculoId)
                .stream()
                .map(ServicoDTO::new)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    @Operation(summary = "Busca um serviço por ID")
    @GetMapping("/{id}")
    public ResponseEntity<ServicoDTO> buscarPorId(
            @PathVariable Long veiculoId,
            @PathVariable Long id) {

        Servico serv = service.buscarPorVeiculo(veiculoId, id);
        return ResponseEntity.ok(new ServicoDTO(serv));
    }

    @Operation(summary = "Cadastra um novo serviço em um veículo")
    @PostMapping
    public ResponseEntity<ServicoDTO> criar(
            @PathVariable Long veiculoId,
            @Valid @RequestBody ServicoDTO dto) {

        Servico criado = service.adicionarAoVeiculo(veiculoId, dto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ServicoDTO(criado));
    }

    @Operation(summary = "Atualiza um serviço existente")
    @PutMapping("/{id}")
    public ResponseEntity<ServicoDTO> atualizar(
            @PathVariable Long veiculoId,
            @PathVariable Long id,
            @Valid @RequestBody ServicoDTO dto) {

        Servico upd = service.atualizar(veiculoId, id, dto);
        return ResponseEntity.ok(new ServicoDTO(upd));
    }

    @Operation(summary = "Remove um serviço")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(
            @PathVariable Long veiculoId,
            @PathVariable Long id) {

        service.remover(veiculoId, id);
        return ResponseEntity.noContent().build();
    }
}
