package model;

public class Atendente extends Usuario {

    public Atendente(int matricula, String nome, String usuario, String senha) {
        super(matricula, nome, usuario, senha, "ATENDENTE");
    }

    public Atendente() {
        super();
        setCargo("ATENDENTE");
    }
}