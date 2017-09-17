package br.com.belongapps.appdelivery.posPedido;

import android.support.annotation.NonNull;

import br.com.belongapps.appdelivery.cardapioOnline.model.Pedido;

public class PedidoKey implements Comparable{
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


    @Override
    public int compareTo(@NonNull Object o) {
        PedidoKey pedidokey = (PedidoKey) o;

        if (Integer.parseInt(this.pedido.getNumero_pedido()) > Integer.parseInt(pedidokey.pedido.getNumero_pedido())) {
            return -1;
        }
        if (Integer.parseInt(this.pedido.getNumero_pedido()) < Integer.parseInt(pedidokey.pedido.getNumero_pedido())) {
            return 1;
        }

        return 0;
    }
}
