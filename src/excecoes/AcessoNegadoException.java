package excecoes;

/**
 * Autoras:
 * Andreísy Neves Ferreira
 * Isabella Paranhos Meireles
 * Lorena da Silva Borges
 */

/**
 * Exceção personalizada utilizada para sinalizar uma falha de autorização no sistema.
 * Esta exceção é lançada quando um usuário tenta executar uma ação ou acessar um recurso
 * para o qual não possui o nível de permissão necessário. Por exemplo, quando um
 * {@link model.Atendente} tenta executar uma funcionalidade restrita a um {@link model.Gerente}.
 *
 * @see java.lang.Exception
 * @see service.UsuarioService#cadastrarAtendente(model.Usuario, model.Atendente)
 */
public class AcessoNegadoException extends Exception {
    /**
     * Construtor que cria uma instância da exceção com uma mensagem de detalhe.
     *
     * @param msg A mensagem de erro que descreve o motivo da falha de acesso.
     */
    public AcessoNegadoException(String msg) {
        super(msg);
    }
}