package br.com.belongapps.appdelivery.cardapioOnline.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ItemPizza implements Parcelable {

    private String categoria;

    private String tamanho;
    private Integer tipo;

    private String nomeMetade1;
    private String descMetade1;
    private String obsMetade1;
    private String imgMetade1;
    private double valorMetade1;

    private String keyItem1;

    private String nomeMetade2;
    private String descMetade2;
    private String obsMetade2;
    private String imgMetade2;
    private double valorMetade2;

    private String keyItem2;

    private String nomeMetade3;
    private String descMetade3;
    private String obsMetade3;
    private String imgMetade3;
    private double valorMetade3;

    private String keyItem3;

    private String nomeMetade4;
    private String descMetade4;
    private String obsMetade4;
    private String imgMetade4;
    private double valorMetade4;

    private String keyItem4;


    public ItemPizza(String categoria, String tamanho, Integer tipo,
                     String nomeMetade1, String descMetade1, String obsMetade1, String imgMetade1, double valorMetade1, String keyItem1,
                     String nomeMetade2, String descMetade2, String obsMetade2, String imgMetade2, double valorMetade2, String keyItem2,
                     String nomeMetade3, String descMetade3, String obsMetade3, String imgMetade3, double valorMetade3, String keyItem3,
                     String nomeMetade4, String descMetade4, String obsMetade4, String imgMetade4, double valorMetade4, String keyItem4) {

        this.categoria = categoria;
        this.tamanho = tamanho;
        this.tipo = tipo;

        this.nomeMetade1 = nomeMetade1;
        this.descMetade1 = descMetade1;
        this.obsMetade1 = obsMetade1;
        this.imgMetade1 = imgMetade1;
        this.valorMetade1 = valorMetade1;
        this.keyItem1 = keyItem1;
        this.nomeMetade2 = nomeMetade2;
        this.descMetade2 = descMetade2;
        this.obsMetade2 = obsMetade2;
        this.imgMetade2 = imgMetade2;
        this.valorMetade2 = valorMetade2;
        this.keyItem2 = keyItem2;
        this.nomeMetade3 = nomeMetade3;
        this.descMetade3 = descMetade3;
        this.obsMetade3 = obsMetade3;
        this.imgMetade3 = imgMetade3;
        this.valorMetade3 = valorMetade3;
        this.keyItem3 = keyItem3;
        this.nomeMetade4 = nomeMetade4;
        this.descMetade4 = descMetade4;
        this.obsMetade4 = obsMetade4;
        this.imgMetade4 = imgMetade4;
        this.valorMetade4 = valorMetade4;
        this.keyItem4 = keyItem4;
    }

    public ItemPizza(){

    }

    private ItemPizza(Parcel in) {

        tamanho = in.readString();
        tipo = in.readInt();

        categoria = in.readString();

        nomeMetade1 = in.readString();
        descMetade1 = in.readString();
        obsMetade1 = in.readString();
        imgMetade1 = in.readString();
        valorMetade1 = in.readDouble();

        keyItem1 = in.readString();

        nomeMetade2 = in.readString();
        descMetade2 = in.readString();
        obsMetade2 = in.readString();
        imgMetade2 = in.readString();
        valorMetade2 = in.readDouble();

        keyItem2 = in.readString();

        nomeMetade3 = in.readString();
        descMetade3 = in.readString();
        obsMetade3 = in.readString();
        imgMetade3 = in.readString();
        valorMetade3 = in.readDouble();

        keyItem3 = in.readString();

        nomeMetade4 = in.readString();
        descMetade4 = in.readString();
        obsMetade4 = in.readString();
        imgMetade4 = in.readString();
        valorMetade4 = in.readDouble();

        keyItem4 = in.readString();

    }

    public static final Parcelable.Creator<ItemPizza> CREATOR
            = new Parcelable.Creator<ItemPizza>() {
        public ItemPizza createFromParcel(Parcel in) {
            return new ItemPizza(in);
        }

        public ItemPizza[] newArray(int size) {
            return new ItemPizza[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(tamanho);
        dest.writeInt(tipo);
        dest.writeString(categoria);

        dest.writeString(nomeMetade1);
        dest.writeString(descMetade1);
        dest.writeString(obsMetade1);
        dest.writeString(imgMetade1);
        dest.writeDouble(valorMetade1);

        dest.writeString(keyItem1);

        dest.writeString(nomeMetade2);
        dest.writeString(descMetade2);
        dest.writeString(obsMetade2);
        dest.writeString(imgMetade2);
        dest.writeDouble(valorMetade2);

        dest.writeString(keyItem2);

        dest.writeString(nomeMetade3);
        dest.writeString(descMetade3);
        dest.writeString(obsMetade3);
        dest.writeString(imgMetade3);
        dest.writeDouble(valorMetade3);

        dest.writeString(keyItem3);

        dest.writeString(nomeMetade4);
        dest.writeString(descMetade4);
        dest.writeString(obsMetade4);
        dest.writeString(imgMetade4);
        dest.writeDouble(valorMetade4);

        dest.writeString(keyItem4);

    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getTamanho() {
        return tamanho;
    }

    public void setTamanho(String tamanho) {
        this.tamanho = tamanho;
    }

    public Integer getTipo() {
        return tipo;
    }

    public void setTipo(Integer tipo) {
        this.tipo = tipo;
    }

    public String getNomeMetade1() {
        return nomeMetade1;
    }

    public void setNomeMetade1(String nomeMetade1) {
        this.nomeMetade1 = nomeMetade1;
    }

    public String getDescMetade1() {
        return descMetade1;
    }

    public void setDescMetade1(String descMetade1) {
        this.descMetade1 = descMetade1;
    }

    public String getObsMetade1() {
        return obsMetade1;
    }

    public void setObsMetade1(String obsMetade1) {
        this.obsMetade1 = obsMetade1;
    }

    public String getImgMetade1() {
        return imgMetade1;
    }

    public void setImgMetade1(String imgMetade1) {
        this.imgMetade1 = imgMetade1;
    }

    public double getValorMetade1() {
        return valorMetade1;
    }

    public void setValorMetade1(double valorMetade1) {
        this.valorMetade1 = valorMetade1;
    }

    public String getKeyItem1() {
        return keyItem1;
    }

    public void setKeyItem1(String keyItem1) {
        this.keyItem1 = keyItem1;
    }

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

    public String getKeyItem2() {
        return keyItem2;
    }

    public void setKeyItem2(String keyItem2) {
        this.keyItem2 = keyItem2;
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

    public String getKeyItem3() {
        return keyItem3;
    }

    public void setKeyItem3(String keyItem3) {
        this.keyItem3 = keyItem3;
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

    public String getKeyItem4() {
        return keyItem4;
    }

    public void setKeyItem4(String keyItem4) {
        this.keyItem4 = keyItem4;
    }

    public static Creator<ItemPizza> getCREATOR() {
        return CREATOR;
    }
}
