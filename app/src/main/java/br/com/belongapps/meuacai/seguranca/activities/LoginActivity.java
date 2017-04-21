package br.com.belongapps.meuacai.seguranca.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import br.com.belongapps.meuacai.R;
import br.com.belongapps.meuacai.promocoes.activities.TelaInicialActivity;

public class LoginActivity extends AppCompatActivity {

    Button btEntrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btEntrar = (Button) findViewById(R.id.button_entrar);

        btEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, TelaInicialActivity.class);
                startActivity(i);
                finish();
            }
        });
    }
}
