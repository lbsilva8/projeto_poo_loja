package controller;

/**
 * Autoras:
 * Andreísy Neves Ferreira
 * Isabella Paranhos Meireles
 * Lorena da Silva Borges
 */

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import model.Produto;
import service.ProdutoService;

/**
 * Classe para a view de cadastro de novos produtos ({@code ProdutoCadastroView.fxml}).
 * Esta classe gerencia o formulário de entrada de dados para um novo produto,
 * realizando validações básicas e delegando a lógica de negócio de persistência
 * para o {@link ProdutoService}.
 *
 * @see controller.MainViewController
 * @see service.ProdutoService
 */
public class ProdutoCadastroViewController {

    @FXML private TextField idField;
    @FXML private TextField nomeField;
    @FXML private TextField tipoField;
    @FXML private TextField precoField;
    @FXML private TextField quantidadeField;
    @FXML private Label statusLabel;

    private final ProdutoService produtoService = new ProdutoService();

    /**
     * Processa o evento de clique do botão "Cadastrar Produto".
     * Coleta os dados dos campos do formulário, realiza validações de entrada,
     * cria um novo objeto {@link Produto} e invoca o serviço para salvá-lo.
     * Fornece feedback ao usuário através de um label de status.
     */
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

    /**
     * Metodo auxiliar para limpar todos os campos do formulário após um
     * cadastro bem-sucedido, preparando a tela para uma nova entrada.
     */
    private void limparCampos() {
        idField.clear();
        nomeField.clear();
        tipoField.clear();
        precoField.clear();
        quantidadeField.clear();
    }
}