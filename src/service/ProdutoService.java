package service;

import model.Produto;
import excecoes.*;
import repository.ProdutoRepository;

import java.util.concurrent.ExecutionException;

import java.util.HashMap;
import java.util.Map;

public class ProdutoService {
    private ProdutoRepository repository = new ProdutoRepository();

    public void cadastrarProduto(Produto p) {
        repository.salvar(p); // salva também no Firebase
    }

    public Produto buscarProduto(String id) throws ProdutoNaoEncontradoException {
        try {
            // Espera o resultado do futuro
            Produto p = repository.buscar(id).get();
            if (p == null) {
                throw new ProdutoNaoEncontradoException("Produto com ID " + id + " não encontrado.");
            }
            return p;
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Erro ao buscar produto no banco de dados.", e);
        }
    }

    public void reduzirEstoque(String id, int qtd) throws ProdutoNaoEncontradoException, EstoqueInsuficienteException {
        Produto p = buscarProduto(id);
        p.reduzirEstoque(qtd);

        try {
            // 1. Chama o método que agora retorna um ApiFuture
            // 2. O método .get() VAI PAUSAR A EXECUÇÃO aqui até que o Firebase confirme a atualização
            repository.atualizar(p).get();

            // Adicionei um log para termos certeza de que passou daqui
            System.out.println("--> LOG: Confirmação recebida. Estoque atualizado no Firebase.");

        } catch (InterruptedException | ExecutionException e) {
            // Se houver um erro na comunicação com o Firebase, ele será capturado aqui
            System.err.println("--> ERRO: Falha ao aguardar a atualização do estoque no Firebase.");
            throw new RuntimeException(e);
        }
    }
}
