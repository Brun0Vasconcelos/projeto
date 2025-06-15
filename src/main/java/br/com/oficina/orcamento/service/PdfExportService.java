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
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class PdfExportService {

    private final OrcamentoService orcamentoService;

    public byte[] exportarOrcamentoParaPdf(Long id) throws Exception {
        OrcamentoDTO dto = orcamentoService.buscarPorId(id);

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            PdfWriter writer = new PdfWriter(baos);
            PdfDocument pdf = new PdfDocument(writer);
            Document doc = new Document(pdf);

            doc.add(new Paragraph("Orçamento #" + dto.id())
                    .setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD))
                    .setFontSize(18));

            // tabela de info
            Table info = new Table(new float[]{1, 2});
            info.addCell("Data:");
            info.addCell(dto.data()
                    .format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            info.addCell("Cliente:");
            info.addCell(dto.nomeCliente() + " (ID: " + dto.clienteId() + ")");
            info.addCell("Veículo:");
            info.addCell(dto.placaVeiculo() + " " + dto.modeloVeiculo()
                    + " (ID: " + dto.veiculoId() + ")");
            info.addCell("Desconto (%):");
            info.addCell(dto.descontoPercentual().toString());
            info.addCell("Parcelas:");
            info.addCell(dto.parcelas().toString());
            doc.add(info);

            doc.add(new Paragraph("\nServiços incluídos:\n").setFontSize(14).setBold());
            Table servTable = new Table(new float[]{4, 2, 2, 2});
            servTable.addHeaderCell("Serviço");
            servTable.addHeaderCell("Entrada");
            servTable.addHeaderCell("Saída");
            servTable.addHeaderCell("Valor");

            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            dto.itens().forEach(i -> {
                servTable.addCell(i.descricao());
                servTable.addCell(i.dataEntrada().format(fmt));
                servTable.addCell(i.dataEntrega().format(fmt));
                servTable.addCell(String.format("R$ %.2f", i.valor()));
            });
            doc.add(servTable);

            doc.add(new Paragraph("\nTotal bruto: R$ " + dto.totalBruto()).setBold());
            doc.add(new Paragraph("Desconto: " + dto.descontoPercentual() + "%"));
            doc.add(new Paragraph("Total líquido: R$ " + dto.totalLiquido()).setBold());
            doc.add(new Paragraph("Parcelas: " + dto.parcelas()));

            doc.close();
            return baos.toByteArray();
        }
    }
}
