package view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;

import excecoes.AutenticacaoException;
import model.Usuario;
import service.UsuarioService;

/**
 * Classe para a tela de login da aplicação ({@code LoginView.fxml}).
 * Esta classe gerencia a interação do usuário na primeira tela do sistema. Suas
 * responsabilidades incluem capturar as credenciais, delegar a autenticação
 * para a camada de serviço e orquestrar a transição para a tela principal
 * após um login bem-sucedido.
 *
 * @see com.seuprojeto.App
 * @see MainViewController
 * @see UsuarioService
 */
public class LoginViewController {

    @FXML
    private TextField usuarioField;
    @FXML
    private PasswordField senhaField;
    @FXML
    private Label statusLabel;
    @FXML
    private Button loginButton;

    private final UsuarioService usuarioService = new UsuarioService();

    /**
     * Manipula o evento de clique do botão de login.
     * Obtém as credenciais dos campos de texto, valida a entrada, chama o serviço
     * de autenticação e, em caso de sucesso, inicia a navegação para a tela principal,
     * fechando a janela de login em seguida. Em caso de falha, exibe uma mensagem
     * de erro na tela.
     *
     * @param event O evento de ação gerado pelo clique do botão, usado para obter
     * a janela (Stage) atual.
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

            // Orquestra a transição para a próxima tela.
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
     * Carrega, configura e exibe a tela principal da aplicação após um login bem-sucedido.
     * Este metodo é responsável por carregar o FXML da tela principal, obter uma
     * referência ao seu controller e passar o objeto do usuário autenticado para ele,
     * garantindo que a tela principal saiba quem está logado.
     *
     * @param usuario O objeto {@link Usuario} autenticado a ser passado para a tela principal.
     * @throws IOException se o arquivo {@code MainView.fxml} não puder ser carregado.
     */
    private void abrirTelaPrincipal(Usuario usuario) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/MainView.fxml"));
        Parent root = loader.load();

        // Pega o controller da nova tela
        MainViewController mainViewController = loader.getController();
        // Passa o usuário logado para o novo controller
        mainViewController.inicializar(usuario);

        // Cria e exibe a nova janela.
        Stage stage = new Stage();
        stage.setTitle("Sistema de Vendas - Painel Principal");
        stage.setScene(new Scene(root));
        stage.show();
    }
}