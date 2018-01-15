package br.com.belongapps.appdelivery.cardapioOnline.activitys;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
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
import java.util.Arrays;
import java.util.List;

import br.com.belongapps.appdelivery.R;
import br.com.belongapps.appdelivery.cardapioOnline.dao.CarrinhoDAO;
import br.com.belongapps.appdelivery.cardapioOnline.model.ItemPedido;
import br.com.belongapps.appdelivery.cardapioOnline.model.Pao;
import br.com.belongapps.appdelivery.cardapioOnline.model.RecheioAcai;
import br.com.belongapps.appdelivery.firebaseAuthApp.FirebaseAuthApp;
import br.com.belongapps.appdelivery.seguranca.activities.LoginActivity;
import br.com.belongapps.appdelivery.util.Print;
import br.com.belongapps.appdelivery.util.StringUtil;

public class DetalhesdoItemActivity extends AppCompatActivity {

    private Button addAoCarrinho;
    private Toolbar mToolbar;
    private ProgressBar mProgressBar;

    //Parâmetros
    private ItemPedido itemPedido; //Pedido
    private String observacao = ""; //enviar
    private int quantidade = 1; //enviar
    private String telaAnterior = "";

    //Views
    private CardView cardDetalhesItem;
    private CardView cardObservacoesItem;
    private CardView cardQuantidadeItem;

    private ImageView imgDetalheProduto;
    private TextView nomeDetalheProduto;
    private TextView descDetalheProduto;
    private TextView valorDetalheProduto;
    private TextView observacaoDetalheProduto;
    private TextView qtdProdutoDetalheProduto;
    private Button btAumentarQtd;
    private Button btDiminuirQtd;
    private Button btVoltar;

    //Sanduiche
    private CardView cardTipoPaoItem;
    private RadioGroup radioGroupPao;
    private List<RadioButton> opcoesDePaes;
    private double valorDoPao = 0.0;//
    private String paoSelecinado = "";

    //Açai
    private CardView cardItensAcai;
    private TextView itensAcai;
    private Button btAlterarItens;
    private ArrayList<RecheioAcai> recheiosPadrao;
    private ArrayList<RecheioAcai> todosRecheios;

    //Combos
    private CardView cardBebidaCombo;
    private TextView nomeBebidacombo;
    private Button btSelecionarBebidaCombo;
    private Button btAlterarBebida;
    private Spinner bebidasSpinner;
    private String bebidaSelecionada;
    private boolean comboCombebidas = false;

    private CardView cardPizzaCombo;
    private TextView nomePizzacombo;
    private Button btSelecionarPizzaCombo;
    private Button btAlterarPizza;
    private String pizzaSelecionada;
    private boolean comboComPizza = false;

    private double valorTotal;//
    private double valorUnitario;//

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_item);

        mToolbar = findViewById(R.id.toolbar_detalhes_item);
        mToolbar.setTitle("Detalhes do Produto");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getParametros(); //Setar parametros recebidos

        initViews();

        pupulateViewDetalhes();

        /*Eventos dos Botoes de Quantidade*/
        btDiminuirQtd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (quantidade > 1) {
                    quantidade--;
                    qtdProdutoDetalheProduto.setText(String.valueOf(quantidade));

                    valorTotal = valorTotal - (valorUnitario + valorDoPao);

                    atualizarViewTotal();
                }
            }
        });

        btAumentarQtd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quantidade++;
                qtdProdutoDetalheProduto.setText(String.valueOf(quantidade));

                valorTotal = quantidade * (valorUnitario + valorDoPao);

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
                } else if (comboCombebidas && pizzaSelecionada == null) {
                    Toast.makeText(DetalhesdoItemActivity.this, "Selecione a pizza de sua preferência!", Toast.LENGTH_SHORT).show();
                } else if (comboCombebidas && bebidaSelecionada == null) {
                    Toast.makeText(DetalhesdoItemActivity.this, "Selecione a bebida de sua preferência!", Toast.LENGTH_SHORT).show();
                } else {
                    adicionarAoCarrinho();
                }

            }
        });

        btVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DetalhesdoItemActivity.this, CardapioMainActivity.class);
                startActivity(i);
                finish();
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
            itemPedido.setValor_unit(valorTotal); //Seta valor unitario final

            //SET DESC SANDUICHE
            itemPedido.setDescricao(itemPedido.getDescricao() + " (" + paoSelecinado + ")");
        }

        if (isPedidoCombo()) {
            String descricaoCombo = "";

            if (pizzaSelecionada != null){
                descricaoCombo += "Pizza " + pizzaSelecionada;
            }

            if (bebidaSelecionada != null) {
                if (pizzaSelecionada != null) {
                    descricaoCombo += " + " + bebidaSelecionada;
                } else{
                    descricaoCombo += bebidaSelecionada;
                }
            }

            itemPedido.setDescricao(descricaoCombo);
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

    public void atualizarViewTotal() {
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

        mProgressBar = findViewById(R.id.progressbar_detalhes_do_item);

        getParametros();

        //Verifica se o pedido é de Sanduíche
        if (isPedidoSanduiche()) {
            buscarKeysPaesdoSanduiche();
        }

        if (isPedidoCombo()) {
            verificarItensAEscolher();
        }

    }

    private boolean isPedidoSanduiche() {
        return getIntent().getStringExtra("sanduiche") != null;
    }

    private boolean isPedidoAcai() {
        return getIntent().getStringExtra("acai") != null;
    }

    private boolean isPedidoCombo() {
        return getIntent().getStringExtra("Combo") != null;
    }

    private void buscarKeysPaesdoSanduiche() {
        openProgressBar();

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

    private void verificarItensAEscolher() {
        openProgressBar();

        Print.logError("VERIFICANDO OS ITEM Q DEVEM SER ESCOLHIDOS...");

        DatabaseReference mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        mDatabaseReference = mDatabaseReference.child("itens_cardapio").child("11").child(itemPedido.getKeyItem());

        ValueEventListener temBebidasValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Boolean temBebidas = dataSnapshot.child("bebidas_permitidas").hasChildren();
                Boolean temPizzas = dataSnapshot.child("pizzas_permitidas").hasChildren();

                if (temBebidas) {
                    Print.logError("TEM BEBIDAS...");
                    comboCombebidas = true;
                    List<String> nomeBebidas = new ArrayList<>();
                    DataSnapshot dataBebidas = dataSnapshot.child("bebidas_permitidas");

                    for (DataSnapshot nomeData : dataBebidas.getChildren()) {
                        String bebida = nomeData.getValue(String.class);
                        nomeBebidas.add(bebida);
                    }

                    mostrarCardEscolherBebida(nomeBebidas);
                }

                if (temPizzas) {
                    Print.logError("TEM PIZZAS...");

                    comboComPizza = true;
                    List<String> keyPizzas = new ArrayList<>();
                    DataSnapshot dataBebidas = dataSnapshot.child("pizzas_permitidas");

                    for (DataSnapshot nomeData : dataBebidas.getChildren()) {
                        String keyPizza = nomeData.getValue(String.class);
                        keyPizzas.add(keyPizza);
                    }

                   for (String pizza : keyPizzas) {
                        Print.logError("PIZZA: " + pizza);
                   }

                    mostrarCardEscolherPizza(keyPizzas);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        mDatabaseReference.addValueEventListener(temBebidasValueEventListener);

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
        closeProgressBar();

        initViews();

        cardTipoPaoItem.setVisibility(View.VISIBLE); //Mostrar card dos paes
        opcoesDePaes = new ArrayList<>();

        for (final Pao pao : paesDoSanduiche) {
            final RadioButton radioButton = new RadioButton(this);

            radioButton.setText(pao.getNome());

            if (pao.getValor_unit() > 0) {
                radioButton.setText(pao.getNome() + " ( " + StringUtil.formatToMoeda(pao.getValor_unit()) + ")");
            }

            opcoesDePaes.add(radioButton);

            radioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    valorTotal = quantidade * (itemPedido.getValor_unit() + pao.getValor_unit());
                    valorDetalheProduto.setText(StringUtil.formatToMoeda(valorTotal));
                    valorDoPao = pao.getValor_unit();
                    paoSelecinado = pao.getNome();
                }
            });

            radioGroupPao.addView(radioButton);
        }

    }

    private void mostrarCardEscolherPizza(final List<String> keyPizzas) {
        closeProgressBar();
        initViewsCombos();
        cardPizzaCombo.setVisibility(View.VISIBLE);
        btSelecionarPizzaCombo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //MANDAR LISTA DE ITENS PARA A TELA DE ESCOLHA

                Intent i = new Intent(DetalhesdoItemActivity.this, EscolherItemComboActivity.class);
                i.putExtra("ItemPedido", itemPedido);
                i.putExtra("ItemNome", "a Pizza");
                i.putStringArrayListExtra("KeyItens", new ArrayList<>(keyPizzas));
                startActivity(i);
                finish();
            }
        });

        atualizarViewsCardSelecionarPizza(keyPizzas);

    }

    private void mostrarCardEscolherBebida(final List<String> nomeBebidas) {
        closeProgressBar();
        initViewsCombos();
        cardBebidaCombo.setVisibility(View.VISIBLE);
        btSelecionarBebidaCombo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exibirDialogSelecionarBebida(nomeBebidas);
            }
        });
    }

    private void exibirDialogSelecionarBebida(final List<String> nomeBebidas) {
        //EXIBIR DIALOG ESCOLHER BEBIDA
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        AlertDialog.Builder mBilder = new AlertDialog.Builder(DetalhesdoItemActivity.this, R.style.MyDialogTheme);
        View layoutDialog = inflater.inflate(R.layout.dialog_selecionar_bebida, null);

        bebidasSpinner = layoutDialog.findViewById(R.id.bebidas_spinner);
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter(DetalhesdoItemActivity.this, android.R.layout.simple_spinner_dropdown_item, nomeBebidas);
        bebidasSpinner.setAdapter(adapter);

        Button confirmar = layoutDialog.findViewById(R.id.bt_confirmar_bebida);
        Button fechar = layoutDialog.findViewById(R.id.bt_fechar_confirmar_bebida);

        mBilder.setView(layoutDialog);
        final AlertDialog dialogEscolherBebida = mBilder.create();
        dialogEscolherBebida.show();

        confirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogEscolherBebida.dismiss();
                atualizarViewsCardSelecionarBebida(nomeBebidas);
            }
        });

        fechar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogEscolherBebida.dismiss();
            }
        });

        bebidasSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                bebidaSelecionada = adapterView.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void atualizarViewsCardSelecionarBebida(final List<String> nomeBebidas) {
        if (bebidaSelecionada != null) {
            nomeBebidacombo = findViewById(R.id.nome_bebida_combo);
            nomeBebidacombo.setText(bebidaSelecionada);
            nomeBebidacombo.setVisibility(View.VISIBLE);

            btSelecionarBebidaCombo.setVisibility(View.GONE);
            btAlterarBebida.setVisibility(View.VISIBLE);

            btAlterarBebida.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                exibirDialogSelecionarBebida(nomeBebidas);
                }
            });
        } else {
            nomeBebidacombo.setVisibility(View.GONE);
            btAlterarBebida.setVisibility(View.GONE);
            btSelecionarBebidaCombo.setVisibility(View.VISIBLE);

        }
    }

    private void atualizarViewsCardSelecionarPizza(final List<String> keyPizzas) {

        initViewsCombos();
        nomePizzacombo = findViewById(R.id.nome_pizza_combo);
        btAlterarPizza = findViewById(R.id.bt_alterar_pizza);

        if (pizzaSelecionada != null) {
            nomePizzacombo.setText(pizzaSelecionada);
            nomePizzacombo.setVisibility(View.VISIBLE);

            btSelecionarPizzaCombo.setVisibility(View.GONE);
            btAlterarPizza.setVisibility(View.VISIBLE);

            btAlterarPizza.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(DetalhesdoItemActivity.this, EscolherItemComboActivity.class);
                    i.putExtra("ItemNome", "a Pizza");
                    i.putExtra("ItemPedido", itemPedido);
                    i.putExtra("ItemSelecionado", pizzaSelecionada);
                    i.putStringArrayListExtra("KeyItens", new ArrayList<>(keyPizzas));
                    startActivity(i);
                    finish();
                }
            });

        } else {
            nomePizzacombo.setVisibility(View.GONE);
            btAlterarPizza.setVisibility(View.GONE);
            btSelecionarPizzaCombo.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
               /* if (telaAnterior != null) {
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
                } else {*/
                Intent intent = new Intent(DetalhesdoItemActivity.this, CardapioMainActivity.class);
                startActivity(intent);
                finish();
//                }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(DetalhesdoItemActivity.this, CardapioMainActivity.class);
        startActivity(intent);
        finish();
    }

    public void getParametros() {

        telaAnterior = getIntent().getStringExtra("TelaAnterior");
        itemPedido = getIntent().getExtras().getParcelable("ItemPedido");

        //Acai
        todosRecheios = getIntent().getParcelableArrayListExtra("recheiosSelecionados");
        recheiosPadrao = getIntent().getParcelableArrayListExtra("recheiosPadrao");

        //Combo
        pizzaSelecionada = getIntent().getStringExtra("ItemSelecionado");

        //SetValorTotal
        valorTotal = itemPedido.getValor_unit();

        //Set Valor Unitario
        valorUnitario = itemPedido.getValor_unit();
    }

    public void initViews() {
        //Cads
        cardDetalhesItem = findViewById(R.id.card_detalhes_item);
        cardDetalhesItem.setVisibility(View.VISIBLE);
        cardObservacoesItem = findViewById(R.id.card_observacoes_item);
        cardObservacoesItem.setVisibility(View.VISIBLE);
        cardQuantidadeItem = findViewById(R.id.card_quantidade_item);
        cardQuantidadeItem.setVisibility(View.VISIBLE);

        //TextViews
        nomeDetalheProduto = findViewById(R.id.nome_produto_detalhe_item);
        descDetalheProduto = findViewById(R.id.descricao_produto_detalhe_item);
        imgDetalheProduto = findViewById(R.id.img_produto_detalhe_item);
        valorDetalheProduto = findViewById(R.id.valor_produto_detalhe_item);
        observacaoDetalheProduto = findViewById(R.id.observacao_produto_detalhe_item);

        observacaoDetalheProduto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                observacaoDetalheProduto.requestFocus();
            }
        });

        qtdProdutoDetalheProduto = findViewById(R.id.txt_qtd_item_delathe_produto);

        //Botoes
        btDiminuirQtd = findViewById(R.id.bt_diminuir_qtd_item_detalhe_produto);
        btAumentarQtd = findViewById(R.id.bt_aumentar_qtd_item_detalhe_produto);
        addAoCarrinho = findViewById(R.id.bt_proximo_detalhes);
        btVoltar = findViewById(R.id.bt_voltar_detalhes);

        //SANDUICHES
        initViewsSanduiches();

        //ACAI
        initViewAcai();

        //COMBOS
        initViewsCombos();

    }

    private void initViewsSanduiches() {
        if (isPedidoSanduiche()) {
            cardTipoPaoItem = findViewById(R.id.card_tipo_pao_item);
            radioGroupPao = findViewById(R.id.radio_group_pao);
        }
    }

    private void initViewAcai() {
        if (isPedidoAcai()) {
            cardItensAcai = findViewById(R.id.card_itens_acai);
            itensAcai = findViewById(R.id.itens_acai);
            btAlterarItens = findViewById(R.id.bt_alterar_itens);

            btAlterarItens.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(DetalhesdoItemActivity.this, MontagemAcaiActivity.class);
                    intent.putExtra("acaiKey", itemPedido.getKeyItem());
                    intent.putExtra("acaiNome", itemPedido.getNome());
                    intent.putExtra("acaiTotal", itemPedido.getValor_unit());
                    intent.putExtra("acaiImg", itemPedido.getRef_img());
                    intent.putExtra("acai", itemPedido);

                    intent.putParcelableArrayListExtra("recheiosSelecionados", todosRecheios);
                    intent.putParcelableArrayListExtra("recheiosPadrao", recheiosPadrao);

                    startActivity(intent);
                    finish();
                }
            });
        }
    }

    private void initViewsCombos() {
        if (isPedidoCombo()) {
            cardPizzaCombo = findViewById(R.id.card_pizza_combo);
            nomePizzacombo = findViewById(R.id.nome_pizza_combo);
            btSelecionarPizzaCombo = findViewById(R.id.bt_selecionar_pizza_combo);
            btAlterarPizza = findViewById(R.id.bt_alterar_pizza);

            cardBebidaCombo = findViewById(R.id.card_bebida_combo);
            nomeBebidacombo = findViewById(R.id.nome_bebida_combo);
            btSelecionarBebidaCombo = findViewById(R.id.bt_selecionar_bebida_combo);
            btAlterarBebida = findViewById(R.id.bt_alterar_bebida);

        }
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

        if (itemPedido.getDescricao() == null || isPedidoAcai()) { // Ocultar campo descrição se em branco ou pedido açai
            descDetalheProduto.setVisibility(View.GONE);
        } else {
            descDetalheProduto.setText(itemPedido.getDescricao());
        }

        valorDetalheProduto.setText(StringUtil.formatToMoeda(valorTotal));
        qtdProdutoDetalheProduto.setText(String.valueOf(quantidade));

        //Verifica se o pedido é de Açai e exibe recheios
        if (isPedidoAcai()) {
            cardItensAcai.setVisibility(View.VISIBLE);
            itensAcai.setText(itemPedido.getDescricao());
        }

    }

    private void openProgressBar() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    private void closeProgressBar() {
        mProgressBar.setVisibility(View.GONE);
    }

}
