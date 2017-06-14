package br.com.belongapps.appdelivery.cardapioOnline.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
import br.com.belongapps.appdelivery.cardapioOnline.dao.CarrinhoDAO;
import br.com.belongapps.appdelivery.cardapioOnline.model.ItemPedido;
import br.com.belongapps.appdelivery.util.StringUtil;

public class DetalhesdoItemPizzaMMActivity extends AppCompatActivity {

    private Toolbar mToolbar;

    //Parâmetros
    private ItemPedido itemPedido; //Pedido

    //enviar
    private String tamPizza;
    private String tipoPizza;

    //Views

    private TextView tamanhoPizza;
    //Metade 01
    private ImageView imgMetade1;
    private TextView nomeMetade1;
    private TextView descMetade1;
    private TextView obsMetade1;
    private TextView valorMetade1;

    //Metade 02
    private ImageView imgMetade2;
    private TextView nomeMetade2;
    private TextView descMetade2;
    private TextView obsMetade2;
    private TextView valorMetade2;

    private TextView subTotal;

    Button btAdicionarAoCarrinho;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_pizza_mm);

        getParametros(); //Setar parametros recebidos

        mToolbar = (Toolbar) findViewById(R.id.toolbar_detalhes_pizza_mm);
        setSupportActionBar(mToolbar);
        mToolbar.setTitle("Pizza ");

        initViews();
        pupulateViewDetalhes();

        btAdicionarAoCarrinho.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CarrinhoDAO crud = new CarrinhoDAO(getBaseContext());

                itemPedido.setQuantidade(1);
                itemPedido.setValor_unit(itemPedido.getValor_unit() + itemPedido.getValorMetade2());
                itemPedido.setValor_total(itemPedido.getValor_unit());
                itemPedido.setDescricao("Metade " + itemPedido.getNome() + "/Metade " + itemPedido.getNomeMetade2());
                itemPedido.setNome(tamPizza);

                Intent intent = new Intent(DetalhesdoItemPizzaMMActivity.this, CarrinhoActivity.class);

                //Salvar Item no Carrinho
                Log.println(Log.ERROR, "RESULT: ", crud.salvarItem(itemPedido));

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
                Intent intent = new Intent(DetalhesdoItemPizzaMMActivity.this, CardapioMainActivity.class);
                startActivity(intent);
                finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(DetalhesdoItemPizzaMMActivity.this, CardapioMainActivity.class);
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
        tamanhoPizza = (TextView) findViewById(R.id.tamanho_da_pizza);

        //TextViews Metade1
        imgMetade1 = (ImageView) findViewById(R.id.img_metade1);
        nomeMetade1 = (TextView) findViewById(R.id.nome_metade1);
        descMetade1 = (TextView) findViewById(R.id.desc_metade1);
        obsMetade1 = (TextView) findViewById(R.id.obs_metade1);
        valorMetade1 = (TextView) findViewById(R.id.valor_metade1);

        //TextViews Metade2
        imgMetade2 = (ImageView) findViewById(R.id.img_metade2);
        nomeMetade2 = (TextView) findViewById(R.id.nome_metade2);
        descMetade2 = (TextView) findViewById(R.id.desc_metade2);
        obsMetade2 = (TextView) findViewById(R.id.obs_metade2);
        valorMetade2 = (TextView) findViewById(R.id.valor_metade2);

        //Sub-Total
        subTotal = (TextView) findViewById(R.id.valor_total_pedido_pizza);

        //Botoes
        btAdicionarAoCarrinho = (Button) findViewById(R.id.bt_adicionar_ao_carrinho_pizza);
    }

    public void pupulateViewDetalhes() {
        tamanhoPizza.setText(tamPizza);

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
        nomeMetade1.setText(itemPedido.getNome());
        descMetade1.setText(itemPedido.getDescricao());
        obsMetade1.setText(itemPedido.getObservacao());
        valorMetade1.setText(StringUtil.formatToMoeda(itemPedido.getValor_unit()));

        //Metade 01
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
        valorMetade2.setText(StringUtil.formatToMoeda(itemPedido.getValorMetade2()));

        //Subtotal
        subTotal.setText("SubTotal: " + StringUtil.formatToMoeda(itemPedido.getValor_unit() + itemPedido.getValorMetade2()));

    }

}
