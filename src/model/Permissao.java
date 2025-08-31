package model;

/**
 * Autoras:
 * Andreísy Neves Ferreira
 * Isabella Paranhos Meireles
 * Lorena da Silva Borges
 */

/**
 * Esta classe é a fonte para o sistema de autorização, fornecendo uma
 * forma segura de representar as capacidades de um usuário.
 * @see Usuario#temPermissao(Permissao)
 */
public enum Permissao {
    /** Permissão para registrar novas vendas no sistema. */
    REALIZAR_VENDA("Realizar Vendas"),
    /** Permissão para apenas visualizar a lista de produtos e seus estoques. */
    VISUALIZAR_ESTOQUE("Visualizar Estoque"),
    /** Permissão para alterar o estoque (adicionar/remover) de produtos existentes. */
    GERENCIAR_ESTOQUE("Gerenciar Estoque (Adicionar/Remover)"),
    /** Permissão para cadastrar novos produtos no sistema. */
    CADASTRAR_PRODUTO("Cadastrar Produtos"),
    /** Permissão para gerenciar outros usuários (cadastrar, ativar/inativar, alterar permissões). */
    GERENCIAR_USUARIOS("Gerenciar Usuários (Cadastrar/Inativar)");

    private final String descricao;

    /**
     * Construtor privado para associar uma descrição a cada constante do enum.
     *
     * @param descricao A descrição amigável da permissão.
     */
    Permissao(String descricao) {
        this.descricao = descricao;
    }

    /**
     * Retorna a descrição amigável da permissão, ideal para ser exibida na interface do usuário.
     *
     * @return Uma {@code String} contendo o texto de exibição da permissão.
     */
    public String getDescricao() {
        return descricao;
    }
}