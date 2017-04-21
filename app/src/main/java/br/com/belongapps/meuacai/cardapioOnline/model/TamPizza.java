package br.com.belongapps.meuacai.cardapioOnline.model;


public class TamPizza {

    private String nome;
    private String ref_img;
    private long apartir_de;


    public TamPizza(String nome, String ref_img, long apartir_de) {
        this.nome = nome;
        this.ref_img = ref_img;
        this.apartir_de = apartir_de;
    }

    public TamPizza(){

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

    public long getApartir_de() {
        return apartir_de;
    }

    public void setApartir_de(long apartir_de) {
        this.apartir_de = apartir_de;
    }
}
