package service;

import model.Atendente;
import model.Gerente;
import model.Usuario;
import repository.UsuarioRepository;

import excecoes.AcessoNegadoException;
import excecoes.AutenticacaoException;

import java.util.concurrent.ExecutionException;
import java.util.List;

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
     * @param senhaDigitada   A senha para verificação.
     * @return O objeto {@link Usuario} (podendo ser {@link Gerente} ou {@link Atendente})
     * autenticado.
     * @throws AutenticacaoException se o nome de usuário não for encontrado ou se a senha
     * estiver incorreta.
     */
    public Usuario autenticar(String usuario, String senhaDigitada) throws AutenticacaoException {
        try {
            Usuario usuarioDoBanco = repository.buscarPorUsuario(usuario).get();

            if (usuarioDoBanco != null) {
                if (!usuarioDoBanco.isAtivo()) {
                    throw new AutenticacaoException("ERRO: Usuário inativo.");
                }
                // Agora passamos a senha digitada para o método do objeto, que fará a verificação do hash
                usuarioDoBanco.autenticar(senhaDigitada);
                return usuarioDoBanco;
            } else {
                throw new AutenticacaoException("ERRO: Usuário ou senha inválidos.");
            }
        } catch (InterruptedException | ExecutionException e) {
            // O 'catch' que faltava. Ele captura erros de execução da busca no banco
            // ou a AutenticacaoException lançada pelo método do objeto Usuario.
            if (e.getCause() instanceof AutenticacaoException) {
                throw (AutenticacaoException) e.getCause();
            }
            // Se a causa não for AutenticacaoException, é um erro mais genérico.
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

        try {
            Usuario usuarioExistente = repository.buscarPorMatricula(novoAtendente.getMatricula()).get();
            if (usuarioExistente != null) {
                throw new IllegalArgumentException("ERRO: A matrícula " + novoAtendente.getMatricula() + " já está cadastrada.");
            }
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Erro ao verificar matrícula no banco de dados.", e);
        }

        repository.salvar(novoAtendente);
        System.out.println("LOG: Gerente '" + usuarioLogado.getNome() + "' cadastrou o atendente '" + novoAtendente.getNome() + "'.");
    }

    /**
     * Retorna uma lista com todos os usuários do banco de dados.
     * @return Uma Lista de Usuários.
     */
    public List<Usuario> buscarTodosUsuarios() {
        try {
            return repository.buscarTodos().get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("ERRO: Erro ao buscar todos os usuários.", e);
        }
    }

    /**
     * Altera o status (ativo/inativo) de um atendente.
     * @param gerenteLogado O gerente executando a ação.
     * @param matriculaAtendente A matrícula do atendente a ser alterado.
     * @param novoStatus O novo status (true para ativar, false para inativar).
     * @throws AcessoNegadoException se o executor não for gerente.
     * @throws IllegalArgumentException se o atendente não for encontrado ou se um gerente tentar se auto-inativar.
     */
    public void alterarStatusAtendente(Usuario gerenteLogado, int matriculaAtendente, boolean novoStatus) throws AcessoNegadoException {
        if (!(gerenteLogado instanceof Gerente)) {
            throw new AcessoNegadoException("ERRO: Apenas gerentes podem alterar o status de atendentes!");
        }
        if (gerenteLogado.getMatricula() == matriculaAtendente) {
            throw new IllegalArgumentException("ERRO: Um gerente não pode inativar a si mesmo.");
        }

        try {
            Usuario atendente = repository.buscarPorMatricula(matriculaAtendente).get();
            if (atendente == null || atendente instanceof Gerente) {
                throw new IllegalArgumentException("ERRO: Atendente com a matrícula " + matriculaAtendente + " não encontrado.");
            }
            atendente.setAtivo(novoStatus);
            repository.salvar(atendente);
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Erro ao buscar atendente para inativar.", e);
        }
    }
}
