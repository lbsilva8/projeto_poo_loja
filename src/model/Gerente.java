package model;

/**
 * Autoras:
 * Andreísy Neves Ferreira
 * Isabella Paranhos Meireles
 * Lorena da Silva Borges
 */

import java.util.Map;

/**
 * Representa um usuário concreto do tipo Gerente no sistema.
 * Esta classe, uma especialização de {@link Usuario}, é usada para modelar um
 * usuário com privilégios administrativos. O sistema utiliza o tipo desta classe
 * para realizar checagens de autorização, liberando o acesso a funcionalidades
 * restritas como o cadastro de novos usuários, gerenciamento de produtos e
 * controle de estoque.
 *
 * @see Usuario
 * @see service.UsuarioService
 * @see repository.UsuarioRepository
 */
public class Gerente extends Usuario {

    /**
     * Construtor principal para criar uma nova instância de Gerente com todos os dados.
     * Este construtor repassa os dados para a classe pai {@link Usuario} e define
     * permanentemente o cargo como "GERENTE".
     *
     * @param matricula O número de matrícula do gerente.
     * @param nome      O nome completo do gerente.
     * @param usuario   O nome de usuário para login.
     * @param senha     A senha para login.
     */
    public Gerente(int matricula, String nome, String usuario, String senha) {
        super(matricula, nome, usuario, senha, "GERENTE");
        darPermissoesDeGerente();
    }

    /**
     * Construtor sem argumentos.
     * Este construtor é necessário para que o Firebase Realtime Database
     * possa instanciar esta classe ao desserializar os dados do banco.
     * Ele garante que, mesmo quando criado sem argumentos, o cargo do objeto
     * seja corretamente definido como "GERENTE".
     */
    public Gerente() {
        super();
        setCargo("GERENTE");
        darPermissoesDeGerente();
    }

    /**
     * Metodo privado que sobrescreve as permissões padrão, concedendo acesso total.
     */
    private void darPermissoesDeGerente() {
        Map<String, Boolean> permissoes = getPermissoes();
        for (Permissao p : Permissao.values()) {
            permissoes.put(p.name(), true);
        }
    }
}