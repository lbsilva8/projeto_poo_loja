package view;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.control.TextInputDialog;
import java.util.Optional;

import model.Gerente;
import model.Produto;
import model.Usuario;
import service.ProdutoService;

public class EstoqueViewController {

    @FXML private TableView<Produto> tabelaProdutos;
    @FXML private TableColumn<Produto, String> colunaId;
    @FXML private TableColumn<Produto, String> colunaNome;
    @FXML private TableColumn<Produto, Double> colunaPreco;
    @FXML private TableColumn<Produto, Integer> colunaEstoque;
    @FXML private HBox controlesGerenteHBox;
    @FXML private TextField quantidadeField;
    @FXML private Label statusLabel;

    private Usuario usuarioLogado;
    private final ProdutoService produtoService = new ProdutoService();

    @FXML
    public void initialize() {
        colunaId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colunaNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colunaPreco.setCellValueFactory(new PropertyValueFactory<>("preco"));
        colunaEstoque.setCellValueFactory(new PropertyValueFactory<>("quantidade"));

        carregarProdutos();
    }

    public void inicializarDados(Usuario usuario) {
        this.usuarioLogado = usuario;

        // AQUI ESTÁ A LÓGICA DE PERMISSÃO DA TELA!
        if (usuarioLogado instanceof Gerente) {
            // Se for Gerente, mostra os controles.
            controlesGerenteHBox.setVisible(true);

            // setManaged(true) diz ao layout para reservar espaço para os controles.
            controlesGerenteHBox.setManaged(true);
        } else {
            // Se for Atendente, esconde os controles.
            controlesGerenteHBox.setVisible(false);

            // setManaged(false) diz ao layout para "fingir" que os controles não existem,
            // fazendo com que o espaço que eles ocupavam desapareça.
            controlesGerenteHBox.setManaged(false);
        }
    }

    private void carregarProdutos() {
        tabelaProdutos.setItems(FXCollections.observableArrayList(produtoService.buscarTodosProdutos()));
    }

    @FXML
    private void handleAdicionar() {
        editarEstoque(true);
    }

    @FXML
    private void handleRemover() {
        editarEstoque(false);
    }

    private void editarEstoque(boolean isAdicao) {
        Produto produtoSelecionado = tabelaProdutos.getSelectionModel().getSelectedItem();
        if (produtoSelecionado == null) {
            exibirAlerta("Selecione um produto na tabela primeiro.");
            return;
        }
        try {
            int quantidade = Integer.parseInt(quantidadeField.getText());
            if (isAdicao) {
                produtoService.adicionarEstoque(produtoSelecionado.getId(), quantidade);
                statusLabel.setText(quantidade + " unidades adicionadas ao estoque de " + produtoSelecionado.getNome());
            } else {
                produtoService.reduzirEstoque(produtoSelecionado.getId(), quantidade);
                statusLabel.setText(quantidade + " unidades removidas do estoque de " + produtoSelecionado.getNome());
            }
            quantidadeField.clear();
            carregarProdutos(); // Atualiza a tabela com os novos valores
        } catch (NumberFormatException e) {
            exibirAlerta("A quantidade deve ser um número válido.");
        } catch (Exception e) {
            exibirAlerta(e.getMessage());
        }
    }

    /**
     * Chamado quando o botão "Atualizar Preço" é clicado.
     * Abre um pop-up para que o gerente insira o novo preço.
     */
    @FXML
    private void handleAtualizarPreco() {
        Produto produtoSelecionado = tabelaProdutos.getSelectionModel().getSelectedItem();
        if (produtoSelecionado == null) {
            exibirAlerta("Selecione um produto na tabela primeiro.");
            return;
        }

        // Cria um pop-up de diálogo para entrada de texto.
        TextInputDialog dialog = new TextInputDialog(String.valueOf(produtoSelecionado.getPreco()));
        dialog.setTitle("Atualização de Preço");
        dialog.setHeaderText("Atualizando preço para: " + produtoSelecionado.getNome());
        dialog.setContentText("Digite o novo preço (R$):");

        // Mostra o diálogo e aguarda a entrada do usuário.
        Optional<String> result = dialog.showAndWait();

        // Se o usuário clicou em OK, o resultado estará presente.
        result.ifPresent(novoPrecoStr -> {
            try {
                double novoPreco = Double.parseDouble(novoPrecoStr);
                produtoService.atualizarPrecoProduto(produtoSelecionado.getId(), novoPreco);
                statusLabel.setText("Preço de " + produtoSelecionado.getNome() + " atualizado com sucesso.");
                carregarProdutos();
            } catch (NumberFormatException e) {
                exibirAlerta("O preço deve ser um número válido.");
            } catch (Exception e) {
                exibirAlerta(e.getMessage());
            }
        });
    }

    private void exibirAlerta(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Aviso");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}