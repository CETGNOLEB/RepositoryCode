package br.com.belongapps.meuacai.cardapioOnline.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.Locale;

import br.com.belongapps.meuacai.R;
import br.com.belongapps.meuacai.cardapioOnline.model.ItemPedido;

public class DetalhesdoItemActivity extends AppCompatActivity {

    private ProgressBar mProgressBar;
    private Button addAoCarrinhoOuEscMetade2;
    private Toolbar mToolbar;

    //Parâmetros
    ItemPedido itemPedido; //Pedido
    String tipoDoPedido;
    String observacao = ""; //enviar
    int quantidade = 1; //enviar
    String telaAnterior = "";
    String tamPizza = "";
    String tipoPizza = "";

    //Views
    ImageView imgDetalheProduto;
    TextView nomeDetalheProduto;
    TextView descDetalheProduto;
    TextView valorDetalheProduto;
    TextView observacaoDetalheProduto;
    TextView qtdProdutoDetalheProduto;
    Button btAumentarQtd;
    Button btDiminuirQtd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_item);

        getParametros(); //Setar parametros recebidos

        mToolbar = (Toolbar) findViewById(R.id.toolbar_detalhes_item);
        setTitleToolbar();
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initViews();
        setTextButtonAddAoCarrinho();

        pupulateViewDetalhes();

        /*Evento dos Botoes*/
        btDiminuirQtd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (quantidade > 1) {
                    quantidade--;
                    qtdProdutoDetalheProduto.setText(String.valueOf(quantidade));
                }
            }
        });

        btAumentarQtd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quantidade++;
                qtdProdutoDetalheProduto.setText(String.valueOf(quantidade));
            }
        });

        /*---*/

        addAoCarrinhoOuEscMetade2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                observacao = observacaoDetalheProduto.getText().toString();
                itemPedido.setObservacao(observacao);
                itemPedido.setQuantidade(quantidade);

                Intent intent = new Intent(DetalhesdoItemActivity.this, CarrinhoActivity.class);
                //Enviar Parâmetros
                intent.putExtra("ItemPedido", itemPedido);

                startActivity(intent);
                finish();
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
                if (telaAnterior.equals("EscolherPizzaActivity")) {
                    Intent intent = new Intent(DetalhesdoItemActivity.this, EscolherPizzaActivity.class);
                    intent.putExtra("tipoPizza", tipoPizza);
                    intent.putExtra("tamPizza", tamPizza);
                    startActivity(intent);

                    finish();
                } else if (telaAnterior.equals("EscolherRecheioActivity")) {
                    Intent intent = new Intent(DetalhesdoItemActivity.this, EscolherRecheioActivity.class);
                    startActivity(intent);
                    finish();
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

        if (telaAnterior.equals("EscolherPizzaActivity")) {
            Intent intent = new Intent(DetalhesdoItemActivity.this, EscolherPizzaActivity.class);
            intent.putExtra("tipoPizza", tipoPizza);
            intent.putExtra("tamPizza", tamPizza);
            startActivity(intent);

            finish();
        } else if (telaAnterior.equals("EscolherRecheioActivity")) {
            Intent intent = new Intent(DetalhesdoItemActivity.this, EscolherRecheioActivity.class);
            startActivity(intent);
            finish();
        } else {
            Intent intent = new Intent(DetalhesdoItemActivity.this, CardapioMainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    public void getParametros() {

        telaAnterior = getIntent().getStringExtra("TelaAnterior");
        itemPedido = getIntent().getExtras().getParcelable("ItemPedido");

        //Pizzas
        tamPizza = getIntent().getStringExtra("TamPizza");
        tipoPizza = getIntent().getStringExtra("TipoPizza");

    }

    public void initViews() {
        //TextViews
        nomeDetalheProduto = (TextView) findViewById(R.id.nome_produto_detalhe_item);
        descDetalheProduto = (TextView) findViewById(R.id.descricao_produto_detalhe_item);
        imgDetalheProduto = (ImageView) findViewById(R.id.img_produto_detalhe_item);
        valorDetalheProduto = (TextView) findViewById(R.id.valor_produto_detalhe_item);
        observacaoDetalheProduto = (TextView) findViewById(R.id.observacao_produto_detalhe_item);
        qtdProdutoDetalheProduto = (TextView) findViewById(R.id.txt_qtd_item_delathe_produto);

        //Botoes
        btDiminuirQtd = (Button) findViewById(R.id.bt_diminuir_qtd_item_detalhe_produto);
        btAumentarQtd = (Button) findViewById(R.id.bt_aumentar_qtd_item_detalhe_produto);
        addAoCarrinhoOuEscMetade2 = (Button) findViewById(R.id.bt_add_ao_carrinho_ou_esc_metade2);
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

        descDetalheProduto.setText(itemPedido.getDescricao());
        valorDetalheProduto.setText(" R$ " +  String.format(Locale.US, "%.2f", itemPedido.getValor_unit()).replace(".", ","));
        qtdProdutoDetalheProduto.setText(String.valueOf(quantidade));
    }

    public void setTitleToolbar() {
        if (tipoPizza != null) {
            if (!tipoPizza.equals("Inteira")) {
                mToolbar.setTitle("Primeira Metade");
            }
        } else {
            mToolbar.setTitle("Detalhes do Produto");
        }
    }

    public void setTextButtonAddAoCarrinho() {
        if (tipoPizza != null) {
            if (!tipoPizza.equals("Inteira")) {
                addAoCarrinhoOuEscMetade2.setText("Confirmar");
            }
        } else {
            addAoCarrinhoOuEscMetade2.setText("Adicionar ao Carrinho");
        }
    }
}
