package model;
import interfaces.IAutenticacao;
import excecoes.*;


public class Atendente implements IAutenticacao {
    private int matricula;
    private String nome;
    private String usuario;
    private String senha;

    public Atendente(int matricula, String nome, String usuario, String senha) {
        this.matricula = matricula;
        this.nome = nome;
        this.usuario = usuario;
        this.senha = senha;
    }

    public int getMatricula() {
        return matricula;
    }

    public void setMatricula(int matricula) {
        this.matricula = matricula;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    @Override
    public boolean autenticar(String usuario, String senha) throws AutenticacaoException {
        if (!this.usuario.equals(usuario) || !this.senha.equals(senha)) {
            throw new AutenticacaoException("Usuário ou senha inválidos");
        }
        return true;
    }
}
