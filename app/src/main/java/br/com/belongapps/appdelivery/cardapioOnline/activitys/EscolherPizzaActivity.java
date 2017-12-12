package br.com.belongapps.appdelivery.cardapioOnline.activitys;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import br.com.belongapps.appdelivery.cardapioOnline.adapters.PizzaAdapter;
import br.com.belongapps.appdelivery.cardapioOnline.model.ItemCardapio;
import br.com.belongapps.appdelivery.cardapioOnline.model.ItemPedido;
import br.com.belongapps.appdelivery.firebaseAuthApp.FirebaseAuthApp;
import br.com.belongapps.appdelivery.seguranca.activities.LoginActivity;

import static android.content.ContentValues.TAG;

public class EscolherPizzaActivity extends AppCompatActivity {

    private Toolbar mToolbar;

    private RecyclerView mListViewPizzas;
    private RecyclerView.Adapter adapter;

    private DatabaseReference mDatabaseReference;

    //Parametros Recebidos
    private Integer paramTamPizza;
    private String paramTipoPizza;
    private int countMetades;

    private ItemPedido itemPedido;

    private ArrayList<ItemCardapio> pizzas;
    private ArrayList<ItemCardapio> pizzasAux;

    //VIEWS
    private TextView tamPizza;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_escolher_pizza);

        paramTamPizza = getIntent().getIntExtra("TamPizza",0);
        paramTipoPizza = getIntent().getStringExtra("TipoPizza");

        mToolbar = (Toolbar) findViewById(R.id.toolbar_escolher_pizzas);
        mToolbar.setTitle(getTamanho() + getStage());

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tamPizza = (TextView) findViewById(R.id.desc_tam_pizza);

        if (paramTipoPizza.equals("Inteira")) {
            tamPizza.setText("Escolha o sabor");
        } else {
            tamPizza.setText("Escolha o primeiro sabor");
        }

    }

    public String getStage(){
        String retorno = "";

        if (paramTipoPizza.equals("Metade-Metade")){
            retorno += " (1/2)";
        } else if(paramTipoPizza.equals("Três Sabores")){
            retorno += " (1/3)";
        } else if(paramTipoPizza.equals("Quatro Sabores")) {
            retorno += " (1/4)";
        }

        return retorno;
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (FirebaseAuthApp.getUsuarioLogado() == null) {
            Intent i = new Intent(EscolherPizzaActivity.this, LoginActivity.class);
            startActivity(i);
            finish();
        }else {

            mProgressBar = (ProgressBar) findViewById(R.id.progressbar_escolher_sabor_pizza);

            paramTamPizza = getIntent().getIntExtra("TamPizza", 0);
            paramTipoPizza = getIntent().getStringExtra("TipoPizza");

            Log.println(Log.ERROR, "TAMANHO DA PIZZA", "TAMANHO: " + paramTamPizza);

            itemPedido = new ItemPedido();

            mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("itens_cardapio");
            mDatabaseReference.keepSynced(true);

            mListViewPizzas = (RecyclerView) findViewById(R.id.list_sabor_pizzas);
            mListViewPizzas.setHasFixedSize(true);
            mListViewPizzas.setLayoutManager(new LinearLayoutManager(this));

            pizzasAux = new ArrayList<>();

            ValueEventListener postListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    try {
                        for (DataSnapshot item : dataSnapshot.getChildren()) {
                            ItemCardapio ic = item.getValue(ItemCardapio.class);

                            if (paramTamPizza == 0) {
                                if (ic.getValor_pizza_p() != 0.0) {
                                    pizzasAux.add(ic);
                                }
                            } else if (paramTamPizza == 1) {

                                if (ic.getValor_pizza_m() != 0.0) {
                                    pizzasAux.add(ic);
                                }
                            } else if (paramTamPizza == 2) {
                                if (ic.getValor_pizza_g() != 0.0) {
                                    pizzasAux.add(ic);
                                }
                            }
                        }

                    } catch (Exception n) {
                    }

                    pizzas = new ArrayList<>();
                    pizzas.addAll(pizzasAux);
                    pizzasAux = new ArrayList<>();

                    countMetades = 1;
                    ItemPedido itemPedido = new ItemPedido();
                    adapter = new PizzaAdapter(pizzas, itemPedido, EscolherPizzaActivity.this, mProgressBar, paramTamPizza, paramTipoPizza, countMetades);
                    mListViewPizzas.setAdapter(adapter);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                }
            };

            mDatabaseReference.child("5").addValueEventListener(postListener);
        }

    }

    public String getTamanho(){
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
                Intent intent = new Intent(EscolherPizzaActivity.this, CardapioMainActivity.class);
                startActivity(intent);
                finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(EscolherPizzaActivity.this, CardapioMainActivity.class);
        startActivity(intent);

        finish();
    }

}