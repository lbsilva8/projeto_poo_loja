package model;

public class Gerente extends Usuario {

    public Gerente(int matricula, String nome, String usuario, String senha) {
        super(matricula, nome, usuario, senha, "GERENTE");
    }

    public Gerente() {
        super();
        setCargo("GERENTE");
    }
}