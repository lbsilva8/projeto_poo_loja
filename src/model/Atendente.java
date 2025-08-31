package model;

/**
 * Autoras:
 * Andreísy Neves Ferreira
 * Isabella Paranhos Meireles
 * Lorena da Silva Borges
 */

/**
 * Representa um usuário concreto do tipo Atendente no sistema.
 * Esta classe é uma especialização da classe abstrata {@link Usuario}. Seu principal
 * propósito é modelar um papel (role) com um conjunto de permissões específico.
 *
 * @see Usuario
 * @see repository.UsuarioRepository
 * @see service.UsuarioService
 */
public class Atendente extends Usuario {

    /**
     * Construtor principal para criar uma nova instância de Atendente com todos os dados.
     * Este construtor repassa os dados para a classe pai {@link Usuario} e define
     * permanentemente o cargo como "ATENDENTE".
     *
     * @param matricula O número de matrícula do atendente.
     * @param nome      O nome completo do atendente.
     * @param usuario   O nome de usuário para login.
     * @param senha     A senha para login.
     */
    public Atendente(int matricula, String nome, String usuario, String senha) {
        super(matricula, nome, usuario, senha, "ATENDENTE");
    }

    /**
     * Construtor sem argumentos.
     * Este construtor é necessário para que o Firebase Realtime Database
     * possa instanciar esta classe ao desserializar os dados do banco.
     * Ele garante que, mesmo quando criado sem argumentos, o cargo do objeto
     * seja corretamente definido como "ATENDENTE".
     */
    public Atendente() {
        super();
        setCargo("ATENDENTE");
    }
}