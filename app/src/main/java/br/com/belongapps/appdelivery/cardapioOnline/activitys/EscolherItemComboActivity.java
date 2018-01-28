package br.com.belongapps.appdelivery.cardapioOnline.activitys;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import br.com.belongapps.appdelivery.R;
import br.com.belongapps.appdelivery.cardapioOnline.adapters.ItemComboAdapter;
import br.com.belongapps.appdelivery.cardapioOnline.model.ItemCardapio;
import br.com.belongapps.appdelivery.cardapioOnline.model.ItemPedido;

public class EscolherItemComboActivity extends AppCompatActivity {

    /*VIEW*/
    private Toolbar mToolbar;
    private ProgressBar mProgressBar;

    private RecyclerView mItemComboList;
    private DatabaseReference mDatabaseReference;
    private RecyclerView.Adapter adapter;

    //PARAMETROS
    private String nomeItem;
    private List<String> keyItens;
    private String itemSelecionado;
    private String bebidaSelecionada;
    private ItemPedido itemPedido;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_escolher_item_combo);

        getParametros();

        mToolbar = findViewById(R.id.toolbar_escolher_item_combo);
        mToolbar.setTitle("Escolha " + nomeItem);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initListagemDosItens();

    }

    private void getParametros() {
        nomeItem = getIntent().getStringExtra("ItemNome");
        itemPedido = getIntent().getParcelableExtra("ItemPedido");
        itemSelecionado = getIntent().getStringExtra("ItemSelecionado");
        bebidaSelecionada = getIntent().getStringExtra("BebidaSelecionada");
        keyItens = getIntent().getStringArrayListExtra("KeyItens");
    }

    private void initListagemDosItens() {

        mProgressBar = findViewById(R.id.progressbar_escolher_item_combo);
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("itens_cardapio").child("5");

        mItemComboList = findViewById(R.id.list_item_combo);
        mItemComboList.setHasFixedSize(true);
        mItemComboList.setLayoutManager(new LinearLayoutManager(this));

        openProgressBar();

        final List<ItemCardapio> itens = new ArrayList<>();

        //BUSCAR PIZZAS DISPONÍVEIS
        ValueEventListener pizzasValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    for (String keyPizzaDisponivel : keyItens) {
                        if (data.getKey().equals(keyPizzaDisponivel)) {
                            ItemCardapio item = data.getValue(ItemCardapio.class);
                            itens.add(item);
                        }
                    }

                }

                adapter = new ItemComboAdapter(itens, EscolherItemComboActivity.this, mProgressBar, itemSelecionado, bebidaSelecionada, itemPedido);
                mItemComboList.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        mDatabaseReference.addValueEventListener(pizzasValueEventListener);

    }

    private void exibirDialogCancelarEscolhaDoItem() {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        AlertDialog.Builder mBilder = new AlertDialog.Builder(EscolherItemComboActivity.this, R.style.MyDialogTheme);
        View layoutDialog = inflater.inflate(R.layout.dialog_cancel_escolha_item_combo, null);

        Button btVoltar = layoutDialog.findViewById(R.id.bt_fechar_dialog_escolha_item_combo);
        Button btCancelarMontagem = layoutDialog.findViewById(R.id.bt_cancel_escolha_item_combo);

        mBilder.setView(layoutDialog);
        final AlertDialog dialogCancelEscolhaItemCombo = mBilder.create();
        dialogCancelEscolhaItemCombo.show();

        btVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogCancelEscolhaItemCombo.dismiss();
            }
        });

        btCancelarMontagem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EscolherItemComboActivity.this, DetalhesdoItemActivity.class);
                intent.putExtra("ItemPedido", itemPedido);
                intent.putExtra("ItemSelecionado", itemSelecionado);
                intent.putExtra("BebidaSelecionada", bebidaSelecionada);
                intent.putExtra("Combo", "Combo");
                startActivity(intent);
                finish();
            }

        });
    }

    @Override
    public void onBackPressed() {
        exibirDialogCancelarEscolhaDoItem();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                exibirDialogCancelarEscolhaDoItem();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void openProgressBar() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

}
