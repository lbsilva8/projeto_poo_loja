package view;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Produto;
import service.ProdutoService;

import java.util.List;

public class BuscaProdutoViewController {

    @FXML private TableView<Produto> tabelaProdutos;
    @FXML private TableColumn<Produto, String> colunaId;
    @FXML private TableColumn<Produto, String> colunaNome;
    @FXML private TableColumn<Produto, Double> colunaPreco;
    @FXML private TableColumn<Produto, Integer> colunaEstoque;

    private final ProdutoService produtoService = new ProdutoService();
    private String produtoIdSelecionado = null;

    @FXML
    public void initialize() {
        // Configura as colunas para buscar os valores dos atributos da classe Produto
        colunaId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colunaNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colunaPreco.setCellValueFactory(new PropertyValueFactory<>("preco"));
        colunaEstoque.setCellValueFactory(new PropertyValueFactory<>("quantidade"));

        // Carrega os produtos na tabela
        List<Produto> todosProdutos = produtoService.buscarTodosProdutos();
        tabelaProdutos.setItems(FXCollections.observableArrayList(todosProdutos));
    }

    @FXML
    private void handleSelecionarProduto() {
        Produto produtoSelecionado = tabelaProdutos.getSelectionModel().getSelectedItem();
        if (produtoSelecionado != null) {
            this.produtoIdSelecionado = produtoSelecionado.getId();
            // Fecha a janela (pop-up)
            Stage stage = (Stage) tabelaProdutos.getScene().getWindow();
            stage.close();
        }
    }

    /**
     * Método para a tela de venda poder pegar o ID do produto que foi selecionado.
     */
    public String getProdutoIdSelecionado() {
        return produtoIdSelecionado;
    }
}