import model.Atendente;
import model.Produto;
import model.Venda;
import service.ProdutoService;
import service.VendaService;
import repository.VendaRepository; // Main ainda precisa dele para injetar no serviço
import excecoes.EstoqueInsuficienteException;
import excecoes.ProdutoNaoEncontradoException;
import database.FirebaseConfig;

public class Main {

    public static void main(String[] args) {
        // 1. Inicialização
        FirebaseConfig.initialize();

        // 2. Injeção de Dependências
        ProdutoService produtoService = new ProdutoService();
        VendaRepository vendaRepo = new VendaRepository();
        VendaService vendaService = new VendaService(produtoService, vendaRepo);

        // 3. Setup do Cenário
        Atendente atendente = new Atendente(1, "Lorena", "lborges", "1234");
        Produto camisa = new Produto("prod001", "Roupas", "Camisa Polo", 79.90, 10);

        // Cadastra o produto usando o serviço
        produtoService.cadastrarProduto(camisa);
        System.out.println("Produto cadastrado: " + camisa.getNome());

        // 4. Execução da Lógica de Negócio
        try {
            System.out.println("\nRealizando uma venda...");
            // A Main só chama o serviço. O serviço faz todo o resto.
            Venda venda = vendaService.registrarVenda("1", atendente, "prod001", 2);

            System.out.println("Venda realizada com sucesso!");
            System.out.println("ID da Venda: " + venda.getId());
            System.out.println("Produto: " + venda.getProduto().getNome());
            System.out.println("Quantidade: " + venda.getQuantidade());
            System.out.println("Valor total: R$" + venda.getValorTotal());

            // Verifica o estoque restante
            Produto produtoAtualizado = produtoService.buscarProduto("prod001");
            System.out.println("Estoque restante da camisa: " + produtoAtualizado.getQuantidade()); // Deve ser 8

        } catch (EstoqueInsuficienteException | ProdutoNaoEncontradoException e) {
            System.err.println("Falha ao realizar venda: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Ocorreu um erro inesperado: " + e.getMessage());
        }
    }
}