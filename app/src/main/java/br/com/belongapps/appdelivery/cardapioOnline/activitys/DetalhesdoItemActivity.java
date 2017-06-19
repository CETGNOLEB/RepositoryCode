package br.com.belongapps.appdelivery.cardapioOnline.activitys;

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

import br.com.belongapps.appdelivery.R;
import br.com.belongapps.appdelivery.cardapioOnline.dao.CarrinhoDAO;
import br.com.belongapps.appdelivery.cardapioOnline.model.ItemPedido;

public class DetalhesdoItemActivity extends AppCompatActivity {

    private Button addAoCarrinho;
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

        addAoCarrinho.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CarrinhoDAO crud = new CarrinhoDAO(getBaseContext());

                observacao = observacaoDetalheProduto.getText().toString();
                itemPedido.setObservacao(observacao);
                itemPedido.setQuantidade(quantidade);

                Double totalProduto = calcularValorToralDoItem(itemPedido.getQuantidade(), itemPedido.getValor_unit());
                itemPedido.setValor_total(totalProduto); //Seta o total no pedido

                Intent intent = new Intent(DetalhesdoItemActivity.this, CarrinhoActivity.class);

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
                Intent intent = new Intent(DetalhesdoItemActivity.this, CardapioMainActivity.class);
                startActivity(intent);
                finish();

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
        addAoCarrinho = (Button) findViewById(R.id.bt_add_ao_carrinho);
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
        valorDetalheProduto.setText(" R$ " + String.format(Locale.US, "%.2f", itemPedido.getValor_unit()).replace(".", ","));
        qtdProdutoDetalheProduto.setText(String.valueOf(quantidade));
    }

    public double calcularValorToralDoItem(int quantidade, double valorProduto) {
        return quantidade * valorProduto;
    }
}
