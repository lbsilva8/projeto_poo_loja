package loja;

public class Cliente extends Pessoa {
     private String email;

    public Cliente(int id, String nome, String cpf, String endereco, String telefone, String email) {
        super(id, nome, cpf, endereco, telefone); 
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
