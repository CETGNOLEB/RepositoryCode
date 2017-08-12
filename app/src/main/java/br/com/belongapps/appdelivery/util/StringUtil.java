package br.com.belongapps.appdelivery.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class StringUtil {

    private static Locale brLocale = new Locale("pt", "BR");

    public static String formatToMoeda(Double valor) {
        return "R$ " + String.format(brLocale, "%.2f", valor).replace(".", ",");
    }

    public static Double formatMoedaToDouble(String textValor) {
        textValor = textValor.replace(",", ".").replace("R$" , "").trim();
        Double valor = Double.parseDouble(textValor);
        return valor;
    }

    public static String mesdoPedido(String data){
        return data.substring(2,8);
    }
}
