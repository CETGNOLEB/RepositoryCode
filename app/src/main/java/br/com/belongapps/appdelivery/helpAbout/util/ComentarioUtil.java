package br.com.belongapps.appdelivery.helpAbout.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;

import br.com.belongapps.appdelivery.R;

public class ComentarioUtil {

    public static void exibirDialogEnviarComentarios(final Context context) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        AlertDialog.Builder mBilder = new AlertDialog.Builder(context, R.style.MyDialogTheme);
        View layoutDialog = inflater.inflate(R.layout.dialog_enviar_comentario, null);

        Button btFechar = (Button) layoutDialog.findViewById(R.id.bt_fechar_envio_comentario);

        RadioButton radioSugestao = (RadioButton) layoutDialog.findViewById(R.id.radio_sugestao);
        RadioButton radioProblema = (RadioButton) layoutDialog.findViewById(R.id.radio_problema);
        RadioButton radioOutro = (RadioButton) layoutDialog.findViewById(R.id.radio_outro);

        mBilder.setView(layoutDialog);
        final AlertDialog dialodDadosDoApp = mBilder.create();
        dialodDadosDoApp.show();

        btFechar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialodDadosDoApp.dismiss();
            }
        });

        //Ações tipo de comentário
        radioSugestao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialodDadosDoApp.dismiss();
                startIntentEnviarComentario(context, "SOGESTÃO");
            }
        });

        radioProblema.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialodDadosDoApp.dismiss();
                startIntentEnviarComentario(context, "PROBLEMA");
            }
        });

        radioOutro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialodDadosDoApp.dismiss();
                startIntentEnviarComentario(context, "COMENTARIO");
            }
        });

    }

    public static void startIntentEnviarComentario(Context context, String tipoDeComentario) {

        Intent intentEmail = new Intent(Intent.ACTION_SENDTO);
        String[] emails = {"belongtec.apps@gmail.com"};

        intentEmail.setData(Uri.parse("mailto:")); // only email apps should handle this
        intentEmail.putExtra(Intent.EXTRA_EMAIL, emails);
        intentEmail.putExtra(Intent.EXTRA_SUBJECT, "APP DELIVERY - " + tipoDeComentario);
        if (intentEmail.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intentEmail);
        }
    }

}
