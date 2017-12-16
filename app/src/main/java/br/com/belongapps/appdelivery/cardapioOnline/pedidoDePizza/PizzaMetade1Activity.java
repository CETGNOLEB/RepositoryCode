package br.com.belongapps.appdelivery.cardapioOnline.pedidoDePizza;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Locale;

import br.com.belongapps.appdelivery.R;
import br.com.belongapps.appdelivery.cardapioOnline.activitys.CardapioMainActivity;
import br.com.belongapps.appdelivery.cardapioOnline.model.ItemCardapio;
import br.com.belongapps.appdelivery.cardapioOnline.model.ItemPizzaDTO;
import br.com.belongapps.appdelivery.cardapioOnline.model.ItemPizza;
import br.com.belongapps.appdelivery.firebaseAuthApp.FirebaseAuthApp;
import br.com.belongapps.appdelivery.seguranca.activities.LoginActivity;

public class PizzaMetade1Activity extends AppCompatActivity {

    private Toolbar mToolbar;

    private RecyclerView mListViewPizzas;
    private RecyclerView.Adapter adapter;

    private DatabaseReference mDatabaseReference;

    private ItemPizza itemPizza;

    private ArrayList<ItemCardapio> pizzas;
    private ArrayList<ItemCardapio> pizzasAux;

    //VIEWS
    private ProgressBar mProgressBar;
    private Button btVoltar;
    private Button btProximo;

    private ArrayList<ItemPizzaDTO> saboresPizzas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pizza_metade1);

        getParametros();

        mToolbar = (Toolbar) findViewById(R.id.toolbar_pizza_metade1);
        mToolbar.setTitle(itemPizza.getTamanho() + " " + getStage());

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initViews();

        btProximo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentVoltar = new Intent(PizzaMetade1Activity.this, CardapioMainActivity.class);
                startActivity(intentVoltar);
                finish();
            }
        });

    }

    public String getStage() {
        String retorno = "";

        if (itemPizza.getTipo() == 1) {
            retorno += " (1/2)";
        } else if (itemPizza.getTipo() == 2) {
            retorno += " (1/3)";
        } else if (itemPizza.getTipo() == 3) {
            retorno += " (1/4)";
        }

        return retorno;
    }

    public void initViews() {
        btVoltar = (Button) findViewById(R.id.bt_voltar_pizza_metade1);
        btProximo = (Button) findViewById(R.id.bt_proximo_pizza_metade1);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (FirebaseAuthApp.getUsuarioLogado() == null) {
            Intent i = new Intent(PizzaMetade1Activity.this, LoginActivity.class);
            startActivity(i);
            finish();

        } else {

            mProgressBar = (ProgressBar) findViewById(R.id.progressbar_pizza_metade1);
            openProgressBar();
            buscarSaboresPizzas();
        }

        getParametros();
    }

    public void buscarSaboresPizzas() {
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("itens_cardapio").child("5");
        mDatabaseReference.keepSynced(true);

        mListViewPizzas = (RecyclerView) findViewById(R.id.pizzas_metade1);
        mListViewPizzas.setHasFixedSize(true);
        mListViewPizzas.setLayoutManager(new LinearLayoutManager(this));

        final FirebaseRecyclerAdapter<ItemPizzaDTO, ViewHolderPizza> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<ItemPizzaDTO, ViewHolderPizza>(
                ItemPizzaDTO.class, R.layout.card_sabor_pizzas, ViewHolderPizza.class, mDatabaseReference
        ) {

            @Override
            public void onBindViewHolder(final ViewHolderPizza viewHolder, final int position) {
                super.onBindViewHolder(viewHolder, position);

                //YoYo.with(Techniques.BounceInUp).playOn(viewHolder.card_sabor_pizza);
            }

            @Override
            protected void populateViewHolder(final ViewHolderPizza viewHolder, final ItemPizzaDTO model, int position) {
                closeProgressBar();

                final String key = getRef(position).getKey();

                if (saboresPizzas == null){
                    ItemPizzaDTO item = model;
                    saboresPizzas.add(item);
                }

                viewHolder.setNome(model.getNome());
                viewHolder.setDescricao(model.getDescricao());

                if (itemPizza.getTamanho().equals("Pizza Pequena")) {
                   // viewHolder.setValorUnitarioEPromocao(model.getValor_pizza_p(), model.isStatus_promocao(), model.getPromo_pizza_p());
                    viewHolder.setValorUnitario(model.getValor_pizza_p());
                } else if (itemPizza.getTamanho().equals("Pizza Média")) {
                    //viewHolder.setValorUnitarioEPromocao(model.getValor_pizza_m(), model.isStatus_promocao(), model.getPromo_pizza_m());
                    viewHolder.setValorUnitario(model.getValor_pizza_m());
                } else { //Pizza Grande
                   // viewHolder.setValorUnitarioEPromocao(model.getValor_pizza_g(), model.isStatus_promocao(), model.getPromo_pizza_g());
                    viewHolder.setValorUnitario(model.getValor_pizza_g());
                }

                viewHolder.setImagem(PizzaMetade1Activity.this, model.getRef_img());
                viewHolder.setStatus(model.getStatus_item());

                if (model.getStatus_item() == 1) { //Se Disponível no Cardápio
                    viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                        model.setChecked(true);
                        viewHolder.checkSelecionado(model.isChecked());
                        desmarcarOutros();
                        }
                    });
                }

            }

        };

        mListViewPizzas.setAdapter(firebaseRecyclerAdapter);
    }

    public static void desmarcarOutros(){

    }

    public void getParametros() {
        itemPizza = getIntent().getParcelableExtra("ItemPizza");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(PizzaMetade1Activity.this, CardapioMainActivity.class);
                startActivity(intent);
                finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(PizzaMetade1Activity.this, CardapioMainActivity.class);
        startActivity(intent);

        finish();
    }

    public static class ViewHolderPizza extends RecyclerView.ViewHolder {

        View mView;
        CardView card_sabor_pizza;

        public ViewHolderPizza(final View itemView) {
            super(itemView);

            mView = itemView;
            card_sabor_pizza = (CardView) mView.findViewById(R.id.card_sabor_pizzas);

        }

        public void setNome(String nome) {

            TextView item_nome = (TextView) mView.findViewById(R.id.nome_sabor_pizza);
            item_nome.setText(nome);

        }

        public void setDescricao(String descricao) {

            TextView item_descricao = (TextView) mView.findViewById(R.id.desc_sabor_pizza);
            item_descricao.setText(descricao);
        }

        public void setValorUnitarioEPromocao(double valor_unit, boolean status_promocao, double valor_promocional) {

            TextView item_valor_promo = (TextView) mView.findViewById(R.id.promo_valor_unit_sabor_pizza);
            TextView item_valor_unit = (TextView) mView.findViewById(R.id.valor_unit_sabor_pizza);

            if (status_promocao == true) {
                item_valor_promo.setText(" R$ " + String.format(Locale.US, "%.2f", valor_promocional).replace(".", ","));
                item_valor_unit.setText(" R$ " + String.format(Locale.US, "%.2f", valor_unit).replace(".", ","));
                item_valor_unit.setPaintFlags(item_valor_unit.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                item_valor_unit.setVisibility(View.VISIBLE);
            } else {
                item_valor_promo.setText(" R$ " + String.format(Locale.US, "%.2f", valor_unit).replace(".", ","));
            }

        }

        public void setValorUnitario(Double valorUnitario){
            TextView item_valor_promo = (TextView) mView.findViewById(R.id.promo_valor_unit_sabor_pizza);
            item_valor_promo.setText(" R$ " + String.format(Locale.US, "%.2f", valorUnitario).replace(".", ","));

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

        public void setStatus(int status) {
            TextView item_status = (TextView) mView.findViewById(R.id.status_sabor_pizza);

            if (status == 0) { //Se Indisponível
                item_status.setVisibility(View.VISIBLE);
            }
        }

        public void checkSelecionado(boolean isChecked){
            ImageView checked = (ImageView) mView.findViewById(R.id.item_pizza_selecionado);

            if (isChecked){
                checked.setVisibility(View.VISIBLE);
            } else{
                checked.setVisibility(View.GONE);
            }
        }

    }

    private void openProgressBar() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    private void closeProgressBar() {
        mProgressBar.setVisibility(View.GONE);
    }

}