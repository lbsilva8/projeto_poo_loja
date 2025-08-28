package service;
import model.Atendente; // <-- Adicione esta linha
import excecoes.*;
public class AtendenteService {
    // private AtendenteRepository repository = new AtendenteRepository();

    public Atendente autenticar(String usuario, String senha) throws AutenticacaoException {
        // Lógica:
        // 1. Chamar o repository para buscar um atendente pelo 'usuario'.
        // Atendente atendenteDoBanco = repository.buscarPorUsuario(usuario);
        // 2. Se não encontrar, lança AutenticacaoException.
        // 3. Se encontrar, compara a senha.
        // if (atendenteDoBanco != null && atendenteDoBanco.getSenha().equals(senha)) {
        //     return atendenteDoBanco;
        // } else {
        //     throw new AutenticacaoException("Usuário ou senha inválidos");
        // }
        // (Isso requer a criação do AtendenteRepository)
        return null; // Apenas para exemplo
    }
}
