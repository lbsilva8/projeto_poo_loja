package model;

/**
 * Autoras:
 * Andreísy Neves Ferreira
 * Isabella Paranhos Meireles
 * Lorena da Silva Borges
 */

/**
 * Representa um conjunto fixo de constantes para as formas de pagamento aceitas pelo sistema.
 * O uso de um Enum para esta finalidade garante a segurança de tipos (type safety),
 * evitando o uso de "magic strings" e prevenindo erros de digitação ao longo do código.
 *
 * @see Venda
 * @see service.VendaService
 */
public enum FormaPagamento {
    DINHEIRO("Dinheiro"),
    PIX("PIX"),
    CARTAO_DEBITO("Cartão de Débito"),
    CARTAO_CREDITO("Cartão de Crédito");

    private final String descricao;

    /**
     * Construtor privado para associar uma descrição a cada constante do enum.
     *
     * @param descricao A descrição amigável da forma de pagamento.
     */
    FormaPagamento(String descricao) {
        this.descricao = descricao;
    }

    /**
     * Retorna a descrição amigável da forma de pagamento.
     *
     * @return Uma {@code String} contendo o texto para exibição (ex: "Cartão de Crédito").
     */
    public String getDescricao() {
        return descricao;
    }
}