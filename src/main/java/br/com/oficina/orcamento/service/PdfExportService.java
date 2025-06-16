package br.com.oficina.orcamento.service;

import br.com.oficina.orcamento.dto.OrcamentoDTO;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.properties.TextAlignment;
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

            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            // Cabeçalho
            doc.add(new Paragraph("ORÇAMENTO OFICINA")
                    .setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD))
                    .setFontSize(18)
                    .setTextAlignment(TextAlignment.CENTER));

            doc.add(new Paragraph("Orçamento ID: " + dto.id()));
            doc.add(new Paragraph("Data: " + dto.data().format(fmt)));
            doc.add(new Paragraph("Cliente: " + dto.nomeCliente() + " (ID: " + dto.clienteId() + ")"));
            doc.add(new Paragraph("Veículo: " + dto.placaVeiculo() + " - " + dto.modeloVeiculo() + " (ID: " + dto.veiculoId() + ")"));

            doc.add(new Paragraph("\nServiços incluídos:\n").setBold());

            Table table = new Table(new float[]{4, 2, 2, 2});
            table.addHeaderCell("Descrição");
            table.addHeaderCell("Entrada");
            table.addHeaderCell("Entrega");
            table.addHeaderCell("Valor");

            dto.itens().forEach(i -> {
                table.addCell(i.descricao());
                table.addCell(i.dataEntrada().format(fmt));
                table.addCell(i.dataEntrega().format(fmt));
                table.addCell(String.format("R$ %.2f", i.valor()));
            });

            doc.add(table);

            doc.add(new Paragraph("\n"));
            doc.add(new Paragraph("Total Bruto: R$ " + dto.totalBruto()).setBold());
            doc.add(new Paragraph("Desconto: " + dto.descontoPercentual() + "%"));
            doc.add(new Paragraph("Total Líquido: R$ " + dto.totalLiquido()).setBold());
            doc.add(new Paragraph("Parcelas: " + dto.parcelas()));

            doc.close();
            return baos.toByteArray();
        }
    }
}
