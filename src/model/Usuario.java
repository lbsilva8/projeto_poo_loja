package model;

import interfaces.IAutenticacao;
import excecoes.AutenticacaoException;

/**
 * Classe base abstrata que define a estrutura e o comportamento comum para todos
 * os tipos de usuários no sistema.
 * Esta classe implementa a interface {@link IAutenticacao}, fornecendo uma lógica
 * de autenticação padrão que é herdada por todas as suas subclasses.
 *
 * @see Atendente
 * @see Gerente
 * @see IAutenticacao
 */
public abstract class Usuario implements IAutenticacao {
    private int matricula;
    private String nome;
    private String usuario;
    private String senha;
    private String cargo;

    /**
     * Construtor para ser utilizado pelas subclasses para inicializar os atributos comuns.
     *
     * @param matricula O número de matrícula do usuário.
     * @param nome      O nome completo do usuário.
     * @param usuario   O nome de usuário para login.
     * @param senha     A senha para login.
     * @param cargo     O cargo ou papel do usuário no sistema (ex: "ATENDENTE").
     */
    public Usuario(int matricula, String nome, String usuario, String senha, String cargo) {
        this.matricula = matricula;
        this.nome = nome;
        this.usuario = usuario;
        this.senha = senha;
        this.cargo = cargo;
    }

    /**
     * Construtor sem argumentos, necessário para a desserialização de dados noFirebase.
     */
    public Usuario() {}

    /**
     * Implementação padrão do metodo de autenticação. Compara o usuário e a senha
     * fornecidos com os armazenados no objeto.
     *
     * @throws AutenticacaoException se o nome de usuário ou a senha não corresponderem.
     */
    @Override
    public boolean autenticar(String usuario, String senha) throws AutenticacaoException {
        if (!this.usuario.equals(usuario) || !this.senha.equals(senha)) {
            throw new AutenticacaoException("Usuário ou senha inválidos");
        }
        return true;
    }

    /**
     * Retorna a matrícula do usuário.
     * @return O número da matrícula.
     */
    public int getMatricula() {
        return matricula;
    }

    /**
     * Define a matrícula do usuário.
     * @param matricula O novo número de matrícula.
     */
    public void setMatricula(int matricula) {
        this.matricula = matricula;
    }

    /**
     * Retorna o nome completo do usuário.
     * @return O nome do usuário.
     */
    public String getNome() {
        return nome;
    }

    /**
     * Define o nome completo do usuário.
     * @param nome O novo nome completo.
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * Retorna o nome de usuário utilizado para login.
     * @return O nome de usuário.
     */
    public String getUsuario() {
        return usuario;
    }

    /**
     * Define o nome de usuário para login.
     * @param usuario O novo nome de usuário.
     */
    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    /**
     * Retorna a senha do usuário.
     * @return A senha.
     */
    public String getSenha() {
        return senha;
    }

    /**
     * Define a senha do usuário.
     * @param senha A nova senha.
     */
    public void setSenha(String senha) {
        this.senha = senha;
    }

    /**
     * Retorna o cargo (papel) do usuário no sistema.
     * @return O cargo do usuário (ex: "GERENTE").
     */
    public String getCargo() {
        return cargo;
    }

    /**
     * Define o cargo (papel) do usuário no sistema.
     * @param cargo O novo cargo do usuário.
     */
    public void setCargo(String cargo) {
        this.cargo = cargo;
    }
}