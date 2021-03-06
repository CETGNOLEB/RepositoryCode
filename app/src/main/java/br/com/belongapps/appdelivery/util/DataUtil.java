package br.com.belongapps.appdelivery.util;

import android.content.ContentResolver;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

public class DataUtil {

    static Locale locale = new Locale("pt", "BR");

    public static String formatar(Date data, String formato) {
        SimpleDateFormat format = new SimpleDateFormat(formato, locale);
        format.setTimeZone(TimeZone.getTimeZone("America/Fortaleza"));
        return format.format(data);
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

    public static String getDiadaSemana() {
        Calendar c = Calendar.getInstance(TimeZone.getTimeZone("America/Sao_Paulo"));

        String nomedodia = "";
        int dia = c.get(c.DAY_OF_WEEK);
        switch (dia) {
            case Calendar.SUNDAY:
                nomedodia = "domingo";
                break;
            case Calendar.MONDAY:
                nomedodia = "segunda";
                break;
            case Calendar.TUESDAY:
                nomedodia = "terca";
                break;
            case Calendar.WEDNESDAY:
                nomedodia = "quarta";
                break;
            case Calendar.THURSDAY:
                nomedodia = "quinta";
                break;
            case Calendar.FRIDAY:
                nomedodia = "sexta";
                break;
            case Calendar.SATURDAY:
                nomedodia = "sabado";
                break;
        }

        Log.println(Log.ERROR, "DIA DA SEMANA:", nomedodia);

        return nomedodia;
    }

    public static boolean horaAutomaticaAtivada(ContentResolver contentResolver) {
        int ativado;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            ativado = Settings.Global.getInt(contentResolver, Settings.Global.AUTO_TIME, 0);
        } else {
            ativado = Settings.System.getInt(contentResolver, Settings.System.AUTO_TIME, 0);
        }

        if (ativado == 1) {
            return true;
        }

        return false;
    }
}
