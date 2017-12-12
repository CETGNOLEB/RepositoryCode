package br.com.belongapps.appdelivery.cardapioOnline.model;


import java.util.Comparator;
import java.util.Locale;

public class FormadePagamento implements Comparator{

    private int cod;
    private String nome; //Ex: Dinheiro
    private Double valorDinheiro;
    private Double valorCartao;
    private String descricao; //EX: Troco: R$ 10,00
    private int imagem;
    private boolean status; //Ativado

    public FormadePagamento(int cod, String nome, Double valorDinheiro, Double valorCartao, String descricao, int imagem, boolean status) {
        this.cod = cod;
        this.nome = nome;
        this.valorDinheiro = valorDinheiro;
        this.valorCartao = valorCartao;
        this.descricao = descricao;
        this.imagem = imagem;
        this.status = status;
    }

    public FormadePagamento(){

    }

    public int getCod() {
        return cod;
    }

    public void setCod(int cod) {
        this.cod = cod;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Double getValorCartao() {
        return valorCartao;
    }

    public void setValorCartao(Double valorCartao) {
        this.valorCartao = valorCartao;
    }

    public Double getValorDinheiro() {
        return valorDinheiro;
    }

    public void setValorDinheiro(Double valorDinheiro) {
        this.valorDinheiro = valorDinheiro;
    }

    public String getDescricao() {
        String descricao = "";

        if (valorDinheiro != null){
            descricao += "Troco para R$ " + String.format(Locale.US, "%.2f", valorDinheiro).replace(".", ",");
        }

        if (valorCartao != null){
            descricao = "";
            descricao += "Cartão: R$ " + String.format(Locale.US, "%.2f", valorCartao).replace(".", ",");
            descricao += " Dinheiro: R$ " + String.format(Locale.US, "%.2f", valorDinheiro).replace(".", ",");
        }

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

    @Override
    public int compare(Object o, Object t1) {
        FormadePagamento f1 = (FormadePagamento) o;
        FormadePagamento f2 = (FormadePagamento) t1;
        return f1.cod < f2.cod ? -1 : (f1.cod > f2.cod ? +1 : 0);
    }
}
