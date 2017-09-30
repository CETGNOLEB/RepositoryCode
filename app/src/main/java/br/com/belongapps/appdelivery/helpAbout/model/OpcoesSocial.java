package br.com.belongapps.appdelivery.helpAbout.model;

public class OpcoesSocial {
    private int icon;
    private String nomeOpcao;

    public  OpcoesSocial(){

    }

    public OpcoesSocial(int icon, String nomeOpcao) {
        this.icon = icon;
        this.nomeOpcao = nomeOpcao;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getNomeOpcao() {
        return nomeOpcao;
    }

    public void setNomeOpcao(String nomeOpcao) {
        this.nomeOpcao = nomeOpcao;
    }
}
