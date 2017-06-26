package br.com.belongapps.appdelivery.cardapioOnline.model;


public class ItemCardapio {

    private String nome;
    private String descricao;
    private String ref_img;
    private double valor_unit;
    private double preco_promocional;
    private int qtd_recheios;
    private String status_item;
    private boolean status_promocao;

    private String categoria_id;

    //PARA PIZZAS
    private double promo_pizza_p;
    private double promo_pizza_m;
    private double promo_pizza_g;
    private double valor_pizza_p;
    private double valor_pizza_m;
    private double valor_pizza_g;

    /*PARA SANDUICHES*/
    private double promo_pao_arabe;
    private double promo_pao_bola;
    private double valor_pao_arabe;
    private double valor_pao_bola;

    public ItemCardapio(String nome, String descricao, String ref_img, double valor_unit, double preco_promocional, int qtd_recheios, String status_item, boolean status_promocao, String categoria_id,
                        double promo_pizza_p, double promo_pizza_m, double promo_pizza_g, double valor_pizza_p, double valor_pizza_m, double valor_pizza_g,
                        double promo_pao_arabe, double promo_pao_bola, double valor_pao_arabe, double valor_pao_bola) {

        this.nome = nome;
        this.descricao = descricao;
        this.ref_img = ref_img;
        this.valor_unit = valor_unit;
        this.preco_promocional = preco_promocional;
        this.qtd_recheios = qtd_recheios;
        this.status_item = status_item;
        this.status_promocao = status_promocao;
        this.categoria_id = categoria_id;

        //PARA PIZZAS
        this.promo_pizza_p = promo_pizza_p;
        this.promo_pizza_m = promo_pizza_m;
        this.promo_pizza_g = promo_pizza_g;
        this.valor_pizza_p = valor_pizza_p;
        this.valor_pizza_m = valor_pizza_m;
        this.valor_pizza_g = valor_pizza_g;

        //PARA SANDUICHES
        this.promo_pao_arabe = promo_pao_arabe;
        this.promo_pao_bola = promo_pao_bola;
        this.valor_pao_arabe = valor_pao_arabe;
        this.valor_pao_bola = valor_pao_bola;
    }

    public ItemCardapio() {

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

    public int getQtd_recheios() {
        return qtd_recheios;
    }

    public void setQtd_recheios(int qtd_recheios) {
        this.qtd_recheios = qtd_recheios;
    }

    public double getValor_unit() {
        return valor_unit;
    }

    public void setValor_unit(double valor_unit) {
        this.valor_unit = valor_unit;
    }

    public double getPreco_promocional() {
        return preco_promocional;
    }

    public void setPreco_promocional(double preco_promocional) {
        this.preco_promocional = preco_promocional;
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

    public boolean isStatus_promocao() {
        return status_promocao;
    }

    public void setStatus_promocao(boolean status_promocao) {
        this.status_promocao = status_promocao;
    }

    public double getPromo_pizza_p() {
        return promo_pizza_p;
    }

    public void setPromo_pizza_p(double promo_pizza_p) {
        this.promo_pizza_p = promo_pizza_p;
    }

    public double getPromo_pizza_m() {
        return promo_pizza_m;
    }

    public void setPromo_pizza_m(double promo_pizza_m) {
        this.promo_pizza_m = promo_pizza_m;
    }

    public double getPromo_pizza_g() {
        return promo_pizza_g;
    }

    public void setPromo_pizza_g(double promo_pizza_g) {
        this.promo_pizza_g = promo_pizza_g;
    }

    public double getValor_pizza_p() {
        return valor_pizza_p;
    }

    public void setValor_pizza_p(double valor_pizza_p) {
        this.valor_pizza_p = valor_pizza_p;
    }

    public double getValor_pizza_m() {
        return valor_pizza_m;
    }

    public void setValor_pizza_m(double valor_pizza_m) {
        this.valor_pizza_m = valor_pizza_m;
    }

    public double getValor_pizza_g() {
        return valor_pizza_g;
    }

    public void setValor_pizza_g(double valor_pizza_g) {
        this.valor_pizza_g = valor_pizza_g;
    }

    public double getPromo_pao_arabe() {
        return promo_pao_arabe;
    }

    public void setPromo_pao_arabe(double promo_pao_arabe) {
        this.promo_pao_arabe = promo_pao_arabe;
    }

    public double getPromo_pao_bola() {
        return promo_pao_bola;
    }

    public void setPromo_pao_bola(double promo_pao_bola) {
        this.promo_pao_bola = promo_pao_bola;
    }

    public double getValor_pao_arabe() {
        return valor_pao_arabe;
    }

    public void setValor_pao_arabe(double valor_pao_arabe) {
        this.valor_pao_arabe = valor_pao_arabe;
    }

    public double getValor_pao_bola() {
        return valor_pao_bola;
    }

    public void setValor_pao_bola(double valor_pao_bola) {
        this.valor_pao_bola = valor_pao_bola;
    }
}
