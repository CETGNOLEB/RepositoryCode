package br.com.belongapps.appdelivery.cardapioOnline.model;


public class Pagamento {

    private double valorTotal;
    private String formaPagamento;
    private String descricaoPagemento;
    private double valorPago;

    public Pagamento(double valorTotal, String formaPagamento, String descricaoPagamento, double valorPago) {
        this.valorTotal = valorTotal;
        this.formaPagamento = formaPagamento;
        this.descricaoPagemento = descricaoPagamento;
        this.valorPago = valorPago;
    }

    public Pagamento(){

    }

    public double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public String getFormaPagamento() {
        return formaPagamento;
    }

    public void setFormaPagamento(String formaPagamento) {
        this.formaPagamento = formaPagamento;
    }

    public String getDescricaoPagemento() {
        return descricaoPagemento;
    }

    public void setDescricaoPagemento(String descricaoPagemento) {
        this.descricaoPagemento = descricaoPagemento;
    }

    public double getValorPago() {
        return valorPago;
    }

    public void setValorPago(double valorPago) {
        this.valorPago = valorPago;
    }
}
