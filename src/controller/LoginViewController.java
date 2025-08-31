package controller;

/**
 * Autoras:
 * Andreísy Neves Ferreira
 * Isabella Paranhos Meireles
 * Lorena da Silva Borges
 */

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import excecoes.AutenticacaoException;
import model.Usuario;
import service.UsuarioService;

/**
 * Classe para a view de login modal ({@code LoginView.fxml}).
 * Esta classe gerencia a janela pop-up de login. Sua responsabilidade é capturar as
 * credenciais do usuário, invocar o {@link UsuarioService} para a validação e,
 * em caso de sucesso, armazenar o objeto {@link Usuario} autenticado para que a
 * tela principal possa recuperá-lo.
 *
 * @see HomeViewController
 * @see service.UsuarioService
 */
public class LoginViewController {

    @FXML private TextField usuarioField;
    @FXML private PasswordField senhaField;
    @FXML private Label statusLabel;
    @FXML private Button loginButton;

    private final UsuarioService usuarioService = new UsuarioService();
    private Usuario usuarioAutenticado = null;

    /**
     * Processa o evento de clique do botão "Entrar".
     * Coleta as credenciais da interface, chama o serviço de autenticação e, se
     * o login for bem-sucedido, armazena o resultado e fecha a janela. Se ocorrer
     * uma falha, exibe uma mensagem de erro para o usuário.
     */
    @FXML
    private void handleLogin() {
        String usuario = usuarioField.getText();
        String senha = senhaField.getText();

        if (usuario.isEmpty() || senha.isEmpty()) {
            statusLabel.setText("Usuário e senha são obrigatórios.");
            return;
        }

        try {
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

    /**
     * Permite que a classe que invocou esta janela recupere o resultado da autenticação.
     * <p>
     * Este metodo deve ser chamado pelo controller pai (ex: {@link HomeViewController})
     * <strong>após</strong> a janela de login ter sido fechada.
     *
     * @return O objeto {@link Usuario} autenticado em caso de sucesso, ou {@code null}
     * se a autenticação falhou ou a janela foi fechada.
     */
    public Usuario getUsuarioAutenticado() {
        return usuarioAutenticado;
    }
}