package br.com.belongapps.appdelivery.helpAbout.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.webkit.WebView;

import br.com.belongapps.appdelivery.R;
import br.com.belongapps.appdelivery.seguranca.activities.CadastrarUsuarioActivity;
import br.com.belongapps.appdelivery.seguranca.model.Usuario;

public class PoliticaPrivacidadeActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_politica_privacidade);

        mToolbar = (Toolbar) findViewById(R.id.toolbar_politica_privacidade);
        mToolbar.setTitle("Política de Privacidade");

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getParametros();

        initWebView();

    }

    private void getParametros() {
        usuario = getIntent().getParcelableExtra("usuario");
    }

    private void initWebView(){
        WebView wvPolitica = (WebView) findViewById(R.id.vw_politica_privacidade);
        wvPolitica.loadUrl("file:///android_asset/politica_privacidade.html");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent;
                if (usuario != null) {
                    intent = new Intent(PoliticaPrivacidadeActivity.this, CadastrarUsuarioActivity.class);
                    intent.putExtra("usuario", usuario);
                } else{
                    intent = new Intent(PoliticaPrivacidadeActivity.this, SobreActivity.class);
                }

                startActivity(intent);
                finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(PoliticaPrivacidadeActivity.this, CadastrarUsuarioActivity.class);
        intent.putExtra("usuario", usuario);
        startActivity(intent);
        finish();
    }


}
