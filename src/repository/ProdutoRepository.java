package repository;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.api.core.ApiFuture;
import database.FirebaseConfig;

import model.Produto;
import interfaces.ICrud;

import java.util.concurrent.CompletableFuture;

/**
 * Implementação concreta do padrão Repository para a entidade {@link Produto}.
 * Esta classe é responsável por abstrair todas as operações de acesso a dados (CRUD)
 * para a entidade {@link Produto} no Firebase Realtime Database. Ela encapsula a
 * lógica específica do Firebase SDK, fornecendo métodos simples e padronizados
 * para a camada de serviço da aplicação.
 *
 * @see ICrud
 * @see Produto
 * @see service.ProdutoService
 */
public class ProdutoRepository implements ICrud<Produto> {

    /**
     * Referência para o nó "produtos" no Firebase Realtime Database.
     */
    private DatabaseReference ref;

    /**
     * Construtor que inicializa o repositório.
     * Ele obtém a referência principal do banco de dados através da classe
     * {@link FirebaseConfig} e aponta para o nó filho "produtos", onde todos
     * os dados de produtos serão armazenados.
     */
    public ProdutoRepository() {
        this.ref = FirebaseConfig.getDatabaseReference().child("produtos");
    }

    /**
     * Salva um objeto {@link Produto} completo no nó correspondente ao seu ID.
     * Se o ID já existir, os dados serão sobrescritos.
     */
    @Override
    public ApiFuture<Void> salvar(Produto produto) {
        return ref.child(produto.getId()).setValueAsync(produto);
    }

    /**
     * Atualiza um objeto {@link Produto} existente, sobrescrevendo todos os seus
     * dados no nó correspondente ao seu ID.
     */
    @Override
    public ApiFuture<Void> atualizar(Produto produto) {
        return ref.child(produto.getId()).setValueAsync(produto);
    }

    /**
     * Remove o nó correspondente ao ID do produto do banco de dados.
     */
    @Override
    public ApiFuture<Void> deletar(String id) {
        return ref.child(id).removeValueAsync();
    }

    /**
     * Realiza uma busca assíncrona por um produto com base no seu ID.
     * O {@link CompletableFuture} retornado é completado com o objeto {@link Produto}
     * em caso de sucesso, com {@code null} se não for encontrado, ou com uma exceção
     * em caso de erro.
     */
    @Override
    public CompletableFuture<Produto> buscar(String id) {
        CompletableFuture<Produto> future = new CompletableFuture<>();

        ref.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            /**
             * Callback executado quando o Firebase retorna os dados com sucesso.
             */
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Produto produto = dataSnapshot.getValue(Produto.class);
                    future.complete(produto);
                } else {
                    future.complete(null);
                }
            }

            /**
             * Callback executado se a leitura for cancelada ou falhar por algum motivo (ex: permissões).
             */
            @Override
            public void onCancelled(DatabaseError databaseError) {
                future.completeExceptionally(databaseError.toException());
            }
        });

        return future;
    }
}