package model;

/**
 * Representa um Objeto de Transferência de Dados (DTO) para a entidade {@link Venda}.
 * Esta classe é projetada especificamente para a camada de persistência. Ela contém
 * uma representação  um objeto {@link Venda}, onde as
 * referências a objetos complexos como {@link Usuario} e {@link Produto} são substituídas
 * por seus respectivos identificadores únicos (matrícula e ID).
 *
 * @see Venda
 * @see repository.VendaRepository
 */
public class VendaDTO {
    private String id;
    private int matriculaUsuario;
    private String idProduto;
    private int quantidade;
    private String dataHora;
    private String formaPagamento; // NOVO CAMPO (como String)
    private double valorTotal;

    /**
     * Construtor de mapeamento que converte uma entidade {@link Venda} em um DTO.
     * Ele extrai os dados primitivos e os identificadores da entidade de domínio
     * para preparar o objeto para a serialização e persistência.
     *
     * @param venda A entidade de domínio {@link Venda} a ser convertida.
     */
    public VendaDTO(Venda venda) {
        this.id = venda.getId();
        this.matriculaUsuario = venda.getUsuario().getMatricula();
        this.idProduto = venda.getProduto().getId();
        this.quantidade = venda.getQuantidade();
        this.dataHora = venda.getDataHora();
        this.formaPagamento = venda.getFormaPagamento().name();
        this.valorTotal = venda.getValorTotal(); // <-- Pegamos o valor já calculado
    }

    /**
     * Construtor sem argumentos, necessário para a desserialização de dados noFirebase.
     */
    public VendaDTO() {}

    /**
     * Retorna o valor total da venda.
     * @return O valor total.
     */
    public double getValorTotal() {
        return valorTotal;
    }

    /**
     * Define o valor total da venda.
     * @param valorTotal O novo valor total.
     */
    public void setValorTotal(double valorTotal) {
        this.valorTotal = valorTotal;
    }

    /**
     * Retorna o ID único da venda.
     * @return O ID da venda.
     */
    public String getId() {
        return id;
    }

    /**
     * Define o ID único da venda.
     * @param id O novo ID da venda.
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Retorna a matrícula do usuário que realizou a venda.
     * @return A matrícula do usuário.
     */
    public int getMatriculaUsuario() {
        return matriculaUsuario;
    }

    /**
     * Define a matrícula do usuário.
     * @param matriculaUsuario A nova matrícula do usuário.
     */
    public void setMatriculaUsuario(int matriculaUsuario) {
        this.matriculaUsuario = matriculaUsuario;
    }

    /**
     * Retorna o ID do produto vendido.
     * @return O ID do produto.
     */
    public String getIdProduto() {
        return idProduto;
    }

    /**
     * Define o ID do produto.
     * @param idProduto O novo ID do produto.
     */
    public void setIdProduto(String idProduto) {
        this.idProduto = idProduto;
    }

    /**
     * Retorna a quantidade de itens vendidos.
     * @return A quantidade.
     */
    public int getQuantidade() {
        return quantidade;
    }

    /**
     * Define a quantidade de itens vendidos.
     * @param quantidade A nova quantidade.
     */
    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    /**
     * Retorna a data e hora da venda.
     * @return A string de data e hora.
     */
    public String getDataHora() {
        return dataHora;
    }

    /**
     * Define a data e hora da venda.
     * @param dataHora A nova string de data e hora.
     */
    public void setDataHora(String dataHora) {
        this.dataHora = dataHora;
    }

    /**
     * Retorna a forma de pagamento como uma String.
     * @return A forma de pagamento (ex: "PIX").
     */
    public String getFormaPagamento() {
        return formaPagamento;
    }

    /**
     * Define a forma de pagamento.
     * @param formaPagamento A nova forma de pagamento.
     */
    public void setFormaPagamento(String formaPagamento) {
        this.formaPagamento = formaPagamento;
    }
}