package br.com.belongapps.appdelivery.helpAbout.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import br.com.belongapps.appdelivery.R;
import br.com.belongapps.appdelivery.helpAbout.util.ComentarioUtil;

public class SobreAplicativoActivity extends AppCompatActivity {

    Toolbar mToolbar;

    //LINKS
    TextView linkComentar;
    TextView linkVisitarSite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sobre_aplicativo);

        mToolbar = (Toolbar) findViewById(R.id.toolbar_sobre_o_app);
        mToolbar.setTitle("Sobre o app");

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initViews();

    }

    private void initViews(){

        linkComentar = (TextView) findViewById(R.id.link_to_coment);
        linkVisitarSite = (TextView) findViewById(R.id.link_to_site);

        linkComentar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ComentarioUtil.exibirDialogEnviarComentarios(SobreAplicativoActivity.this);
            }
        });

        linkVisitarSite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SobreAplicativoActivity.this, "Link para o site", Toast.LENGTH_SHORT).show();
            }
        });
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(SobreAplicativoActivity.this, SobreActivity.class);
                startActivity(intent);
                finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(SobreAplicativoActivity.this, SobreActivity.class);
        startActivity(intent);
        finish();
    }
}
