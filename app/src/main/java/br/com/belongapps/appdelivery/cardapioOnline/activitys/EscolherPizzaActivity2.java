package br.com.belongapps.appdelivery.cardapioOnline.activitys;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.Locale;

import br.com.belongapps.appdelivery.R;
import br.com.belongapps.appdelivery.cardapioOnline.model.ItemCardapio;
import br.com.belongapps.appdelivery.cardapioOnline.model.ItemPedido;

public class EscolherPizzaActivity2 extends AppCompatActivity {

    private Toolbar mToolbar;
    private RecyclerView mListViewPizzas;
    private DatabaseReference mDatabaseReference;

    private TextView tamPizza;

    //Parametros
    //-- A receber
    private String paramTamPizza;
    private String paramTipoPizza;
    private ItemPedido itemPedido;

    //-- A enviar
    private String telaAnterior;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_escolher_pizza2);

        getParametros();
        initViews();

        mToolbar = (Toolbar) findViewById(R.id.toolbar_escolher_pizzas2);
        mToolbar.setTitle(paramTamPizza);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    private void getParametros() {
        paramTamPizza = getIntent().getStringExtra("TamPizza");
        paramTipoPizza = getIntent().getStringExtra("TipoPizza");
        itemPedido = getIntent().getParcelableExtra("ItemPedido");
    }

    private void initViews() {
        tamPizza = (TextView) findViewById(R.id.desc_tam_pizza2);
        tamPizza.setText("Escolha o sabor da segunda metade");
    }

    @Override
    protected void onStart() {
        super.onStart();

        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("itens_cardapio").child("5");
        mDatabaseReference.keepSynced(true);
        mListViewPizzas = (RecyclerView) findViewById(R.id.list_sabor_pizzas2);
        mListViewPizzas.setHasFixedSize(true);
        mListViewPizzas.setLayoutManager(new LinearLayoutManager(this));

        final FirebaseRecyclerAdapter<ItemCardapio, EscolherPizzaActivity2.PizzasViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<ItemCardapio, PizzasViewHolder>(
                ItemCardapio.class, R.layout.card_sabor_pizzas, EscolherPizzaActivity2.PizzasViewHolder.class, mDatabaseReference
        ) {

            @Override
            public void onBindViewHolder(final EscolherPizzaActivity2.PizzasViewHolder viewHolder, final int position) {
                super.onBindViewHolder(viewHolder, position);

                YoYo.with(Techniques.BounceInUp).playOn(viewHolder.card_pizzas);
            }

            @Override
            protected void populateViewHolder(final EscolherPizzaActivity2.PizzasViewHolder viewHolder, final ItemCardapio model, int position) {
                viewHolder.setNome(model.getNome());
                viewHolder.setDescricao(model.getDescricao());
                viewHolder.setValor_unit(model.getValor_unit());
                viewHolder.setImagem(EscolherPizzaActivity2.this, model.getRef_img());

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Segunda Metade
                        itemPedido.setNomeMetade2(model.getNome());
                        itemPedido.setDescMetade2(model.getDescricao());
                        itemPedido.setImgMetade2(model.getRef_img());
                        itemPedido.setValorMetade2(model.getValor_unit() / 2);

                        Intent i = new Intent(EscolherPizzaActivity2.this, DetalhesdoItemPizzaActivity.class);
                        i.putExtra("TelaAnterior", "EscolherPizzaActivity2");
                        i.putExtra("ItemPedido", itemPedido);
                        i.putExtra("TamPizza", paramTamPizza);
                        i.putExtra("TipoPizza", paramTipoPizza);

                        startActivity(i);
                        finish();
                    }
                });

            }

        };

        mListViewPizzas.setAdapter(firebaseRecyclerAdapter);

    }

    public static class PizzasViewHolder extends RecyclerView.ViewHolder {

        View mView;
        CardView card_pizzas;

        public PizzasViewHolder(final View itemView) {
            super(itemView);

            mView = itemView;
            card_pizzas = (CardView) mView.findViewById(R.id.card_sabor_pizzas);

        }

        public void setNome(String nome) {

            TextView item_nome = (TextView) mView.findViewById(R.id.item_nome_sabor_pizza);
            item_nome.setText(nome);

        }

        public void setDescricao(String descricao) {

            TextView item_desc = (TextView) mView.findViewById(R.id.item_desc_sabor_pizza);
            item_desc.setText(descricao);

        }

        public void setValor_unit(double valor_unit) {

            TextView apartir_de = (TextView) mView.findViewById(R.id.valor_unit_sabor_pizza);
            apartir_de.setText("R$ " + String.format(Locale.US, "%.2f", valor_unit).replace(".", ","));

        }

        public void setImagem(final Context context, final String url) {
            final ImageView item_ref_image = (ImageView) mView.findViewById(R.id.img_sabor_pizza);

            Picasso.with(context).load(url).networkPolicy(NetworkPolicy.OFFLINE).into(item_ref_image, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {
                    Picasso.with(context).load(url).into(item_ref_image);
                }
            });
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(EscolherPizzaActivity2.this, DetalhesdoItemPizzaActivity.class);
                intent.putExtra("TipoPizza", paramTipoPizza);
                intent.putExtra("TamPizza", paramTamPizza);
                intent.putExtra("ItemPedido", itemPedido);

                startActivity(intent);

                finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(EscolherPizzaActivity2.this, DetalhesdoItemPizzaActivity.class);
        intent.putExtra("TipoPizza", paramTipoPizza);
        intent.putExtra("TamPizza", paramTamPizza);
        intent.putExtra("ItemPedido", itemPedido);

        startActivity(intent);

        finish();
    }
}