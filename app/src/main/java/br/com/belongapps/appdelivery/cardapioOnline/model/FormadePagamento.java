package br.com.belongapps.appdelivery.cardapioOnline.model;


public class FormadePagamento {

    private String nome; //Ex: Dinheiro
    private String descricao; //EX: Troco: R$ 10,00
    private int imagem;
    private boolean status; //Ativado

    public FormadePagamento(String nome, String descricao, int imagem, boolean status) {
        this.nome = nome;
        this.descricao = descricao;
        this.imagem = imagem;
        this.status = status;
    }

    public FormadePagamento(){

    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public int getImagem() {
        return imagem;
    }

    public void setImagem(int imagem) {
        this.imagem = imagem;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
