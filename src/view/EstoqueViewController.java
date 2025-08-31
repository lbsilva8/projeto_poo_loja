package view;

import excecoes.ProdutoNaoEncontradoException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Produto;
import service.ProdutoService;

import java.io.IOException;

public class EstoqueViewController {

    @FXML private Label infoProdutoLabel;
    @FXML private TextField quantidadeAdicionalField;
    @FXML private Button adicionarButton;
    @FXML private Label statusLabel;

    private final ProdutoService produtoService = new ProdutoService();
    private Produto produtoSelecionado;

    /**
     * Chamado quando o botão "Buscar Produto..." é clicado.
     * Abre a mesma janela de busca de produto usada na tela de vendas.
     */
    @FXML
    private void handleBuscarProduto() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/BuscaProdutoView.fxml"));
            Parent root = loader.load();
            BuscaProdutoViewController buscaController = loader.getController();

            Stage buscaStage = new Stage();
            buscaStage.setTitle("Buscar Produto");
            buscaStage.setScene(new Scene(root));
            buscaStage.initModality(Modality.APPLICATION_MODAL);
            buscaStage.setResizable(false);
            buscaStage.showAndWait(); // Pausa aqui até o pop-up ser fechado

            String idSelecionado = buscaController.getProdutoIdSelecionado();
            if (idSelecionado != null) {
                // Se um produto foi selecionado, busca os dados completos
                produtoSelecionado = produtoService.buscarProduto(idSelecionado);
                infoProdutoLabel.setText("Produto: " + produtoSelecionado.getNome() + " | Estoque Atual: " + produtoSelecionado.getQuantidade());

                // Habilita os controles para adicionar estoque
                quantidadeAdicionalField.setDisable(false);
                adicionarButton.setDisable(false);
                statusLabel.setText(""); // Limpa status anterior
            }
        } catch (IOException | ProdutoNaoEncontradoException e) {
            e.printStackTrace();
            statusLabel.setText("ERRO: Falha ao carregar ou buscar produto.");
        }
    }

    /**
     * Chamado quando o botão "Adicionar Estoque" é clicado.
     */
    @FXML
    private void handleAdicionarEstoque() {
        if (produtoSelecionado == null) {
            statusLabel.setText("ERRO: Nenhum produto selecionado.");
            return;
        }

        try {
            int quantidadeAdicional = Integer.parseInt(quantidadeAdicionalField.getText());
            produtoService.adicionarEstoque(produtoSelecionado.getId(), quantidadeAdicional);

            statusLabel.setText("Estoque adicionado com sucesso!");
            quantidadeAdicionalField.clear();

            // Atualiza as informações do produto selecionado na tela
            produtoSelecionado = produtoService.buscarProduto(produtoSelecionado.getId());
            infoProdutoLabel.setText("Produto: " + produtoSelecionado.getNome() + " | Estoque Atual: " + produtoSelecionado.getQuantidade());

        } catch (NumberFormatException e) {
            statusLabel.setText("ERRO: A quantidade deve ser um número.");
        } catch (IllegalArgumentException | ProdutoNaoEncontradoException e) {
            statusLabel.setText("ERRO: " + e.getMessage());
        }
    }
}