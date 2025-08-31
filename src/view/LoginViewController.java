package view;

import excecoes.AutenticacaoException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Usuario;
import service.UsuarioService;

public class LoginViewController {

    @FXML private TextField usuarioField;
    @FXML private PasswordField senhaField;
    @FXML private Label statusLabel;
    @FXML private Button loginButton;

    private final UsuarioService usuarioService = new UsuarioService();
    private Usuario usuarioAutenticado = null;

    @FXML
    private void handleLogin() {
        String usuario = usuarioField.getText();
        String senha = senhaField.getText();

        if (usuario.isEmpty() || senha.isEmpty()) {
            statusLabel.setText("Usuário e senha são obrigatórios.");
            return;
        }

        try {
            // A linha mais importante: vamos ver o que o serviço retorna.
            Usuario resultadoDaAutenticacao = usuarioService.autenticar(usuario, senha);


            this.usuarioAutenticado = resultadoDaAutenticacao;

            Stage stage = (Stage) loginButton.getScene().getWindow();
            stage.close();

        } catch (AutenticacaoException e) {
            statusLabel.setText(e.getMessage());
            this.usuarioAutenticado = null;
        } catch (Exception e) {
            statusLabel.setText("Erro inesperado no sistema.");
            this.usuarioAutenticado = null;
            e.printStackTrace();
        }
    }

    public Usuario getUsuarioAutenticado() {
        return usuarioAutenticado;
    }
}