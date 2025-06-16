package br.com.oficina.orcamento.service;

import br.com.oficina.orcamento.dto.OrcamentoDTO;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class ExcelExportService {

    private final OrcamentoService orcamentoService;

    public byte[] exportarOrcamentoParaExcel(Long orcamentoId) throws IOException {
        OrcamentoDTO dto = orcamentoService.buscarPorId(orcamentoId);

        try (SXSSFWorkbook wb = new SXSSFWorkbook();
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

            Sheet sheet = wb.createSheet("Orçamento");
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            Font boldFont = wb.createFont();
            boldFont.setBold(true);
            CellStyle boldStyle = wb.createCellStyle();
            boldStyle.setFont(boldFont);

            int rowIdx = 0;

            Row row = sheet.createRow(rowIdx++);
            Cell title = row.createCell(0);
            title.setCellValue("Orçamento Oficina");
            title.setCellStyle(boldStyle);

            row = sheet.createRow(rowIdx++);
            row.createCell(0).setCellValue("ID:");
            row.createCell(1).setCellValue(dto.id());

            row = sheet.createRow(rowIdx++);
            row.createCell(0).setCellValue("Data:");
            row.createCell(1).setCellValue(dto.data().format(fmt));

            row = sheet.createRow(rowIdx++);
            row.createCell(0).setCellValue("Cliente:");
            row.createCell(1).setCellValue(dto.nomeCliente() + " (ID: " + dto.clienteId() + ")");

            row = sheet.createRow(rowIdx++);
            row.createCell(0).setCellValue("Veículo:");
            row.createCell(1).setCellValue(dto.placaVeiculo() + " - " + dto.modeloVeiculo());

            rowIdx++;

            // Cabeçalho dos serviços
            row = sheet.createRow(rowIdx++);
            String[] headers = {"Descrição", "Entrada", "Entrega", "Valor"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = row.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(boldStyle);
            }

            // Dados dos itens
            for (var item : dto.itens()) {
                row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(item.descricao());
                row.createCell(1).setCellValue(item.dataEntrada().format(fmt));
                row.createCell(2).setCellValue(item.dataEntrega().format(fmt));
                row.createCell(3).setCellValue(String.format(Locale.US, "R$ %.2f", item.valor()));
            }

            rowIdx++;

            // Totais
            row = sheet.createRow(rowIdx++);
            row.createCell(0).setCellValue("Total Bruto:");
            row.createCell(1).setCellValue(dto.totalBruto().doubleValue());

            row = sheet.createRow(rowIdx++);
            row.createCell(0).setCellValue("Desconto (%):");
            row.createCell(1).setCellValue(dto.descontoPercentual());

            row = sheet.createRow(rowIdx++);
            row.createCell(0).setCellValue("Total Líquido:");
            row.createCell(1).setCellValue(dto.totalLiquido().doubleValue());

            row = sheet.createRow(rowIdx++);
            row.createCell(0).setCellValue("Parcelas:");
            row.createCell(1).setCellValue(dto.parcelas());

            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            wb.write(baos);
            return baos.toByteArray();
        }
    }
}
