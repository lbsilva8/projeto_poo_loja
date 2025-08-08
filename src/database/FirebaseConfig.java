package database;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.FileInputStream;
import java.io.IOException;

public class FirebaseConfig {

    private static FirebaseDatabase firebaseDatabase;

    public static void initialize() {
        try {
            // Carrega o arquivo de credenciais da pasta resources
            FileInputStream serviceAccount =
                    new FileInputStream("src/main/resources/serviceAccountKey.json"); // Ajuste o caminho se necessário

            // URL do seu Realtime Database (pegue no console do Firebase)
            String databaseUrl = "https://SEU-PROJETO-ID.firebaseio.com"; // SUBSTITUA PELO URL DO SEU DB

            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setDatabaseUrl(databaseUrl)
                    .build();

            // Inicializa o app do Firebase, mas somente se não foi inicializado ainda
            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
            }

            firebaseDatabase = FirebaseDatabase.getInstance();
            System.out.println("Conexão com o Firebase estabelecida com sucesso!");

        } catch (IOException e) {
            System.err.println("Erro ao inicializar o Firebase: " + e.getMessage());
            // Lançar uma exceção ou tratar o erro de forma apropriada para sua aplicação
            throw new RuntimeException(e);
        }
    }

    public static DatabaseReference getDatabaseReference() {
        if (firebaseDatabase == null) {
            // Garante que a inicialização ocorreu
            initialize();
        }
        return firebaseDatabase.getReference();
    }
}