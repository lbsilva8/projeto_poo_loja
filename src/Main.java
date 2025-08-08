import database.FirebaseConfig;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.CountDownLatch;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        // 1. Inicializa a conexão com o Firebase
        FirebaseConfig.initialize();

        // 2. Obtém a referência para o nó raiz do banco de dados
        DatabaseReference ref = FirebaseConfig.getDatabaseReference();
        System.out.println("Referência do banco de dados obtida.");

        // MUDANÇA 1: Inicialize o Latch para esperar por DUAS operações.
        CountDownLatch latch = new CountDownLatch(2);

        // --- TESTE DE ESCRITA ---
        DatabaseReference usersRef = ref.child("usuarios");
        UserPOO usuarioTeste = new UserPOO("João da Silva", "joao.silva@email.com", 30);

        usersRef.child("user01").setValue(usuarioTeste, (databaseError, databaseReference) -> {
            if (databaseError != null) {
                System.out.println("Falha ao salvar os dados: " + databaseError.getMessage());
            } else {
                System.out.println("Dados salvos com sucesso!");
            }
            // MUDANÇA 2: Avise o Latch que a operação de escrita terminou.
            latch.countDown();
        });

        // --- TESTE DE LEITURA ---
        DatabaseReference userToReadRef = ref.child("usuarios").child("user01");

        userToReadRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserPOO usuarioLido = dataSnapshot.getValue(UserPOO.class);
                if (usuarioLido != null) {
                    System.out.println("--- Leitura do Banco de Dados ---");
                    System.out.println("Nome: " + usuarioLido.getNome());
                    System.out.println("Email: " + usuarioLido.getEmail());
                    System.out.println("Idade: " + usuarioLido.getIdade());
                } else {
                    System.out.println("Usuário não encontrado.");
                }
                // Avise o Latch que a operação de leitura terminou.
                latch.countDown();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("A leitura falhou: " + databaseError.getCode());
                latch.countDown();
            }
        });

        System.out.println("Aguardando as operações de escrita e leitura...");
        latch.await(); // Agora o programa vai pausar aqui até que countDown() seja chamado DUAS VEZES.

        System.out.println("Teste concluído.");

        System.exit(0);
    }
}

class UserPOO {
    private String nome;
    private String email;
    private int idade;

    // Construtor vazio é necessário para a desserialização do Firebase
    public UserPOO() {}

    public UserPOO(String nome, String email, int idade) {
        this.nome = nome;
        this.email = email;
        this.idade = idade;
    }

    // Getters e Setters
    public String getNome() { return nome; }
    public String getEmail() { return email; }
    public int getIdade() { return idade; }
}