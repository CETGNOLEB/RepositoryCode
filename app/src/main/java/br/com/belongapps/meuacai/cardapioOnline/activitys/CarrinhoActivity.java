package br.com.belongapps.meuacai.cardapioOnline.activitys;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import br.com.belongapps.meuacai.R;
import br.com.belongapps.meuacai.cardapioOnline.adapters.ItemCarrinhoAdapter;
import br.com.belongapps.meuacai.cardapioOnline.dao.CarrinhoDAO;
import br.com.belongapps.meuacai.cardapioOnline.model.ItemPedido;
import br.com.belongapps.meuacai.cardapioOnline.model.Pedido;

public class CarrinhoActivity extends AppCompatActivity {

    private RecyclerView mRecyclerViewItemCarrinho;
    private RecyclerView.Adapter adapter;

    private List<ItemPedido> itens_pedido;

    private Toolbar mToolbar;
    private TextView textTotal;
    private Button continuarComprando;
    private Button confirmarPedido;

    //TOTAL
    private double totalCarrinho;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carrinho);

        itens_pedido = new ArrayList<>();
        CarrinhoDAO crud = new CarrinhoDAO(getBaseContext());
        itens_pedido = crud.getAllItens();

        totalCarrinho = calcularValorTotal();

        textTotal = (TextView) findViewById(R.id.text_valor_pedido);
        if (totalCarrinho != 0.0) {
            textTotal.setText("Total: R$ " + String.format(Locale.US, "%.2f", totalCarrinho).replace(".", ","));
        }

        mToolbar = (Toolbar) findViewById(R.id.toolbar_carrinho);
        mToolbar.setTitle("Meu Carrinho");
        setSupportActionBar(mToolbar);

        mRecyclerViewItemCarrinho = (RecyclerView) findViewById(R.id.itens_carrinho);
        mRecyclerViewItemCarrinho.setHasFixedSize(true);
        mRecyclerViewItemCarrinho.setLayoutManager(new LinearLayoutManager(this));

        adapter = new ItemCarrinhoAdapter(itens_pedido, this, textTotal);
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

                AlertDialog.Builder mBilder = new AlertDialog.Builder(CarrinhoActivity.this, R.style.MyDialogTheme);
                View layoutDialog = getLayoutInflater().inflate(R.layout.dialog_tipo_entrega, null);

                Button btEntrega = (Button) layoutDialog.findViewById(R.id.bt_entrega_delivery);
                Button btRetirada = (Button) layoutDialog.findViewById(R.id.bt_retirar_na_loja);

                btEntrega.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(CarrinhoActivity.this, FinalizarPedidoActivity.class);
                        intent.putExtra("totalPedido", totalCarrinho);
                        intent.putExtra("tipoEntrega", 1);

                        startActivity(intent);

                    }
                });

                btRetirada.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(CarrinhoActivity.this, FinalizarPedidoActivity.class);
                        intent.putExtra("totalPedido", totalCarrinho);
                        intent.putExtra("tipoEntrega", 0);
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

    private double calcularValorTotal() {
        double total = 0;

        for (ItemPedido item : itens_pedido) {
            total += item.getValor_total();
        }

        return total;

    }

}
