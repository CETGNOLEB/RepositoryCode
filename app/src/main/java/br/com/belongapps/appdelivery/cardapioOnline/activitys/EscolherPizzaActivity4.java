package br.com.belongapps.appdelivery.cardapioOnline.activitys;

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
import java.util.List;

import br.com.belongapps.appdelivery.R;
import br.com.belongapps.appdelivery.cardapioOnline.adapters.PizzaAdapter;
import br.com.belongapps.appdelivery.cardapioOnline.model.ItemCardapio;
import br.com.belongapps.appdelivery.cardapioOnline.model.ItemPedido;

import static android.content.ContentValues.TAG;

public class EscolherPizzaActivity4 extends AppCompatActivity {

    private Toolbar mToolbar;
    private RecyclerView mListViewPizzas;
    private RecyclerView.Adapter adapter;
    private DatabaseReference mDatabaseReference;
    private List<ItemCardapio> pizzas;
    private List<ItemCardapio> pizzasAux;

    private TextView tamPizza;
    private ProgressBar mProgressBar;

    //Parametros
    //-- A receber
    private int paramTamPizza;
    private String paramTipoPizza;
    private int countMetades;
    private ItemPedido itemPedido;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_escolher_pizza4);

        getParametros();
        initViews();

        mToolbar = (Toolbar) findViewById(R.id.toolbar_escolher_pizzas4);
        mToolbar.setTitle(getTamanho() + " (4/4)");

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    private void getParametros() {
        paramTamPizza = getIntent().getIntExtra("TamPizza", 0);
        paramTipoPizza = getIntent().getStringExtra("TipoPizza");
        countMetades = getIntent().getIntExtra("CountMetades", 0);
        itemPedido = getIntent().getParcelableExtra("ItemPedido");
    }

    private void initViews() {
        tamPizza = (TextView) findViewById(R.id.desc_tam_pizza4);
        mProgressBar = (ProgressBar) findViewById(R.id.progressbar_escolher_sabor_4_pizza);
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
    protected void onStart() {
        super.onStart();

        mProgressBar = (ProgressBar) findViewById(R.id.progressbar_escolher_sabor_4_pizza);

        paramTamPizza = getIntent().getIntExtra("TamPizza", 0);
        paramTipoPizza = getIntent().getStringExtra("TipoPizza");
        countMetades = getIntent().getIntExtra("CountMetades", 0);
        itemPedido = getIntent().getParcelableExtra("ItemPedido");

        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("itens_cardapio");
        mDatabaseReference.keepSynced(true);

        mListViewPizzas = (RecyclerView) findViewById(R.id.list_sabor_pizzas4);
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

                } catch (Exception n){ }

                pizzas = new ArrayList<>();
                pizzas.addAll(pizzasAux);
                pizzasAux = new ArrayList<>();

                countMetades = 4;
                adapter = new PizzaAdapter(pizzas, itemPedido, EscolherPizzaActivity4.this, mProgressBar, paramTamPizza, paramTipoPizza, countMetades);
                mListViewPizzas.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        };

        mDatabaseReference.child("5").addValueEventListener(postListener);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                /*Intent intent = new Intent(EscolherPizzaActivity2.this, DetalhesdoItemPizzaActivity.class);
                intent.putExtra("TipoPizza", paramTipoPizza);
                intent.putExtra("TamPizza", paramTamPizza);
                intent.putExtra("ItemPedido", itemPedido);

                startActivity(intent);

                finish();*/
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

       /* Intent intent = new Intent(EscolherPizzaActivity2.this, DetalhesdoItemPizzaActivity.class);
        intent.putExtra("TipoPizza", paramTipoPizza);
        intent.putExtra("TamPizza", paramTamPizza);
        intent.putExtra("ItemPedido", itemPedido);

        startActivity(intent);

        finish();*/
    }
}