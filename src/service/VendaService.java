package service;

import model.FormaPagamento;
import model.Produto;
import model.Venda;
import model.Usuario;
import excecoes.*;
import repository.VendaRepository;

public class VendaService {
    private final ProdutoService produtoService;
    private final VendaRepository vendaRepository;

    public VendaService(ProdutoService produtoService, VendaRepository vendaRepository) {
        this.produtoService = produtoService;
        this.vendaRepository = vendaRepository;
    }

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