package service;

import model.Produto;
import excecoes.*;
import repository.ProdutoRepository;

import java.util.concurrent.ExecutionException;

/**
 * Classe de serviço responsável por encapsular as regras de negócio e a orquestração
 * das operações relacionadas à entidade {@link Produto}.
 * Ela define as funcionalidades  disponíveis para a manipulação de produtos, garantindo
 * a execução correta das regras de negócio e o tratamento adequado de exceções.
 *
 * @see Produto
 * @see ProdutoRepository
 * @see VendaService
 */
public class ProdutoService {
    private ProdutoRepository repository = new ProdutoRepository();

    /**
     * Cadastra um novo produto no sistema.
     * Delega a operação de persistência para o repositório.
     *
     * @param p O objeto {@link Produto} a ser cadastrado.
     */
    public void cadastrarProduto(Produto p) {
        repository.salvar(p);
    }

    /**
     * Busca um produto pelo seu identificador único.
     * Este metodo aguarda a conclusão da busca assíncrona do repositório e trata
     * o resultado.
     *
     * @param id O ID do produto a ser buscado.
     * @return O objeto {@link Produto} encontrado.
     * @throws ProdutoNaoEncontradoException se nenhum produto for encontrado com o ID fornecido.
     * @throws RuntimeException se ocorrer um erro de baixo nível (ex: falha de comunicação)
     * durante a busca no banco de dados.
     */
    public Produto buscarProduto(String id) throws ProdutoNaoEncontradoException {
        try {
            Produto p = repository.buscar(id).get();
            if (p == null) {
                throw new ProdutoNaoEncontradoException("LOG: Produto com ID " + id + " não encontrado.");
            }
            return p;
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("ERRO: Erro ao buscar produto no banco de dados.", e);
        }
    }

    /**
     * Reduz o estoque de um produto específico.
     * Orquestra a operação: primeiro busca o produto, depois delega a lógica de
     * redução para o próprio objeto Produto e, por fim, persiste a alteração.
     *
     * @param id  O ID do produto cujo estoque será reduzido.
     * @param qtd A quantidade a ser subtraída do estoque.
     * @throws ProdutoNaoEncontradoException  se o produto não for encontrado.
     * @throws EstoqueInsuficienteException se a quantidade a ser reduzida for maior
     * que o estoque atual, lançada pelo método {@link Produto#reduzirEstoque(int)}.
     */
    public void reduzirEstoque(String id, int qtd) throws ProdutoNaoEncontradoException, EstoqueInsuficienteException {
        Produto p = buscarProduto(id);
        p.reduzirEstoque(qtd);

        try {
            repository.atualizar(p).get();
            System.out.println("LOG: Estoque atualizado no Firebase.");

        } catch (InterruptedException | ExecutionException e) {
            System.err.println("ERRO: Falha ao aguardar a atualização do estoque no Firebase.");
            throw new RuntimeException(e);
        }
    }

    /**
     * Adiciona uma quantidade ao estoque de um produto existente.
     *
     * @param produtoId O ID do produto a ser atualizado.
     * @param quantidadeAdicional A quantidade a ser somada ao estoque atual.
     * @throws ProdutoNaoEncontradoException se o produto não for encontrado.
     * @throws IllegalArgumentException se a quantidade a ser adicionada for menor ou igual a zero.
     */
    public void adicionarEstoque(String produtoId, int quantidadeAdicional) throws ProdutoNaoEncontradoException {
        if (quantidadeAdicional <= 0) {
            throw new IllegalArgumentException("LOG: A quantidade a ser adicionada deve ser maior que zero.");
        }

        Produto produto = buscarProduto(produtoId);

        int novoEstoque = produto.getQuantidade() + quantidadeAdicional;
        produto.setQuantidade(novoEstoque);


        try {
            repository.atualizar(produto).get();
            System.out.println("--> LOG: Estoque do produto '" + produto.getNome() + "' atualizado no Firebase.");
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("ERRO: Falha ao atualizar o produto no banco de dados.", e);
        }
    }
}

