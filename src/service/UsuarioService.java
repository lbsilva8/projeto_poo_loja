package service;

import model.Atendente;
import model.Gerente;
import model.Usuario;
import repository.UsuarioRepository;

import excecoes.AcessoNegadoException;
import excecoes.AutenticacaoException;

import java.util.concurrent.ExecutionException;

public class UsuarioService {
    private final UsuarioRepository repository = new UsuarioRepository();

    public Usuario autenticar(String usuario, String senha) throws AutenticacaoException {
        try {
            Usuario usuarioDoBanco = repository.buscarPorUsuario(usuario).get(); // Espera o resultado

            if (usuarioDoBanco != null) {
                usuarioDoBanco.autenticar(usuario, senha);
                return usuarioDoBanco;
            } else {
                throw new AutenticacaoException("ERRO: Usuário ou senha inválidos.");
            }
        } catch (InterruptedException | ExecutionException e) {
            throw new AutenticacaoException("ERRO: Usuário ou senha inválidos.");
        }
    }

    public void cadastrarAtendente(Usuario usuarioLogado, Atendente novoAtendente) throws AcessoNegadoException {

        if (!(usuarioLogado instanceof Gerente)) {
            throw new AcessoNegadoException("ERRO: Apenas gerentes podem cadastrar novos atendentes!");
        }

        repository.salvar(novoAtendente);
        System.out.println("LOG: Gerente '" + usuarioLogado.getNome() + "' cadastrou o atendente '" + novoAtendente.getNome() + "'.");
    }
}