package model;

import java.time.LocalDateTime;

public class Venda {
    private String id;
    private Usuario usuario;
    private Produto produto;
    private int quantidade;
    private String dataHora;
    private FormaPagamento formaPagamento;
    private double valorTotal;

    public Venda(String id, Usuario usuario, Produto produto, int quantidade, FormaPagamento formaPagamento, double desconto) {
        this.id = id;
        this.usuario = usuario;
        this.produto = produto;
        this.quantidade = quantidade;
        this.dataHora = LocalDateTime.now().toString();
        this.formaPagamento = formaPagamento;
        double valorBruto = produto.getPreco() * quantidade;
        this.valorTotal = valorBruto - desconto;
    }

    public Venda() {}

    public double getValorTotal() {
        return valorTotal;
    }

    public FormaPagamento getFormaPagamento() {
        return formaPagamento;
    }

    public void setValorTotal(double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public void setFormaPagamento(FormaPagamento formaPagamento) {
        this.formaPagamento = formaPagamento;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public String getDataHora() {
        return dataHora;
    }

    public void setDataHora(String dataHora) {
        this.dataHora = dataHora;
    }
}