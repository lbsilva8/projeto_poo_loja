package model;

/**
 * Autoras:
 * Andreísy Neves Ferreira
 * Isabella Paranhos Meireles
 * Lorena da Silva Borges
 */


import interfaces.IAutenticacao;
import excecoes.AutenticacaoException;

import org.mindrot.jbcrypt.BCrypt;
import java.util.HashMap;
import java.util.Map;


/**
 * Classe base abstrata que define a estrutura e o comportamento comum para todos
 * os tipos de usuários no sistema.
 * Esta classe implementa a interface {@link IAutenticacao}, fornecendo uma lógica
 * de autenticação padrão que é herdada por todas as suas subclasses.
 *
 * @see Atendente
 * @see Gerente
 * @see IAutenticacao
 * @see Permissao
 */
public abstract class Usuario implements IAutenticacao {
    private int matricula;
    private String nome;
    private String usuario;
    private String senha;
    private String cargo;
    private Map<String, Boolean> permissoes = new HashMap<>();
    private boolean ativo = true;

    /**
     * Construtor para ser utilizado pelas subclasses para inicializar os atributos comuns.
     *
     * @param matricula O número de matrícula do usuário.
     * @param nome      O nome completo do usuário.
     * @param usuario   O nome de usuário para login.
     * @param senha     A senha (geralmente o hash vindo do Firebase).
     * @param cargo     O cargo ou papel do usuário no sistema (ex: "ATENDENTE").
     */
    public Usuario(int matricula, String nome, String usuario, String senha, String cargo) {
        this.matricula = matricula;
        this.nome = nome;
        this.usuario = usuario;
        this.senha = senha;
        this.cargo = cargo;
        this.ativo = true;
        definirPermissoesPadrao();
    }

    /**
     * Construtor sem argumentos, necessário para a desserialização de dados noFirebase.
     */
    public Usuario() {
        definirPermissoesPadrao();
    }

    /**
     * Implementação padrão do metodo de autenticação. Compara o usuário e a senha
     * fornecidos com os armazenados no objeto.
     *
     * @param senhaDigitada A senha em texto plano fornecida pelo usuário no momento do login.
     * @throws AutenticacaoException se o nome de usuário ou a senha não corresponderem.
     */
    @Override
    public boolean autenticar(String senhaDigitada) throws AutenticacaoException {
        if (!BCrypt.checkpw(senhaDigitada, this.getSenha())) {
            throw new AutenticacaoException("Usuário ou senha inválidos");
        }
        return true;
    }

    /**
     * Metodo auxiliar privado que define o conjunto de permissões base para um novo usuário,
     * equivalente às permissões de um Atendente.
     */
    private void definirPermissoesPadrao() {
        for (Permissao p : Permissao.values()) {
            permissoes.put(p.name(), false);
        }
        permissoes.put(Permissao.REALIZAR_VENDA.name(), true);
        permissoes.put(Permissao.VISUALIZAR_ESTOQUE.name(), true);
    }

    /**
     * Verifica se o usuário possui uma permissão específica.
     *
     * @param permissao O {@link Permissao} a ser verificado.
     * @return {@code true} se o usuário tiver a permissão, {@code false} caso contrário.
     */
    public boolean temPermissao(Permissao permissao) {
        return this.permissoes.getOrDefault(permissao.name(), false);
    }

    /**
     * Retorna o mapa de permissões do usuário.
     * A chave do mapa é o nome da permissão (ex: "GERENCIAR_USUARIOS") e o valor é um booleano.
     * @return O mapa de permissões.
     */
    public Map<String, Boolean> getPermissoes() {
        return permissoes;
    }

    /**
     * Define o mapa de permissões do usuário.
     * Usado para carregar dados do Firebase ou para atualizar as permissões de um usuário.
     * @param permissoes O novo mapa de permissões.
     */
    public void setPermissoes(Map<String, Boolean> permissoes) {
        this.permissoes = permissoes;
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
     * Metodo de negócio para definir ou alterar uma senha.
     * Recebe uma senha em texto plano e a armazena de forma criptografada.
     * @param senhaPlana A senha em texto plano a ser criptografada.
     */
    public void definirNovaSenha(String senhaPlana) {
        this.senha = BCrypt.hashpw(senhaPlana, BCrypt.gensalt());
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

    /**
     * Retorna status do usuário no sistema.
     * @return O status do usuário.
     */
    public boolean isAtivo() {
        return ativo;
    }

    /**
     * Define o status do usuário no sistema.
     * @param ativo O novo cargo do usuário.
     */
    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }
}