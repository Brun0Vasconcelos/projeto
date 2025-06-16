package br.com.oficina.orcamento.controller;

import br.com.oficina.orcamento.dto.ServicoDTO;
import br.com.oficina.orcamento.model.Servico;
import br.com.oficina.orcamento.service.ServicoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/servicos")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "Serviços", description = "Gerencia os serviços realizados nos veículos")
public class ServicoController {

    private final ServicoService service;

    @Operation(summary = "Cadastra um serviço para um veículo")
    @PostMapping("/veiculo/{veiculoId}")
    public ResponseEntity<ServicoDTO> criar(
            @PathVariable Long veiculoId,
            @RequestBody @Valid ServicoDTO dto
    ) {
        Servico salvo = service.adicionarAoVeiculo(veiculoId, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ServicoDTO(salvo));
    }

    @Operation(summary = "Lista todos os serviços")
    @GetMapping
    public ResponseEntity<List<ServicoDTO>> listar() {
        List<ServicoDTO> lista = service.listar()
                .stream()
                .map(ServicoDTO::new)
                .toList();
        return ResponseEntity.ok(lista);
    }

    @Operation(summary = "Busca serviço por ID")
    @GetMapping("/{id}")
    public ResponseEntity<ServicoDTO> buscarPorId(@PathVariable Long id) {
        Servico s = service.buscarPorId(id);
        return ResponseEntity.ok(new ServicoDTO(s));
    }

    @Operation(summary = "Lista serviços de um veículo")
    @GetMapping("/veiculo/{veiculoId}")
    public ResponseEntity<List<ServicoDTO>> listarPorVeiculo(@PathVariable Long veiculoId) {
        List<ServicoDTO> servicos = service.listarPorVeiculo(veiculoId)
                .stream()
                .map(ServicoDTO::new)
                .toList();
        return ResponseEntity.ok(servicos);
    }

    @Operation(summary = "Atualiza um serviço")
    @PutMapping("/{id}")
    public ResponseEntity<ServicoDTO> atualizar(
            @PathVariable Long id,
            @RequestBody @Valid ServicoDTO dto
    ) {
        Servico atualizado = service.atualizar(id, dto);
        return ResponseEntity.ok(new ServicoDTO(atualizado));
    }

    @Operation(summary = "Remove um serviço")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        service.remover(id);
        return ResponseEntity.noContent().build();
    }
}
