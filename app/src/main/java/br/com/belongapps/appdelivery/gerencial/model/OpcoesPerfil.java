package br.com.belongapps.appdelivery.gerencial.model;

public class OpcoesPerfil {
    private int icon;
    private String nomeOpcao;

    public OpcoesPerfil(int icon, String nomeOpcao) {
        this.icon = icon;
        this.nomeOpcao = nomeOpcao;
    }

    public OpcoesPerfil(){

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
