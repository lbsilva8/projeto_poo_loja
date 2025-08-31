package view;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;

import excecoes.ProdutoNaoEncontradoException;
import model.Produto;
import service.ProdutoService;


/**
 * Classe para a tela de adição de estoque ({@code EstoqueView.fxml}).
 * Gerencia a interface do usuário e o fluxo de trabalho para o caso de uso de
 * adicionar quantidade ao estoque de um produto. Este processo envolve a seleção
 * de um produto através de uma janela de busca modal e, em seguida, a entrada
 * da quantidade a ser adicionada.
 *
 * @see BuscaProdutoViewController
 * @see ProdutoService
 */
public class EstoqueViewController {

    @FXML private Label infoProdutoLabel;
    @FXML private TextField quantidadeAdicionalField;
    @FXML private Button adicionarButton;
    @FXML private Label statusLabel;

    private final ProdutoService produtoService = new ProdutoService();
    private Produto produtoSelecionado;

    /**
     * Manipula o clique no botão "Buscar Produto".
     * Este metodo abre a janela de busca de produtos ({@code BuscaProdutoView}) como um
     * pop-up modal. Ele aguarda o usuário selecionar um produto e fechar a janela.
     * Após a seleção, ele busca os dados completos do produto, atualiza a interface
     * e habilita os controles para a adição de estoque.
     */
    @FXML
    private void handleBuscarProduto() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/BuscaProdutoView.fxml"));
            Parent root = loader.load();
            BuscaProdutoViewController buscaController = loader.getController();

            // Configura e exibe a janela como um modal (bloqueia a janela principal).
            Stage buscaStage = new Stage();
            buscaStage.setTitle("Buscar Produto");
            buscaStage.setScene(new Scene(root));
            buscaStage.initModality(Modality.APPLICATION_MODAL);
            buscaStage.setResizable(false);
            buscaStage.showAndWait();

            // Após o fechamento da janela, recupera o resultado.
            String idSelecionado = buscaController.getProdutoIdSelecionado();
            if (idSelecionado != null) {
                // Se um produto foi selecionado, busca os dados completos
                produtoSelecionado = produtoService.buscarProduto(idSelecionado);
                infoProdutoLabel.setText("Produto: " + produtoSelecionado.getNome() + " | Estoque Atual: " + produtoSelecionado.getQuantidade());

                // Habilita os controles para adicionar estoque
                quantidadeAdicionalField.setDisable(false);
                adicionarButton.setDisable(false);
                statusLabel.setText("");
            }
        } catch (IOException | ProdutoNaoEncontradoException e) {
            e.printStackTrace();
            statusLabel.setText("ERRO: Falha ao carregar ou buscar produto.");
        }
    }

    /**
     * Manipula o clique no botão "Adicionar Estoque".
     * Pega a quantidade do campo de texto, chama o serviço de produto para executar
     * a lógica de negócio e atualiza a interface com o resultado e o novo estoque.
     */
    @FXML
    private void handleAdicionarEstoque() {
        // Validação de estado: garante que um produto foi selecionado antes de prosseguir.
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