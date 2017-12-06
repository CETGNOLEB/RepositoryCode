package br.com.belongapps.appdelivery.cardapioOnline.model;


public class ItemPizzaDTO {

    private String categoria_id;
    private String descricao;
    private String nome;
    private Integer permite_entrega;
    private Double promo_pizza_g;
    private Double promo_pizza_m;
    private Double promo_pizza_p;
    private String ref_img;
    private Integer status_item;
    private boolean status_promocao;
    private Double valor_pizza_g;
    private Double valor_pizza_m;
    private Double valor_pizza_p;

    private boolean isChecked;

    public ItemPizzaDTO(String categoria_id, String descricao, String nome, Integer permite_entrega, Double promo_pizza_g, Double promo_pizza_m, Double promo_pizza_p, String ref_img, Integer status_item, boolean status_promocao, Double valor_pizza_g, Double valor_pizza_m, Double valor_pizza_p, boolean isChecked) {

        this.categoria_id = categoria_id;
        this.descricao = descricao;
        this.nome = nome;
        this.permite_entrega = permite_entrega;
        this.promo_pizza_g = promo_pizza_g;
        this.promo_pizza_m = promo_pizza_m;
        this.promo_pizza_p = promo_pizza_p;
        this.ref_img = ref_img;
        this.status_item = status_item;
        this.status_promocao = status_promocao;
        this.valor_pizza_g = valor_pizza_g;
        this.valor_pizza_m = valor_pizza_m;
        this.valor_pizza_p = valor_pizza_p;

        this.isChecked = isChecked;
    }

    public ItemPizzaDTO() {
    }

    public String getCategoria_id() {
        return categoria_id;
    }

    public void setCategoria_id(String categoria_id) {
        this.categoria_id = categoria_id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Integer getPermite_entrega() {
        return permite_entrega;
    }

    public void setPermite_entrega(Integer permite_entrega) {
        this.permite_entrega = permite_entrega;
    }

    public Double getPromo_pizza_g() {
        return promo_pizza_g;
    }

    public void setPromo_pizza_g(Double promo_pizza_g) {
        this.promo_pizza_g = promo_pizza_g;
    }

    public Double getPromo_pizza_m() {
        return promo_pizza_m;
    }

    public void setPromo_pizza_m(Double promo_pizza_m) {
        this.promo_pizza_m = promo_pizza_m;
    }

    public Double getPromo_pizza_p() {
        return promo_pizza_p;
    }

    public void setPromo_pizza_p(Double promo_pizza_p) {
        this.promo_pizza_p = promo_pizza_p;
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

    public boolean isStatus_promocao() {
        return status_promocao;
    }

    public void setStatus_promocao(boolean status_promocao) {
        this.status_promocao = status_promocao;
    }

    public Double getValor_pizza_g() {
        return valor_pizza_g;
    }

    public void setValor_pizza_g(Double valor_pizza_g) {
        this.valor_pizza_g = valor_pizza_g;
    }

    public Double getValor_pizza_m() {
        return valor_pizza_m;
    }

    public void setValor_pizza_m(Double valor_pizza_m) {
        this.valor_pizza_m = valor_pizza_m;
    }

    public Double getValor_pizza_p() {
        return valor_pizza_p;
    }

    public void setValor_pizza_p(Double valor_pizza_p) {
        this.valor_pizza_p = valor_pizza_p;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
