package br.com.oficina.orcamento.controller;

import br.com.oficina.orcamento.service.PdfExportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orcamentos")
@RequiredArgsConstructor
public class PdfExportController {

    private final PdfExportService pdfExportService;

    /**
     * Gera e retorna o PDF do orçamento.
     * GET /orcamentos/{id}/pdf
     */
    @GetMapping(value = "/{id}/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> gerarPdf(@PathVariable("id") Long orcamentoId) {
        try {
            byte[] pdfBytes = pdfExportService.exportarOrcamentoParaPdf(orcamentoId);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            // opcional: força download com nome de arquivo
            headers.setContentDispositionFormData("attachment",
                    "orcamento-" + orcamentoId + ".pdf");
            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            // trata erro de geração
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(("Erro ao gerar PDF: " + e.getMessage()).getBytes());
        }
    }
}
