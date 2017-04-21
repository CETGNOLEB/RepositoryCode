package br.com.belongapps.meuacai.gerencial.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import br.com.belongapps.meuacai.R;
import br.com.belongapps.meuacai.cardapioOnline.activitys.CardapioMainActivity;

public class EnderecosActivity extends AppCompatActivity {

    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enderecos);

        mToolbar = (Toolbar) findViewById(R.id.toolbar_enderecos);
        mToolbar.setTitle("Meus Endereços");

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(EnderecosActivity.this, CardapioMainActivity.class);
        startActivity(intent);

        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();

        }

        return super.onOptionsItemSelected(item);
    }
}
