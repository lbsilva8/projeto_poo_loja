package model;

import java.time.LocalDateTime;

/**
 * Representa uma transação de venda única no sistema.
 * Esta classe funciona como um registro histórico, encapsulando todos os detalhes
 * pertinentes a uma venda no momento em que ela foi concluída.
 * O valor total da venda, já com os descontos aplicados, é calculado e armazenado
 * no momento da criação do objeto, garantindo a imutabilidade do registro financeiro.
 *
 * @see VendaDTO
 * @see service.VendaService
 * @see repository.VendaRepository
 * @see Usuario
 * @see Produto
 */
public class Venda {
    private String id;
    private Usuario usuario;
    private Produto produto;
    private int quantidade;
    private String dataHora;
    private FormaPagamento formaPagamento;
    private double valorTotal;

    /**
     * Construtor completo para criar uma nova instância de Venda.
     * Este construtor inicializa todos os dados da venda, define automaticamente
     * a data e a hora atuais para a transação e calcula o valor total final
     * com base no preço do produto, na quantidade e em qualquer desconto aplicado.
     *
     * @param id             O identificador único para esta venda.
     * @param usuario        O objeto {@link Usuario} (Atendente ou Gerente) que realizou a venda.
     * @param produto        O objeto {@link Produto} vendido.
     * @param quantidade     O número de unidades do produto que foram vendidas.
     * @param formaPagamento A {@link FormaPagamento} utilizada na transação.
     * @param desconto       O valor do desconto em R$ a ser subtraído do total.
     */
    public Venda(String id, Usuario usuario, Produto produto, int quantidade, FormaPagamento formaPagamento, double desconto) {
        this.id = id;
        this.usuario = usuario;
        this.produto = produto;
        this.quantidade = quantidade;
        this.dataHora = LocalDateTime.now().toString();
        this.formaPagamento = formaPagamento;

        // Calcula o preço final da venda
        double valorBruto = produto.getPreco() * quantidade;
        this.valorTotal = valorBruto - desconto;
    }

    /**
     * Construtor sem argumentos, necessário para a desserialização de dados noFirebase.
     */
    public Venda() {}

    /**
     * Retorna o valor total final da venda, já com descontos aplicados.
     * @return O valor total pago.
     */
    public double getValorTotal() {
        return valorTotal;
    }

    /**
     * Define o valor total da venda.
     * @param valorTotal O novo valor total.
     */
    public void setValorTotal(double valorTotal) {
        this.valorTotal = valorTotal;
    }

    /**
     * Retorna a forma de pagamento utilizada na venda.
     * @return O enum {@link FormaPagamento}.
     */
    public FormaPagamento getFormaPagamento() {
        return formaPagamento;
    }

    /**
     * Define a forma de pagamento da venda.
     * @param formaPagamento A nova {@link FormaPagamento}.
     */
    public void setFormaPagamento(FormaPagamento formaPagamento) {
        this.formaPagamento = formaPagamento;
    }

    /**
     * Retorna o identificador único da venda.
     * @return O ID da venda.
     */
    public String getId() {
        return id;
    }

    /**
     * Define o identificador único da venda.
     * @param id O novo ID da venda.
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Retorna o usuário que realizou a venda.
     * @return O objeto {@link Usuario} associado à venda.
     */
    public Usuario getUsuario() {
        return usuario;
    }

    /**
     * Define o usuário que realizou a venda.
     * @param usuario O novo objeto {@link Usuario}.
     */
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    /**
     * Retorna o produto que foi vendido.
     * @return O objeto {@link Produto} associado à venda.
     */
    public Produto getProduto() {
        return produto;
    }

    /**
     * Define o produto que foi vendido.
     * @param produto O novo objeto {@link Produto}.
     */
    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    /**
     * Retorna a quantidade de itens vendidos.
     * @return O número de unidades vendidas.
     */
    public int getQuantidade() {
        return quantidade;
    }

    /**
     * Define a quantidade de itens vendidos.
     * @param quantidade A nova quantidade.
     */
    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    /**
     * Retorna a data e a hora em que a venda foi registrada.
     * @return Uma {@code String} representando a data e a hora.
     */
    public String getDataHora() {
        return dataHora;
    }

    /**
     * Define a data e a hora da venda.
     * @param dataHora A nova {@code String} de data e hora.
     */
    public void setDataHora(String dataHora) {
        this.dataHora = dataHora;
    }
}