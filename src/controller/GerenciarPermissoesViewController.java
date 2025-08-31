package controller;

/**
 * Autoras:
 * Andreísy Neves Ferreira
 * Isabella Paranhos Meireles
 * Lorena da Silva Borges
 */

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import model.Atendente;
import model.Permissao;
import model.Usuario;
import service.UsuarioService;

/**
 * Classe controladora para a view de gerenciamento de permissões ({@code GerenciarPermissoesView.fxml}).
 * <p>
 * Esta classe gerencia uma interface mestre-detalhe, onde uma lista de atendentes é
 * exibida e, ao selecionar um, suas permissões são exibidas dinamicamente como
 * checkboxes para edição por um gerente.
 *
 * @see MainViewController
 * @see service.UsuarioService
 * @see model.Permissao
 */
public class GerenciarPermissoesViewController {

    @FXML private ListView<Usuario> listaUsuarios;
    @FXML private VBox painelPermissoesVBox;
    @FXML private Button salvarButton;
    @FXML private Label statusLabel;

    private Usuario gerenteLogado;
    private final UsuarioService usuarioService = new UsuarioService();

    /**
     * Metodo de inicialização do JavaFX, chamado automaticamente após o FXML ser carregado.
     * Configura a aparência da lista de usuários e adiciona um listener que reage
     * a mudanças na seleção de itens, acionando a exibição das permissões correspondentes.
     */
    @FXML
    public void initialize() {
        listaUsuarios.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Usuario item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getNome() + " (Matrícula: " + item.getMatricula() + ")");
                }
            }
        });

        listaUsuarios.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> exibirPermissoes(newSelection)
        );
    }

    public void inicializarDados(Usuario gerente) {
        this.gerenteLogado = gerente;
        carregarUsuariosNaLista();
    }

    /**
     * Busca os usuários do serviço, filtra para exibir apenas Atendentes, e popula a ListView.
     */
    private void carregarUsuariosNaLista() {
        List<Usuario> atendentes = usuarioService.buscarTodosUsuarios().stream()
                .filter(u -> u instanceof Atendente)
                .collect(Collectors.toList());
        listaUsuarios.setItems(FXCollections.observableArrayList(atendentes));
    }

    /**
     * Gera e exibe dinamicamente as checkboxes de permissão para um usuário selecionado.
     *
     * @param usuario O usuário selecionado na lista, ou null se a seleção for limpa.
     */
    private void exibirPermissoes(Usuario usuario) {
        painelPermissoesVBox.getChildren().clear(); // Limpa as checkboxes antigas
        statusLabel.setText("");

        if (usuario == null) {
            salvarButton.setDisable(true);
            return;
        }

        for (Permissao p : Permissao.values()) {
            CheckBox checkBox = new CheckBox(p.getDescricao());
            checkBox.setSelected(usuario.getPermissoes().getOrDefault(p.name(), false));
            checkBox.setUserData(p);
            painelPermissoesVBox.getChildren().add(checkBox);
        }
        salvarButton.setDisable(false);
    }

    /**
     * Processa o clique no botão "Salvar Permissões".
     * Reconstrói o mapa de permissões com base no estado atual das checkboxes
     * e chama o serviço para persistir as alterações no banco de dados.
     */
    @FXML
    private void handleSalvarPermissoes() {
        Usuario usuarioSelecionado = listaUsuarios.getSelectionModel().getSelectedItem();
        if (usuarioSelecionado == null) {
            return;
        }

        Map<String, Boolean> novasPermissoes = new HashMap<>();
        for (Node node : painelPermissoesVBox.getChildren()) {
            if (node instanceof CheckBox) {
                CheckBox checkBox = (CheckBox) node;
                Permissao p = (Permissao) checkBox.getUserData();
                novasPermissoes.put(p.name(), checkBox.isSelected());
            }
        }

        try {
            usuarioService.atualizarPermissoes(gerenteLogado, usuarioSelecionado.getMatricula(), novasPermissoes);
            statusLabel.setText("Permissões de " + usuarioSelecionado.getNome() + " salvas com sucesso!");
        } catch (Exception e) {
            statusLabel.setText("ERRO: " + e.getMessage());
            e.printStackTrace();
        }
    }
}