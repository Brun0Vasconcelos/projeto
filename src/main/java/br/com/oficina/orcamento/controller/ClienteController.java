package br.com.oficina.orcamento.controller;

import br.com.oficina.orcamento.dto.ClienteDTO;
import br.com.oficina.orcamento.model.Cliente;
import br.com.oficina.orcamento.service.ClienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clientes")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@Tag(name = "Clientes", description = "Gerencia o cadastro de clientes")
public class ClienteController {

    private final ClienteService service;

    @Operation(summary = "Cadastra um novo cliente")
    @PostMapping
    public ResponseEntity<ClienteDTO> criar(@RequestBody @Valid ClienteDTO dto) {
        Cliente criado = service.salvar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ClienteDTO(criado));
    }

    @Operation(summary = "Lista todos os clientes")
    @GetMapping
    public ResponseEntity<List<ClienteDTO>> listarTodos() {
        List<ClienteDTO> clientes = service.listar().stream()
                .map(ClienteDTO::new)
                .toList();
        return ResponseEntity.ok(clientes);
    }

    @Operation(summary = "Busca cliente por ID")
    @GetMapping("/{id}")
    public ResponseEntity<ClienteDTO> buscarPorId(@PathVariable Long id) {
        Cliente cliente = service.buscarPorId(id);
        return ResponseEntity.ok(new ClienteDTO(cliente));
    }

    @Operation(summary = "Busca clientes por nome (parcial ou completo)")
    @GetMapping("/search")
    public ResponseEntity<List<ClienteDTO>> buscarPorNome(@RequestParam String nome) {
        List<ClienteDTO> clientes = service.buscarPorNome(nome).stream()
                .map(ClienteDTO::new)
                .toList();
        return ResponseEntity.ok(clientes);
    }

    @Operation(summary = "Atualiza um cliente")
    @PutMapping("/{id}")
    public ResponseEntity<ClienteDTO> atualizar(
            @PathVariable Long id,
            @RequestBody @Valid ClienteDTO dto) {
        Cliente atualizado = service.atualizar(id, dto);
        return ResponseEntity.ok(new ClienteDTO(atualizado));
    }

    @Operation(summary = "Remove um cliente")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        service.remover(id);
        return ResponseEntity.noContent().build();
    }
}
