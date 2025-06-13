// src/main/java/br/com/oficina/orcamento/dto/ServicoDTO.java
package br.com.oficina.orcamento.dto;

import br.com.oficina.orcamento.enums.FormaPagamento;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ServicoDTO {

    @NotBlank(message = "Descrição é obrigatória")
    private String descricao;

    @NotNull(message = "Data de entrada é obrigatória")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate dataEntrada;

    @NotNull(message = "Data de saída é obrigatória")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate dataEntrega;

    @NotNull(message = "Valor é obrigatório")
    private BigDecimal valor;

    @NotNull(message = "Forma de pagamento é obrigatória")
    private FormaPagamento formaPagamento;

    private String linkNotaFiscal;

    public ServicoDTO() {}

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public LocalDate getDataEntrada() {
        return dataEntrada;
    }

    public void setDataEntrada(LocalDate dataEntrada) {
        this.dataEntrada = dataEntrada;
    }

    public LocalDate getDataEntrega() {
        return dataEntrega;
    }

    public void setDataEntrega(LocalDate dataEntrega) {
        this.dataEntrega = dataEntrega;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public FormaPagamento getFormaPagamento() {
        return formaPagamento;
    }

    public void setFormaPagamento(FormaPagamento formaPagamento) {
        this.formaPagamento = formaPagamento;
    }

    public String getLinkNotaFiscal() {
        return linkNotaFiscal;
    }

    public void setLinkNotaFiscal(String linkNotaFiscal) {
        this.linkNotaFiscal = linkNotaFiscal;
    }
}
