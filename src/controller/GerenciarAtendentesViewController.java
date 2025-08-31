package controller;

/**
 * Autoras:
 * Andreísy Neves Ferreira
 * Isabella Paranhos Meireles
 * Lorena da Silva Borges
 */

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import model.Gerente;
import model.Usuario;
import service.UsuarioService;

/**
 * Classe para a view de gerenciamento de atendentes ({@code GerenciarAtendentesView.fxml}).
 * Esta classe é responsável por exibir uma lista de todos os usuários e permitir que
 * um gerente ative ou inative as contas dos atendentes.
 *
 * @see MainViewController
 * @see service.UsuarioService
 */
public class GerenciarAtendentesViewController {

    @FXML private TableView<Usuario> tabelaAtendentes;
    @FXML private TableColumn<Usuario, Integer> colunaMatricula;
    @FXML private TableColumn<Usuario, String> colunaNome;
    @FXML private TableColumn<Usuario, String> colunaUsuario;
    @FXML private TableColumn<Usuario, String> colunaStatus;
    @FXML private Label statusLabel;

    private Usuario gerenteLogado;
    private final UsuarioService usuarioService = new UsuarioService();

    /**
     * Metodo de inicialização do JavaFX, chamado automaticamente após o FXML ser carregado.
     * Configura as colunas da tabela para se vincularem aos atributos da classe {@link Usuario}.
     * A coluna "Status" utiliza uma fábrica de células customizada para exibir um texto
     * amigável ("Ativo" ou "Inativo") em vez do valor booleano.
     */
    @FXML
    public void initialize() {
        colunaMatricula.setCellValueFactory(new PropertyValueFactory<>("matricula"));
        colunaNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colunaUsuario.setCellValueFactory(new PropertyValueFactory<>("usuario"));
        colunaStatus.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().isAtivo() ? "Ativo" : "Inativo"));
    }

    public void inicializarDados(Usuario gerente) {
        this.gerenteLogado = gerente;
        carregarAtendentes(); // Carrega os dados na tabela
    }

    /**
     * Busca os dados mais recentes dos usuários no serviço e atualiza a tabela.
     */
    private void carregarAtendentes() {
        tabelaAtendentes.setItems(FXCollections.observableArrayList(usuarioService.buscarTodosUsuarios()));
    }

    /**
     * Processa o evento de clique do botão "Ativar Selecionado".
     */
    @FXML
    private void handleAtivar() {
        alterarStatusSelecionado(true);
    }

    /**
     * Processa o evento de clique do botão "Inativar Selecionado".
     */
    @FXML
    private void handleInativar() {
        alterarStatusSelecionado(false);
    }

    /**
     * Lógica central para alterar o status de um usuário selecionado na tabela.
     *
     * @param novoStatus {@code true} para ativar o usuário, {@code false} para inativar.
     */
    private void alterarStatusSelecionado(boolean novoStatus) {
        Usuario selecionado = tabelaAtendentes.getSelectionModel().getSelectedItem();
        if (selecionado == null) {
            exibirAlerta("Nenhum usuário selecionado.");
            return;
        }
        if (selecionado instanceof Gerente) {
            exibirAlerta("Não é possível alterar o status de um gerente.");
            return;
        }

        try {
            usuarioService.alterarStatusAtendente(gerenteLogado, selecionado.getMatricula(), novoStatus);
            statusLabel.setText("Status de " + selecionado.getNome() + " alterado com sucesso.");
            carregarAtendentes(); // Recarrega a tabela para mostrar a mudança
        } catch (Exception e) {
            exibirAlerta(e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Metodo auxiliar para exibir uma janela de alerta padrão para o usuário.
     *
     * @param mensagem A mensagem a ser exibida.
     */
    private void exibirAlerta(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Aviso");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}