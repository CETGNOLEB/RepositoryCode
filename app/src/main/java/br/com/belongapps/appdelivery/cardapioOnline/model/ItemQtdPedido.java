package br.com.belongapps.appdelivery.cardapioOnline.model;

public class ItemQtdPedido {
    String keycategoria;
    String keyItem;
    int qtdPedido;
    int quantidade; //Quantidade pedida pelo cliente

    public ItemQtdPedido(String keycategoria, String keyItem, int qtdPedido, int quantidade) {
        this.keycategoria = keycategoria;
        this.keyItem = keyItem;
        this.qtdPedido = qtdPedido;
        this.quantidade = quantidade;
    }

    public String getKeycategoria() {
        return keycategoria;
    }

    public void setKeycategoria(String keycategoria) {
        this.keycategoria = keycategoria;
    }

    public String getKeyItem() {
        return keyItem;
    }

    public void setKeyItem(String keyItem) {
        this.keyItem = keyItem;
    }

    public int getQtdPedido() {
        return qtdPedido;
    }

    public void setQtdPedido(int qtdPedido) {
        this.qtdPedido = qtdPedido;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }
}
