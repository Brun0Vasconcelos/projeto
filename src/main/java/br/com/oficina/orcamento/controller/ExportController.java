// src/main/java/br/com/oficina/orcamento/controller/ExportController.java
package br.com.oficina.orcamento.controller;

import br.com.oficina.orcamento.service.ExcelExportService;
import br.com.oficina.orcamento.service.PdfExportService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class ExportController {

    private final PdfExportService pdfService;
    private final ExcelExportService excelService;

    @GetMapping("/orcamentos/{id}/pdf")
    public ResponseEntity<?> baixarPdf(@PathVariable Long id) {
        try {
            byte[] pdf = pdfService.exportarOrcamentoParaPdf(id);
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"orcamento-" + id + ".pdf\"")
                    .body(new ByteArrayResource(pdf));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("mensagem", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("mensagem", "Erro ao gerar PDF"));
        }
    }

    @GetMapping("/orcamentos/{id}/excel")
    public ResponseEntity<?> baixarExcel(@PathVariable Long id) {
        try {
            byte[] xlsx = excelService.exportarOrcamentoParaExcel(id);
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(
                            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"orcamento-" + id + ".xlsx\"")
                    .body(new ByteArrayResource(xlsx));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("mensagem", e.getMessage()));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("mensagem", "Erro ao gerar Excel"));
        }
    }
}
