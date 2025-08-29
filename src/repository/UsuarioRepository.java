package repository;

import com.google.firebase.database.*;
import database.FirebaseConfig;

import model.Atendente;
import model.Gerente;
import model.Usuario;

import java.util.concurrent.CompletableFuture;

public class UsuarioRepository {

    private final DatabaseReference ref;

    public UsuarioRepository() {
        this.ref = FirebaseConfig.getDatabaseReference().child("usuarios");
    }

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

    public void salvar(Usuario usuario) {
        ref.child(String.valueOf(usuario.getMatricula())).setValueAsync(usuario);
    }
}