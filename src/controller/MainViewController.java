package controller;

/**
 * Autoras:
 * Andreísy Neves Ferreira
 * Isabella Paranhos Meireles
 * Lorena da Silva Borges
 */

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import java.io.IOException;


import model.Permissao;
import model.Usuario;

/**
 * Classe para a tela principal da aplicação ({@code MainView.fxml}).
 * Esta classe atua como o painel de controle principal após a autenticação do usuário.
 * Suas responsabilidades incluem:
 * Configurar a interface com base nas permissões do usuário logado.
 * Atuar como um navegador, carregando diferentes (views) na área de conteúdo central.
 * Passar o contexto do usuário logado para as sub-views que são carregadas.
 *
 * @see LoginViewController
 * @see VendaViewController
 * @see EstoqueViewController
 */
public class MainViewController {

    @FXML
    private Label welcomeLabel;
    @FXML
    private Button realizarVendaButton;
    @FXML
    private Button cadastrarProdutoButton;
    @FXML
    private Button gerenciarEstoqueButton;
    @FXML
    private Button cadastrarAtendenteButton;
    @FXML
    private Button gerenciarAtendentesButton;
    @FXML
    private VBox centerPane;
    @FXML
    private Button gerenciarPermissoesButton;


    private Usuario usuarioLogado;

    /**
     * Metodo de inicialização que recebe o usuário autenticado da tela de login.
     * Este é o ponto de entrada de dados para este controller. Ele armazena o estado
     * do usuário e personaliza a visibilidade dos componentes da interface com base
     * nos acessos, implementando o controle de autorização na view.
     *
     * @param usuario O usuário que acabou de ser autenticado.
     */
    public void inicializar(Usuario usuario) {
        this.usuarioLogado = usuario;
        welcomeLabel.setText("Bem-vindo(a), " + usuarioLogado.getNome() + "!");

        cadastrarProdutoButton.setVisible(usuarioLogado.temPermissao(Permissao.CADASTRAR_PRODUTO));
        cadastrarAtendenteButton.setVisible(usuarioLogado.temPermissao(Permissao.GERENCIAR_USUARIOS));
        gerenciarAtendentesButton.setVisible(usuarioLogado.temPermissao(Permissao.GERENCIAR_USUARIOS));
        gerenciarPermissoesButton.setVisible(usuarioLogado.temPermissao(Permissao.GERENCIAR_USUARIOS));

        gerenciarEstoqueButton.setText(
                usuarioLogado.temPermissao(Permissao.GERENCIAR_ESTOQUE) ? "Gerenciar Estoque" : "Visualizar Estoque"
        );
    }

    /**
     * Processa o clique no botão "Gerenciar Permissões", carregando a view correspondente.
     * @param event O evento de ação do clique.
     */
    @FXML
    private void handleGerenciarPermissoes(ActionEvent event) {
        carregarView("/view/GerenciarPermissoesView.fxml");
    }

    /**
     * Manipula o clique no botão "Realizar Venda", carregando a view de vendas.
     * @param event O evento de ação do clique.
     */
    @FXML
    private void handleRealizarVenda(ActionEvent event) {
        carregarView("/view/VendaView.fxml");
    }

    /**
     * Manipula o clique no botão "Adicionar Estoque", carregando a view de estoque.
     * @param event O evento de ação do clique.
     */
    @FXML
    private void handleGerenciarEstoque(ActionEvent event) {
        carregarView("/view/EstoqueView.fxml");
    }

    /**
     * Processa o clique no botão "Cadastrar Atendente", carregando a view correspondente.
     * @param event O evento de ação do clique.
     */
    @FXML
    private void handleCadastrarAtendente(ActionEvent event) {
        carregarView("/view/AtendenteCadastroView.fxml");
    }

    /**
     * Processa o clique no botão "Gerenciar Atendentes", carregando a view correspondente.
     * @param event O evento de ação do clique.
     */
    @FXML
    private void handleGerenciarAtendentes(ActionEvent event) {
        carregarView("/view/GerenciarAtendentesView.fxml");
    }

    /**
     * Manipula o clique no botão "Cadastrar Produto".
     * @param event O evento de ação do clique.
     */
    @FXML
    private void handleCadastrarProduto(ActionEvent event) {
        carregarView("/view/ProdutoCadastroView.fxml");
    }

    /**
     * Metodo utilitário reutilizável para carregar uma view FXML no painel central da tela.
     * Este metodo é o núcleo da navegação "single-window" da aplicação. Ele carrega a
     * view especificada, obtém seu controller e passa o contexto do usuário logado
     * para ele, antes de exibi-lo no painel central.
     *
     * @param fxmlPath O caminho do recurso para o arquivo FXML a ser carregado.
     */
    private void carregarView(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Node view = loader.load();
            Object controller = loader.getController();

            if (controller instanceof VendaViewController) {
                ((VendaViewController) controller).inicializarDados(this.usuarioLogado);
            } else if (controller instanceof AtendenteCadastroViewController) {
                ((AtendenteCadastroViewController) controller).inicializarDados(this.usuarioLogado);
            } else if (controller instanceof GerenciarAtendentesViewController) {
                ((GerenciarAtendentesViewController) controller).inicializarDados(this.usuarioLogado);
            } else if (controller instanceof EstoqueViewController) {
                ((EstoqueViewController) controller).inicializarDados(this.usuarioLogado);
            }
            else if (controller instanceof GerenciarPermissoesViewController) {
                ((GerenciarPermissoesViewController) controller).inicializarDados(this.usuarioLogado);
            }

            centerPane.getChildren().setAll(view);
        } catch (IOException e) {
            System.err.println("Erro ao carregar a view: " + fxmlPath);
            e.printStackTrace();
        }
    }
}