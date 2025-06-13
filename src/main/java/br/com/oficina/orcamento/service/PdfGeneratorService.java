package br.com.oficina.orcamento.service;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;

@Service
public class PdfGeneratorService {

    /**
     * Gera um PDF simples contendo um título e um parágrafo de texto.
     * @param titulo título que aparecerá em negrito no topo
     * @param conteudo bloco de texto principal
     * @return array de bytes do PDF gerado
     */
    public byte[] gerarPdfComTexto(String titulo, String conteudo) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            // 1) Criar writer e documento iText
            PdfWriter writer = new PdfWriter(baos);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            // 2) Carregar fonte padrão (Helvetica) e criar Paragraphs
            PdfFont bold = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
            PdfFont regular = PdfFontFactory.createFont(StandardFonts.HELVETICA);

            // 3) Adicionar título em negrito
            Paragraph pTitulo = new Paragraph(titulo)
                    .setFont(bold)
                    .setFontSize(16);
            document.add(pTitulo);

            // Espaço
            document.add(new Paragraph("\n"));

            // 4) Adicionar corpo de texto
            Paragraph pConteudo = new Paragraph(conteudo)
                    .setFont(regular)
                    .setFontSize(12);
            document.add(pConteudo);

            // 5) Fechar documento
            document.close();

            return baos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar PDF", e);
        }
    }
}
