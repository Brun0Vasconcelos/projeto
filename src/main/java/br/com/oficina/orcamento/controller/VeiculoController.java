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
@CrossOrigin(origins = "*")
@Tag(name = "Veículos", description = "Gerencia os veículos dos clientes")
public class VeiculoController {

    private final VeiculoService service;

    @Operation(summary = "Cadastra um veículo para um cliente")
    @PostMapping("/clientes/{clienteId}/veiculo")
    public ResponseEntity<VeiculoDTO> criar(
            @PathVariable Long clienteId,
            @RequestBody @Valid VeiculoDTO dto
    ) {
        Veiculo salvo = service.adicionarAoCliente(clienteId, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(new VeiculoDTO(salvo));
    }

    @Operation(summary = "Lista todos os veículos")
    @GetMapping
    public ResponseEntity<List<VeiculoDTO>> listar() {
        List<VeiculoDTO> lista = service.listar()
                .stream()
                .map(VeiculoDTO::new)
                .toList();
        return ResponseEntity.ok(lista);
    }

    @Operation(summary = "Busca veículo por ID")
    @GetMapping("/{id}")
    public ResponseEntity<VeiculoDTO> buscarPorId(@PathVariable Long id) {
        Veiculo v = service.buscarPorId(id);
        return ResponseEntity.ok(new VeiculoDTO(v));
    }

    @Operation(summary = "Lista veículos de um cliente")
    @GetMapping("/clientes/{clienteId}/veiculos")
    public ResponseEntity<List<VeiculoDTO>> listarPorCliente(
            @PathVariable Long clienteId) {
        var lista = service
                .listarPorCliente(clienteId)        // novo método no service
                .stream()
                .map(VeiculoDTO::new)
                .toList();
        return ResponseEntity.ok(lista);
    }

    @Operation(summary = "Atualiza um veículo existente")
    @PutMapping("/{id}")
    public ResponseEntity<VeiculoDTO> atualizar(
            @PathVariable Long id,
            @RequestBody @Valid VeiculoDTO dto
    ) {
        Veiculo atualizado = service.atualizar(id, dto);
        return ResponseEntity.ok(new VeiculoDTO(atualizado));
    }

    @Operation(summary = "Remove um veículo")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        service.remover(id);
        return ResponseEntity.noContent().build();
    }
}
