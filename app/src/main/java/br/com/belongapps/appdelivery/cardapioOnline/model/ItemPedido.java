package br.com.belongapps.appdelivery.cardapioOnline.model;


import android.os.Parcel;
import android.os.Parcelable;

public class ItemPedido implements Parcelable{

    private String nome;
    private String descricao;
    private String observacao;
    private String ref_img;
    private double valor_unit;
    private int quantidade;

    private double valor_total;

    //Para Pizzas
    private String nomeMetade2;
    private String descMetade2;
    private String obsMetade2;
    private String imgMetade2;
    private double valorMetade2;

    private String nomeMetade3;
    private String descMetade3;
    private String obsMetade3;
    private String imgMetade3;
    private double valorMetade3;

    private String nomeMetade4;
    private String descMetade4;
    private String obsMetade4;
    private String imgMetade4;
    private double valorMetade4;

    //referencia do item no banco
    private String categoria;
    private String keyItem;

    public ItemPedido(String nome, String descricao, String observacao, String ref_img, double valor_unit, int quantidade, double valor_total,
                      String nomeMetade2, String descMetade2, String obsMetade2, String imgMetade2, double valorMetade2,
                      String nomeMetade3, String descMetade3, String obsMetade3, String imgMetade3, double valorMetade3,
                      String nomeMetade4, String descMetade4, String obsMetade4, String imgMetade4, double valorMetade4,
                      String categoria, String keyItem) {

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

        this.nomeMetade3 = nomeMetade3;
        this.descMetade3 = descMetade3;
        this.obsMetade3 = obsMetade3;
        this.imgMetade3 = imgMetade3;
        this.valorMetade3 = valorMetade3;

        this.nomeMetade4 = nomeMetade4;
        this.descMetade4 = descMetade4;
        this.obsMetade4 = obsMetade4;
        this.imgMetade4 = imgMetade4;
        this.valorMetade4 = valorMetade4;

        this.keyItem = keyItem;
        this.categoria = categoria;
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
        nomeMetade2 = in.readString();
        descMetade2 = in.readString();
        obsMetade2 = in.readString();
        imgMetade2 = in.readString();
        valorMetade2 = in.readDouble();

        nomeMetade3 = in.readString();
        descMetade3 = in.readString();
        obsMetade3 = in.readString();
        imgMetade3 = in.readString();
        valorMetade3 = in.readDouble();

        nomeMetade4 = in.readString();
        descMetade4 = in.readString();
        obsMetade4 = in.readString();
        imgMetade4 = in.readString();
        valorMetade4 = in.readDouble();

        keyItem = in.readString();
        categoria = in.readString();
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

        dest.writeString(nomeMetade3);
        dest.writeString(descMetade3);
        dest.writeString(obsMetade3);
        dest.writeString(imgMetade3);
        dest.writeDouble(valorMetade3);

        dest.writeString(nomeMetade4);
        dest.writeString(descMetade4);
        dest.writeString(obsMetade4);
        dest.writeString(imgMetade4);
        dest.writeDouble(valorMetade4);

        dest.writeString(keyItem);
        dest.writeString(categoria);
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

    public String getNomeMetade3() {
        return nomeMetade3;
    }

    public void setNomeMetade3(String nomeMetade3) {
        this.nomeMetade3 = nomeMetade3;
    }

    public String getDescMetade3() {
        return descMetade3;
    }

    public void setDescMetade3(String descMetade3) {
        this.descMetade3 = descMetade3;
    }

    public String getObsMetade3() {
        return obsMetade3;
    }

    public void setObsMetade3(String obsMetade3) {
        this.obsMetade3 = obsMetade3;
    }

    public String getImgMetade3() {
        return imgMetade3;
    }

    public void setImgMetade3(String imgMetade3) {
        this.imgMetade3 = imgMetade3;
    }

    public double getValorMetade3() {
        return valorMetade3;
    }

    public void setValorMetade3(double valorMetade3) {
        this.valorMetade3 = valorMetade3;
    }

    public String getNomeMetade4() {
        return nomeMetade4;
    }

    public void setNomeMetade4(String nomeMetade4) {
        this.nomeMetade4 = nomeMetade4;
    }

    public String getDescMetade4() {
        return descMetade4;
    }

    public void setDescMetade4(String descMetade4) {
        this.descMetade4 = descMetade4;
    }

    public String getObsMetade4() {
        return obsMetade4;
    }

    public void setObsMetade4(String obsMetade4) {
        this.obsMetade4 = obsMetade4;
    }

    public String getImgMetade4() {
        return imgMetade4;
    }

    public void setImgMetade4(String imgMetade4) {
        this.imgMetade4 = imgMetade4;
    }

    public double getValorMetade4() {
        return valorMetade4;
    }

    public void setValorMetade4(double valorMetade4) {
        this.valorMetade4 = valorMetade4;
    }

    public String getKeyItem() {
        return keyItem;
    }

    public void setKeyItem(String keyItem) {
        this.keyItem = keyItem;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
}
