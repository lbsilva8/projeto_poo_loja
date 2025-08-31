package controller;

/**
 * Autoras:
 * Andreísy Neves Ferreira
 * Isabella Paranhos Meireles
 * Lorena da Silva Borges
 */

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import excecoes.AcessoNegadoException;
import model.Atendente;
import model.Usuario;
import service.UsuarioService;

/**
 * Classe controladora para a view de cadastro de novos atendentes ({@code AtendenteCadastroView.fxml}).
 * Este controller gerencia o formulário de cadastro, validando as entradas e delegando
 * a criação do novo atendente para o {@link UsuarioService}. A permissão para
 * executar esta ação é validada na camada de serviço.
 *
 * @see MainViewController
 * @see service.UsuarioService
 */
public class AtendenteCadastroViewController {

    @FXML private TextField matriculaField;
    @FXML private TextField nomeField;
    @FXML private TextField usuarioField;
    @FXML private PasswordField senhaField;
    @FXML private Label statusLabel;

    private Usuario gerenteLogado;
    private final UsuarioService usuarioService = new UsuarioService();

    /**
     * Recebe o usuário gerente logado da tela principal. Este objeto é necessário
     * para a camada de serviço realizar a verificação de permissão.
     */
    public void inicializarDados(Usuario gerente) {
        this.gerenteLogado = gerente;
    }

    /**
     * Processa o evento de clique do botão "Cadastrar Atendente".
     * Coleta os dados do formulário, realiza validações, cria um novo objeto
     * {@link Atendente}, define sua senha de forma criptografada e invoca o
     * serviço para salvá-lo. Fornece feedback ao usuário através de um label de status.
     */
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

    /**
     * Metodo auxiliar para limpar os campos do formulário após um cadastro bem-sucedido.
     */
    private void limparCampos() {
        matriculaField.clear();
        nomeField.clear();
        usuarioField.clear();
        senhaField.clear();
    }
}