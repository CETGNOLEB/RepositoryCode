package br.com.belongapps.meuacai.cardapioOnline.model;

public class Cliente {

    private String nomeCliente;
    private String ruaEndCliente;
    private String numeroEndCliente;
    private String bairroEndCliente;
    private String complementoEndCliente;

    public Cliente(String nomeCliente, String ruaEndCliente, String numeroEndCliente, String bairroEndCliente, String complementoEndCliente) {
        this.nomeCliente = nomeCliente;
        this.ruaEndCliente = ruaEndCliente;
        this.numeroEndCliente = numeroEndCliente;
        this.bairroEndCliente = bairroEndCliente;
        this.complementoEndCliente = complementoEndCliente;
    }

    public Cliente() {

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
}
