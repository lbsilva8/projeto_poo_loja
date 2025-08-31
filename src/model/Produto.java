package model;

/**
 * Autoras:
 * Andreísy Neves Ferreira
 * Isabella Paranhos Meireles
 * Lorena da Silva Borges
 */

import excecoes.*;

/**
 * Representa um produto no inventário da loja.
 * Esta classe encapsula todos os dados pertinentes a um produto, como seu identificador,
 * nome, preço e quantidade em estoque. Além de armazenar o estado do produto, ela também
 * contém lógicas de negócio essenciais, como a validação para redução de estoque.
 *
 * @see Venda
 * @see repository.ProdutoRepository
 * @see service.ProdutoService
 * @see service.VendaService
 */
public class Produto {
    private String id;
    private String tipo;
    private String nome;
    private double preco;
    private int quantidade;

    /**
     * Construtor completo para criar uma instância de Produto com todos os atributos.
     *
     * @param id         O identificador único do produto (SKU).
     * @param tipo       A categoria ou tipo do produto.
     * @param nome       O nome de exibição do produto.
     * @param preco      O preço unitário do produto.
     * @param quantidade A quantidade inicial do produto em estoque.
     */
    public Produto(String id, String tipo ,String nome, double preco, int quantidade) {
        this.id = id;
        this.tipo = tipo;
        this.nome = nome;
        this.preco = preco;
        this.quantidade = quantidade;
    }

    /**
     * Construtor sem argumentos, necessário para a desserialização de dados noFirebase.
     */
    public Produto() {}

    /**
     * Reduz a quantidade de itens do produto em estoque.
     * Este metodo contém uma regra de negócio que impede que o estoque se torne negativo.
     *
     * @param qtd A quantidade de itens a ser subtraída do estoque.
     * @throws EstoqueInsuficienteException se a quantidade a ser reduzida for maior
     * que a quantidade atualmente em estoque.
     */
    public void reduzirEstoque(int qtd) throws EstoqueInsuficienteException {
        if (qtd > quantidade) {
            throw new EstoqueInsuficienteException("ERRO: Estoque insuficiente para o produto: " + nome);
        }
        this.quantidade -= qtd;
    }

    /**
     * Retorna o identificador único do produto.
     * @return O ID do produto.
     */
    public String getId() {
        return id;
    }

    /**
     * Define o identificador único do produto.
     * @param id O novo ID do produto.
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Retorna o tipo ou categoria do produto.
     * @return O tipo do produto.
     */
    public String getTipo() {
        return tipo;
    }

    /**
     * Define o tipo ou categoria do produto.
     * @param tipo O novo tipo do produto.
     */
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    /**
     * Retorna o nome de exibição do produto.
     * @return O nome do produto.
     */
    public String getNome() {
        return nome;
    }

    /**
     * Define o nome de exibição do produto.
     * @param nome O novo nome do produto.
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * Retorna o preço unitário do produto.
     * @return O preço do produto.
     */
    public double getPreco() {
        return preco;
    }

    /**
     * Define o preço unitário do produto.
     * @param preco O novo preço do produto.
     */
    public void setPreco(double preco) {
        this.preco = preco;
    }

    /**
     * Retorna a quantidade atual de itens do produto em estoque.
     * @return A quantidade em estoque.
     */
    public int getQuantidade() {
        return quantidade;
    }

    /**
     * Define a quantidade de itens do produto em estoque.
     * @param quantidade A nova quantidade em estoque.
     */
    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }
}
