package service;

import model.Atendente;
import model.Produto;
import model.Venda;
import excecoes.*;
import repository.VendaRepository; // Precisa do repositório de Venda!


public class VendaService {
    private final ProdutoService produtoService;
    private final VendaRepository vendaRepository;

    public VendaService(ProdutoService produtoService, VendaRepository vendaRepository) {
        this.produtoService = produtoService;
        this.vendaRepository = vendaRepository;
    }

    public Venda registrarVenda(String id, Atendente atendente, String produtoId, int quantidade)
            throws ProdutoNaoEncontradoException, EstoqueInsuficienteException {
        // 1. Busca o produto (responsabilidade do ProdutoService)
        Produto produto = produtoService.buscarProduto(produtoId);

        // 2. Reduz o estoque (responsabilidade do ProdutoService)
        produtoService.reduzirEstoque(produtoId, quantidade);

        // 3. Cria a Venda
        // Geração de ID único para a venda
        String novoIdVenda = java.util.UUID.randomUUID().toString();
        Venda novaVenda = new Venda(novoIdVenda, atendente, produto, quantidade);

        // 4. Salva a venda no banco (responsabilidade do VendaRepository)
        vendaRepository.salvar(novaVenda);

        return novaVenda;
    }
}
