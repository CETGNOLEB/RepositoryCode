package br.com.belongapps.appdelivery.cardapioOnline.model;

public class Pao {

    private String categoria_id;
    private String nome;
    private int status_item;
    private double valor_unit;

    public Pao(String categoria_id, String nome, int status_item, double valor_unit) {
        this.categoria_id = categoria_id;
        this.nome = nome;
        this.status_item = status_item;
        this.valor_unit = valor_unit;
    }

    public Pao(){

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

    public int getStatus_item() {
        return status_item;
    }

    public void setStatus_item(int status_item) {
        this.status_item = status_item;
    }

    public double getValor_unit() {
        return valor_unit;
    }

    public void setValor_unit(double valor_unit) {
        this.valor_unit = valor_unit;
    }
}