package br.com.belongapps.appdelivery.cardapioOnline.activitys;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import br.com.belongapps.appdelivery.cardapioOnline.adapters.RecheiosAcaiAdapter;
import br.com.belongapps.appdelivery.cardapioOnline.model.RecheioAcai;
import br.com.belongapps.appdelivery.util.StringUtil;

public class MontagemAcaiActivity extends AppCompatActivity {

    private DatabaseReference mDatabaseReference;
    private RecyclerView.Adapter adapter;

    //VIEWS
    private ProgressBar mProgressBar;
    private Toolbar mToolbar;
    private RecyclerView mRecheiosList;
    private TextView tvTotalAcai;
    private Button btProximo;

    //PARAMETROS RECEBIDOS
    private String acaiNome;
    private String acaiImg;
    private String acaiKey;
    private Double acaiTotal;

    //LISTAS
    private List<RecheioAcai> todosRecheios;
    private List<String> recheiosAcaiKey;
    private List<RecheioAcai> recheiosPadrao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_montagem_acai);

        getParametros();
        mToolbar = (Toolbar) findViewById(R.id.toolbar_montagem_acai);
        mToolbar.setTitle(acaiNome);

    }

    private void initViews() {
        mProgressBar = (ProgressBar) findViewById(R.id.progressbar_montagem_acai);
        tvTotalAcai = (TextView) findViewById(R.id.tv_total_acai);
        tvTotalAcai.setText("Total: " + StringUtil.formatToMoeda(acaiTotal));

        btProximo = (Button) findViewById(R.id.bt_proximo_montagem_acai);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(MontagemAcaiActivity.this, CardapioMainActivity.class);
        startActivity(intent);

        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();

        initViews();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();

        getParametros();
        buscarRecheiosDoAcai();
        buscarTodosRecheios();
    }

    private void getParametros() {
        acaiNome = getIntent().getStringExtra("acaiNome");
        acaiImg = getIntent().getStringExtra("acaiImg");
        acaiKey = getIntent().getStringExtra("acaiKey");
        acaiTotal = getIntent().getDoubleExtra("acaiTotal", 0);
    }

    private void buscarTodosRecheios() {
//        openProgressBar();
        mRecheiosList = (RecyclerView) findViewById(R.id.recheios);
        mRecheiosList.setHasFixedSize(true);
        mRecheiosList.setLayoutManager(new LinearLayoutManager(this));

        todosRecheios = new ArrayList<>();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("itens_cardapio").child("4");

        ValueEventListener recheiosListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    RecheioAcai recheio = data.getValue(RecheioAcai.class);
                    recheio.setItemKey(data.getKey());
                    todosRecheios.add(recheio);
                }

                //DEFINIR RECHEIOS PADRÕES
                recheiosPadrao = new ArrayList<>();
                for (RecheioAcai recheio : todosRecheios) {
                    for (String keysRecheiosdoAcai : recheiosAcaiKey) {
                        if (keysRecheiosdoAcai.equals(recheio.getItemKey())) {
                            recheio.setQtd(1);
                        } else{
                            recheio.setQtd(0);
                        }
                    }

                    recheiosPadrao.add(recheio);
                }

                adapter = new RecheiosAcaiAdapter(acaiNome, acaiImg, acaiTotal, acaiKey, todosRecheios, recheiosAcaiKey, recheiosPadrao, btProximo, tvTotalAcai, MontagemAcaiActivity.this);
                mRecheiosList.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };

        mDatabaseReference.addListenerForSingleValueEvent(recheiosListener);

    }

    private void buscarRecheiosDoAcai() {

        recheiosAcaiKey = new ArrayList<>();

        ValueEventListener recheiosListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    String keyrecheio = data.getValue(String.class);
                    recheiosAcaiKey.add(keyrecheio);

                    Log.println(Log.ERROR, "KEY RECHEIO: ", keyrecheio);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };

        mDatabaseReference.child("itens_cardapio").child("1").child(acaiKey).child("recheios_iniciais").addValueEventListener(recheiosListener);
    }

    private void openProgressBar() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    private void closeProgressBar() {
        mProgressBar.setVisibility(View.GONE);
    }
}
