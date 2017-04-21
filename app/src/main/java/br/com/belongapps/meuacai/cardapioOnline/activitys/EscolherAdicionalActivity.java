package br.com.belongapps.meuacai.cardapioOnline.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import br.com.belongapps.meuacai.R;
import br.com.belongapps.meuacai.cardapioOnline.model.ItemCardapio;
import br.com.belongapps.meuacai.cardapioOnline.model.ItemPedido;

public class EscolherAdicionalActivity extends AppCompatActivity {

    private ProgressBar mProgressBar;
    private ListView mListViewAdicionais;
    private Button voltar, proximo;
    private Toolbar mToolbar;

    ItemPedido itemPedido;
    String descProduto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_escolher_adicional);

        mToolbar = (Toolbar) findViewById(R.id.toolbar_escolher_adicional);
        mToolbar.setTitle("Açai Adicionais");

        final Intent intent = getIntent();
        itemPedido = intent.getExtras().getParcelable("ItemPedido");

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        voltar = (Button) findViewById(R.id.bt_voltar_adicionais);
        proximo = (Button) findViewById(R.id.bt_proximo_adicionais);

        mListViewAdicionais = (ListView) findViewById(R.id.list_adicionais);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://appacai001.firebaseio.com/itens_cardapio/2");

        FirebaseListAdapter<ItemCardapio> firebaseListAdapter = new FirebaseListAdapter<ItemCardapio>(
                this,
                ItemCardapio.class,
                R.layout.list_icon_adicionais,
                databaseReference

        ) {
            @Override
            protected void populateView(View v, ItemCardapio model, int position) {

                TextView item_nome = (TextView) v.findViewById(R.id.item_nome_adicionais);
                TextView item_valor = (TextView) v.findViewById(R.id.item_valor_unit_adicionais);
                ImageView item_ref_imagem = (ImageView) v.findViewById(R.id.item_img_adicionais);

                item_nome.setText(model.getNome());
                item_valor.setText("R$ " + model.getValor_unit() + ",00");

                Picasso.with(EscolherAdicionalActivity.this).load(model.getRef_img()).into(item_ref_imagem);

            }
        };

        mListViewAdicionais.setAdapter(firebaseListAdapter);

        voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(EscolherAdicionalActivity.this, EscolherRecheioActivity.class);
//                intent1.putExtra("nomeItem", nomeProduto);
                startActivity(intent1);
                finish();
            }
        });

        proximo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemPedido.setDescricao("Recheios: Amendoim + castanha");

                Intent intent2 = new Intent(EscolherAdicionalActivity.this, DetalhesdoItemActivity.class);
                intent2.putExtra("ItemPedido", itemPedido);

                intent2.putExtra("TelaAnterior", "EscolherAdionalActivity");

                startActivity(intent2);
                finish();
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(EscolherAdicionalActivity.this, EscolherRecheioActivity.class);
                startActivity(intent);
                finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(EscolherAdicionalActivity.this, EscolherRecheioActivity.class);
        startActivity(intent);

        finish();
    }

}
