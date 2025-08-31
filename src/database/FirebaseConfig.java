package database;

/**
 * Autoras:
 * Andreísy Neves Ferreira
 * Isabella Paranhos Meireles
 * Lorena da Silva Borges
 */

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class FirebaseConfig {

    private static FirebaseDatabase firebaseDatabase;

    public static void initialize() {
        try {
            // Caminho do arquivo JSON na raiz do src
            //FileInputStream serviceAccount = new FileInputStream("/serviceAccountKey.json");
            InputStream serviceAccount = FirebaseConfig.class
                    .getResourceAsStream("/serviceAccountKey.json");

            if (serviceAccount == null) {
                throw new RuntimeException("ERRO: Não foi possível encontrar o arquivo 'serviceAccountKey.json'. Verifique se ele está na pasta 'resources'.");
            }

            // URL do seu Realtime Database (pegar no console do Firebase)
            String databaseUrl = "https://projeto-poo-43e7d-default-rtdb.firebaseio.com/"; // Troque pelo URL correto

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setDatabaseUrl(databaseUrl)
                    .build();

            // Inicializa o app do Firebase, mas somente se ainda não tiver inicializado
            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
            }

            firebaseDatabase = FirebaseDatabase.getInstance();
            System.out.println("Conexão com o Firebase estabelecida com sucesso!");

        } catch (IOException e) {
            System.err.println("Erro ao inicializar o Firebase: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public static DatabaseReference getDatabaseReference() {
        if (firebaseDatabase == null) {
            initialize();
        }
        return firebaseDatabase.getReference();
    }
}
