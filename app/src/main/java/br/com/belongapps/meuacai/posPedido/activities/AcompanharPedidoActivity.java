package br.com.belongapps.meuacai.posPedido.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import br.com.belongapps.meuacai.R;
import br.com.belongapps.meuacai.cardapioOnline.activitys.CardapioMainActivity;
import br.com.belongapps.meuacai.helpAbout.activities.AjudaActivity;

public class AcompanharPedidoActivity extends AppCompatActivity {

    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acompanhar_pedido);

        mToolbar = (Toolbar) findViewById(R.id.toolbar_acompanhar_pedido);
        mToolbar.setTitle("Acompanhar Pedido");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled( true);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(AcompanharPedidoActivity.this, MeusPedidosActivity.class);
                startActivity(intent);
                finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(AcompanharPedidoActivity.this, MeusPedidosActivity.class);
        startActivity(intent);

        finish();
    }
}
