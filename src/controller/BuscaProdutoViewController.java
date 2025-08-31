package controller;

/**
 * Autoras:
 * Andreísy Neves Ferreira
 * Isabella Paranhos Meireles
 * Lorena da Silva Borges
 */

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.util.List;

import model.Produto;
import service.ProdutoService;


/**
 * Classe para a tela de busca de produtos.
 * Esta classe gerencia a janela pop-up utilizada para listar e selecionar um produto.
 * Suas responsabilidades incluem popular a {@link TableView} com dados do
 * {@link ProdutoService} e retornar o ID do produto selecionado para a tela que a invocou.
 *
 * @see VendaViewController
 * @see EstoqueViewController
 */
public class BuscaProdutoViewController {

    @FXML private TableView<Produto> tabelaProdutos;
    @FXML private TableColumn<Produto, String> colunaId;
    @FXML private TableColumn<Produto, String> colunaNome;
    @FXML private TableColumn<Produto, Double> colunaPreco;
    @FXML private TableColumn<Produto, Integer> colunaEstoque;

    private final ProdutoService produtoService = new ProdutoService();
    private String produtoIdSelecionado = null;

    /**
     * Metodo de inicialização do JavaFX, chamado automaticamente após o FXML ser carregado.
     * Configura as colunas da tabela para se vincularem aos atributos da classe {@link Produto}
     * e carrega a lista inicial de todos os produtos do banco de dados.
     */
    @FXML
    public void initialize() {
        colunaId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colunaNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colunaPreco.setCellValueFactory(new PropertyValueFactory<>("preco"));
        colunaEstoque.setCellValueFactory(new PropertyValueFactory<>("quantidade"));

        List<Produto> todosProdutos = produtoService.buscarTodosProdutos();
        tabelaProdutos.setItems(FXCollections.observableArrayList(todosProdutos));
    }

    /**
     * Manipula o evento de clique do botão "Selecionar".
     * Obtém o item atualmente selecionado na tabela, armazena seu ID e fecha
     * a janela pop-up.
     */
    @FXML
    private void handleSelecionarProduto() {
        Produto produtoSelecionado = tabelaProdutos.getSelectionModel().getSelectedItem();
        if (produtoSelecionado != null) {
            this.produtoIdSelecionado = produtoSelecionado.getId();
            Stage stage = (Stage) tabelaProdutos.getScene().getWindow();
            stage.close();
        }
    }

    /**
     * Permite que a classe que abriu esta janela recupere o ID do produto que foi
     * selecionado pelo usuário.
     * @return O ID do produto selecionado como uma {@code String}, ou {@code null} se
     * nenhum produto foi selecionado antes de a janela ser fechada.
     */
    public String getProdutoIdSelecionado() {
        return produtoIdSelecionado;
    }
}