package view;

import excecoes.AcessoNegadoException;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import model.Atendente;
import model.Usuario;
import service.UsuarioService;

public class AtendenteCadastroViewController {

    @FXML private TextField matriculaField;
    @FXML private TextField nomeField;
    @FXML private TextField usuarioField;
    @FXML private PasswordField senhaField;
    @FXML private Label statusLabel;

    private Usuario gerenteLogado;
    private final UsuarioService usuarioService = new UsuarioService();

    /**
     * Recebe o gerente logado da tela principal para validar a permissão.
     */
    public void inicializarDados(Usuario gerente) {
        this.gerenteLogado = gerente;
    }

    @FXML
    private void handleCadastrar() {
        try {
            int matricula = Integer.parseInt(matriculaField.getText());
            String nome = nomeField.getText();
            String usuario = usuarioField.getText();
            String senha = senhaField.getText();

            if (nome.isEmpty() || usuario.isEmpty() || senha.isEmpty()) {
                statusLabel.setText("ERRO: Todos os campos são obrigatórios.");
                return;
            }

            Atendente novoAtendente = new Atendente(matricula, nome, usuario, "temp");
            novoAtendente.definirNovaSenha(senha);
            usuarioService.cadastrarAtendente(gerenteLogado, novoAtendente);

            statusLabel.setText("Atendente " + nome + " cadastrado com sucesso!");
            limparCampos();

        } catch (NumberFormatException e) {
            statusLabel.setText("ERRO: Matrícula deve ser um número válido.");
        } catch (AcessoNegadoException e) {
            statusLabel.setText(e.getMessage());
        } catch (Exception e) {
            statusLabel.setText("ERRO: Ocorreu um problema inesperado.");
            e.printStackTrace();
        }
    }

    private void limparCampos() {
        matriculaField.clear();
        nomeField.clear();
        usuarioField.clear();
        senhaField.clear();
    }
}