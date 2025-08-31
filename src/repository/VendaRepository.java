package repository;

/**
 * Autoras:
 * Andreísy Neves Ferreira
 * Isabella Paranhos Meireles
 * Lorena da Silva Borges
 */

import com.google.firebase.database.DatabaseReference;
import database.FirebaseConfig;

import model.Venda;
import model.VendaDTO;

/**
 * Implementação do padrão Repository para a entidade {@link Venda}.
 * Sua principal função é receber um objeto de domínio
 * {@link Venda}, convertê-lo para seu respectivo Objeto de Transferência de Dados
 * ({@link VendaDTO}), e então salvá-lo no banco.
 *
 * @see Venda
 * @see VendaDTO
 * @see service.VendaService
 */
public class VendaRepository {
    /**
     * Referência para o nó "vendas" no Firebase Realtime Database.
     */
    private DatabaseReference ref;

    /**
     * Construtor que inicializa a referência do banco de dados para o nó "vendas".
     */
    public VendaRepository() {
        this.ref = FirebaseConfig.getDatabaseReference().child("vendas");
    }
    /**
     * Converte uma entidade {@link Venda} em um {@link VendaDTO} e a salva de forma
     * assíncrona no Firebase.
     * O ID do objeto {@code Venda} é usado como a chave única para o registro no banco.
     * A operação de escrita não bloqueia a execução do programa.
     *
     * @param venda O objeto de domínio {@link Venda} completo que representa a transação a ser salva.
     */
    public void salvar(Venda venda) {
        VendaDTO dto = new VendaDTO(venda);
        ref.child(venda.getId()).setValueAsync(dto);
    }
}
