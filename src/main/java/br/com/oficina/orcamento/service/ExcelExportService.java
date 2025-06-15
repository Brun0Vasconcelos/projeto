package br.com.oficina.orcamento.service;

import br.com.oficina.orcamento.dto.OrcamentoDTO;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class ExcelExportService {

    private final OrcamentoService orcService;

    /**
     * Método chamado pelo seu controller:
     * GET /orcamentos/{id}/excel
     */
    public byte[] exportarOrcamentoParaExcel(Long orcamentoId) throws IOException {
        OrcamentoDTO dto = orcService.buscarPorId(orcamentoId);
        return gerarExcel(dto);
    }

    /**
     * Gera o .xlsx a partir do DTO
     */
    private byte[] gerarExcel(OrcamentoDTO orc) throws IOException {
        try (SXSSFWorkbook wb = new SXSSFWorkbook();
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

            // Cria sheet streaming e habilita o tracking para auto-sizing
            SXSSFSheet sheet = (SXSSFSheet) wb.createSheet("Orçamento");
            sheet.trackAllColumnsForAutoSizing();

            // Fonte e estilo para cabeçalho
            Font boldFont = wb.createFont();
            boldFont.setBold(true);
            CellStyle headerStyle = wb.createCellStyle();
            headerStyle.setFont(boldFont);

            // Formato de data BR
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            int rowIdx = 0;
            Row row;
            Cell cell;

            // Título
            row = sheet.createRow(rowIdx++);
            cell = row.createCell(0);
            cell.setCellValue("ORÇAMENTO");
            CellStyle titleStyle = wb.createCellStyle();
            titleStyle.setFont(boldFont);
            cell.setCellStyle(titleStyle);

            // Linha de ID
            row = sheet.createRow(rowIdx++);
            row.createCell(0).setCellValue("Orçamento ID:");
            row.createCell(1).setCellValue(orc.id());

            // Cliente
            row = sheet.createRow(rowIdx++);
            row.createCell(0).setCellValue("Cliente:");
            row.createCell(1).setCellValue(
                    String.format("%s (ID: %d)", orc.nomeCliente(), orc.clienteId())
            );

            // Veículo
            row = sheet.createRow(rowIdx++);
            row.createCell(0).setCellValue("Veículo:");
            row.createCell(1).setCellValue(
                    String.format("%s %s (ID: %d)",
                            orc.placaVeiculo(), orc.modeloVeiculo(), orc.veiculoId())
            );

            // Data do orçamento
            row = sheet.createRow(rowIdx++);
            row.createCell(0).setCellValue("Data:");
            row.createCell(1).setCellValue(
                    orc.data().format(fmt)
            );

            // Linha em branco
            rowIdx++;

            // Cabeçalho da tabela de itens
            row = sheet.createRow(rowIdx++);
            String[] headers = { "Serviço", "Entrada", "Saída", "Valor" };
            for (int i = 0; i < headers.length; i++) {
                cell = row.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            // Linhas de cada item
            for (var item : orc.itens()) {
                row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(item.descricao());
                row.createCell(1).setCellValue(item.dataEntrada().format(fmt));
                row.createCell(2).setCellValue(item.dataEntrega().format(fmt));
                row.createCell(3).setCellValue(
                        String.format(Locale.US, "R$ %.2f", item.valor())
                );
            }

            // Linha em branco antes dos totais
            rowIdx++;

            // Totais
            row = sheet.createRow(rowIdx++);
            cell = row.createCell(0);
            cell.setCellValue("Total bruto:");
            cell.setCellStyle(headerStyle);
            row.createCell(1).setCellValue(
                    String.format(Locale.US, "R$ %.2f", orc.totalBruto())
            );

            row = sheet.createRow(rowIdx++);
            cell = row.createCell(0);
            cell.setCellValue("Desconto (%):");
            cell.setCellStyle(headerStyle);
            row.createCell(1).setCellValue(orc.descontoPercentual());

            row = sheet.createRow(rowIdx++);
            cell = row.createCell(0);
            cell.setCellValue("Total líquido:");
            cell.setCellStyle(headerStyle);
            row.createCell(1).setCellValue(
                    String.format(Locale.US, "R$ %.2f", orc.totalLiquido())
            );

            row = sheet.createRow(rowIdx++);
            cell = row.createCell(0);
            cell.setCellValue("Parcelas:");
            cell.setCellStyle(headerStyle);
            row.createCell(1).setCellValue(orc.parcelas());

            // Agora sim: auto-size em todas as colunas usadas
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }
            sheet.autoSizeColumn(0);
            sheet.autoSizeColumn(1);

            // Grava em memória e retorna bytes
            wb.write(baos);
            return baos.toByteArray();
        }
    }
}
