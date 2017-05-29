package br.com.belongapps.appdelivery.cardapioOnline.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Cliente {

    private String nomeCliente;
    private String ruaEndCliente;
    private String numeroEndCliente;
    private String bairroEndCliente;
    private String complementoEndCliente;

    private List<KeyPedido> keypedidos;

    public Cliente(String nomeCliente, String ruaEndCliente, String numeroEndCliente, String bairroEndCliente, String complementoEndCliente, List<KeyPedido> keypedidos) {
        this.nomeCliente = nomeCliente;
        this.ruaEndCliente = ruaEndCliente;
        this.numeroEndCliente = numeroEndCliente;
        this.bairroEndCliente = bairroEndCliente;
        this.complementoEndCliente = complementoEndCliente;
        this.keypedidos = keypedidos;
    }

    public Cliente() {

    }

    public Map<String, Object> toMapPedidos() {
        HashMap<String, Object> result = new HashMap<>();
        /*Pedido*/
        result.put("pedidos", keypedidos);

        return result;
    }

    public String getNomeCliente() {
        return nomeCliente;
    }

    public void setNomeCliente(String nomeCliente) {
        this.nomeCliente = nomeCliente;
    }

    public String getRuaEndCliente() {
        return ruaEndCliente;
    }

    public void setRuaEndCliente(String ruaEndCliente) {
        this.ruaEndCliente = ruaEndCliente;
    }

    public String getNumeroEndCliente() {
        return numeroEndCliente;
    }

    public void setNumeroEndCliente(String numeroEndCliente) {
        this.numeroEndCliente = numeroEndCliente;
    }

    public String getBairroEndCliente() {
        return bairroEndCliente;
    }

    public void setBairroEndCliente(String bairroEndCliente) {
        this.bairroEndCliente = bairroEndCliente;
    }

    public String getComplementoEndCliente() {
        return complementoEndCliente;
    }

    public void setComplementoEndCliente(String complementoEndCliente) {
        this.complementoEndCliente = complementoEndCliente;
    }

    public List<KeyPedido> getKeypedidos() {
        return keypedidos;
    }

    public void setKeypedidos(List<KeyPedido> keypedidos) {
        this.keypedidos = keypedidos;
    }
}
