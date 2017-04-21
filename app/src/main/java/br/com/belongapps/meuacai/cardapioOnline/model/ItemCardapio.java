package br.com.belongapps.meuacai.cardapioOnline.model;


public class ItemCardapio{

    private String nome;
    private String descricao;
    private String ref_img;
    private double valor_unit;
    private long qtd_recheios;
    private String status_item;

    private String categoria_id;


    public ItemCardapio(String nome, String descricao, String ref_img, double valor_unit, long qtd_recheios, String status_item, String categoria_id) {
        this.nome = nome;
        this.descricao = descricao;
        this.ref_img = ref_img;
        this.valor_unit = valor_unit;
        this.qtd_recheios = qtd_recheios;
        this.status_item = status_item;
        this.categoria_id = categoria_id;
    }

    public ItemCardapio(){

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

    public String getRef_img() {
        return ref_img;
    }

    public void setRef_img(String ref_img) {
        this.ref_img = ref_img;
    }

    public long getQtd_recheios() {
        return qtd_recheios;
    }

    public void setQtd_recheios(long qtd_recheios) {
        this.qtd_recheios = qtd_recheios;
    }

    public double getValor_unit() {
        return valor_unit;
    }

    public void setValor_unit(double valor_unit) {
        this.valor_unit = valor_unit;
    }

    public String getStatus_item() {
        return status_item;
    }

    public void setStatus_item(String status_item) {
        this.status_item = status_item;
    }

    public String getCategoria_id() {
        return categoria_id;
    }

    public void setCategoria_id(String categoria_id) {
        this.categoria_id = categoria_id;
    }


}
