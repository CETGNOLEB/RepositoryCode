package br.com.belongapps.meuacai.promocoes.model;


public class Promocao {

    private String ref_img;
    private String status;

    public Promocao( String ref_img, String status) {
        this.ref_img = ref_img;
        this.status = status;
    }

    public Promocao(){

    }

    public String getRef_img() {
        return ref_img;
    }

    public void setRef_img(String ref_img) {
        this.ref_img = ref_img;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
