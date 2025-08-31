package view;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.Produto;
import service.ProdutoService;

public class ProdutoCadastroViewController {

    @FXML private TextField idField;
    @FXML private TextField nomeField;
    @FXML private TextField tipoField;
    @FXML private TextField precoField;
    @FXML private TextField quantidadeField;
    @FXML private Label statusLabel;

    private final ProdutoService produtoService = new ProdutoService();

    @FXML
    private void handleCadastrar() {
        try {
            String id = idField.getText();
            String nome = nomeField.getText();
            String tipo = tipoField.getText();
            double preco = Double.parseDouble(precoField.getText());
            int quantidade = Integer.parseInt(quantidadeField.getText());

            if (id.isEmpty() || nome.isEmpty() || tipo.isEmpty()) {
                statusLabel.setText("ERRO: Todos os campos são obrigatórios.");
                statusLabel.setTextFill(javafx.scene.paint.Color.RED);
                return;
            }

            Produto novoProduto = new Produto(id, tipo, nome, preco, quantidade);
            produtoService.cadastrarProduto(novoProduto);

            statusLabel.setText("Produto '" + nome + "' cadastrado com sucesso!");
            statusLabel.setTextFill(javafx.scene.paint.Color.GREEN);
            limparCampos();

        } catch (NumberFormatException e) {
            statusLabel.setText("ERRO: Preço e Quantidade devem ser números válidos.");
            statusLabel.setTextFill(javafx.scene.paint.Color.RED);
        } catch (Exception e) {
            statusLabel.setText("ERRO: " + e.getMessage());
            statusLabel.setTextFill(javafx.scene.paint.Color.RED);
            e.printStackTrace();
        }
    }

    private void limparCampos() {
        idField.clear();
        nomeField.clear();
        tipoField.clear();
        precoField.clear();
        quantidadeField.clear();
    }
}