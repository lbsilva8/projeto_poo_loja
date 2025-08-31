package controller;

/**
 * Autoras:
 * Andreísy Neves Ferreira
 * Isabella Paranhos Meireles
 * Lorena da Silva Borges
 */

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.control.TextInputDialog;
import java.util.Optional;

import model.Permissao;
import model.Produto;
import model.Usuario;
import service.ProdutoService;

/**
 * Classe para a view de gerenciamento de estoque ({@code GerenciarEstoqueView.fxml}).
 * Esta classe exibe todos os produtos em uma tabela e ajusta a interface com base
 * nas permissões do usuário logado. Para Atendentes, funciona como uma tela de
 * visualização. Para Gerentes, habilita controles para adicionar/remover
 * estoque e atualizar preços.
 *
 * @see MainViewController
 * @see service.ProdutoService
 */
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

    /**
     * Metodo de inicialização do JavaFX. Configura as colunas da tabela
     * para se vincularem aos atributos da classe {@link Produto} e carrega os dados.
     */
    @FXML
    public void initialize() {
        colunaId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colunaNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colunaPreco.setCellValueFactory(new PropertyValueFactory<>("preco"));
        colunaEstoque.setCellValueFactory(new PropertyValueFactory<>("quantidade"));

        carregarProdutos();
    }

    /**
     * Recebe o usuário logado e ajusta a visibilidade dos controles de edição
     * com base na permissão {@link Permissao#GERENCIAR_ESTOQUE}.
     */
    public void inicializarDados(Usuario usuario) {
        this.usuarioLogado = usuario;
        boolean podeGerenciar = usuarioLogado.temPermissao(Permissao.GERENCIAR_ESTOQUE);

        controlesGerenteHBox.setVisible(podeGerenciar);
        controlesGerenteHBox.setManaged(podeGerenciar);
    }

    /**
     * Busca os dados mais recentes dos produtos e atualiza a tabela.
     */
    private void carregarProdutos() {
        tabelaProdutos.setItems(FXCollections.observableArrayList(produtoService.buscarTodosProdutos()));
    }

    /** Processa o clique no botão "Adicionar". */
    @FXML
    private void handleAdicionar() {
        editarEstoque(true);
    }

    /** Processa o clique no botão "Remover". */
    @FXML
    private void handleRemover() {
        editarEstoque(false);
    }

    /**
     * Lógica central para adicionar ou remover estoque de um produto selecionado.
     * @param isAdicao {@code true} para adicionar, {@code false} para remover.
     */
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
            carregarProdutos();
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

        TextInputDialog dialog = new TextInputDialog(String.valueOf(produtoSelecionado.getPreco()));
        dialog.setTitle("Atualização de Preço");
        dialog.setHeaderText("Atualizando preço para: " + produtoSelecionado.getNome());
        dialog.setContentText("Digite o novo preço (R$):");
        Optional<String> result = dialog.showAndWait();

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

    /**
     * Metodo auxiliar para exibir uma janela de alerta padrão.
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