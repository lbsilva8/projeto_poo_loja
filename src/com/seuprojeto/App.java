package com.seuprojeto;

import database.FirebaseConfig;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        // Carrega o arquivo FXML da tela de login
        Parent root = FXMLLoader.load(getClass().getResource("/view/LoginView.fxml"));

        primaryStage.setTitle("Sistema de Vendas  - Login");
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        // Inicializa serviços essenciais antes de iniciar a UI
        System.out.println("Inicializando Firebase...");
        FirebaseConfig.initialize();
        System.out.println("Firebase inicializado.");

        // Chama o metodo que inicia a aplicação JavaFX
        launch(args);
    }
}
