import database.FirebaseConfig;
import excecoes.*;
import model.*;
import repository.UsuarioRepository;
import repository.VendaRepository;
import service.ProdutoService;
import service.UsuarioService;
import service.VendaService;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);
    private static final ProdutoService produtoService = new ProdutoService();
    private static final UsuarioService usuarioService = new UsuarioService();
    private static final VendaService vendaService = new VendaService(produtoService, new VendaRepository());

    public static void main(String[] args) {
        System.out.println("Iniciando conexão com o banco de dados...");
        FirebaseConfig.initialize();
        System.out.println("Conexão estabelecida com sucesso!");

        // SETUP INICIAL NO BANCO (executar apenas uma vez para popular o banco)
         //setupUsuariosIniciais(); // Comente esta linha após a primeira execução

        Usuario usuarioLogado = null;
        while (usuarioLogado == null) {
            try {
                System.out.println("\n--- LOGIN DO SISTEMA ---");
                System.out.print("Usuário: ");
                String login = scanner.nextLine();
                System.out.print("Senha: ");
                String senha = scanner.nextLine();
                usuarioLogado = usuarioService.autenticar(login, senha);
                System.out.println("\nLogin bem-sucedido! Bem-vindo(a), " + usuarioLogado.getNome() + " (" + usuarioLogado.getCargo() + ")");
            } catch (AutenticacaoException e) {
                System.err.println("ACESSO NEGADO: " + e.getMessage());
            } catch (Exception e) {
                System.err.println("Ocorreu um erro inesperado no login: " + e.getMessage());
            }
        }

        boolean executando = true;
        while (executando) {
            exibirMenu(usuarioLogado);
            try {
                int opcao = scanner.nextInt();
                scanner.nextLine();

                if (usuarioLogado instanceof Gerente) {
                    executando = processarOpcaoGerente(opcao, usuarioLogado);
                } else { // É um Atendente
                    executando = processarOpcaoAtendente(opcao, usuarioLogado);
                }
            } catch (InputMismatchException e) {
                System.err.println("Erro: Por favor, digite um número válido para a opção.");
                scanner.nextLine();
            }
        }

        System.out.println("Encerrando o sistema...");
        scanner.close();
    }

    private static void exibirMenu(Usuario usuario) {
        System.out.println("\n--- MENU PRINCIPAL ---");
        System.out.println("1. Realizar Venda");
        if (usuario instanceof Gerente) {
            System.out.println("2. Cadastrar Produto");
            System.out.println("3. Cadastrar Novo Atendente");
            System.out.println("4. Adicionar Estoque de Produto");
        }
        System.out.println("0. Sair do Sistema");
        System.out.print("Escolha uma opção: ");
    }

    private static boolean processarOpcaoGerente(int opcao, Usuario gerenteLogado) {
        switch (opcao) {
            case 1:
                realizarVenda(gerenteLogado);
                break;
            case 2:
                cadastrarProduto();
                break;
            case 3:
                cadastrarNovoAtendente(gerenteLogado);
                break;
            case 4:
                adicionarEstoqueProduto(); // <-- NOVA CHAMADA
                break;
            case 0:
                return false;
            default:
                System.err.println("Opção inválida. Tente novamente.");
        }
        return true;
    }

    private static boolean processarOpcaoAtendente(int opcao, Usuario atendenteLogado) {
        switch (opcao) {
            case 1:
                realizarVenda(atendenteLogado);
                break;
            case 0:
                return false;
            default:
                System.err.println("Opção inválida. Tente novamente.");
        }
        return true;
    }

    private static void realizarVenda(Usuario usuario) {
        try {
            System.out.println("\n--- REGISTRO DE VENDA ---");
            System.out.print("Digite o ID do produto: ");
            String produtoId = scanner.nextLine();

            Produto produto = produtoService.buscarProduto(produtoId);
            System.out.println("Produto encontrado: " + produto.getNome() + " | Preço Unitário: R$" + produto.getPreco());

            System.out.print("Digite a quantidade: ");
            int quantidade = scanner.nextInt();
            scanner.nextLine();

            System.out.print("Digite o desconto em R$ (ou 0): ");
            double desconto = scanner.nextDouble();
            scanner.nextLine();

            System.out.println("Escolha a forma de pagamento:");
            for (FormaPagamento fp : FormaPagamento.values()) {
                System.out.println((fp.ordinal() + 1) + " - " + fp.getDescricao());
            }
            System.out.print("Opção: ");
            int formaPagamentoOp = scanner.nextInt();
            scanner.nextLine();
            FormaPagamento formaPagamento = FormaPagamento.values()[formaPagamentoOp - 1];

            Venda venda = vendaService.registrarVenda(usuario, produtoId, quantidade, formaPagamento, desconto);

            System.out.println("\nVENDA REALIZADA COM SUCESSO!");
            System.out.println("Valor Total Pago: R$" + String.format("%.2f", venda.getValorTotal()));
            System.out.println("Estoque restante do produto: " + produtoService.buscarProduto(produtoId).getQuantidade());

        } catch (ProdutoNaoEncontradoException | EstoqueInsuficienteException e) {
            System.err.println("Erro na venda: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.err.println("Erro de validação: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Ocorreu um erro inesperado ao registrar a venda: " + e.getMessage());
        }
    }

    private static void cadastrarProduto() {
        try {
            System.out.println("\n--- CADASTRO DE PRODUTO ---");
            System.out.print("ID do produto (ex: prod001): ");
            String id = scanner.nextLine();
            System.out.print("Nome do produto: ");
            String nome = scanner.nextLine();
            System.out.print("Tipo/Categoria: ");
            String tipo = scanner.nextLine();
            System.out.print("Preço (ex: 79.90): ");
            double preco = scanner.nextDouble();
            scanner.nextLine();
            System.out.print("Quantidade em estoque: ");
            int quantidade = scanner.nextInt();
            scanner.nextLine();

            Produto novoProduto = new Produto(id, tipo, nome, preco, quantidade);
            produtoService.cadastrarProduto(novoProduto);

            System.out.println("\nProduto '" + nome + "' cadastrado com sucesso!");
        } catch (InputMismatchException e) {
            System.err.println("Erro de entrada. Verifique se os valores numéricos estão corretos.");
            scanner.nextLine();
        } catch (Exception e) {
            System.err.println("Ocorreu um erro ao cadastrar o produto: " + e.getMessage());
        }
    }

    private static void cadastrarNovoAtendente(Usuario gerenteLogado) {
        try {
            System.out.println("\n--- CADASTRO DE NOVO ATENDENTE ---");
            System.out.print("Matrícula (ex: 201): ");
            int matricula = scanner.nextInt();
            scanner.nextLine();
            System.out.print("Nome completo: ");
            String nome = scanner.nextLine();
            System.out.print("Nome de usuário (para login): ");
            String usuario = scanner.nextLine();
            System.out.print("Senha: ");
            String senha = scanner.nextLine();

            Atendente novoAtendente = new Atendente(matricula, nome, usuario, senha);
            usuarioService.cadastrarAtendente(gerenteLogado, novoAtendente);

            System.out.println("\nAtendente '" + nome + "' cadastrado com sucesso!");
        } catch (AcessoNegadoException e) {
            System.err.println("Erro de Permissão: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Ocorreu um erro ao cadastrar o atendente: " + e.getMessage());
        }
    }
    private static void adicionarEstoqueProduto() {
        try {
            System.out.println("\n--- ADICIONAR AO ESTOQUE ---");
            System.out.print("Digite o ID do produto para atualizar: ");
            String produtoId = scanner.nextLine();

            Produto produto = produtoService.buscarProduto(produtoId);
            System.out.println("Produto: " + produto.getNome() + " | Estoque Atual: " + produto.getQuantidade());

            System.out.print("Digite a quantidade a ser ADICIONADA ao estoque: ");
            int quantidadeAdicional = scanner.nextInt();
            scanner.nextLine();

            produtoService.adicionarEstoque(produtoId, quantidadeAdicional);

            Produto produtoAtualizado = produtoService.buscarProduto(produtoId);
            System.out.println("\nESTOQUE ATUALIZADO COM SUCESSO!");
            System.out.println("Novo estoque do produto '" + produtoAtualizado.getNome() + "': " + produtoAtualizado.getQuantidade());

        } catch (ProdutoNaoEncontradoException e) {
            System.err.println("Erro: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.err.println("Erro de Validação: " + e.getMessage());
        } catch (InputMismatchException e) {
            System.err.println("Erro: Por favor, digite um número válido para a quantidade.");
            scanner.nextLine();
        } catch (Exception e) {
            System.err.println("Ocorreu um erro inesperado: " + e.getMessage());
        }
    }

    private static void setupUsuariosIniciais() {
        System.out.println("Configurando usuários iniciais no Firebase...");
        Gerente gerenteAdmin = new Gerente(101, "Ana Gerente", "gerente", "admin");
        Atendente atendentePadrao = new Atendente(202, "Bruno Atendente", "atendente", "123");

        UsuarioRepository usuarioRepo = new UsuarioRepository();
        usuarioRepo.salvar(gerenteAdmin);
        usuarioRepo.salvar(atendentePadrao);
        System.out.println("Usuários iniciais configurados.");
    }
}