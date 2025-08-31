package view;

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

public class GerenciarAtendentesViewController {

    @FXML private TableView<Usuario> tabelaAtendentes;
    @FXML private TableColumn<Usuario, Integer> colunaMatricula;
    @FXML private TableColumn<Usuario, String> colunaNome;
    @FXML private TableColumn<Usuario, String> colunaUsuario;
    @FXML private TableColumn<Usuario, String> colunaStatus;
    @FXML private Label statusLabel;

    private Usuario gerenteLogado;
    private final UsuarioService usuarioService = new UsuarioService();

    @FXML
    public void initialize() {
        colunaMatricula.setCellValueFactory(new PropertyValueFactory<>("matricula"));
        colunaNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colunaUsuario.setCellValueFactory(new PropertyValueFactory<>("usuario"));

        // Coluna de Status customizada para mostrar "Ativo" ou "Inativo"
        colunaStatus.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().isAtivo() ? "Ativo" : "Inativo"));
    }

    public void inicializarDados(Usuario gerente) {
        this.gerenteLogado = gerente;
        carregarAtendentes(); // Carrega os dados na tabela
    }

    private void carregarAtendentes() {
        tabelaAtendentes.setItems(FXCollections.observableArrayList(usuarioService.buscarTodosUsuarios()));
    }

    @FXML
    private void handleAtivar() {
        alterarStatusSelecionado(true);
    }

    @FXML
    private void handleInativar() {
        alterarStatusSelecionado(false);
    }

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

    private void exibirAlerta(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Aviso");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}