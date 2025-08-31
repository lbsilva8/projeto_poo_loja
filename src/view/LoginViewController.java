package view;

import excecoes.AutenticacaoException;
import javafx.event.ActionEvent; // Importe
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader; // Importe
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button; // Importe
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage; // Importe
import model.Usuario;
import service.UsuarioService;

import java.io.IOException;

public class LoginViewController {

    @FXML
    private TextField usuarioField;
    @FXML
    private PasswordField senhaField;
    @FXML
    private Label statusLabel;
    @FXML
    private Button loginButton; // Adicione o fx:id ao seu botão no FXML também

    private final UsuarioService usuarioService = new UsuarioService();

    /**
     * Modificado para receber o ActionEvent do clique do botão.
     */
    @FXML
    private void handleLogin(ActionEvent event) {
        String usuario = usuarioField.getText();
        String senha = senhaField.getText();

        if (usuario.isEmpty() || senha.isEmpty()) {
            statusLabel.setText("Usuário e senha são obrigatórios.");
            return;
        }

        try {
            Usuario usuarioAutenticado = usuarioService.autenticar(usuario, senha);
            statusLabel.setText("Login bem-sucedido!");

            // --- LÓGICA PARA TROCAR DE TELA ---
            abrirTelaPrincipal(usuarioAutenticado);

            // Fecha a janela de login
            Stage stageLogin = (Stage) loginButton.getScene().getWindow();
            stageLogin.close();

        } catch (AutenticacaoException e) {
            statusLabel.setText(e.getMessage());
        } catch (Exception e) {
            statusLabel.setText("Erro inesperado no sistema.");
            e.printStackTrace();
        }
    }

    /**
     * Carrega a tela principal e passa o usuário logado para o seu controller.
     */
    private void abrirTelaPrincipal(Usuario usuario) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/MainView.fxml"));
        Parent root = loader.load();

        // Pega o controller da nova tela
        MainViewController mainViewController = loader.getController();
        // Passa o usuário logado para o novo controller
        mainViewController.inicializar(usuario);

        Stage stage = new Stage();
        stage.setTitle("Sistema de Vendas - Painel Principal");
        stage.setScene(new Scene(root));
        stage.show();
    }
}