package com.seuprojeto;

import database.FirebaseConfig;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Classe principal que serve como ponto de entrada para a aplicação
 * gráfica JavaFX.
 * Esta classe herda de {@link Application}.
 * Suas responsabilidades incluem a inicialização de serviços de back-end (como o Firebase)
 * e o carregamento e exibição da primeira tela da interface do usuário (a tela de login).
 *
 * @see javafx.application.Application
 * @see view.LoginViewController
 */
public class App extends Application {

    /**
     * O metodo principal do ciclo de vida do JavaFX, chamado após a inicialização
     * da aplicação.
     * Este metodo é responsável por configurar e exibir a tela inicial.
     * Ele carrega a view de login a partir do arquivo FXML,
     * define o título da janela e a torna visível para o usuário.
     *
     * @param primaryStage A janela principal da aplicação, fornecida pelo
     * toolkit JavaFX. É o container para todas as cenas.
     * @throws IOException se houver um erro ao carregar o arquivo FXML.
     */
    @Override
    public void start(Stage primaryStage) throws IOException {
        // Carrega o arquivo FXML da tela principal
        Parent root = FXMLLoader.load(getClass().getResource("/view/HomeView.fxml"));

        primaryStage.setTitle("Sistema de Vendas  - Login");
        primaryStage.setScene(new Scene(root));
        primaryStage.setMaximized(true);
        primaryStage.show();
    }

    /**
     * O ponto de entrada padrão da aplicação Java.
     * Suas principais funções são realizar configurações prévias que não dependem
     * da interface gráfica (como inicializar a conexão com o banco de dados) e
     * depois iniciar o ciclo de vida da aplicação JavaFX.
     *
     * @param args Argumentos de linha de comando (não utilizados nesta aplicação).
     */
    public static void main(String[] args) {
        // Inicializa serviços essenciais antes de iniciar a UI
        System.out.println("Inicializando Firebase...");
        FirebaseConfig.initialize();
        System.out.println("Firebase inicializado.");

        // Chama o metodo que inicia a aplicação JavaFX
        launch(args);
    }
}
