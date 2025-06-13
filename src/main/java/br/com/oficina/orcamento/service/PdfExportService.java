// src/main/java/br/com/oficina/orcamento/service/PdfExportService.java
package br.com.oficina.orcamento.service;

import br.com.oficina.orcamento.dto.OrcamentoDTO;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;

@Service
@RequiredArgsConstructor
public class PdfExportService {

    private final OrcamentoService orcService;

    public byte[] exportarOrcamentoParaPdf(Long orcamentoId) throws Exception {
        // busca o DTO sem alterar nada
        OrcamentoDTO dto = orcService.buscarPorId(orcamentoId);

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            PdfWriter writer = new PdfWriter(baos);
            PdfDocument pdf = new PdfDocument(writer);
            Document doc = new Document(pdf);

            // Título
            doc.add(new Paragraph("Orçamento #" + dto.id())
                    .setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD))
                    .setFontSize(18));

            // Informações gerais (tabela 2 colunas)
            Table info = new Table(new float[]{1, 2});
            info.addCell("Data:");
            info.addCell(dto.data().toString());
            info.addCell("Veículo ID:");
            info.addCell(dto.veiculoId().toString());
            info.addCell("Cliente ID:");
            info.addCell(dto.clienteId().toString());
            info.addCell("Desconto (%):");
            info.addCell(dto.descontoPercentual().toString());
            info.addCell("Parcelas:");
            info.addCell(dto.parcelas().toString());
            doc.add(info);

            // Serviços
            doc.add(new Paragraph("Serviços incluídos:")
                    .setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD))
                    .setFontSize(14));

            Table servTable = new Table(new float[]{1});
            servTable.addHeaderCell("Serviço ID");
            for (Long sid : dto.servicosIds()) {
                servTable.addCell(sid.toString());
            }
            doc.add(servTable);

            // Totais
            doc.add(new Paragraph("Total bruto: R$ " + dto.totalBruto()));
            doc.add(new Paragraph("Total líquido: R$ " + dto.totalLiquido()));

            doc.close();
            return baos.toByteArray();
        }
    }
}
