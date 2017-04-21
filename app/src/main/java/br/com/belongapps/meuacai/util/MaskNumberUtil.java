package br.com.belongapps.meuacai.util;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

public class MaskNumberUtil implements TextWatcher {

    EditText editText;

    public MaskNumberUtil(EditText editText2) {
        this.editText = editText2;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        editText.removeTextChangedListener(this);

        try {

            BigDecimal parsed = parseToBigDecimal(s.toString(), Locale.US);
            String formatted = NumberFormat.getCurrencyInstance(Locale.US).format(parsed);

            //setting text after format to EditText
            editText.setText(formatted.replace(".", ","));
            editText.setSelection(editText.getText().length());
        } catch (NumberFormatException nfe) {
            nfe.printStackTrace();
        }

        editText.addTextChangedListener(this);
    }

    private BigDecimal parseToBigDecimal(String value, Locale locale) {
        String replaceable = String.format("[%s,.\\s]", NumberFormat.getCurrencyInstance(locale).getCurrency().getSymbol());

        String cleanString = value.replaceAll(replaceable, "");

        return new BigDecimal(cleanString).setScale(
                2, BigDecimal.ROUND_FLOOR).divide(new BigDecimal(100), BigDecimal.ROUND_FLOOR
        );
    }
}
