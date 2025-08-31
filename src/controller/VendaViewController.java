package controller;

/**
 * Autoras:
 * Andreísy Neves Ferreira
 * Isabella Paranhos Meireles
 * Lorena da Silva Borges
 */

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
import excecoes.EstoqueInsuficienteException;
import excecoes.ProdutoNaoEncontradoException;

/**
 * Classe para a tela de registro de vendas ({@code VendaView.fxml}).
 * Esta classe gerencia o formulário de vendas, capturando as entradas do usuário,
 * orquestrando a busca de produtos através de uma janela modal e delegando a lógica
 * de negócio para o {@link VendaService}.
 *
 * @see MainViewController
 * @see BuscaProdutoViewController
 * @see VendaService
 */
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
     * Metodo de inicialização do JavaFX, chamado automaticamente após o FXML ser carregado.
     * Configura os componentes iniciais da tela, como o preenchimento do ComboBox
     * de formas de pagamento.
     */
    @FXML
    public void initialize() {
        formaPagamentoBox.getItems().setAll(FormaPagamento.values());
        formaPagamentoBox.getSelectionModel().selectFirst();
    }

    /**
     * Recebe o usuário logado da tela principal para associá-lo à venda.
     * Este metodo é o ponto de entrada de dados para este controller.
     *
     * @param usuario O objeto {@link Usuario} que está realizando a venda.
     */
    public void inicializarDados(Usuario usuario) {
        this.usuarioLogado = usuario;
    }

    /**
     * Manipula o clique no botão "Buscar", abrindo uma janela modal para a
     * seleção de produtos.
     * Após a janela de busca ser fechada, o ID do produto selecionado é
     * preenchido no campo de texto correspondente.
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

            buscaStage.showAndWait();

            String idSelecionado = buscaController.getProdutoIdSelecionado();
            if (idSelecionado != null) {
                produtoIdField.setText(idSelecionado);
            }

        } catch (IOException e) {
            e.printStackTrace();
            exibirAlerta("Erro", "Não foi possível abrir a tela de busca de produtos.");
        }
    }

    /**
     * Manipula o clique no botão "Registrar Venda".
     * Coleta todos os dados do formulário, valida as entradas numéricas e chama
     * o {@link VendaService} para processar a venda. Exibe alertas para o usuário
     * em caso de erro ou uma mensagem de sucesso.
     */
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

    /**
     * Metodo auxiliar para limpar todos os campos do formulário após uma operação bem-sucedida.
     */
    private void limparCampos() {
        produtoIdField.clear();
        quantidadeField.clear();
        descontoField.setText("0.0");
        formaPagamentoBox.getSelectionModel().selectFirst();
    }

    /**
     * Metodo auxiliar para exibir uma janela de alerta de erro padrão para o usuário.
     *
     * @param titulo O título da janela de alerta.
     * @param mensagem A mensagem de erro a ser exibida.
     */
    private void exibirAlerta(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}