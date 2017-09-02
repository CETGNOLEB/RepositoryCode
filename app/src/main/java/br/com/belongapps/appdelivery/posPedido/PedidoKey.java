package br.com.belongapps.appdelivery.posPedido;

import br.com.belongapps.appdelivery.cardapioOnline.model.Pedido;

public class PedidoKey {
    private Pedido pedido;
    private String key;

    public PedidoKey(Pedido pedido, String key) {
        this.pedido = pedido;
        this.key = key;
    }

    public PedidoKey(){

    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
