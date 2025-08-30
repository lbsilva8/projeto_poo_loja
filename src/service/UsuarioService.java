package service;

import model.Atendente;
import model.Gerente;
import model.Usuario;
import repository.UsuarioRepository;

import excecoes.AcessoNegadoException;
import excecoes.AutenticacaoException;

import java.util.concurrent.ExecutionException;

/**
 * Classe de serviço que encapsula a lógica de negócio para autenticação e
 * gerenciamento de usuários.
 *  Ela utiliza o {@link UsuarioRepository} para interagir com a camada
 * de persistência de dados.
 *
 * @see Usuario
 * @see UsuarioRepository
 */
public class UsuarioService {
    private final UsuarioRepository repository = new UsuarioRepository();

    /**
     * Autentica um usuário com base em suas credenciais (login e senha).
     * O metodo primeiro busca o usuário pelo seu nome de login no repositório.
     * Se encontrado, ele delega a verificação da senha para o próprio objeto {@link Usuario}.
     *
     * @param usuario O nome de usuário (login) para autenticar.
     * @param senha   A senha para verificação.
     * @return O objeto {@link Usuario} (podendo ser {@link Gerente} ou {@link Atendente})
     * autenticado.
     * @throws AutenticacaoException se o nome de usuário não for encontrado ou se a senha
     * estiver incorreta.
     */
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

    /**
     * Cadastra um novo atendente no sistema, validando a permissão do usuário executor.
     * Este metodo contém uma regra de autorização crítica: apenas um usuário que seja
     * uma instância de {@link Gerente} pode executar esta operação com sucesso.
     *
     * @param usuarioLogado  O usuário que está tentando realizar a operação de cadastro.
     * @param novoAtendente  O objeto {@link Atendente} com os dados a serem salvos.
     * @throws AcessoNegadoException se o {@code usuarioLogado} não for um {@link Gerente}.
     */
    public void cadastrarAtendente(Usuario usuarioLogado, Atendente novoAtendente) throws AcessoNegadoException {

        if (!(usuarioLogado instanceof Gerente)) {
            throw new AcessoNegadoException("ERRO: Apenas gerentes podem cadastrar novos atendentes!");
        }

        repository.salvar(novoAtendente);
        System.out.println("LOG: Gerente '" + usuarioLogado.getNome() + "' cadastrou o atendente '" + novoAtendente.getNome() + "'.");
    }
}