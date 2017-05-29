package br.com.belongapps.appdelivery.cardapioOnline.BO;

import android.util.Log;

import java.util.Date;

import br.com.belongapps.appdelivery.util.DataUtil;

public class PedidoBO {

    public static String gerarNumerodoPedido(String data) {
        String numero = "";

        Date datadeHoje = new Date();
        String dia = DataUtil.formatar(datadeHoje, "dd/mm/yyyy");


        Log.println(Log.ERROR, "RETORNO: ", numero);

        return numero;
    }
}