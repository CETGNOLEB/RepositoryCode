package br.com.belongapps.meuacai.cardapioOnline.model;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.Map;

public class ItemPedido implements Parcelable{

    private String nome;
    private String descricao;
    private String observacao;
    private String ref_img;
    private double valor_unit;
    private int quantidade;

    private double valor_total;

    //Para Pizza
    private String nomeMetade2;
    private String descMetade2;
    private String obsMetade2;
    private String imgMetade2;
    private double valorMetade2;

    public ItemPedido(String nome, String descricao, String observacao, String ref_img, double valor_unit, int quantidade, double valor_total, String nomeMetade2, String descMetade2, String obsMetade2, String imgMetade2, Long valorMetade2) {
        this.nome = nome;
        this.descricao = descricao;
        this.observacao = observacao;
        this.ref_img = ref_img;
        this.valor_unit = valor_unit;
        this.quantidade = quantidade;
        this.valor_total = valor_total;

        //----
        this.nomeMetade2 = nomeMetade2;
        this.descMetade2 = descMetade2;
        this.obsMetade2 = obsMetade2;
        this.imgMetade2 = imgMetade2;
        this.valorMetade2 = valorMetade2;
    }

    public ItemPedido(){

    }

    private ItemPedido(Parcel in) {
        nome = in.readString();
        descricao = in.readString();
        observacao = in.readString();
        ref_img = in.readString();

        valor_unit = in.readDouble();
        quantidade = in.readInt();

        valor_total = in.readDouble();

        //PARA PIZZAS

    }

    public static final Parcelable.Creator<ItemPedido> CREATOR
            = new Parcelable.Creator<ItemPedido>() {
        public ItemPedido createFromParcel(Parcel in) {
            return new ItemPedido(in);
        }

        public ItemPedido[] newArray(int size) {
            return new ItemPedido[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(nome);
        dest.writeString(descricao);
        dest.writeString(observacao);
        dest.writeString(ref_img);
        dest.writeDouble(valor_unit);

        dest.writeInt(quantidade);
        dest.writeDouble(valor_total);

        //PARA PIZZAS
        dest.writeString(nomeMetade2);
        dest.writeString(descMetade2);
        dest.writeString(obsMetade2);
        dest.writeString(imgMetade2);
        dest.writeDouble(valorMetade2);

    }

    /*public Map<String, Object> toMap() {

    }*/


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

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public String getRef_img() {
        return ref_img;
    }

    public void setRef_img(String ref_img) {
        this.ref_img = ref_img;
    }

    public double getValor_unit() {
        return valor_unit;
    }

    public void setValor_unit(double valor_unit) {
        this.valor_unit = valor_unit;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public double getValor_total() {
        return valor_total;
    }

    public void setValor_total(double valor_total) {
        this.valor_total = valor_total;
    }

    //PARA PIZZA

    public String getNomeMetade2() {
        return nomeMetade2;
    }

    public void setNomeMetade2(String nomeMetade2) {
        this.nomeMetade2 = nomeMetade2;
    }

    public String getDescMetade2() {
        return descMetade2;
    }

    public void setDescMetade2(String descMetade2) {
        this.descMetade2 = descMetade2;
    }

    public String getObsMetade2() {
        return obsMetade2;
    }

    public void setObsMetade2(String obsMetade2) {
        this.obsMetade2 = obsMetade2;
    }

    public String getImgMetade2() {
        return imgMetade2;
    }

    public void setImgMetade2(String imgMetade2) {
        this.imgMetade2 = imgMetade2;
    }

    public double getValorMetade2() {
        return valorMetade2;
    }

    public void setValorMetade2(double valorMetade2) {
        this.valorMetade2 = valorMetade2;
    }
}
