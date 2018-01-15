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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import br.com.belongapps.appdelivery.R;
import br.com.belongapps.appdelivery.cardapioOnline.dao.CarrinhoDAO;
import br.com.belongapps.appdelivery.cardapioOnline.model.ItemPedido;
import br.com.belongapps.appdelivery.util.Print;
import br.com.belongapps.appdelivery.util.StringUtil;

public class DetalhesdoItemPizzaMMActivity extends AppCompatActivity {

    private Toolbar mToolbar;

    //Parâmetros
    private ItemPedido itemPedido; //Pedido

    //enviar
    private int tamPizza;
    private String tipoPizza;
    private int quantidade = 1;

    //Views
    private TextView tamanhoPizza;
    private TextView tipodaPizza;

    //Metade 01
    private ImageView imgMetade1;
    private TextView nomeMetade1, descMetade1, obsMetade1, valorMetade1;

    //Metade 02
    private ImageView imgMetade2;
    private TextView nomeMetade2, descMetade2, obsMetade2, valorMetade2;

    //Metade 03
    private LinearLayout txtDescMetade3;
    private CardView cardSabor3;
    private ImageView imgMetade3;
    private TextView nomeMetade3, descMetade3, obsMetade3, valorMetade3;

    //Metade 04
    private LinearLayout txtDescMetade4;
    private CardView cardSabor4;
    private ImageView imgMetade4;
    private TextView nomeMetade4, descMetade4, obsMetade4, valorMetade4;

    //Observação
    private TextView observacaoProdutoDetalhePizza;

    private TextView qtdPizza;
    private Button btAumentarQtd;
    private Button btDiminuirQtd;

    //BUTTONS
    private TextView buttonEditarMetade1;
    private TextView buttonEditarMetade2;
    private TextView buttonEditarMetade3;
    private TextView buttonEditarMetade4;

    private Button btAdicionarAoCarrinho;
    private TextView btCancelarPedidoPizza;
    private TextView textTotalPizza;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_pizza_mm);

        getParametros(); //Setar parametros recebidos
        initViews();

        mToolbar = findViewById(R.id.toolbar_detalhes_pizza_mm);
        mToolbar.setTitle("Detalhes do Produto");
        setSupportActionBar(mToolbar);

        pupulateViewDetalhes();

        /*Eventos dos Botoes de Quantidade*/
        btDiminuirQtd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (quantidade > 1) {
                    quantidade--;
                    qtdPizza.setText(String.valueOf(quantidade));
                }
            }
        });

        btAumentarQtd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quantidade++;
                qtdPizza.setText(String.valueOf(quantidade));
            }
        });

        btAdicionarAoCarrinho.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CarrinhoDAO crud = new CarrinhoDAO(getBaseContext());

                itemPedido.setQuantidade(quantidade);
                itemPedido.setObservacao(observacaoProdutoDetalhePizza.getText().toString());

                if (tipoPizza.equals("Metade-Metade")) {
                    // itemPedido.setValor_unit(itemPedido.getValor_unit() + itemPedido.getValorMetade2()); // Valor das Metades
                    itemPedido.setDescricao(itemPedido.getNome() + " + " + itemPedido.getNomeMetade2());
                }

                if (tipoPizza.equals("Três Sabores")) {
                    //itemPedido.setValor_unit(itemPedido.getValor_unit() + itemPedido.getValorMetade2() + itemPedido.getValorMetade3()); // Valor das Metades
                    itemPedido.setDescricao(itemPedido.getNome() + " + " + itemPedido.getNomeMetade2() + " + " + itemPedido.getNomeMetade3());
                }

                if (tipoPizza.equals("Quatro Sabores")) {
                    //itemPedido.setValor_unit(itemPedido.getValor_unit() + itemPedido.getValorMetade2() + itemPedido.getValorMetade3() + itemPedido.getValorMetade4()); // Valor das Metades
                    itemPedido.setDescricao(itemPedido.getNome() + " + " + itemPedido.getNomeMetade2() + " + " + itemPedido.getNomeMetade3() + " + " + itemPedido.getNomeMetade4());
                }

                itemPedido.setValor_unit(calcularValorDaPizza());
                itemPedido.setValor_total(quantidade * itemPedido.getValor_unit()); //Qtd * Valor das Metades
                itemPedido.setNome(getTamanho());

                Intent intent = new Intent(DetalhesdoItemPizzaMMActivity.this, CarrinhoActivity.class);

                //Salvar Item no Carrinho
                Log.println(Log.ERROR, "RESULT: ", crud.salvarItem(itemPedido));

                startActivity(intent);
                finish();

            }
        });

        // BOTOES DE EDICAO
        buttonEditarMetade1 = findViewById(R.id.button_editar_metade1);
        buttonEditarMetade1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(DetalhesdoItemPizzaMMActivity.this, EditarPizzaActivity.class);
                i.putExtra("ItemPedido", itemPedido);
                i.putExtra("TamPizza", tamPizza);
                i.putExtra("TipoPizza", tipoPizza);
                i.putExtra("Metade", "01");
                startActivity(i);
                finish();
            }
        });

        buttonEditarMetade2 = findViewById(R.id.button_editar_metade2);
        buttonEditarMetade2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(DetalhesdoItemPizzaMMActivity.this, EditarPizzaActivity.class);
                i.putExtra("ItemPedido", itemPedido);
                i.putExtra("TamPizza", tamPizza);
                i.putExtra("TipoPizza", tipoPizza);
                i.putExtra("Metade", "02");
                startActivity(i);
                finish();
            }
        });

        buttonEditarMetade3 = findViewById(R.id.button_editar_metade3);
        buttonEditarMetade3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(DetalhesdoItemPizzaMMActivity.this, EditarPizzaActivity.class);
                i.putExtra("ItemPedido", itemPedido);
                i.putExtra("TamPizza", tamPizza);
                i.putExtra("TipoPizza", tipoPizza);
                i.putExtra("Metade", "03");
                startActivity(i);
                finish();
            }
        });

        buttonEditarMetade4 = findViewById(R.id.button_editar_metade4);
        buttonEditarMetade4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(DetalhesdoItemPizzaMMActivity.this, EditarPizzaActivity.class);
                i.putExtra("ItemPedido", itemPedido);
                i.putExtra("TamPizza", tamPizza);
                i.putExtra("TipoPizza", tipoPizza);
                i.putExtra("Metade", "04");
                startActivity(i);
                finish();
            }
        });

        //Cancelar Montagem da Pizza
        btCancelarPedidoPizza.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mostrarDialogCancelarMontagem();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(DetalhesdoItemPizzaMMActivity.this, CardapioMainActivity.class);
                startActivity(intent);
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detalhe_pizza, menu);

        *//*MenuItem item = menu.getItem(0);
        item.setTitle(getValorPedido());*//*

        return true;
    }*/

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mostrarDialogCancelarMontagem();
    }

    public void getParametros() {
        itemPedido = getIntent().getExtras().getParcelable("ItemPedido");
        //Pizzas
        tamPizza = getIntent().getIntExtra("TamPizza", 0);
        tipoPizza = getIntent().getStringExtra("TipoPizza");

    }

    public void initViews() {
        tamanhoPizza = findViewById(R.id.tamanho_da_pizza);
        tipodaPizza = findViewById(R.id.tipo_da_pizza);

        //Views Metade1
        imgMetade1 = findViewById(R.id.img_metade1);
        nomeMetade1 = findViewById(R.id.nome_metade1);
        descMetade1 = findViewById(R.id.desc_metade1);
        obsMetade1 = findViewById(R.id.obs_metade1);
        //valorMetade1 = findViewById(R.id.valor_metade1);

        //Views Metade2
        imgMetade2 = findViewById(R.id.img_metade2);
        nomeMetade2 = findViewById(R.id.nome_metade2);
        descMetade2 = findViewById(R.id.desc_metade2);
        obsMetade2 = findViewById(R.id.obs_metade2);
        //valorMetade2 = findViewById(R.id.valor_metade2);

        //Observacao
        observacaoProdutoDetalhePizza = findViewById(R.id.observacao_produto_detalhe_pizza);
        observacaoProdutoDetalhePizza.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                observacaoProdutoDetalhePizza.requestFocus();
            }
        });

        btCancelarPedidoPizza = findViewById(R.id.bt_cancelar_pedido_pizza);
        textTotalPizza = findViewById(R.id.text_total_pizza);


        //Quantidade
        btDiminuirQtd = findViewById(R.id.bt_diminuir_qtd_pizza);
        btAumentarQtd = findViewById(R.id.bt_aumentar_qtd_pizza);
        qtdPizza = findViewById(R.id.txt_qtd_pizza);

        //Botoes
        btAdicionarAoCarrinho = findViewById(R.id.bt_adicionar_ao_carrinho_pizza);

    }

    public void pupulateViewDetalhes() {
        tamanhoPizza.setText(getTamanho());
        tipodaPizza.setText(tipoPizza);

        //Metade 01
        Picasso.with(DetalhesdoItemPizzaMMActivity.this).load(itemPedido.getRef_img()).networkPolicy(NetworkPolicy.OFFLINE).into(imgMetade1, new Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError() {
                Picasso.with(DetalhesdoItemPizzaMMActivity.this).load(itemPedido.getRef_img()).into(imgMetade1);
            }
        });

        textTotalPizza.setText(StringUtil.formatToMoeda(calcularValorDaPizza()));

        nomeMetade1.setText(itemPedido.getNome());
        descMetade1.setText(itemPedido.getDescricao());
        obsMetade1.setText(itemPedido.getObservacao());
        //valorMetade1.setText(StringUtil.formatToMoeda(itemPedido.getValor_unit()));

        //Metade 02
        Picasso.with(DetalhesdoItemPizzaMMActivity.this).load(itemPedido.getImgMetade2()).networkPolicy(NetworkPolicy.OFFLINE).into(imgMetade2, new Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError() {
                Picasso.with(DetalhesdoItemPizzaMMActivity.this).load(itemPedido.getRef_img()).into(imgMetade2);
            }
        });
        nomeMetade2.setText(itemPedido.getNomeMetade2());
        descMetade2.setText(itemPedido.getDescMetade2());
        obsMetade2.setText(itemPedido.getObsMetade2());
        //valorMetade2.setText(StringUtil.formatToMoeda(itemPedido.getValorMetade2()));

        //subTotal.setText(StringUtil.formatToMoeda(itemPedido.getValor_unit() + itemPedido.getValorMetade2()));

        //Metade 03
        if (tipoPizza.equals("Três Sabores")) {

            exibirCardSabor3();

            Picasso.with(DetalhesdoItemPizzaMMActivity.this).load(itemPedido.getImgMetade3()).networkPolicy(NetworkPolicy.OFFLINE).into(imgMetade3, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {
                    Picasso.with(DetalhesdoItemPizzaMMActivity.this).load(itemPedido.getRef_img()).into(imgMetade2);
                }
            });
            nomeMetade3.setText(itemPedido.getNomeMetade3());
            descMetade3.setText(itemPedido.getDescMetade3());
            obsMetade3.setText(itemPedido.getObsMetade3());
            //valorMetade3.setText(StringUtil.formatToMoeda(itemPedido.getValorMetade3()));

            //subTotal.setText(StringUtil.formatToMoeda(itemPedido.getValor_unit() + itemPedido.getValorMetade2() + itemPedido.getValorMetade3()));
        }

        //Metade 04
        if (tipoPizza.equals("Quatro Sabores")) {

            exibirCardSabor3();
            Picasso.with(DetalhesdoItemPizzaMMActivity.this).load(itemPedido.getImgMetade3()).networkPolicy(NetworkPolicy.OFFLINE).into(imgMetade3, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {
                    Picasso.with(DetalhesdoItemPizzaMMActivity.this).load(itemPedido.getRef_img()).into(imgMetade2);
                }
            });
            nomeMetade3.setText(itemPedido.getNomeMetade3());
            descMetade3.setText(itemPedido.getDescMetade3());
            obsMetade3.setText(itemPedido.getObsMetade3());
            //valorMetade3.setText(StringUtil.formatToMoeda(itemPedido.getValorMetade3()));

            exibirCardSabor4();
            Picasso.with(DetalhesdoItemPizzaMMActivity.this).load(itemPedido.getImgMetade4()).networkPolicy(NetworkPolicy.OFFLINE).into(imgMetade4, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {
                    Picasso.with(DetalhesdoItemPizzaMMActivity.this).load(itemPedido.getRef_img()).into(imgMetade2);
                }
            });
            nomeMetade4.setText(itemPedido.getNomeMetade4());
            descMetade4.setText(itemPedido.getDescMetade4());
            obsMetade4.setText(itemPedido.getObsMetade4());
            //valorMetade4.setText(StringUtil.formatToMoeda(itemPedido.getValorMetade4()));

            //subTotal.setText(StringUtil.formatToMoeda(itemPedido.getValor_unit() + itemPedido.getValorMetade2()
            //       + itemPedido.getValorMetade3() + itemPedido.getValorMetade4()));
        }

        qtdPizza.setText(String.valueOf(quantidade));

    }

    public void exibirCardSabor3() {
        //Views Metade3
        txtDescMetade3 = findViewById(R.id.txt_desc_metade3);
        cardSabor3 = findViewById(R.id.card_sabor3);

        imgMetade3 = findViewById(R.id.img_metade3);
        nomeMetade3 = findViewById(R.id.nome_metade3);
        descMetade3 = findViewById(R.id.desc_metade3);
        obsMetade3 = findViewById(R.id.obs_metade3);
        //valorMetade3 = findViewById(R.id.valor_metade3);

        txtDescMetade3.setVisibility(View.VISIBLE);
        cardSabor3.setVisibility(View.VISIBLE);

    }

    public void exibirCardSabor4() {
        //Views Metade4
        txtDescMetade4 = findViewById(R.id.txt_desc_metade4);
        cardSabor4 = findViewById(R.id.card_sabor4);

        imgMetade4 = findViewById(R.id.img_metade4);
        nomeMetade4 = findViewById(R.id.nome_metade4);
        descMetade4 = findViewById(R.id.desc_metade4);
        obsMetade4 = findViewById(R.id.obs_metade4);
        //valorMetade4 = findViewById(R.id.valor_metade4);

        txtDescMetade4.setVisibility(View.VISIBLE);
        cardSabor4.setVisibility(View.VISIBLE);


    }

    public String getTamanho() {
        String retorno = "";

        if (tamPizza == 0) {
            retorno = "Pizza Pequena";
        } else if (tamPizza == 1) {
            retorno = "Pizza Média";
        } else if (tamPizza == 2) {
            retorno = "Pizza Grande";
        }

        return retorno;
    }

    /*public String getValorPedido() {
        tipoPizza = getIntent().getStringExtra("TipoPizza");

        if (tipoPizza.equals("Três Sabores")) {
            return StringUtil.formatToMoeda(itemPedido.getValor_unit() + itemPedido.getValorMetade2()
                    + itemPedido.getValorMetade3());
        } else if (tipoPizza.equals("Quatro Sabores")) {
            return StringUtil.formatToMoeda(itemPedido.getValor_unit() + itemPedido.getValorMetade2()
                    + itemPedido.getValorMetade3() + itemPedido.getValorMetade4());
        } else {
            return StringUtil.formatToMoeda(itemPedido.getValor_unit() + itemPedido.getValorMetade2());
        }
    }*/

    private Double calcularValorDaPizza() {
        Double valorMetade1 = itemPedido.getValor_unit();
        Double valorMetade2 = itemPedido.getValorMetade2();

        Double maiorValor = Math.max(valorMetade1, valorMetade2);

        if (tipoPizza.equals("Três Sabores")) {
            Double valorMetade3 = itemPedido.getValorMetade3();
            maiorValor = Math.max(maiorValor, valorMetade3);
        }

        if (tipoPizza.equals("Quatro Sabores")) {
            Double valorMetade4 = itemPedido.getValorMetade4();
            maiorValor = Math.max(maiorValor, valorMetade4);
        }

        Print.logError("MAIOR VALOR: " + StringUtil.formatToMoeda(maiorValor));

        return maiorValor;
    }


    private void mostrarDialogCancelarMontagem() {

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        AlertDialog.Builder mBilder = new AlertDialog.Builder(DetalhesdoItemPizzaMMActivity.this, R.style.MyDialogTheme);
        View layoutDialog = inflater.inflate(R.layout.dialog_cancel_pedido_pizza, null);

        Button btOK = layoutDialog.findViewById(R.id.bt_ok_cancelar_pedido_pizza);
        Button btFechar = layoutDialog.findViewById(R.id.bt_cancel_cancelar_pedido_pizza);

        mBilder.setView(layoutDialog);
        final AlertDialog dialogEstabelecimentoFechado = mBilder.create();
        dialogEstabelecimentoFechado.show();

        btOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogEstabelecimentoFechado.dismiss();

                Intent i = new Intent(DetalhesdoItemPizzaMMActivity.this, CardapioMainActivity.class);
                startActivity(i);
                finish();
            }
        });

        btFechar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogEstabelecimentoFechado.dismiss();
            }
        });
    }

}
