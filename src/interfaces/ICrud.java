package interfaces;
import com.google.api.core.ApiFuture;

import java.util.concurrent.CompletableFuture; // Importe a classe

public interface ICrud<T> {
    ApiFuture<Void> salvar(T obj) throws Exception;
    CompletableFuture<T> buscar(String id) throws Exception;
    ApiFuture<Void>  atualizar(T obj) throws Exception;
    ApiFuture<Void>  deletar(String id) throws Exception;
}
