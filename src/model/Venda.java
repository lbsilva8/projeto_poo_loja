package model;
import java.time.LocalDateTime;

public class Venda {
    private String id;
    private Atendente atendente;
    private Produto produto;
    private int quantidade;
    private String dataHora;

    public Venda() {}

    public Venda(String id, Atendente atendente, Produto produto, int quantidade) {
        this.id = id;
        this.atendente = atendente;
        this.produto = produto;
        this.quantidade = quantidade;
        this.dataHora = LocalDateTime.now().toString();
    }

    public double getValorTotal() {
        return produto.getPreco() * quantidade;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Atendente getAtendente() {
        return atendente;
    }

    public void setAtendente(Atendente atendente) {
        this.atendente = atendente;
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
