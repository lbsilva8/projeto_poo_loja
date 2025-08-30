package repository;

import com.google.firebase.database.*;
import database.FirebaseConfig;

import model.Atendente;
import model.Gerente;
import model.Usuario;

import java.util.concurrent.CompletableFuture;

/**
 * Implementação do padrão Repository para a entidade {@link Usuario} e suas subclasses.
 * Esta classe gerencia o acesso aos dados dos usuários no Firebase Realtime Database.
 * Ao buscar um usuário,esta classe determina se ele é um {@link Gerente} ou um {@link Atendente} com base
 * no campo "cargo" armazenado no banco, instanciando o tipo de objeto correto.
 *
 * @see Usuario
 * @see Gerente
 * @see Atendente
 * @see service.UsuarioService
 */
public class UsuarioRepository {

    /**
     * Referência para o nó "usuarios" no Firebase Realtime Database.
     */
    private final DatabaseReference ref;

    /**
     * Construtor que inicializa a referência do banco de dados para o nó "usuarios".
     */
    public UsuarioRepository() {
        this.ref = FirebaseConfig.getDatabaseReference().child("usuarios");
    }

    /**
     * Busca um usuário de forma assíncrona pelo seu nome de usuário (login).
     * Este metodo executa uma query no Firebase para encontrar um registro onde o campo
     * "usuario" corresponda ao parâmetro fornecido. Após encontrar os dados, ele
     * inspeciona o campo "cargo" para instanciar a subclasse correta de {@link Usuario}.
     *
     * @param nomeUsuario O nome de usuário (login) a ser procurado.
     * @return Um {@link CompletableFuture} que será completado com a instância de
     * {@link Gerente} ou {@link Atendente} correspondente, ou com {@code null}
     * se nenhum usuário for encontrado.
     */
    public CompletableFuture<Usuario> buscarPorUsuario(String nomeUsuario) {
        CompletableFuture<Usuario> future = new CompletableFuture<>();

        Query query = ref.orderByChild("usuario").equalTo(nomeUsuario);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    DataSnapshot userSnapshot = dataSnapshot.getChildren().iterator().next();
                    String cargo = userSnapshot.child("cargo").getValue(String.class);

                    Usuario usuario = null;
                    if ("GERENTE".equals(cargo)) {
                        usuario = userSnapshot.getValue(Gerente.class);
                    } else {
                        usuario = userSnapshot.getValue(Atendente.class);
                    }
                    future.complete(usuario);
                } else {
                    future.complete(null);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                future.completeExceptionally(databaseError.toException());
            }
        });

        return future;
    }

    /**
     * Salva ou atualiza os dados de um {@link Usuario} no Firebase.
     * A matrícula do usuário é usada como a chave única para o registro no banco de dados.
     * Se um usuário com a mesma matrícula já existir, seus dados serão sobrescritos.
     *
     * @param usuario O objeto {@link Usuario} (ou uma de suas subclasses) a ser salvo.
     */
    public void salvar(Usuario usuario) {
        ref.child(String.valueOf(usuario.getMatricula())).setValueAsync(usuario);
    }
}