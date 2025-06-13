// src/main/java/br/com/oficina/orcamento/service/ExcelExportService.java
package br.com.oficina.orcamento.service;

import br.com.oficina.orcamento.dto.OrcamentoDTO;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
@RequiredArgsConstructor
public class ExcelExportService {

    private final OrcamentoService orcService;

    /**
     * Gera um arquivo .xlsx em memória contendo
     * todos os dados do orçamento de um veículo.
     */
    public byte[] exportarOrcamentoParaExcel(Long orcamentoId) throws IOException {
        // aqui usamos porVeiculo() e não atualizar(...)
        OrcamentoDTO dto = orcService.porVeiculo(orcamentoId);

        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet("Orçamento " + dto.id());
            int rowIdx = 0;

            // cabeçalho
            Row title = sheet.createRow(rowIdx++);
            CellStyle h1 = workbook.createCellStyle();
            Font font = workbook.createFont();
            font.setBold(true);
            font.setFontHeightInPoints((short)14);
            h1.setFont(font);
            title.createCell(0).setCellValue("Orçamento #" + dto.id());
            title.getCell(0).setCellStyle(h1);

            // linha em branco
            rowIdx++;

            // dados gerais
            String[] labels = { "Data", "Veículo ID", "Cliente ID", "Desconto (%)", "Parcelas" };
            Object[] values = {
                    dto.data().toString(),
                    dto.veiculoId(),
                    dto.clienteId(),
                    dto.descontoPercentual(),
                    dto.parcelas()
            };
            Row header = sheet.createRow(rowIdx++);
            Row data   = sheet.createRow(rowIdx++);
            for (int i = 0; i < labels.length; i++) {
                Cell c0 = header.createCell(i);
                c0.setCellValue(labels[i]);
                c0.setCellStyle(h1);

                Cell c1 = data.createCell(i);
                if (values[i] instanceof Number) {
                    c1.setCellValue(((Number) values[i]).doubleValue());
                } else {
                    c1.setCellValue(values[i].toString());
                }
            }

            // linha em branco
            rowIdx++;

            // serviços
            Row servLabel = sheet.createRow(rowIdx++);
            servLabel.createCell(0).setCellValue("Serviços incluídos (IDs):");
            servLabel.getCell(0).setCellStyle(h1);

            for (Long sid : dto.servicosIds()) {
                Row r = sheet.createRow(rowIdx++);
                r.createCell(0).setCellValue(sid);
            }

            // linha em branco
            rowIdx++;

            // totais
            Row totLabel = sheet.createRow(rowIdx++);
            totLabel.createCell(0).setCellValue("Total Bruto (R$)");
            totLabel.createCell(1).setCellValue("Total Líquido (R$)");
            totLabel.getCell(0).setCellStyle(h1);
            totLabel.getCell(1).setCellStyle(h1);

            Row totValues = sheet.createRow(rowIdx++);
            totValues.createCell(0).setCellValue(dto.totalBruto().doubleValue());
            totValues.createCell(1).setCellValue(dto.totalLiquido().doubleValue());

            // auto-ajusta colunas
            for (int i = 0; i <= 4; i++) {
                sheet.autoSizeColumn(i);
            }

            // escreve tudo no ByteArray e retorna
            workbook.write(out);
            return out.toByteArray();
        }
    }
}
