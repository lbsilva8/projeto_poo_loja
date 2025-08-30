package excecoes;

/**
 * Exceção personalizada utilizada para sinalizar uma falha no processo de autenticação de um usuário.
 * Esta exceção é lançada quando as credenciais fornecidas (como nome de usuário e senha)
 * não correspondem aos registros do sistema, impedindo o login.
 *
 * @see java.lang.Exception
 * @see model.Usuario#autenticar(String, String)
 * @see service.UsuarioService#autenticar(String, String)
 * @see AcessoNegadoException
 */
public class AutenticacaoException extends Exception {
    /**
     * Construtor que cria uma nova instância da exceção com uma mensagem de detalhe.
     *
     * @param msg A mensagem de erro que descreve o motivo da falha de autenticação, como "Usuário ou senha inválidos".
     */
    public AutenticacaoException(String msg) {
        super(msg);
    }
}
