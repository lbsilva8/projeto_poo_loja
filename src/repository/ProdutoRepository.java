package repository;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.api.core.ApiFuture;

import database.FirebaseConfig;
import model.Produto;
import interfaces.ICrud;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;


public class ProdutoRepository implements ICrud<Produto> {

    private DatabaseReference ref;

    public ProdutoRepository() {
        this.ref = FirebaseConfig.getDatabaseReference().child("produtos");
    }

    @Override
    public ApiFuture<Void> salvar(Produto produto) {
        return ref.child(produto.getId()).setValueAsync(produto);
    }

    @Override
    public ApiFuture<Void> atualizar(Produto produto) {
        return ref.child(produto.getId()).setValueAsync(produto);
    }

    @Override
    public ApiFuture<Void> deletar(String id) {
        return ref.child(id).removeValueAsync();
    }

    @Override
    public CompletableFuture<Produto> buscar(String id) {
        CompletableFuture<Produto> future = new CompletableFuture<>();

        ref.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Produto produto = dataSnapshot.getValue(Produto.class);
                    future.complete(produto);
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
}