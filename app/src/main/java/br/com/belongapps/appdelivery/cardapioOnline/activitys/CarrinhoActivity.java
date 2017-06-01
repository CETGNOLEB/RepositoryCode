package br.com.belongapps.appdelivery.cardapioOnline.activitys;

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
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import br.com.belongapps.appdelivery.R;
import br.com.belongapps.appdelivery.cardapioOnline.adapters.ItemCarrinhoAdapter;
import br.com.belongapps.appdelivery.cardapioOnline.dao.CarrinhoDAO;
import br.com.belongapps.appdelivery.cardapioOnline.model.ItemPedido;

public class CarrinhoActivity extends AppCompatActivity {

    private RecyclerView mRecyclerViewItemCarrinho;
    private RecyclerView.Adapter adapter;

    private List<ItemPedido> itens_pedido;

    private Toolbar mToolbar;
    private TextView textTotal;
    private TextView textQtdItem;

    private Button continuarComprando;
    private Button confirmarPedido;

    private ImageView imgCardEmpty;

    //TOTAL
    private double totalCarrinho;
    private TextView textCarrinhoVazio;
    private TextView descCarrinhoVazio;

    private int tipoEntregaSelecionada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carrinho);

        itens_pedido = new ArrayList<>();
        CarrinhoDAO crud = new CarrinhoDAO(getBaseContext());
        itens_pedido = crud.getAllItens();

        totalCarrinho = calcularValorTotal();

        mToolbar = (Toolbar) findViewById(R.id.toolbar_carrinho);
        mToolbar.setTitle("Meu Carrinho");
        mToolbar.setNavigationIcon(R.drawable.ic_action_cart);
        setSupportActionBar(mToolbar);

        //CARRINHO VAZIO
        imgCardEmpty = (ImageView) findViewById(R.id.img_carrinho_vazio);
        textCarrinhoVazio = (TextView) findViewById(R.id.text_carrinho_vazio);
        descCarrinhoVazio = (TextView) findViewById(R.id.desc_carrinho_vazio);

        if (itens_pedido.size() == 0){
            imgCardEmpty.setVisibility(View.VISIBLE);
            textCarrinhoVazio.setVisibility(View.VISIBLE);
            descCarrinhoVazio.setVisibility(View.VISIBLE);
        }

        mRecyclerViewItemCarrinho = (RecyclerView) findViewById(R.id.itens_carrinho);
        mRecyclerViewItemCarrinho.setHasFixedSize(true);
        mRecyclerViewItemCarrinho.setLayoutManager(new LinearLayoutManager(this));

        textTotal = (TextView) findViewById(R.id.text_total_carrinho);
        textTotal.setText("R$ " + String.format(Locale.US, "%.2f", totalCarrinho).replace(".", ","));

        textQtdItem = (TextView) findViewById(R.id.text_qtd_item);

        if (itens_pedido.size() == 1){
            textQtdItem.setText(itens_pedido.size() + " Item");
        } else{
            textQtdItem.setText(itens_pedido.size() + " Itens");
        }

        adapter = new ItemCarrinhoAdapter(itens_pedido, this, textTotal, textQtdItem, imgCardEmpty, textCarrinhoVazio, descCarrinhoVazio);
        mRecyclerViewItemCarrinho.setAdapter(adapter);

        continuarComprando = (Button) findViewById(R.id.bt_continuar_comprando);
        confirmarPedido = (Button) findViewById(R.id.bt_realizar_pedido);

        /*EVENTO BOTOES*/
        continuarComprando.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //atualiza itens do pedido
                CarrinhoDAO.atualizarItens(itens_pedido);

                Intent intent = new Intent(CarrinhoActivity.this, CardapioMainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        confirmarPedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //atualiza itens do pedido
                CarrinhoDAO.atualizarItens(itens_pedido);

                AlertDialog.Builder mBilder = new AlertDialog.Builder(CarrinhoActivity.this, R.style.MyDialogTheme);
                View layoutDialog = getLayoutInflater().inflate(R.layout.dialog_tipo_entrega, null);

                mBilder.setView(layoutDialog);
                final AlertDialog dialogEscolherFormEntrega = mBilder.create();
                dialogEscolherFormEntrega.show();

                Button btCancel = (Button) layoutDialog.findViewById(R.id.bt_cancel_esc_forma_pagamento);
                Button btConfirm = (Button) layoutDialog.findViewById(R.id.bt_confirmar_forma_entrega);

                btCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogEscolherFormEntrega.dismiss();
                    }
                });

                btConfirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(CarrinhoActivity.this, FinalizarPedidoActivity.class);
                        intent.putExtra("totalPedido", Double.parseDouble(textTotal.getText().toString().replace("R$ ", "").replace(",", ".")));
                        intent.putExtra("tipoEntrega", tipoEntregaSelecionada);
                        startActivity(intent);

                    }
                });


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

    private double calcularValorTotal() {
        double total = 0;

        for (ItemPedido item : itens_pedido) {
            total += item.getValor_total();
        }

        return total;

    }

    public void onTipodeEntregaSelected(View view) {
        boolean checked = ((RadioButton) view).isChecked();

        switch(view.getId()) {
            case R.id.radio_delivery:
                if (checked)
                    tipoEntregaSelecionada = 0;
                    break;
            case R.id.radio_retirada:
                if (checked)
                    tipoEntregaSelecionada = 1;
                    break;
            case R.id.radio_mesa:
                if (checked)
                    tipoEntregaSelecionada = 2;
                    break;
            default:
                tipoEntregaSelecionada = 0;
                break;
        }
    }

}
