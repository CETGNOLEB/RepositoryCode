package br.com.belongapps.appdelivery.gerencial.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;
import java.util.Map;

public class Endereco implements Parcelable{

    private String rua;
    private String numero;
    private String bairro;
    private String complemento;
    private String nome;

    public Endereco(String rua, String numero, String bairro, String complemento, String nome) {
        this.rua = rua;
        this.numero = numero;
        this.bairro = bairro;
        this.complemento = complemento;
        this.nome = nome;
    }

    public Endereco() {
    }

    public String getRua() {
        return rua;
    }

    public void setRua(String rua) {
        this.rua = rua;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    private Endereco(Parcel in) {
        rua = in.readString();
        numero = in.readString();
        bairro = in.readString();
        complemento = in.readString();
        nome = in.readString();
    }

    public static final Parcelable.Creator<Endereco> CREATOR
            = new Parcelable.Creator<Endereco>() {
        public Endereco createFromParcel(Parcel in) {
            return new Endereco(in);
        }

        public Endereco[] newArray(int size) {
            return new Endereco[size];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(rua);
        dest.writeString(numero);
        dest.writeString(bairro);
        dest.writeString(complemento);
        dest.writeString(nome);
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("rua", rua);
        result.put("numero", numero);
        result.put("bairro", bairro);
        result.put("complemento", complemento);
        result.put("nome", nome);

        return result;
    }


}
