package br.com.oficina.orcamento.controller;

import br.com.oficina.orcamento.service.ExcelExportService;
import br.com.oficina.orcamento.service.PdfExportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orcamentos")
@Tag(name = "Exportação", description = "Exporta orçamentos para PDF ou Excel")
@RequiredArgsConstructor
public class ExportController {

    private final PdfExportService pdfService;
    private final ExcelExportService excelService;

    @Operation(summary = "Exporta um orçamento para PDF")
    @ApiResponse(
            responseCode = "200",
            description = "Arquivo PDF",
            content = @Content(
                    mediaType = MediaType.APPLICATION_PDF_VALUE,
                    schema = @Schema(type = "string", format = "binary")
            )
    )
    @GetMapping(value = "/{id}/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> exportarPdf(@PathVariable Long id) {
        try {
            byte[] pdf = pdfService.exportarOrcamentoParaPdf(id);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDisposition(ContentDisposition
                    .attachment()
                    .filename("orcamento_" + id + ".pdf")
                    .build());
            return new ResponseEntity<>(pdf, headers, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Exporta um orçamento para Excel")
    @ApiResponse(
            responseCode = "200",
            description = "Arquivo Excel (.xlsx)",
            content = @Content(
                    mediaType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                    schema = @Schema(type = "string", format = "binary")
            )
    )
    @GetMapping(value = "/{id}/excel", produces = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    public ResponseEntity<byte[]> exportarExcel(@PathVariable Long id) {
        try {
            byte[] excel = excelService.exportarOrcamentoParaExcel(id);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType(
                    "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
            ));
            headers.setContentDisposition(ContentDisposition
                    .attachment()
                    .filename("orcamento_" + id + ".xlsx")
                    .build());
            return new ResponseEntity<>(excel, headers, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
