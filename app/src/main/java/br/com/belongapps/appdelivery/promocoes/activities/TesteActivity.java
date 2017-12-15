package br.com.belongapps.appdelivery.promocoes.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import br.com.belongapps.appdelivery.R;
import br.com.belongapps.appdelivery.util.DataUtil;

public class TesteActivity extends AppCompatActivity {

    private TextView mostra_data, mostra_hora, mostra_dia_semana;
    private Button btAtualizar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teste);

        initView();

        btAtualizar = (Button) findViewById(R.id.bt_atualizar);
        btAtualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initView();
            }
        });

    }

    private void initView(){
        String data = DataUtil.formatar(DataUtil.getCurrenteDate(), "dd/MM/yyyy");
        String hora = DataUtil.formatar(DataUtil.getCurrenteDate(), "HH:mm");
        String diaSemana = DataUtil.getDiadaSemana();

        mostra_data = (TextView) findViewById(R.id.mostra_data);
        mostra_data.setText(data);

        mostra_hora = (TextView) findViewById(R.id.mostra_hora);
        mostra_hora.setText(hora);

        mostra_dia_semana = (TextView) findViewById(R.id.mostra_dia_semana);
        mostra_dia_semana.setText(diaSemana);

    }
}
