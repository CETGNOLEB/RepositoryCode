package br.com.belongapps.appdelivery.helpAbout.model;

public class OpcoesSobre {
    private int icon;
    private String nomeOpcao;

    public OpcoesSobre(){

    }

    public OpcoesSobre(int icon, String nomeOpcao) {
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
