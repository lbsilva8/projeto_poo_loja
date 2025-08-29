package model;
import excecoes.*;

public class Produto {
    private String id;
    private String tipo;
    private String nome;
    private double preco;
    private int quantidade;


    public Produto(String id, String tipo ,String nome, double preco, int quantidade) {
        this.id = id;
        this.tipo = tipo;
        this.nome = nome;
        this.preco = preco;
        this.quantidade = quantidade;
    }

    public Produto() {}

    public void reduzirEstoque(int qtd) throws EstoqueInsuficienteException {
        if (qtd > quantidade) {
            throw new EstoqueInsuficienteException("Estoque insuficiente para o produto: " + nome);
        }
        this.quantidade -= qtd;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }
}
