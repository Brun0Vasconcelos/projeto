package br.com.oficina.orcamento.dto;

import br.com.oficina.orcamento.enums.FormaPagamento;
import br.com.oficina.orcamento.model.Servico;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ServicoDTO {
    private Long id;
    private String descricao;
    private LocalDate dataEntrada;
    private LocalDate dataEntrega;
    private BigDecimal valor;
    private FormaPagamento formaPagamento;
    private String linkNotaFiscal;

    public ServicoDTO() {
        // construtor vazio para frameworks
    }

    public ServicoDTO(Long id, String descricao, LocalDate dataEntrada, LocalDate dataEntrega,
                      BigDecimal valor, FormaPagamento formaPagamento, String linkNotaFiscal) {
        this.id = id;
        this.descricao = descricao;
        this.dataEntrada = dataEntrada;
        this.dataEntrega = dataEntrega;
        this.valor = valor;
        this.formaPagamento = formaPagamento;
        this.linkNotaFiscal = linkNotaFiscal;
    }

    public ServicoDTO(Servico s) {
        this(s.getId(), s.getDescricao(), s.getDataEntrada(), s.getDataEntrega(),
                s.getValor(), s.getFormaPagamento(), s.getLinkNotaFiscal());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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
