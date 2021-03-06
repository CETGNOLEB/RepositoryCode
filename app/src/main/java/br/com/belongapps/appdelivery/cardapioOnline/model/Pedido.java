package br.com.belongapps.appdelivery.cardapioOnline.model;

import android.os.Parcel;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@IgnoreExtraProperties
public class Pedido{

    /*---PEDIDO---*/
    private String data;
    private String status_tempo;
    private String numero_pedido;
    private int status;
    private int entrega_retirada;
    private double valor_total;
    private List<ItemPedido> itens_pedido;

    /*---CLIENTE---*/
    Cliente cliente = new Cliente();

    /*---PAGAMENTO---*/
    Pagamento pagamento = new Pagamento();
    private Double taxa_entrega;

    /*CANCELAMENTO*/
    private String motivo_cancel;

    private String id_cliente;

    public Pedido(String data, String status_tempo, String numero_pedido, int status, int entrega_retirada, double valor_total, List<ItemPedido> itens_pedido, Cliente cliente, Pagamento pagamento, Double taxa_entrega, String motivo_cancel, String id_cliente) {
        this.data = data;
        this.status_tempo = status_tempo;
        this.numero_pedido = numero_pedido;
        this.status = status;
        this.entrega_retirada = entrega_retirada;
        this.valor_total = valor_total;
        this.itens_pedido = itens_pedido;
        this.cliente = cliente;
        this.pagamento = pagamento;
        this.taxa_entrega = taxa_entrega;
        this.motivo_cancel = motivo_cancel;
        this.id_cliente = id_cliente;
    }

    public Pedido() {
    }

    protected Pedido(Parcel in) {
        data = in.readString();
        status_tempo = in.readString();
        numero_pedido = in.readString();
        status = in.readInt();
        entrega_retirada = in.readInt();
        valor_total = in.readDouble();
        itens_pedido = in.createTypedArrayList(ItemPedido.CREATOR);

        motivo_cancel = in.readString();
        id_cliente = in.readString();
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        /*Pedido*/
        result.put("data", data);
        result.put("status_tempo", status_tempo);
        result.put("numero_pedido", numero_pedido);
        result.put("status", status);
        result.put("entrega_retirada", entrega_retirada);
        //Itens do Pedido
        result.put("itens_pedido", itens_pedido);

        //cliente
        result.put("id_cliente", id_cliente);

        result.put("nome_cliente", cliente.getNomeCliente());
        result.put("rua_end_cliente", cliente.getRuaEndCliente());
        result.put("numero_end_cliente", cliente.getNumeroEndCliente());
        result.put("bairro_end_cliente", cliente.getBairroEndCliente());
        result.put("complemento_end_cliente", cliente.getComplementoEndCliente());
        result.put("telefone_cliente", cliente.getCelular());

        //pagamento
        result.put("valor_total", pagamento.getValorTotal());
        result.put("forma_pagamento", pagamento.getFormaPagamento());
        result.put("descricao_pagamento", pagamento.getDescricaoPagemento());
        result.put("valor_pago", pagamento.getValorPago());
        result.put("taxa_entrega", taxa_entrega);

        return result;
    }


    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getStatus_tempo() {
        return status_tempo;
    }

    public void setStatus_tempo(String status_tempo) {
        this.status_tempo = status_tempo;
    }

    public String getNumero_pedido() {
        return numero_pedido;
    }

    public void setNumero_pedido(String numero_pedido) {
        this.numero_pedido = numero_pedido;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getEntrega_retirada() {
        return entrega_retirada;
    }

    public void setEntrega_retirada(int entrega_retirada) {
        this.entrega_retirada = entrega_retirada;
    }

    public double getValor_total() {
        return valor_total;
    }

    public void setValor_total(double valor_total) {
        this.valor_total = valor_total;
    }

    public List<ItemPedido> getItens_pedido() {
        return itens_pedido;
    }

    public void setItens_pedido(List<ItemPedido> itens_pedido) {
        this.itens_pedido = itens_pedido;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Pagamento getPagamento() {
        return pagamento;
    }

    public Double getTaxa_entrega() {
        return taxa_entrega;
    }

    public void setTaxa_entrega(Double taxa_entrega) {
        this.taxa_entrega = taxa_entrega;
    }

    public void setPagamento(Pagamento pagamento) {
        this.pagamento = pagamento;
    }

    public String getMotivo_cancel() {
        return motivo_cancel;
    }

    public void setMotivo_cancel(String motivo_cancel) {
        this.motivo_cancel = motivo_cancel;
    }

    public String getId_cliente() {
        return id_cliente;
    }

    public void setId_cliente(String id_cliente) {
        this.id_cliente = id_cliente;
    }
}
