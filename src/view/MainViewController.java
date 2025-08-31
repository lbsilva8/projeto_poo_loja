package view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox; // Importe
import model.Gerente;
import model.Usuario;

import java.io.IOException;

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
     * Método de inicialização para receber dados da tela anterior (login).
     * @param usuario O usuário que acabou de ser autenticado.
     */
    public void inicializar(Usuario usuario) {
        this.usuarioLogado = usuario;
        welcomeLabel.setText("Bem-vindo(a), " + usuarioLogado.getNome() + "!");

        // Esconde os botões de gerente se o usuário não for um
        if (!(usuarioLogado instanceof Gerente)) {
            cadastrarProdutoButton.setVisible(false);
            adicionarEstoqueButton.setVisible(false);
            cadastrarAtendenteButton.setVisible(false);
        }
    }

    @FXML
    private void handleRealizarVenda(ActionEvent event) {
        System.out.println("Carregando tela de venda...");
        carregarView("/view/VendaView.fxml");
    }


    /**
     * Novo método para o botão de adicionar estoque.
     */
    @FXML
    private void handleAdicionarEstoque(ActionEvent event) {
        System.out.println("Carregando tela de adicionar estoque...");
        carregarView("/view/EstoqueView.fxml");
    }

    /**
     * Método genérico para carregar um arquivo FXML no painel central.
     * @param fxmlPath O caminho para o arquivo FXML a ser carregado.
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
            // Adicione outros 'if (controller instanceof ...)' para outras telas

            centerPane.getChildren().setAll(view);
        } catch (IOException e) {
            System.err.println("Erro ao carregar a view: " + fxmlPath);
            e.printStackTrace();
        }
    }
}