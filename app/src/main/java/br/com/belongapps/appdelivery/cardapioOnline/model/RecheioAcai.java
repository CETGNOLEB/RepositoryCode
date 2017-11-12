package br.com.belongapps.appdelivery.cardapioOnline.model;

public class RecheioAcai {

    String categoria_id;
    String nome;
    String ref_img;
    Integer status_item;
    Integer tipo_recheio;
    Double valor_unit;

    Integer qtd;

    private String itemKey;

    public RecheioAcai(String categoria_id, String nome, String ref_img, Integer status_item,
                       Integer tipo_recheio, Double valor_unit, Integer qtd, String itemKey) {
        this.categoria_id = categoria_id;
        this.nome = nome;
        this.ref_img = ref_img;
        this.status_item = status_item;
        this.tipo_recheio = tipo_recheio;
        this.valor_unit = valor_unit;
        this.qtd = qtd;

        this.itemKey = itemKey;
    }

    public RecheioAcai(){}

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

    public Integer getStatus_item() {
        return status_item;
    }

    public void setStatus_item(Integer status_item) {
        this.status_item = status_item;
    }

    public Integer getTipo_recheio() {
        return tipo_recheio;
    }

    public void setTipo_recheio(Integer tipo_recheio) {
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
