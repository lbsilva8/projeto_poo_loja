package model;

public class VendaDTO {
    private String id;
    private int matriculaUsuario;
    private String idProduto;
    private int quantidade;
    private String dataHora;
    private String formaPagamento; // NOVO CAMPO (como String)
    private double valorTotal;

    public VendaDTO(Venda venda) {
        this.id = venda.getId();
        this.matriculaUsuario = venda.getUsuario().getMatricula();
        this.idProduto = venda.getProduto().getId();
        this.quantidade = venda.getQuantidade();
        this.dataHora = venda.getDataHora();
        this.formaPagamento = venda.getFormaPagamento().name();
        this.valorTotal = venda.getValorTotal(); // <-- Pegamos o valor já calculado
    }

    public VendaDTO() {}

    public double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getMatriculaUsuario() {
        return matriculaUsuario;
    }

    public void setMatriculaUsuario(int matriculaUsuario) {
        this.matriculaUsuario = matriculaUsuario;
    }

    public String getIdProduto() {
        return idProduto;
    }

    public void setIdProduto(String idProduto) {
        this.idProduto = idProduto;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public String getDataHora() {
        return dataHora;
    }

    public void setDataHora(String dataHora) {
        this.dataHora = dataHora;
    }

    public String getFormaPagamento() {
        return formaPagamento;
    }

    public void setFormaPagamento(String formaPagamento) {
        this.formaPagamento = formaPagamento;
    }
}