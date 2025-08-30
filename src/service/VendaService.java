package service;

import model.FormaPagamento;
import model.Produto;
import model.Venda;
import model.Usuario;
import excecoes.*;
import repository.VendaRepository;

/**
 * Classe de serviço que orquestra a lógica de negócio para o registro de vendas.
 * Suas responsabilidades incluem a validação de dados,
 * a atualização do estoque e a persistência do registro da venda.
 *
 * @see Venda
 * @see ProdutoService
 * @see VendaRepository
 * @see VendaService
 */
public class VendaService {
    private final ProdutoService produtoService;
    private final VendaRepository vendaRepository;

    /**
     * Construtor que utiliza injeção de dependência para receber as instâncias
     * dos serviços e repositórios necessários.
     *
     * @param produtoService  A instância do serviço de produtos, usada para buscar
     * produtos e gerenciar o estoque.
     * @param vendaRepository A instância do repositório de vendas, usada para
     * persistir os dados da venda.
     */
    public VendaService(ProdutoService produtoService, VendaRepository vendaRepository) {
        this.produtoService = produtoService;
        this.vendaRepository = vendaRepository;
    }

    /**
     * Executa o processo completo de registro de uma nova venda no sistema.
     * Este metodo orquestra uma sequência de operações transacionais:
     * 1. Busca e valida o produto.
     * 2. Valida o valor do desconto.
     * 3. Comanda a redução do estoque.
     * 4. Gera um ID único para a venda.
     * 5. Cria a entidade {@link Venda} com os dados finais da transação.
     * 6. Persiste a venda através do repositório.
     *
     * @param usuario        O {@link Usuario} que está realizando a venda.
     * @param produtoId      O ID do {@link Produto} a ser vendido.
     * @param quantidade     A quantidade de itens a serem vendidos.
     * @param formaPagamento A {@link FormaPagamento} utilizada.
     * @param desconto       O valor do desconto a ser aplicado sobre o valor bruto.
     * @return O objeto {@link Venda} finalizado e salvo.
     * @throws ProdutoNaoEncontradoException se o produto com o ID fornecido não existir.
     * @throws EstoqueInsuficienteException  se a quantidade vendida for maior que o estoque disponível.
     * @throws IllegalArgumentException      se o valor do desconto for inválido (negativo ou maior que o total).
     */
    public Venda registrarVenda(Usuario usuario, String produtoId, int quantidade, FormaPagamento formaPagamento, double desconto)
            throws ProdutoNaoEncontradoException, EstoqueInsuficienteException {

        Produto produto = produtoService.buscarProduto(produtoId);

        double valorBruto = produto.getPreco() * quantidade;
        if (desconto < 0 || desconto > valorBruto) {
            throw new IllegalArgumentException("ERRO: O valor do desconto é inválido.");
        }
        produtoService.reduzirEstoque(produtoId, quantidade);
        String novoIdVenda = java.util.UUID.randomUUID().toString();

        Venda novaVenda = new Venda(novoIdVenda, usuario, produto, quantidade, formaPagamento, desconto);

        vendaRepository.salvar(novaVenda);
        return novaVenda;
    }
}