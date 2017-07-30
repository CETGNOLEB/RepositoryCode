package br.com.belongapps.appdelivery.gerencial.model;

public class Bairro {
    private String bairro;
    private double taxa;

    public Bairro(String bairro, double taxa) {
        this.bairro = bairro;
        this.taxa = taxa;
    }

    public Bairro() {
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public double getTaxa() {
        return taxa;
    }

    public void setTaxa(double taxa) {
        this.taxa = taxa;
    }
}
