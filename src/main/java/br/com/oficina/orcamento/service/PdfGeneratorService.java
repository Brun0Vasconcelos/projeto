package br.com.oficina.orcamento.service;

import br.com.oficina.orcamento.dto.ItemOrcamentoDTO;
import br.com.oficina.orcamento.dto.OrcamentoDTO;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.math.RoundingMode;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class PdfGeneratorService {

    public byte[] gerarPdf(OrcamentoDTO orc) throws Exception {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            var writer = new PdfWriter(baos);
            var pdf = new com.itextpdf.kernel.pdf.PdfDocument(writer);
            var doc = new Document(pdf);

            // Cabeçalho
            doc.add(new Paragraph("ORÇAMENTO")
                    .setFont(PdfFontFactory.createFont())
                    .setFontSize(18)
                    .setBold()
                    .setTextAlignment(TextAlignment.CENTER));

            doc.add(new Paragraph(String.format("Orçamento ID: %d", orc.id()))
                    .setTextAlignment(TextAlignment.LEFT));
            doc.add(new Paragraph(String.format("Cliente: %s (ID: %d)", orc.nomeCliente(), orc.clienteId())));
            doc.add(new Paragraph(String.format("Veículo: %s %s (ID: %d)", orc.placaVeiculo(), orc.modeloVeiculo(), orc.veiculoId())));
            doc.add(new Paragraph(String.format("Data: %s", orc.data()))
                    .setTextAlignment(TextAlignment.RIGHT));
            doc.add(new Paragraph("\n"));

            // Tabela de itens
            Table tabela = new Table(new float[]{40, 100, 60, 60, 60});
            // Definindo largura em percentual
            tabela.setWidth(UnitValue.createPercentValue(100));

            tabela.addHeaderCell(headerCell("ID"));
            tabela.addHeaderCell(headerCell("Descrição"));
            tabela.addHeaderCell(headerCell("Entrada"));
            tabela.addHeaderCell(headerCell("Saída"));
            tabela.addHeaderCell(headerCell("Valor"));

            for (ItemOrcamentoDTO item : orc.itens()) {
                tabela.addCell(cell(item.id().toString()));
                tabela.addCell(cell(item.descricao()));
                tabela.addCell(cell(item.dataEntrada().toString()));
                tabela.addCell(cell(item.dataEntrega().toString()));
                tabela.addCell(cell(item.valor()
                        .setScale(2, RoundingMode.HALF_UP)
                        .toString()));
            }
            doc.add(tabela);

            // Totais
            doc.add(new Paragraph("\n"));
            doc.add(new Paragraph(String.format(Locale.US,
                    "Total bruto: R$ %.2f", orc.totalBruto()))
                    .setBold());
            doc.add(new Paragraph(
                    "Desconto: " + orc.descontoPercentual() + "%"));
            doc.add(new Paragraph(String.format(Locale.US,
                    "Total líquido: R$ %.2f", orc.totalLiquido()))
                    .setBold());
            doc.add(new Paragraph("Parcelas: " + orc.parcelas()));

            doc.close();
            return baos.toByteArray();
        }
    }

    private Cell headerCell(String texto) {
        return new Cell()
                .add(new Paragraph(texto)
                        .setBold()
                        .setFontColor(ColorConstants.WHITE))
                .setBackgroundColor(ColorConstants.DARK_GRAY)
                .setTextAlignment(TextAlignment.CENTER);
    }

    private Cell cell(String texto) {
        return new Cell()
                .add(new Paragraph(texto));
    }
}
