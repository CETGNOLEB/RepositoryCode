package br.com.belongapps.appdelivery.cardapioOnline.activitys;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import br.com.belongapps.appdelivery.R;
import br.com.belongapps.appdelivery.cardapioOnline.model.ItemCardapio;
import br.com.belongapps.appdelivery.cardapioOnline.model.ItemPedido;
import br.com.belongapps.appdelivery.gerencial.activities.EnderecosActivity;
import br.com.belongapps.appdelivery.seguranca.activities.LoginActivity;

public class EscolherRecheioActivity extends AppCompatActivity {

    private Toolbar mToolbar;

    private ProgressBar mProgressBar;
    private RecyclerView mListViewRecheios;

    private DatabaseReference mDatabaseReference;
    private Button voltar, proximo;
    private ImageButton menosqtd, maisqtd;
    private TextView qtdRcheiosEscolha;
    private TextView qtdItem;

    private int contQtd = 0;

    /*PARAMETROS*/

    ItemPedido itemPedido;

    long qtdRecheiosParaEscolher; //recebido
    String descRecheios; //A enviar

    long qtdRecheioSelecionado = 0;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_escolher_recheio);

        mAuth = FirebaseAuth.getInstance();

        Intent intent = getIntent();
        itemPedido = intent.getExtras().getParcelable("ItemPedido");

        qtdRecheiosParaEscolher = intent.getLongExtra("qtd_recheios_item_cardapio", 0);

        mToolbar = (Toolbar) findViewById(R.id.toolbar_escolher_recheios);
        mToolbar.setTitle("Recheios");

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        qtdRcheiosEscolha = (TextView) findViewById(R.id.qtd_recheios_para_escolha);

        if (qtdRecheiosParaEscolher > 1) {
            qtdRcheiosEscolha.setText("Você pode escolher " + qtdRecheiosParaEscolher + " opções de recheio.");
        } else {
            qtdRcheiosEscolha.setText("Você pode escolher apenas uma opção.");
        }

        voltar = (Button) findViewById(R.id.bt_voltar_recheios);
        proximo = (Button) findViewById(R.id.bt_proximo_recheios);

        voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(EscolherRecheioActivity.this, CardapioMainActivity.class);
                startActivity(intent1);
                finish();
            }
        });

        proximo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(EscolherRecheioActivity.this, EscolherAdicionalActivity.class);
                intent2.putExtra("ItemPedido", itemPedido);

                intent2.putExtra("DescProduto", "Recheios: Amendoim + castanha");


                startActivity(intent2);
                finish();
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser usuarioLogado = mAuth.getCurrentUser();

        if (usuarioLogado == null) {
            Intent i = new Intent(EscolherRecheioActivity.this, LoginActivity.class);
            startActivity(i);
            finish();
        }

        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("itens_cardapio").child("4");
        mDatabaseReference.keepSynced(true);
        mListViewRecheios = (RecyclerView) findViewById(R.id.list_recheios);
        mListViewRecheios.setHasFixedSize(true);
        mListViewRecheios.setLayoutManager(new LinearLayoutManager(this));

        final FirebaseRecyclerAdapter<ItemCardapio, EscolherRecheioActivity.RecheiosViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<ItemCardapio, RecheiosViewHolder>(
                ItemCardapio.class, R.layout.list_icon_recheios, EscolherRecheioActivity.RecheiosViewHolder.class, mDatabaseReference
        ) {

            @Override
            public void onBindViewHolder(final EscolherRecheioActivity.RecheiosViewHolder viewHolder, final int position) {
                super.onBindViewHolder(viewHolder, position);

                YoYo.with(Techniques.BounceInUp).playOn(viewHolder.card_recheio);
            }

            @Override
            protected void populateViewHolder(final EscolherRecheioActivity.RecheiosViewHolder viewHolder, final ItemCardapio model, int position) {

                final TextView qtd_item = (TextView) viewHolder.mView.findViewById(R.id.qtd_item_recheio);

                final ImageView selected_um = (ImageView) viewHolder.mView.findViewById(R.id.item_selecionado_recheio_um);
                final ImageView selected_dois = (ImageView) viewHolder.mView.findViewById(R.id.item_selecionado_recheio_dois);
                final ImageView selected_tres = (ImageView) viewHolder.mView.findViewById(R.id.item_selecionado_recheio_tres);
                final ImageView selected_quatro = (ImageView) viewHolder.mView.findViewById(R.id.item_selecionado_recheio_quatro);

                viewHolder.setNome(model.getNome());
                viewHolder.setImagem(EscolherRecheioActivity.this, model.getRef_img());


                if (qtdRecheiosParaEscolher > 1) {
                    viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    });

                } else {
                    viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            viewHolder.selectedOne();
                        }
                    });
                }

            }

        };

        mListViewRecheios.setAdapter(firebaseRecyclerAdapter);

    }

    public static class RecheiosViewHolder extends RecyclerView.ViewHolder {

        View mView;
        CardView card_recheio;
        ImageView selected_item;

        public RecheiosViewHolder(final View itemView) {
            super(itemView);

            mView = itemView;
            card_recheio = (CardView) mView.findViewById(R.id.card_adicionais);
            selected_item = (ImageView) mView.findViewById(R.id.item_selecionado_recheio_um);

        }

        public void setNome(String nome) {

            TextView item_nome = (TextView) mView.findViewById(R.id.item_nome_recheio);
            item_nome.setText(nome);

        }

        public void setImagem(Context context, String url) {
            ImageView item_ref_image = (ImageView) mView.findViewById(R.id.item_img_recheio);
            Picasso.with(context).load(url).into(item_ref_image);
        }

        public void selectedOne(){
            selected_item.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(EscolherRecheioActivity.this, CardapioMainActivity.class);
                startActivity(intent);
                finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(EscolherRecheioActivity.this, CardapioMainActivity.class);
        startActivity(intent);

        finish();
    }

    public void selectQuatroOpcoes(TextView qtd_item, TextView selected_um, TextView selected_dois, TextView selected_tres, TextView selected_quatro) {
        if (qtd_item.getText().equals("0")) {
            selected_um.setVisibility(View.VISIBLE);
            qtd_item.setText("1");
            return;
        } else if (qtd_item.getText().equals("1")) {
            selected_dois.setVisibility(View.VISIBLE);
            qtd_item.setText("2");
            return;
        } else if (qtd_item.getText().equals("2")) {
            selected_tres.setVisibility(View.VISIBLE);
            qtd_item.setText("3");
            return;
        } else if (qtd_item.getText().equals("3")) {
            selected_quatro.setVisibility(View.VISIBLE);
            qtd_item.setText("3");
            return;
        }
    }

}