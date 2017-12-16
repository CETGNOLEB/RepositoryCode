package br.com.belongapps.appdelivery.seguranca.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Usuario implements Parcelable {

    private String nome;
    private String email;
    private String senha;
    private String celular;

    //DATA NASC
    private int ano = 1998;
    private int mes = 7;
    private int dia = 15;

    public Usuario(String nome, String email, String senha, String celular, int ano, int mes, int dia) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.celular = celular;
        this.ano = ano;
        this.mes = mes;
        this.dia = dia;
    }

    public Usuario() {

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nome);
        dest.writeString(email);
        dest.writeString(senha);
        dest.writeString(celular);
        dest.writeInt(ano);
        dest.writeInt(mes);
        dest.writeInt(dia);
    }

    private Usuario(Parcel in) {
        nome = in.readString();
        email = in.readString();
        senha = in.readString();
        celular = in.readString();
        ano = in.readInt();
        mes = in.readInt();
        dia = in.readInt();
    }

    public static final Parcelable.Creator<Usuario> CREATOR
            = new Parcelable.Creator<Usuario>() {
        public Usuario createFromParcel(Parcel in) {
            return new Usuario(in);
        }

        public Usuario[] newArray(int size) {
            return new Usuario[size];
        }
    };

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }


    public int getAno() {
        return ano;
    }

    public void setAno(int ano) {
        this.ano = ano;
    }

    public int getMes() {
        return mes;
    }

    public void setMes(int mes) {
        this.mes = mes;
    }

    public int getDia() {
        return dia;
    }

    public void setDia(int dia) {
        this.dia = dia;
    }
}
