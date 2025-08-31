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
    private Button adicionarEstoqueButton;
    @FXML
    private Button cadastrarAtendenteButton;
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
        if (!(usuarioLogado instanceof Gerente)) {
            cadastrarProdutoButton.setVisible(false);
            adicionarEstoqueButton.setVisible(false);
            cadastrarAtendenteButton.setVisible(false);
        }
    }

    /**
     * Manipula o clique no botão "Realizar Venda", carregando a view de vendas.
     * @param event O evento de ação do clique.
     */
    @FXML
    private void handleRealizarVenda(ActionEvent event) {
        System.out.println("Carregando tela de venda...");
        carregarView("/view/VendaView.fxml");
    }

    /**
     * Manipula o clique no botão "Adicionar Estoque", carregando a view de estoque.
     * @param event O evento de ação do clique.
     */
    @FXML
    private void handleAdicionarEstoque(ActionEvent event) {
        System.out.println("Carregando tela de adicionar estoque...");
        carregarView("/view/EstoqueView.fxml");
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

            // Pega o controller da view carregada para passar dados, se necessário
            Object controller = loader.getController();
            if (controller instanceof VendaViewController) {
                ((VendaViewController) controller).inicializarDados(this.usuarioLogado);
            }

            centerPane.getChildren().setAll(view);
        } catch (IOException e) {
            System.err.println("Erro ao carregar a view: " + fxmlPath);
            e.printStackTrace();
        }
    }
}