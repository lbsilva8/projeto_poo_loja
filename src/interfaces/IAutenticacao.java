package interfaces;

public interface IAutenticacao {
    boolean autenticar(String usuario, String senha) throws Exception;
}
