package br.com.belongapps.appdelivery.cardapioOnline.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import br.com.belongapps.appdelivery.R;
import br.com.belongapps.appdelivery.cardapioOnline.dao.CarrinhoDAO;
import br.com.belongapps.appdelivery.cardapioOnline.model.ItemPedido;
import br.com.belongapps.appdelivery.cardapioOnline.model.Pao;
import br.com.belongapps.appdelivery.firebaseAuthApp.FirebaseAuthApp;
import br.com.belongapps.appdelivery.seguranca.activities.LoginActivity;
import br.com.belongapps.appdelivery.util.Print;
import br.com.belongapps.appdelivery.util.StringUtil;

public class DetalhesdoItemActivity extends AppCompatActivity {

    private Button addAoCarrinho;
    private Toolbar mToolbar;

    //Parâmetros
    private ItemPedido itemPedido; //Pedido
    private String tipoDoPedido;
    private String observacao = ""; //enviar
    private int quantidade = 1; //enviar
    private String telaAnterior = "";
    private String tamPizza = "";
    private String tipoPizza = "";
    private String categoria = "";

    //Views
    private ImageView imgDetalheProduto;
    private TextView nomeDetalheProduto;
    private TextView descDetalheProduto;
    private TextView valorDetalheProduto;
    private TextView observacaoDetalheProduto;
    private TextView qtdProdutoDetalheProduto;
    private Button btAumentarQtd;
    private Button btDiminuirQtd;

    //Sanduiche
    private CardView cardTipoPaoItem;
    private RadioGroup radioGroupPao;
    private List<RadioButton> opcoesDePaes;

    private double valorTotal;//
    private double valorUnitario;//

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_item);

        getParametros(); //Setar parametros recebidos

        mToolbar = (Toolbar) findViewById(R.id.toolbar_detalhes_item);
        mToolbar.setTitle("Detalhes do Produto");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initViews();

        pupulateViewDetalhes();

        /*Eventos dos Botoes de Quantidade*/
        btDiminuirQtd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (quantidade > 1) {
                    quantidade--;
                    qtdProdutoDetalheProduto.setText(String.valueOf(quantidade));

                    valorTotal = valorTotal - valorUnitario;

                    atualizarViewTotal();
                }
            }
        });

        btAumentarQtd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quantidade++;
                qtdProdutoDetalheProduto.setText(String.valueOf(quantidade));

                valorTotal = quantidade * valorUnitario;

                atualizarViewTotal();

            }
        });

        /*---*/

        addAoCarrinho.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isPedidoSanduiche()) {
                    if (!opcaoDePaoSelecionado()) { //Pão não selecionado (Pedido de Sanduiches)
                        Toast.makeText(DetalhesdoItemActivity.this, "Selecione uma tipo de pão!", Toast.LENGTH_SHORT).show();
                    } else {
                        adicionarAoCarrinho();
                    }
                } else {
                    adicionarAoCarrinho();
                }

            }
        });

    }

    private void adicionarAoCarrinho() {
        CarrinhoDAO crud = new CarrinhoDAO(getBaseContext());

        observacao = observacaoDetalheProduto.getText().toString();

        if (!observacao.isEmpty()) {
            itemPedido.setObservacao(observacao);
        }

        itemPedido.setQuantidade(quantidade);
        itemPedido.setValor_total(valorTotal); //Seta o total no pedido

        if (isPedidoSanduiche()) {
            //SET DESC SANDUICHE
            String desc = itemPedido.getDescricao();
            itemPedido.setDescricao(desc += " (" + paoSelecionado() + ")");
        }

        Intent intent = new Intent(DetalhesdoItemActivity.this, CarrinhoActivity.class);

        //Salvar Item no Carrinho
        Log.println(Log.ERROR, "RESULT: ", crud.salvarItem(itemPedido));

        Print.logError("ITEMPEDIDO KEY: " + itemPedido.getKeyItem());

        startActivity(intent);
        finish();
    }

    private boolean opcaoDePaoSelecionado() {
        for (RadioButton opcao : opcoesDePaes) {
            if (opcao.isChecked()) {
                return true;
            }
        }

        return false;
    }


    private String paoSelecionado() {
        for (RadioButton opcao : opcoesDePaes) {
            if (opcao.isChecked()) {
                return opcao.getText().toString();
            }
        }

        return "Pão Bola";
    }

    public void atualizarViewTotal(){
        valorDetalheProduto.setText(StringUtil.formatToMoeda(valorTotal));
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (FirebaseAuthApp.getUsuarioLogado() == null) {
            Intent i = new Intent(DetalhesdoItemActivity.this, LoginActivity.class);
            startActivity(i);
            finish();
        }

        //Verifica se o pedido é de Sanduíche
        boolean isPedidoSanduiche = isPedidoSanduiche();
        if (isPedidoSanduiche) {
            buscarKeysPaesdoSanduiche();
        }
    }

    private boolean isPedidoSanduiche() {
        Print.logError("SANDUICHE SELECIONADO: " + getIntent().getStringExtra("sanduiche"));

        if (getIntent().getStringExtra("sanduiche") != null) {
            return true;
        }

        return false;
    }

    private void buscarKeysPaesdoSanduiche() {
        Print.logError("BUSCANDO PÃES...");

        final List<String> keyPaesSanduiche = new ArrayList<>();

        DatabaseReference mDatabaseReference = FirebaseDatabase.getInstance().getReference();

        mDatabaseReference = mDatabaseReference.child("itens_cardapio").child("7");

        ValueEventListener paesValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    String keyPao = data.getValue(String.class);
                    keyPaesSanduiche.add(keyPao);
                }

                buscarPaesDoSanduiche(keyPaesSanduiche);
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        mDatabaseReference.child(getIntent().getStringExtra("sanduiche")).child("paes_aceitos").addValueEventListener(paesValueEventListener);

    }

    private void buscarPaesDoSanduiche(final List<String> keysPaesSanduiche) {
        DatabaseReference mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        mDatabaseReference = mDatabaseReference.child("itens_cardapio").child("10");

        final List<Pao> paesDoSanduiche = new ArrayList<>();

        ValueEventListener paoesValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    for (String keyPaoSanduiche : keysPaesSanduiche) {
                        if (keyPaoSanduiche.equals(data.getKey())) {
                            Pao pao = data.getValue(Pao.class);
                            Print.logError(pao.getNome());
                            paesDoSanduiche.add(pao);
                        }
                    }
                }

                mostrarPaesParaEscolha(paesDoSanduiche);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        mDatabaseReference.addValueEventListener(paoesValueEventListener);
    }

    private void mostrarPaesParaEscolha(List<Pao> paesDoSanduiche) {
        initViews();

        cardTipoPaoItem.setVisibility(View.VISIBLE); //Mostrar card dos paes
        opcoesDePaes = new ArrayList<>();

        for (final Pao pao : paesDoSanduiche) {
            final RadioButton radioButton = new RadioButton(this);

            if (!pao.getNome().equals("Pão Bola")) {
                radioButton.setText(pao.getNome() + " (+ " + StringUtil.formatToMoeda(pao.getValor_unit()) + ")");
            } else {
                radioButton.setText(pao.getNome());
            }

            opcoesDePaes.add(radioButton);

            radioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!pao.getNome().equals("Pão Bola")) {
                        valorTotal = itemPedido.getValor_unit() + pao.getValor_unit();
                        valorDetalheProduto.setText(StringUtil.formatToMoeda(valorTotal));

                    } else {
                        valorTotal = itemPedido.getValor_unit();
                        valorDetalheProduto.setText(StringUtil.formatToMoeda(valorTotal));
                    }
                }
            });

            radioGroupPao.addView(radioButton);
        }

    }

    private void checkedRadioButton(RadioButton radioSelecionado, List<RadioButton> opcoes) {

        for (RadioButton opcao : opcoes) {
            if (radioSelecionado.getText().toString().equals(opcao.getText().toString())) {
                radioSelecionado.setChecked(true);
            } else {
                radioSelecionado.setChecked(false);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (telaAnterior != null) {
                    if (telaAnterior.equals("MontagemAcai")) {
                        Intent intent = new Intent(DetalhesdoItemActivity.this, MontagemAcaiActivity.class);
                        intent.putExtra("acaiNome", itemPedido.getNome());
                        intent.putExtra("acaiImg", itemPedido.getRef_img());
                        intent.putExtra("acaiKey", itemPedido.getKeyItem());
                        intent.putExtra("acaiTotal", itemPedido.getValor_unit());

                        startActivity(intent);
                        finish();
                    } else {
                        Intent intent = new Intent(DetalhesdoItemActivity.this, CardapioMainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                } else {
                    Intent intent = new Intent(DetalhesdoItemActivity.this, CardapioMainActivity.class);
                    startActivity(intent);
                    finish();
                }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if (telaAnterior != null) {
            if (telaAnterior.equals("MontagemAcai")) {
                Intent intent = new Intent(DetalhesdoItemActivity.this, MontagemAcaiActivity.class);
                intent.putExtra("acaiNome", itemPedido.getNome());
                intent.putExtra("acaiImg", itemPedido.getRef_img());
                intent.putExtra("acaiKey", itemPedido.getKeyItem());
                intent.putExtra("acaiTotal", itemPedido.getValor_unit());

                startActivity(intent);
                finish();
            } else {
                Intent intent = new Intent(DetalhesdoItemActivity.this, CardapioMainActivity.class);
                startActivity(intent);
                finish();
            }
        } else {
            Intent intent = new Intent(DetalhesdoItemActivity.this, CardapioMainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    public void getParametros() {

        telaAnterior = getIntent().getStringExtra("TelaAnterior");
        itemPedido = getIntent().getExtras().getParcelable("ItemPedido");
        categoria = getIntent().getStringExtra("Categoria");

        //Pizzas
        tamPizza = getIntent().getStringExtra("TamPizza");
        tipoPizza = getIntent().getStringExtra("TipoPizza");

        //SetValorTotal
        valorTotal = itemPedido.getValor_unit();

        //Set Valor Unitario
        valorUnitario = itemPedido.getValor_unit();
    }

    public void initViews() {
        //TextViews
        nomeDetalheProduto = (TextView) findViewById(R.id.nome_produto_detalhe_item);
        descDetalheProduto = (TextView) findViewById(R.id.descricao_produto_detalhe_item);
        imgDetalheProduto = (ImageView) findViewById(R.id.img_produto_detalhe_item);
        valorDetalheProduto = (TextView) findViewById(R.id.valor_produto_detalhe_item);
        observacaoDetalheProduto = (TextView) findViewById(R.id.observacao_produto_detalhe_item);

        observacaoDetalheProduto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                observacaoDetalheProduto.requestFocus();
            }
        });

        qtdProdutoDetalheProduto = (TextView) findViewById(R.id.txt_qtd_item_delathe_produto);

        //Botoes
        btDiminuirQtd = (Button) findViewById(R.id.bt_diminuir_qtd_item_detalhe_produto);
        btAumentarQtd = (Button) findViewById(R.id.bt_aumentar_qtd_item_detalhe_produto);
        addAoCarrinho = (Button) findViewById(R.id.bt_add_ao_carrinho);


        //PAO
        cardTipoPaoItem = (CardView) findViewById(R.id.card_tipo_pao_item);
        radioGroupPao = (RadioGroup) findViewById(R.id.radio_group_pao);

    }

    public void pupulateViewDetalhes() {
        //Exibir Imagem
        Picasso.with(DetalhesdoItemActivity.this).load(itemPedido.getRef_img()).networkPolicy(NetworkPolicy.OFFLINE).into(imgDetalheProduto, new Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError() {
                Picasso.with(DetalhesdoItemActivity.this).load(itemPedido.getRef_img()).into(imgDetalheProduto);
            }
        });

        //nome, descricao e valor
        nomeDetalheProduto.setText(itemPedido.getNome());

        if (itemPedido.getDescricao() != null) { // Ocultar campo descrição se em branco
            descDetalheProduto.setText(itemPedido.getDescricao());
        } else {
            descDetalheProduto.setVisibility(View.GONE);
        }

        valorDetalheProduto.setText(StringUtil.formatToMoeda(valorTotal));
        qtdProdutoDetalheProduto.setText(String.valueOf(quantidade));

        /*//SANDUICHES
        if (getIntent().getStringExtra("sanduiche") != null) {
            cardTipoPaoItem.setVisibility(View.VISIBLE);
        }*/
    }

    public double calcularValorTotalDoItem(int quantidade, double valorProduto) {
        return quantidade * valorProduto;
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch (((RadioButton) view).getText().toString()) {
            case "Pão Árabe":
                if (checked)
                    Print.logError("SELECIONOU O PÃO ÁRABE");
                break;
            case "Pão Bola":
                if (checked)
                    Print.logError("SELECIONOU O PÃO BOLA");
                break;
        }
    }
}
