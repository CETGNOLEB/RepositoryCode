package br.com.belongapps.appdelivery.cardapioOnline.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import br.com.belongapps.appdelivery.R;
import br.com.belongapps.appdelivery.cardapioOnline.adapters.EditarPizzaAdapter;
import br.com.belongapps.appdelivery.cardapioOnline.model.ItemCardapio;
import br.com.belongapps.appdelivery.cardapioOnline.model.ItemPedido;

import static android.content.ContentValues.TAG;

public class EditarPizzaActivity extends AppCompatActivity {

    private Toolbar mToolbar;

    private RecyclerView mListViewPizzas;
    private RecyclerView.Adapter adapter;

    private DatabaseReference mDatabaseReference;

    //Parametros Recebidos
    private int paramTamPizza;
    private String paramTipoPizza;
    private ItemPedido itemPedido;
    private String metade;

    private ArrayList<ItemCardapio> pizzas;
    private ArrayList<ItemCardapio> pizzasAux;

    //VIEWS
    private TextView tamPizza;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_pizza);

    }

    @Override
    protected void onStart() {
        super.onStart();

        mProgressBar = (ProgressBar) findViewById(R.id.progressbar_editar_sabor_pizza);
        tamPizza = (TextView) findViewById(R.id.desc_metade_pizza);

        paramTamPizza = getIntent().getIntExtra("TamPizza", 0);
        paramTipoPizza = getIntent().getStringExtra("TipoPizza");
        metade = getIntent().getStringExtra("Metade");
        itemPedido = getIntent().getParcelableExtra("ItemPedido");

        mToolbar = (Toolbar) findViewById(R.id.toolbar_editar_pizza);
        mToolbar.setTitle(getTamanhoDesc());

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tamPizza.setText("Escolha o sabor da metade " + metade);

        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("itens_cardapio");
        mDatabaseReference.keepSynced(true);

        mListViewPizzas = (RecyclerView) findViewById(R.id.list_sabor_pizzas_editar);
        mListViewPizzas.setHasFixedSize(true);
        mListViewPizzas.setLayoutManager(new LinearLayoutManager(this));

        pizzasAux = new ArrayList<>();

        final int tamPizza = paramTamPizza;

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                try {
                    for (DataSnapshot item : dataSnapshot.getChildren()) {
                        ItemCardapio ic = item.getValue(ItemCardapio.class);

                        if (tamPizza == 0) {
                            if (ic.getValor_pizza_p() != 0.0) {
                                pizzasAux.add(ic);
                            }
                        } else if (tamPizza == 1) {

                            if (ic.getValor_pizza_m() != 0.0) {
                                pizzasAux.add(ic);
                            }
                        } else if (tamPizza == 2) {
                            if (ic.getValor_pizza_g() != 0.0) {
                                pizzasAux.add(ic);
                            }
                        }
                    }

                } catch (Exception n){ }

                pizzas = new ArrayList<>();
                pizzas.addAll(pizzasAux);
                pizzasAux = new ArrayList<>();

                adapter = new EditarPizzaAdapter(pizzas, itemPedido, EditarPizzaActivity.this, mProgressBar, tamPizza, paramTipoPizza, metade);
                mListViewPizzas.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        };

        mDatabaseReference.child("5").addValueEventListener(postListener);

    }


    public String getTamanhoDesc(){
        String retorno = "";

        if (paramTamPizza == 0){
            retorno = "Pizza Pequena";
        } else if (paramTamPizza == 1){
            retorno = "Pizza Média";
        } else if(paramTamPizza == 2){
            retorno = "Pizza Grande";
        }

        return retorno;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent i = new Intent(EditarPizzaActivity.this, DetalhesdoItemPizzaMMActivity.class);
                i.putExtra("TamPizza", paramTamPizza);
                i.putExtra("TipoPizza", paramTipoPizza);
                i.putExtra("ItemPedido", itemPedido);
                startActivity(i);
                finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent i = new Intent(EditarPizzaActivity.this, DetalhesdoItemPizzaMMActivity.class);
        i.putExtra("TamPizza", paramTamPizza);
        i.putExtra("TipoPizza", paramTipoPizza);
        i.putExtra("ItemPedido", itemPedido);
        startActivity(i);
        finish();
    }

}