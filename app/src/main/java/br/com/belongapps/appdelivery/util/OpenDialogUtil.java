package br.com.belongapps.appdelivery.util;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import br.com.belongapps.appdelivery.R;

public class OpenDialogUtil {

    private static LayoutInflater inflater;
    private static AlertDialog.Builder mBilder;

    public static void openSimpleDialog(String title, String msg, Context context){

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mBilder = new AlertDialog.Builder(context, R.style.MyDialogTheme);

        View layoutDialog = inflater.inflate(R.layout.simple_dialog, null);
        Button closeDialog = layoutDialog.findViewById(R.id.bt_close_simple_dialog);
        mBilder.setView(layoutDialog);

        TextView titulo = layoutDialog.findViewById(R.id.title_simple_dialog);
        titulo.setText(title);
        TextView mensagem = layoutDialog.findViewById(R.id.msg_simple_dialog);
        mensagem.setText(msg);

        final AlertDialog simpleDialog = mBilder.create();
        simpleDialog.show();

        closeDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                simpleDialog.dismiss();
            }
        });
    }
}
