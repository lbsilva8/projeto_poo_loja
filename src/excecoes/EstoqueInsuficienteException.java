package excecoes;

/**
 * Autoras:
 * Andreísy Neves Ferreira
 * Isabella Paranhos Meireles
 * Lorena da Silva Borges
 */

/**
 * Exceção personalizada para sinalizar uma falha de regra de negócio quando uma
 * operação tenta consumir mais itens de um produto do que o disponível em estoque.
 * Esta exceção é tipicamente lançada durante o registro de uma venda ou qualquer
 * outra operação que resulte na diminuição do estoque de um {@link model.Produto}.
 *
 * @see java.lang.Exception
 * @see model.Produto#reduzirEstoque
 * @see service.ProdutoService#reduzirEstoque(String, int)
 * @see service.VendaService#registrarVenda
 */
public class EstoqueInsuficienteException extends Exception {
    /**
     * Construtor que cria uma nova instância da exceção com uma mensagem de detalhe.
     *
     * @param msg A mensagem de erro que descreve qual produto está com o estoque insuficiente.
     */
    public EstoqueInsuficienteException(String msg) {
        super(msg);
    }
}
