package model;

public class VendaDTO {
    private String id;
    private String nomeAtendente;
    private String usuarioAtendente;
    private String nomeProduto;
    private double precoProduto;
    private int quantidade;
    private String dataHora;

    public VendaDTO() {} // necessário para Firebase

    public VendaDTO(Venda venda) {
        this.id = venda.getId();
        this.nomeAtendente = venda.getAtendente().getNome();
        this.usuarioAtendente = venda.getAtendente().getUsuario();
        this.nomeProduto = venda.getProduto().getNome();
        this.precoProduto = venda.getProduto().getPreco();
        this.quantidade = venda.getQuantidade();
        this.dataHora = venda.getDataHora();
    }

    // Getters e setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getNomeAtendente() { return nomeAtendente; }
    public void setNomeAtendente(String nomeAtendente) { this.nomeAtendente = nomeAtendente; }
    public String getUsuarioAtendente() { return usuarioAtendente; }
    public void setUsuarioAtendente(String usuarioAtendente) { this.usuarioAtendente = usuarioAtendente; }
    public String getNomeProduto() { return nomeProduto; }
    public void setNomeProduto(String nomeProduto) { this.nomeProduto = nomeProduto; }
    public double getPrecoProduto() { return precoProduto; }
    public void setPrecoProduto(double precoProduto) { this.precoProduto = precoProduto; }
    public int getQuantidade() { return quantidade; }
    public void setQuantidade(int quantidade) { this.quantidade = quantidade; }
    public String getDataHora() { return dataHora; }
    public void setDataHora(String dataHora) { this.dataHora = dataHora; }
}
