package br.com.belongapps.appdelivery.cardapioOnline.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import br.com.belongapps.appdelivery.R;
import br.com.belongapps.appdelivery.cardapioOnline.model.ItemPedido;
import br.com.belongapps.appdelivery.util.StringUtil;

public class DetalhesdoItemPizzaActivity extends AppCompatActivity {

    private Toolbar mToolbar;

    //Parâmetros
    private ItemPedido itemPedido; //Pedido
    private String observacao; //enviar
    private String tamPizza;
    private String tipoPizza;

    //Views
    private ImageView imgDetalheProduto;
    private TextView nomeDetalheProduto;
    private TextView descDetalheProduto;
    private TextView valorDetalheProduto;
    private EditText observacaoDetalheProduto;

    Button btConfirmarPizza;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_item_pizza);

        getParametros(); //Setar parametros recebidos

        mToolbar = (Toolbar) findViewById(R.id.toolbar_detalhes_item_pizza);
        setTitleToolbar();
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initViews();
        pupulateViewDetalhes();

        btConfirmarPizza.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (itemPedido.getNomeMetade2() == null) { //Escolher Segunda metade

                    observacao = observacaoDetalheProduto.getText().toString();
                    itemPedido.setObservacao(observacao);

                    Intent intent = new Intent(DetalhesdoItemPizzaActivity.this, EscolherPizzaActivity2.class);
                    intent.putExtra("ItemPedido", itemPedido);
                    intent.putExtra("TamPizza", tamPizza);
                    intent.putExtra("TipoPizza", tipoPizza);

                    startActivity(intent);
                    finish();
                } else{ //Visualizar escolhas

                    observacao = observacaoDetalheProduto.getText().toString();
                    itemPedido.setObsMetade2(observacao);

                    Intent intent = new Intent(DetalhesdoItemPizzaActivity.this, DetalhesdoItemPizzaMMActivity.class);
                    intent.putExtra("ItemPedido", itemPedido);
                    intent.putExtra("TamPizza", tamPizza);

                    startActivity(intent);
                    finish();
                }
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
                Intent intent = new Intent(DetalhesdoItemPizzaActivity.this, CardapioMainActivity.class);
                startActivity(intent);
                finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(DetalhesdoItemPizzaActivity.this, CardapioMainActivity.class);
        startActivity(intent);
        finish();
    }

    public void getParametros() {
        itemPedido = getIntent().getExtras().getParcelable("ItemPedido");

        //Pizzas
        tamPizza = getIntent().getStringExtra("TamPizza");
        tipoPizza = getIntent().getStringExtra("TipoPizza");

    }

    public void initViews() {
        //TextViews
        nomeDetalheProduto = (TextView) findViewById(R.id.nome_produto_detalhe_item_pizza);
        descDetalheProduto = (TextView) findViewById(R.id.descricao_produto_detalhe_item_pizza);
        imgDetalheProduto = (ImageView) findViewById(R.id.img_produto_detalhe_item_pizza);
        valorDetalheProduto = (TextView) findViewById(R.id.valor_produto_detalhe_item_pizza);
        observacaoDetalheProduto = (EditText) findViewById(R.id.observacao_produto_detalhe_item_pizza);

        //Botoes
        btConfirmarPizza = (Button) findViewById(R.id.bt_confirmar_pizza);
    }

    public void pupulateViewDetalhes() {

        if (itemPedido.getNomeMetade2() == null){
            //Exibir Imagem
            Picasso.with(DetalhesdoItemPizzaActivity.this).load(itemPedido.getRef_img()).networkPolicy(NetworkPolicy.OFFLINE).into(imgDetalheProduto, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {
                    Picasso.with(DetalhesdoItemPizzaActivity.this).load(itemPedido.getRef_img()).into(imgDetalheProduto);
                }
            });

            //nome, descricao e valor
            nomeDetalheProduto.setText(itemPedido.getNome());
            descDetalheProduto.setText(itemPedido.getDescricao());
            valorDetalheProduto.setText(StringUtil.formatToMoeda(itemPedido.getValor_unit()));
        }else{
            //Exibir Imagem
            Picasso.with(DetalhesdoItemPizzaActivity.this).load(itemPedido.getImgMetade2()).networkPolicy(NetworkPolicy.OFFLINE).into(imgDetalheProduto, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {
                    Picasso.with(DetalhesdoItemPizzaActivity.this).load(itemPedido.getRef_img()).into(imgDetalheProduto);
                }
            });
            //nome, descricao e valor
            nomeDetalheProduto.setText(itemPedido.getNomeMetade2());
            descDetalheProduto.setText(itemPedido.getDescMetade2());
            valorDetalheProduto.setText(StringUtil.formatToMoeda(itemPedido.getValorMetade2()));
        }


    }

    public void setTitleToolbar() {
        if (itemPedido.getNomeMetade2() == null) {
            mToolbar.setTitle("Primeira Metade");
        } else {
            mToolbar.setTitle("Segunda Metade");
        }
    }

    public double calcularValorToralDoItem(int quantidade, double valorProduto) {
        return quantidade * valorProduto;
    }
}
