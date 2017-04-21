package br.com.belongapps.meuacai.cardapioOnline.activitys;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import java.util.ArrayList;

import br.com.belongapps.meuacai.R;
import br.com.belongapps.meuacai.cardapioOnline.adapters.ItemCarrinhoAdapter;
import br.com.belongapps.meuacai.cardapioOnline.model.ItemPedido;

public class CarrinhoActivity extends AppCompatActivity {

    private RecyclerView mRecyclerViewItemCarrinho;
    private RecyclerView.Adapter adapter;

    private ArrayList<ItemPedido> itens_pedido;

    private Toolbar mToolbar;
    private Button continuarComprando;
    private Button confirmarPedido;

    //Controle Quantidade
    private ImageButton diminuirQuantidade;
    private ImageButton aumentarQuantidade;

    //Parametros Recebidos
    ItemPedido itemPedido;

    //TOTAL
    private double totalProduto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carrinho);

        mToolbar = (Toolbar) findViewById(R.id.toolbar_carrinho);
        mToolbar.setTitle("Meu Carrinho");

        createItensCarrinho();

        mRecyclerViewItemCarrinho = (RecyclerView) findViewById(R.id.itens_carrinho);
        mRecyclerViewItemCarrinho.setHasFixedSize(true);
        mRecyclerViewItemCarrinho.setLayoutManager(new LinearLayoutManager(this));

        adapter = new ItemCarrinhoAdapter(itens_pedido, this);
        mRecyclerViewItemCarrinho.setAdapter(adapter);

        continuarComprando = (Button) findViewById(R.id.bt_continuar_comprando);
        confirmarPedido = (Button) findViewById(R.id.bt_realizar_pedido);


        /*EVENTO BOTOES*/
        continuarComprando.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CarrinhoActivity.this, CardapioMainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        confirmarPedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final double totalPedido = calcularValorTotal();

                AlertDialog.Builder mBilder = new AlertDialog.Builder(CarrinhoActivity.this, R.style.MyDialogTheme);
                View layoutDialog = getLayoutInflater().inflate(R.layout.dialog_tipo_entrega, null);

                Button btEntrega = (Button) layoutDialog.findViewById(R.id.bt_entrega_delivery);
                Button btRetirada = (Button) layoutDialog.findViewById(R.id.bt_retirar_na_loja);

                btEntrega.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(CarrinhoActivity.this, FinalizarPedidoActivity.class);
                        intent.putExtra("totalPedido" , totalPedido);
                        startActivity(intent);

                    }
                });

                btRetirada.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(CarrinhoActivity.this, FinalizarPedidoActivity.class);
                        intent.putExtra("totalPedido" , totalPedido);
                        startActivity(intent);
                    }
                });

                mBilder.setView(layoutDialog);
                AlertDialog dialogEscolherFormEntrega = mBilder.create();
                dialogEscolherFormEntrega.show();

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(CarrinhoActivity.this, CardapioMainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(CarrinhoActivity.this, CardapioMainActivity.class);
                startActivity(intent);
                finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private double calcularValorTotal(){
        double total = 0;

        for (ItemPedido item: itens_pedido) {
              total += item.getValor_total();
        }

        return total;

    }

    public double calcularValorToralDoItem(int quantidade, double valorProduto){
        return quantidade * valorProduto;
    }


    public void createItensCarrinho(){
        Intent intent = getIntent();
        itemPedido = intent.getExtras().getParcelable("ItemPedido");

        totalProduto = calcularValorToralDoItem(itemPedido.getQuantidade(), itemPedido.getValor_unit());
        itemPedido.setValor_total(totalProduto); //Seta o total no pedido

        //Recuperar Itens na Lista

        itens_pedido = new ArrayList<>();
        ItemPedido item = new ItemPedido();
        item.setNome("Açai 300ml");
        item.setDescricao("Cereais + Amendoim");
        item.setRef_img("https://firebasestorage.googleapis.com/v0/b/appacai001.appspot.com/o/itensCardapio%2Facai%20copo%201.png?alt=media&token=7e3d044c-f8fd-4543-b903-0375c00d7986");
        item.setQuantidade(1);
        item.setValor_total(5);
        item.setValor_unit(5);

        ItemPedido pedidoRealizado = new ItemPedido();
        pedidoRealizado.setNome("Açai 400ml");
        pedidoRealizado.setDescricao("Cereais + Amendoim");
        pedidoRealizado.setRef_img("https://firebasestorage.googleapis.com/v0/b/appacai001.appspot.com/o/itensCardapio%2Fa%C3%A7ai%204.png?alt=media&token=84c27ae1-494a-4553-b64d-408129fcc602");
        pedidoRealizado.setQuantidade(2);
        pedidoRealizado.setValor_total(10);
        pedidoRealizado.setValor_unit(5);

        itens_pedido.add(itemPedido);
        //itens_pedido.add(item);
        itens_pedido.add(pedidoRealizado);
    }
}
