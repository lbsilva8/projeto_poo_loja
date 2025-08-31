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
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import model.Usuario;

import java.io.IOException;

/**
 * Classe para a view principal e "casca" da aplicação ({@code HomeView.fxml}).
 * Este é o controller mestre que gerencia o estado geral da interface, alternando
 * entre a visualização de boas-vindas (não logado) e o painel principal da aplicação
 * (logado). Ele orquestra o fluxo de login e logout.
 *
 * @see com.seuprojeto.App
 * @see LoginViewController
 * @see MainViewController
 */
public class HomeViewController {

    @FXML private StackPane rootPane;
    @FXML private ImageView backgroundImageView;
    @FXML private MenuItem loginMenuItem;

    private Usuario usuarioLogado;
    private Parent mainView;
    private MainViewController mainViewController;

    /**
     * Metodo de inicialização do JavaFX, chamado automaticamente após o FXML ser carregado.
     */
    @FXML
    public void initialize() {
    }

    /**
     * Processa a ação do item de menu principal, que tem dupla função.
     * Se nenhum usuário estiver logado, este metodo inicia o fluxo de login, abrindo
     * a janela modal de autenticação. Se um usuário já estiver logado, este método
     * inicia o fluxo de logout.
     *
     * @param event O evento de ação do clique no menu.
     */
    @FXML
    private void handleLoginAction(ActionEvent event) {
        if (usuarioLogado == null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/LoginView.fxml"));
                Parent root = loader.load();
                LoginViewController loginController = loader.getController();

                Stage loginStage = new Stage();
                loginStage.setTitle("Login");
                loginStage.setScene(new Scene(root));
                loginStage.initModality(Modality.APPLICATION_MODAL);
                loginStage.setResizable(false);
                loginStage.showAndWait();

                Usuario usuarioAutenticado = loginController.getUsuarioAutenticado();

                this.usuarioLogado = usuarioAutenticado;
                atualizarParaTelaPrincipal();

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            handleLogoutAction();
        }
    }

    /**
     * Executa a lógica de logout, redefinindo o estado da aplicação para o inicial.
     */
    private void handleLogoutAction() {
        this.usuarioLogado = null;
        atualizarParaTelaInicial();
    }

    /**
     * Metodo que transiciona a UI para o estado "logado".
     * Carrega a {@code MainView}, a insere no painel principal, e atualiza o menu
     * para exibir a opção de "Logout".
     */
    private void atualizarParaTelaPrincipal() {
        try {
            if (mainView == null) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/MainView.fxml"));
                mainView = loader.load();
                mainViewController = loader.getController();
            }
            mainViewController.inicializar(this.usuarioLogado);

            rootPane.getChildren().set(0, mainView);

            loginMenuItem.setText("Logout (" + this.usuarioLogado.getNome() + ")");

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * Metodo que restaura a UI para o estado inicial "não logado".
     * Restaura a imagem de fundo e redefine o menu para exibir a opção de "Login".
     */
    private void atualizarParaTelaInicial() {
        if (backgroundImageView != null) {
            rootPane.getChildren().set(0, backgroundImageView);
        }
        loginMenuItem.setText("Login");
    }
}