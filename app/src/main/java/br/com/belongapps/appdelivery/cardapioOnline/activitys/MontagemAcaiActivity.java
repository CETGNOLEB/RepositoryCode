package br.com.belongapps.appdelivery.cardapioOnline.activitys;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
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
import br.com.belongapps.appdelivery.cardapioOnline.model.ItemPedido;
import br.com.belongapps.appdelivery.cardapioOnline.model.RecheioAcai;
import br.com.belongapps.appdelivery.util.Print;
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
    private String telaAnterior;
    private ItemPedido itemPedido;

    //LISTAS
    private List<RecheioAcai> todosRecheios;
    private List<RecheioAcai> recheiosDoAcai;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_montagem_acai);

        getParametros();

        mToolbar = (Toolbar) findViewById(R.id.toolbar_montagem_acai);
        mToolbar.setTitle(acaiNome);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    private void initViews() {
        mProgressBar = (ProgressBar) findViewById(R.id.progressbar_montagem_acai);
        tvTotalAcai = (TextView) findViewById(R.id.tv_total_acai);
        tvTotalAcai.setText("Total: " + StringUtil.formatToMoeda(acaiTotal));

        btProximo = (Button) findViewById(R.id.bt_proximo_montagem_acai);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(MontagemAcaiActivity.this, DetalhesdoItemActivity.class);
                intent.putExtra("acai", "pedidoDeAcai");
                intent.putExtra("ItemPedido", itemPedido);
                startActivity(intent);
                finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(MontagemAcaiActivity.this, DetalhesdoItemActivity.class);
        intent.putExtra("ItemPedido", itemPedido);
        startActivity(intent);

        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();

        mDatabaseReference = FirebaseDatabase.getInstance().getReference();

        initViews();
        getParametros();

        if (todosRecheios == null) {
            buscarTodosOsRecheiosComPadraoDefinido();
        } else {
            preencherRecyclerView();
        }

    }

    private void getParametros() {
        acaiNome = getIntent().getStringExtra("acaiNome");
        acaiImg = getIntent().getStringExtra("acaiImg");
        acaiKey = getIntent().getStringExtra("acaiKey");
        acaiTotal = getIntent().getDoubleExtra("acaiTotal", 0);
        itemPedido = getIntent().getParcelableExtra("acai");

        recheiosDoAcai = getIntent().getParcelableArrayListExtra("recheiosSelecionados");

        telaAnterior = getIntent().getStringExtra("telaAnterior");

    }

    private void buscarTodosOsRecheiosComPadraoDefinido() {

        openProgressBar();

        final List<String> recheiosAcaiKey = new ArrayList<>();

        //BUCAR CHAVES DOS RECHEIOS DO ACAI
        ValueEventListener recheiosListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    String keyrecheio = data.getValue(String.class);
                    recheiosAcaiKey.add(keyrecheio);
                }

                buscarRecheiosPorChave(recheiosAcaiKey);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };

        mDatabaseReference.child("itens_cardapio").child("1").child(acaiKey).child("recheios_iniciais").addListenerForSingleValueEvent(recheiosListener);
    }

    private void buscarRecheiosPorChave(final List<String> recheiosAcaiKey) {

        recheiosDoAcai = new ArrayList<>();

        final ValueEventListener recheiosListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    for (String keyRecheioAcai : recheiosAcaiKey) {
                        if (data.getKey().equals(keyRecheioAcai)) {
                            RecheioAcai recheioPadrao = data.getValue(RecheioAcai.class);
                            recheioPadrao.setQtd(1);
                            recheioPadrao.setItemKey(data.getKey());
                            recheiosDoAcai.add(recheioPadrao);
                        }
                    }
                }

                for (RecheioAcai r :
                        recheiosDoAcai) {
                    Print.logError("R DO AÇAI: " + r.getNome());
                    Print.logError("QTD: " + r.getQtd());
                    Print.logError("KEY: " + r.getItemKey());
                }

                buscarTodosRecheios();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }

        };

        mDatabaseReference.child("itens_cardapio").child("4").addListenerForSingleValueEvent(recheiosListener);


    }

    private void buscarTodosRecheios() {
        todosRecheios = new ArrayList<>();

        ValueEventListener recheiosListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    RecheioAcai recheioAcai = data.getValue(RecheioAcai.class);
                    recheioAcai.setItemKey(data.getKey());
                    recheioAcai.setQtd(0);
                    todosRecheios.add(recheioAcai);
                }

                //DEFINIR QTD PADRÃO
                List<RecheioAcai> recheiosAux = new ArrayList<>();
                for (RecheioAcai recheio : todosRecheios) {
                    for (RecheioAcai recheioPadrao : recheiosDoAcai) {
                        if (recheioPadrao.getItemKey().equals(recheio.getItemKey())) {
                            Print.logError("DEFINIU 0 ITEM: " + recheio.getNome());
                            Print.logError("QTD ITEM: " + recheioPadrao.getQtd());
                            recheio.setQtd(recheioPadrao.getQtd());
                        }
                    }
                }

                Print.logError("---------- AUX ----------- ");
                for (RecheioAcai r : recheiosAux) {
                    Print.logError("AUX: " + r.getNome());
                    Print.logError("QTD: " + r.getQtd());
                }

                Print.logError("TODOS OS RECH: ");
                for (RecheioAcai r : todosRecheios) {
                    Print.logError("RECH: " + r.getNome());
                    Print.logError("QTD: " + r.getQtd());
                }

                preencherRecyclerView();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };

        mDatabaseReference.child("itens_cardapio").child("4").addListenerForSingleValueEvent(recheiosListener);
    }

    private void preencherRecyclerView() {
        closeProgressBar();


        mRecheiosList = (RecyclerView) findViewById(R.id.recheios);
        mRecheiosList.setHasFixedSize(true);
        mRecheiosList.setLayoutManager(new LinearLayoutManager(this));

        adapter = new RecheiosAcaiAdapter(acaiNome, acaiImg, acaiTotal, acaiKey, todosRecheios, recheiosDoAcai, btProximo, tvTotalAcai, MontagemAcaiActivity.this);
        mRecheiosList.setAdapter(adapter);
    }

    private void openProgressBar() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    private void closeProgressBar() {
        mProgressBar.setVisibility(View.GONE);
    }
}
