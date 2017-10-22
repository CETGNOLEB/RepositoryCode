package br.com.belongapps.appdelivery.helpAbout.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import br.com.belongapps.appdelivery.R;
import br.com.belongapps.appdelivery.cardapioOnline.activitys.CardapioMainActivity;
import br.com.belongapps.appdelivery.helpAbout.adapters.OpcoesSobreAdapter;
import br.com.belongapps.appdelivery.helpAbout.model.OpcoesSobre;
import br.com.belongapps.appdelivery.helpAbout.model.OpcoesSocial;
import br.com.belongapps.appdelivery.helpAbout.adapters.OpcoesSocialAdapter;

public class SobreActivity extends AppCompatActivity {

    Toolbar mToolbar;

    /*Social*/
    RecyclerView recyclerViewOpcoesSocial;
    List<OpcoesSocial> listaDeOpcoesSocial;

    /*Sobre*/
    RecyclerView recyclerViewOpcoesSobre;
    List<OpcoesSobre> listaDeOpcoesSobre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sobre);

        mToolbar = (Toolbar) findViewById(R.id.toolbar_sobre);
        mToolbar.setTitle("Sobre o app");

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initViews();
        preencherOpcoesSocial();
        preencherOpcoesSobre();
    }

    public void initViews(){
        recyclerViewOpcoesSocial = (RecyclerView) findViewById(R.id.opcoes_social);
        recyclerViewOpcoesSobre = (RecyclerView) findViewById(R.id.opcoes_sobre);
    }

    public void preencherOpcoesSocial(){
        listaDeOpcoesSocial = new ArrayList<>();

        OpcoesSocial opcoesSocial1 = new OpcoesSocial(R.drawable.ic_comment, "Envie Comentários");
        OpcoesSocial opcoesSocial2 = new OpcoesSocial(R.drawable.ic_cheart, "Gostou do aplicativo? Avalie!");
        OpcoesSocial opcoesSocial3 = new OpcoesSocial(R.drawable.ic_facebook_box, "Curta nosso Facebook");
        OpcoesSocial opcoesSocial4 = new OpcoesSocial(R.drawable.ic_share_variant, "Conte aos amigos");

        listaDeOpcoesSocial.add(opcoesSocial1);
        listaDeOpcoesSocial.add(opcoesSocial2);
        listaDeOpcoesSocial.add(opcoesSocial3);
        listaDeOpcoesSocial.add(opcoesSocial4);

        recyclerViewOpcoesSocial.setLayoutManager(new LinearLayoutManager(this));

        OpcoesSocialAdapter adapter = new OpcoesSocialAdapter(listaDeOpcoesSocial, this);
        recyclerViewOpcoesSocial.setAdapter(adapter);
    }

    public void preencherOpcoesSobre(){
        listaDeOpcoesSobre = new ArrayList<>();

        OpcoesSobre opcoesSobre1 = new OpcoesSobre(R.drawable.ic_alert_circle, "Sobre o aplicativo");
        OpcoesSobre opcoesSobre2 = new OpcoesSobre(R.drawable.ic_earth, "Nosso site");
        OpcoesSobre opcoesSobre3 = new OpcoesSobre(R.drawable.ic_file, "Política de privacidade");
        OpcoesSobre opcoesSobre4 = new OpcoesSobre(R.drawable.ic_view_list, "Versão 1.0.0");

        listaDeOpcoesSobre.add(opcoesSobre1);
        listaDeOpcoesSobre.add(opcoesSobre2);
        listaDeOpcoesSobre.add(opcoesSobre3);
        listaDeOpcoesSobre.add(opcoesSobre4);

        recyclerViewOpcoesSobre.setLayoutManager(new LinearLayoutManager(this));

        OpcoesSobreAdapter adapter = new OpcoesSobreAdapter(listaDeOpcoesSobre, this);
        recyclerViewOpcoesSobre.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(SobreActivity.this, CardapioMainActivity.class);
                startActivity(intent);
                finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(SobreActivity.this, CardapioMainActivity.class);
        startActivity(intent);
        finish();
    }
}