package view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import java.io.IOException;

import model.Gerente;
import model.Usuario;

/**
 * Classe para a tela principal da aplicação ({@code MainView.fxml}).
 * Esta classe atua como o painel de controle principal após a autenticação do usuário.
 * Suas responsabilidades incluem:
 * Configurar a interface com base nas permissões do usuário logado (Gerente ou Atendente).
 * Atuar como um navegador, carregando diferentes "mini-telas" (views) na área de conteúdo central.
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

    private Usuario usuarioLogado;

    /**
     * Metodo de inicialização que recebe o usuário autenticado da tela de login.
     * Este é o ponto de entrada de dados para este controller. Ele armazena o estado
     * do usuário e personaliza a visibilidade dos componentes da interface com base
     * no cargo (role) do usuário, implementando o controle de autorização na view.
     *
     * @param usuario O usuário que acabou de ser autenticado.
     */
    public void inicializar(Usuario usuario) {
        this.usuarioLogado = usuario;
        welcomeLabel.setText("Bem-vindo(a), " + usuarioLogado.getNome() + "!");

        // Regra de autorização da View: esconde botões se o usuário não for Gerente.
        if (usuarioLogado instanceof Gerente) {
            // Se for Gerente, GARANTE que os botões estão visíveis.
            cadastrarProdutoButton.setVisible(true);
            cadastrarAtendenteButton.setVisible(true);
            gerenciarAtendentesButton.setVisible(true);
            gerenciarEstoqueButton.setText("Gerenciar Estoque"); // Texto completo para o gerente
        } else {
            // Se for Atendente, GARANTE que os botões estão escondidos.
            cadastrarProdutoButton.setVisible(false);
            cadastrarAtendenteButton.setVisible(false);
            gerenciarAtendentesButton.setVisible(false);
            gerenciarEstoqueButton.setText("Visualizar Estoque"); // Texto diferente para o atendente
        }
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

    @FXML
    private void handleCadastrarAtendente(ActionEvent event) {
        carregarView("/view/AtendenteCadastroView.fxml");
    }

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

            centerPane.getChildren().setAll(view);
        } catch (IOException e) {
            System.err.println("Erro ao carregar a view: " + fxmlPath);
            e.printStackTrace();
        }
    }
}