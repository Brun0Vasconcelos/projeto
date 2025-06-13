// src/main/java/br/com/oficina/orcamento/controller/PdfController.java
package br.com.oficina.orcamento.controller;

import br.com.oficina.orcamento.service.PdfExportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pdf")
@Tag(name = "PDF", description = "Exportação de orçamentos para PDF")
public class PdfController {

    private final PdfExportService pdfExportService;

    public PdfController(PdfExportService pdfExportService) {
        this.pdfExportService = pdfExportService;
    }

    @Operation(summary = "Exporta um orçamento para PDF")
    @GetMapping(value = "/orcamento/{id}", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> exportarOrcamentoPdf(@PathVariable Long id) {
        try {
            byte[] pdf = pdfExportService.exportarOrcamentoParaPdf(id);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "orcamento_" + id + ".pdf");
            return new ResponseEntity<>(pdf, headers, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
