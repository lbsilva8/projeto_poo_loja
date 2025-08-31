package view;

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

public class HomeViewController {

    @FXML private StackPane rootPane;
    @FXML private ImageView backgroundImageView;
    @FXML private MenuItem loginMenuItem;

    private Usuario usuarioLogado;
    private Parent mainView;
    private MainViewController mainViewController;

    @FXML
    public void initialize() {
    }

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

    private void handleLogoutAction() {
        this.usuarioLogado = null;
        atualizarParaTelaInicial();
    }

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

    private void atualizarParaTelaInicial() {
        if (backgroundImageView != null) {
            rootPane.getChildren().set(0, backgroundImageView);
        }
        loginMenuItem.setText("Login");
    }
}