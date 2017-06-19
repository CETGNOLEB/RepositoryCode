package br.com.belongapps.appdelivery.gerencial.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import br.com.belongapps.appdelivery.R;
import br.com.belongapps.appdelivery.cardapioOnline.activitys.CardapioMainActivity;
import br.com.belongapps.appdelivery.cardapioOnline.adapters.PromocoesAdapter;
import br.com.belongapps.appdelivery.cardapioOnline.model.ItemCardapio;

import static android.content.ContentValues.TAG;

public class EnderecosActivity extends AppCompatActivity {

    Toolbar mToolbar;

    private DatabaseReference mDatabaseReference;
    private ArrayList<ItemCardapio> pizzas;
    private ArrayList<ItemCardapio> pizzasAux;

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

    @Override
    protected void onStart() {
        super.onStart();

        final int tamPizza = 2;

        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("itens_cardapio");

        pizzasAux = new ArrayList<>();

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

                Log.println(Log.ERROR, "SIZE", "" + pizzasAux.size());

                pizzas = new ArrayList<>();
                pizzas.addAll(pizzasAux);
                pizzasAux = new ArrayList<>();


                /*adapter = new PromocoesAdapter(itensPromocao, getContext(), mProgressBar);
                mItemPromoList.setAdapter(adapter);*/
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        };

        mDatabaseReference.child("5").

                addValueEventListener(postListener);

    }
}
