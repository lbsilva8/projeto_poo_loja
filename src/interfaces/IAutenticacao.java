package interfaces;

/**
 * Define um contrato para todas as classes cujos objetos podem ser autenticados no sistema.
 * Uma classe que implementa esta interface garante que possui a lógica necessária para
 * validar um par de credenciais (usuário e senha).
 *
 * @see model.Usuario
 */
public interface IAutenticacao {
    /**
     * Valida as credenciais fornecidas contra as credenciais do objeto.
     *
     * @param senhaDigitada   A senha a ser verificada.
     * @return {@code true} se as credenciais forem válidas e a autenticação for bem-sucedida.
     * @throws Exception Lançada se a autenticação falhar, geralmente uma
     * {@link excecoes.AutenticacaoException} contendo o motivo da falha.
     */
    boolean autenticar(String senhaDigitada) throws Exception;
}
