package br.com.belongapps.meuacai.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DataUtil {
    public static String formatar(Date data, String formato) {
        return new SimpleDateFormat(formato).format(data);
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
}
