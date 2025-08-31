package view;

import excecoes.EstoqueInsuficienteException;
import excecoes.ProdutoNaoEncontradoException;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;

import model.FormaPagamento;
import model.Usuario;
import repository.VendaRepository;
import service.ProdutoService;
import service.VendaService;

public class VendaViewController {

    @FXML private TextField produtoIdField;
    @FXML private TextField quantidadeField;
    @FXML private TextField descontoField;
    @FXML private ComboBox<FormaPagamento> formaPagamentoBox;
    @FXML private Label statusVendaLabel;

    private Usuario usuarioLogado;
    private final ProdutoService produtoService = new ProdutoService();
    private final VendaService vendaService = new VendaService(produtoService, new VendaRepository());

    /**
     * Este método é chamado automaticamente pelo JavaFX após o FXML ser carregado.
     * Usado para configurar componentes.
     */
    @FXML
    public void initialize() {
        // Preenche o ComboBox com as opções do Enum FormaPagamento
        formaPagamentoBox.getItems().setAll(FormaPagamento.values());
        formaPagamentoBox.getSelectionModel().selectFirst(); // Seleciona o primeiro por padrão
    }

    /**
     * Recebe o usuário logado da tela principal para associá-lo à venda.
     */
    public void inicializarDados(Usuario usuario) {
        this.usuarioLogado = usuario;
    }

    @FXML
    private void handleBuscarProduto() {
        try {
            // 1. Carrega o FXML da tela de busca
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/BuscaProdutoView.fxml"));
            Parent root = loader.load();

            // 2. Pega o controller da tela de busca
            BuscaProdutoViewController buscaController = loader.getController();

            // 3. Cria e configura a nova janela (Stage) como um pop-up modal
            Stage buscaStage = new Stage();
            buscaStage.setTitle("Buscar Produto");
            buscaStage.setScene(new Scene(root));
            buscaStage.initModality(Modality.APPLICATION_MODAL); // Bloqueia a janela de vendas
            buscaStage.setResizable(false);

            // 4. Mostra a janela e ESPERA até que ela seja fechada
            buscaStage.showAndWait();

            // 5. Após fechar, pega o resultado do controller da busca
            String idSelecionado = buscaController.getProdutoIdSelecionado();
            if (idSelecionado != null) {
                // 6. Coloca o ID selecionado no campo de texto da tela de vendas
                produtoIdField.setText(idSelecionado);
            }

        } catch (IOException e) {
            e.printStackTrace();
            exibirAlerta("Erro", "Não foi possível abrir a tela de busca de produtos.");
        }
    }

    @FXML
    private void handleRegistrarVenda() {
        try {
            String produtoId = produtoIdField.getText();
            int quantidade = Integer.parseInt(quantidadeField.getText());
            double desconto = Double.parseDouble(descontoField.getText());
            FormaPagamento formaPagamento = formaPagamentoBox.getValue();

            vendaService.registrarVenda(usuarioLogado, produtoId, quantidade, formaPagamento, desconto);

            statusVendaLabel.setText("Venda registrada com sucesso!");
            limparCampos();

        } catch (NumberFormatException e) {
            exibirAlerta("Erro de Formato", "Quantidade e desconto devem ser números válidos.");
        } catch (ProdutoNaoEncontradoException | EstoqueInsuficienteException | IllegalArgumentException e) {
            exibirAlerta("Erro de Venda", e.getMessage());
        } catch (Exception e) {
            exibirAlerta("Erro Inesperado", "Ocorreu um erro: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void limparCampos() {
        produtoIdField.clear();
        quantidadeField.clear();
        descontoField.setText("0.0");
        formaPagamentoBox.getSelectionModel().selectFirst();
    }

    private void exibirAlerta(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}