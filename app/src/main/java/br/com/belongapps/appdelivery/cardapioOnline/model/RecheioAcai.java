package br.com.belongapps.appdelivery.cardapioOnline.model;

import android.os.Parcel;
import android.os.Parcelable;

public class RecheioAcai implements Parcelable {

    String categoria_id;
    String nome;
    String ref_img;
    int status_item;
    int tipo_recheio;
    Double valor_unit;

    Integer qtd;

    private String itemKey;

    public RecheioAcai(String categoria_id, String nome, String ref_img, int status_item,
                       int tipo_recheio, Double valor_unit, Integer qtd, String itemKey) {
        this.categoria_id = categoria_id;
        this.nome = nome;
        this.ref_img = ref_img;
        this.status_item = status_item;
        this.tipo_recheio = tipo_recheio;
        this.valor_unit = valor_unit;
        this.qtd = qtd;

        this.itemKey = itemKey;
    }

    public RecheioAcai() {

    }

    private RecheioAcai(Parcel in) {
        categoria_id = in.readString();
        nome = in.readString();
        ref_img = in.readString();
        status_item = in.readInt();
        tipo_recheio = in.readInt();
        valor_unit = in.readDouble();
        qtd = in.readInt();
        itemKey = in.readString();
    }

    public static final Parcelable.Creator<RecheioAcai> CREATOR
            = new Parcelable.Creator<RecheioAcai>() {
        public RecheioAcai createFromParcel(Parcel in) {
            return new RecheioAcai(in);
        }

        public RecheioAcai[] newArray(int size) {
            return new RecheioAcai[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(categoria_id);
        dest.writeString(nome);
        dest.writeString(ref_img);
        dest.writeInt(status_item);
        dest.writeInt(tipo_recheio);
        dest.writeDouble(valor_unit);
        dest.writeInt(qtd);
        dest.writeString(itemKey);
    }

    public String getCategoria_id() {
        return categoria_id;
    }

    public void setCategoria_id(String categoria_id) {
        this.categoria_id = categoria_id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getRef_img() {
        return ref_img;
    }

    public void setRef_img(String ref_img) {
        this.ref_img = ref_img;
    }

    public int getStatus_item() {
        return status_item;
    }

    public void setStatus_item(int status_item) {
        this.status_item = status_item;
    }

    public int getTipo_recheio() {
        return tipo_recheio;
    }

    public void setTipo_recheio(int tipo_recheio) {
        this.tipo_recheio = tipo_recheio;
    }

    public Double getValor_unit() {
        return valor_unit;
    }

    public void setValor_unit(Double valor_unit) {
        this.valor_unit = valor_unit;
    }

    public Integer getQtd() {
        return qtd;
    }

    public void setQtd(Integer qtd) {
        this.qtd = qtd;
    }

    public String getItemKey() {
        return itemKey;
    }

    public void setItemKey(String itemKey) {
        this.itemKey = itemKey;
    }
}
