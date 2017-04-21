package br.com.belongapps.meuacai.cardapioOnline.model;


public class Pagamento {
    private double valorTotal;
    private String formaPagamento;
    private double trocoPedido;
    private double valorDinheiro;
    private double valorCartao;

    public Pagamento(double valorTotal, String formaPagamento, double trocoPedido, double valorDinheiro, double valorCartao) {
        this.valorTotal = valorTotal;
        this.formaPagamento = formaPagamento;
        this.trocoPedido = trocoPedido;
        this.valorDinheiro = valorDinheiro;
        this.valorCartao = valorCartao;
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

    public double getTrocoPedido() {
        return trocoPedido;
    }

    public void setTrocoPedido(double trocoPedido) {
        this.trocoPedido = trocoPedido;
    }

    public double getValorDinheiro() {
        return valorDinheiro;
    }

    public void setValorDinheiro(double valorDinheiro) {
        this.valorDinheiro = valorDinheiro;
    }

    public double getValorCartao() {
        return valorCartao;
    }

    public void setValorCartao(double valorCartao) {
        this.valorCartao = valorCartao;
    }
}
