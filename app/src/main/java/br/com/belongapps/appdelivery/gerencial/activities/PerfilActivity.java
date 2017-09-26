package br.com.belongapps.appdelivery.gerencial.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import br.com.belongapps.appdelivery.R;
import br.com.belongapps.appdelivery.cardapioOnline.activitys.CardapioMainActivity;
import br.com.belongapps.appdelivery.gerencial.adapters.OpcoesPerfilAdapter;
import br.com.belongapps.appdelivery.gerencial.model.OpcoesPerfil;

public class PerfilActivity extends AppCompatActivity {

    Toolbar mToolbar;
    RecyclerView recyclerViewOpcoes;
    List<OpcoesPerfil> listaDeOpcoes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        mToolbar = (Toolbar) findViewById(R.id.toolbar_perfil);
        mToolbar.setTitle("Perfil");

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerViewOpcoes = (RecyclerView) findViewById(R.id.opcoes_perfil);

        inicializeOpcoes();
        preencherOpcoes();

    }

    private void inicializeOpcoes(){
        listaDeOpcoes = new ArrayList<>();

        OpcoesPerfil opcoesPerfil1 = new OpcoesPerfil(R.drawable.ic_check_all, "Meus Pedidos");
        OpcoesPerfil opcoesPerfil2 = new OpcoesPerfil(R.drawable.ic_home, "Endereços de Entrega");
        OpcoesPerfil opcoesPerfil3 = new OpcoesPerfil(R.drawable.ic_home, "Sair");


        listaDeOpcoes.add(opcoesPerfil1);
        listaDeOpcoes.add(opcoesPerfil2);
        listaDeOpcoes.add(opcoesPerfil3);
    }

    public void preencherOpcoes(){
        recyclerViewOpcoes.setLayoutManager(new LinearLayoutManager(this));

        OpcoesPerfilAdapter adapter = new OpcoesPerfilAdapter(listaDeOpcoes, this);
        recyclerViewOpcoes.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(PerfilActivity.this, CardapioMainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(PerfilActivity.this, CardapioMainActivity.class);
                startActivity(intent);
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
