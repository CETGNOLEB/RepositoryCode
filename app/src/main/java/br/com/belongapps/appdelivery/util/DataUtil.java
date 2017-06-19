package br.com.belongapps.appdelivery.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DataUtil {
    public static String formatar(Date data, String formato) {
        return new SimpleDateFormat(formato).format(data);
    }

    public static String formatarDiaMeseAnoDesc(Date data) {
        String dataFormatada = formatar(data, "dd") + " de " + formatar(data, "MM").toLowerCase() + " de " + formatar(data, "yyyy");
        return dataFormatada;
    }

    public static Date createDate(String data) {
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
        Date date = null;
        try {
            date = formato.parse(data);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;
    }

    public static Date getCurrenteDate() {

        Calendar c = Calendar.getInstance();
        Date data = c.getTime();

        return data;
    }

    public static  String getDataPedido(String data) {
        String retorno = data.substring(0, 10);
        return retorno;
    }

    public static  String getHoraPedido(String data) {
        String retorno = data.trim().substring(10, data.length());
        return retorno;
    }
}
