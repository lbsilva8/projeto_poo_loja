package controller;

/**
 * Autoras:
 * Andreísy Neves Ferreira
 * Isabella Paranhos Meireles
 * Lorena da Silva Borges
 */

import database.FirebaseConfig;
import excecoes.*;
import model.*;
import repository.UsuarioRepository;
import service.ProdutoService;
import service.UsuarioService;
import service.VendaService;

import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Classe controladora principal da aplicação Ponto de Venda (PDV).
 * Seguindo o padrão de design MVC (Model-View-Controller), esta classe atua como o
 * Controller. Ela é o "cérebro" da interação com o usuário, responsável por:
 * Controlar o fluxo da aplicação (login, menu, encerramento).</li>
 * Receber as entradas do usuário (através da View, que neste caso é o console).</li>
 * Delegar as operações de negócio para a camada de Serviço (Model).</li>
 * Esta separação de responsabilidades torna a aplicação mais organizada, testável e flexível.
 *
 * @see service.UsuarioService
 * @see service.ProdutoService
 * @see service.VendaService
 * @see model.Permissao
 */
public class SistemaController {

    private final Scanner scanner = new Scanner(System.in);
    private final ProdutoService produtoService;
    private final UsuarioService usuarioService;
    private final VendaService vendaService;

    /**
     * Constrói o controlador, inicializando todas as dependências de serviço necessárias.
     * Este construtor atua como a "raiz de composição" da aplicação, criando as instâncias
     * dos serviços e injetando as dependências necessárias (como o ProdutoService no VendaService).
     */
    public SistemaController() {
        this.produtoService = new ProdutoService();
        this.usuarioService = new UsuarioService();
        this.vendaService = new VendaService(this.produtoService, new repository.VendaRepository());
    }

    /**
     * Inicia e executa o ciclo de vida completo da aplicação.
     * Este é o metodo de ponto de entrada público para o fluxo da aplicação. Ele orquestra
     * a inicialização do banco, o loop de login e o loop do menu principal.
     */
    public void iniciar() {
        FirebaseConfig.initialize();
        System.out.println("Conexão estabelecida com sucesso!");

         //setupUsuariosIniciais(); // Descomente para popular o banco na primeira execução.

        Usuario usuarioLogado = executarLoopDeLogin();

        if (usuarioLogado != null) {
            executarLoopPrincipal(usuarioLogado);
        }

        System.out.println("Encerrando o sistema...");
        scanner.close();
    }

    /**
     * Gerencia o fluxo de autenticação, solicitando credenciais até que um login
     * válido seja realizado.
     *
     * @return O objeto {@link Usuario} que foi autenticado com sucesso.
     */
    private Usuario executarLoopDeLogin() {
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
        return usuarioLogado;
    }

    /**
     * Gerencia o loop principal da aplicação, exibindo o menu e processando as opções
     * do usuário até que a opção de sair seja escolhida.
     *
     * @param usuarioLogado O usuário que está logado e interagindo com o sistema.
     */
    private void executarLoopPrincipal(Usuario usuarioLogado) {
        boolean executando = true;
        while (executando) {
            exibirMenu(usuarioLogado);
            try {
                int opcao = scanner.nextInt();
                scanner.nextLine();

                switch (opcao) {
                    case 1:
                        if (usuarioLogado.temPermissao(Permissao.REALIZAR_VENDA)) realizarVenda(usuarioLogado);
                        else System.err.println("Acesso Negado.");
                        break;
                    case 2:
                        if (usuarioLogado.temPermissao(Permissao.CADASTRAR_PRODUTO)) cadastrarProduto();
                        else System.err.println("Acesso Negado.");
                        break;
                    case 3:
                        if (usuarioLogado.temPermissao(Permissao.GERENCIAR_USUARIOS))
                            cadastrarNovoAtendente(usuarioLogado);
                        else System.err.println("Acesso Negado.");
                        break;
                    case 4:
                        if (usuarioLogado.temPermissao(Permissao.GERENCIAR_ESTOQUE)) adicionarEstoqueProduto();
                        else System.err.println("Acesso Negado.");
                        break;
                    case 0:
                        executando = false;
                        break;
                    default:
                        System.err.println("Opção inválida. Tente novamente.");
                }
            } catch (InputMismatchException e) {
                System.err.println("Erro: Por favor, digite um número válido para a opção.");
                scanner.nextLine();
            }
        }
    }

    /**
     * Exibe o menu de opções apropriado com base nas permissões do usuário..
     *
     * @param usuario O usuário atualmente logado.
     */
    private void exibirMenu(Usuario usuario) {
        System.out.println("\n--- MENU PRINCIPAL ---");
        if (usuario.temPermissao(Permissao.REALIZAR_VENDA)) {
            System.out.println("1. Realizar Venda");
        }
        if (usuario.temPermissao(Permissao.CADASTRAR_PRODUTO)) {
            System.out.println("2. Cadastrar Produto");
        }
        if (usuario.temPermissao(Permissao.GERENCIAR_USUARIOS)) {
            System.out.println("3. Cadastrar Novo Atendente");
        }
        if (usuario.temPermissao(Permissao.GERENCIAR_ESTOQUE)) {
            System.out.println("4. Gerenciar Estoque");
        }
        System.out.println("0. Sair do Sistema");
        System.out.print("Escolha uma opção: ");
    }

    /**
     * Direciona a ação com base na opção escolhida por um Gerente.
     *
     * @param opcao         A opção numérica escolhida.
     * @param gerenteLogado O objeto do Gerente logado.
     * @return {@code false} se a opção de sair for escolhida, {@code true} caso contrário.
     */
    private boolean processarOpcaoGerente(int opcao, Usuario gerenteLogado) {
        switch (opcao) {
            case 1: realizarVenda(gerenteLogado); break;
            case 2: cadastrarProduto(); break;
            case 3: cadastrarNovoAtendente(gerenteLogado); break;
            case 4: adicionarEstoqueProduto(); break;
            case 0: return false;
            default: System.err.println("Opção inválida. Tente novamente.");
        }
        return true;
    }

    /**
     * Direciona a ação com base na opção escolhida por um Atendente.
     *
     * @param opcao           A opção numérica escolhida.
     * @param atendenteLogado O objeto do Atendente logado.
     * @return {@code false} se a opção de sair for escolhida, {@code true} caso contrário.
     */
    private boolean processarOpcaoAtendente(int opcao, Usuario atendenteLogado) {
        switch (opcao) {
            case 1: realizarVenda(atendenteLogado); break;
            case 0: return false;
            default: System.err.println("Opção inválida. Tente novamente.");
        }
        return true;
    }

    /**
     * Gerencia a interface de console para o caso de uso "Realizar Venda".
     *
     * @param usuario O usuário que está realizando a venda.
     */
    private void realizarVenda(Usuario usuario) {
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

    /**
     * Gerencia a interface de console para o caso de uso "Cadastrar Produto".
     */
    private void cadastrarProduto() {
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

    /**
     * Gerencia a interface de console para o caso de uso "Cadastrar Novo Atendente".
     *
     * @param gerenteLogado O gerente que está executando a ação.
     */
    private void cadastrarNovoAtendente(Usuario gerenteLogado) {
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

    /**
     * Gerencia a interface de console para o caso de uso "Adicionar Estoque de Produto".
     */
    private void adicionarEstoqueProduto() {
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

    /**
     * Metodo utilitário para popular o banco de dados com usuários iniciais para teste.
     */
    private void setupUsuariosIniciais() {
        System.out.println("Configurando usuários iniciais no Firebase...");
        Gerente gerenteAdmin = new Gerente(101, "Ana Gerente", "gerente", "admin");
        gerenteAdmin.definirNovaSenha("admin");
        Atendente atendentePadrao = new Atendente(202, "Bruno Atendente", "atendente", "123");
        atendentePadrao.definirNovaSenha("123");
        UsuarioRepository usuarioRepo = new repository.UsuarioRepository();
        usuarioRepo.salvar(gerenteAdmin);
        usuarioRepo.salvar(atendentePadrao);
        System.out.println("Usuários iniciais configurados.");
    }
}