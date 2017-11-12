package br.com.belongapps.appdelivery.util;

import android.widget.EditText;

public class MoedaUtil {

    public static double somarValoresDeEditeTexts(EditText valor1, EditText valor2){
        Double v1 = StringUtil.formatMoedaToDouble(valor1.getText().toString());
        Double v2 = StringUtil.formatMoedaToDouble(valor2.getText().toString());
        return v1 + v2;
    }
}
