package interfaces;

/**
 * Autoras:
 * Andreísy Neves Ferreira
 * Isabella Paranhos Meireles
 * Lorena da Silva Borges
 */

import com.google.api.core.ApiFuture;
import java.util.concurrent.CompletableFuture;

/**
 * Define um contrato genérico para as operações básicas de persistência de dados (CRUD).
 * Esta interface utiliza um tipo genérico {@code <T>} para permitir sua reutilização com
 * qualquer classe de modelo (entidade) do sistema.
 * As operações são assíncronas, retornando objetos {@link ApiFuture} ou
 * {@link CompletableFuture} para não bloquear a execução do programa enquanto
 * aguarda a resposta do banco de dados.
 *
 * @param <T> O tipo da entidade que será gerenciada pela implementação do CRUD.
 * @see repository.ProdutoRepository
 */
public interface ICrud<T> {
    /**
     * Persiste um novo objeto no banco de dados. (Operação Create)
     *
     * @param obj O objeto do tipo {@code T} a ser salvo.
     * @return Um {@link ApiFuture} que é completado quando a operação de salvar termina.
     * @throws Exception se ocorrer um erro durante a comunicação com o banco de dados.
     */
    ApiFuture<Void> salvar(T obj) throws Exception;

    /**
     * Recupera um objeto do banco de dados com base em seu identificador único. (Operação Read)
     *
     * @param id O identificador único (ID) do objeto a ser buscado.
     * @return Um {@link CompletableFuture} que será completado com o objeto {@code T} encontrado,
     * ou {@code null} se nenhum objeto for encontrado com o ID fornecido.
     * @throws Exception se ocorrer um erro durante a comunicação com o banco de dados.
     */
    CompletableFuture<T> buscar(String id) throws Exception;

    /**
     * Atualiza os dados de um objeto já existente no banco de dados. (Operação Update)
     *
     * @param obj O objeto do tipo {@code T} com os dados modificados. O ID do objeto
     * deve corresponder a um registro existente.
     * @return Um {@link ApiFuture} que é completado quando a operação de atualizar termina.
     * @throws Exception se ocorrer um erro durante a comunicação com o banco de dados.
     */
    ApiFuture<Void>  atualizar(T obj) throws Exception;

    /**
     * Remove um objeto do banco de dados com base em seu identificador único. (Operação Delete)
     *
     * @param id O identificador único (ID) do objeto a ser deletado.
     * @return Um {@link ApiFuture} que é completado quando a operação de deletar termina.
     * @throws Exception se ocorrer um erro durante a comunicação com o banco de dados.
     */
    ApiFuture<Void>  deletar(String id) throws Exception;
}
