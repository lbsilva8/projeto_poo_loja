package excecoes;

import model.FormaPagamento;
import model.Usuario;

/**
 * Exceção personalizada para sinalizar que um produto específico não foi encontrado
 * no banco de dados durante uma operação de busca.
 * Ela representa uma falha
 * na recuperação de dados onde o identificador fornecido não corresponde a nenhum
 * registro existente.
 *
 * @see java.lang.Exception
 * @see service.ProdutoService#buscarProduto(String)
 * @see service.ProdutoService#reduzirEstoque(String, int)
 * @see service.ProdutoService#adicionarEstoque(String, int)
 * @see service.VendaService#registrarVenda(Usuario, String, int, FormaPagamento, double)
 */
public class ProdutoNaoEncontradoException extends Exception {
    /**
     * Construtor que cria uma nova instância da exceção com uma mensagem de detalhe.
     *
     * @param msg A mensagem de erro que descreve a falha na busca, geralmente
     * incluindo o identificador do produto que não foi encontrado.
     */
    public ProdutoNaoEncontradoException(String msg) {
        super(msg);
    }
}
