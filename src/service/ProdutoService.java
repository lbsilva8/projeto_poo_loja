package service;

import model.Produto;
import excecoes.*;
import repository.ProdutoRepository;

import java.util.concurrent.ExecutionException;


public class ProdutoService {
    private ProdutoRepository repository = new ProdutoRepository();

    public void cadastrarProduto(Produto p) {
        repository.salvar(p);
    }

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

