package br.com.belongapps.appdelivery.cardapioOnline.activitys;

import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import br.com.belongapps.appdelivery.R;
import br.com.belongapps.appdelivery.cardapioOnline.adapters.ItemCarrinhoAdapter;
import br.com.belongapps.appdelivery.cardapioOnline.dao.CarrinhoDAO;
import br.com.belongapps.appdelivery.cardapioOnline.model.ItemPedido;
import br.com.belongapps.appdelivery.firebaseAuthApp.FirebaseAuthApp;
import br.com.belongapps.appdelivery.seguranca.activities.LoginActivity;
import br.com.belongapps.appdelivery.util.ConexaoUtil;
import br.com.belongapps.appdelivery.util.DataUtil;
import br.com.belongapps.appdelivery.util.OpenDialogUtil;
import br.com.belongapps.appdelivery.util.Print;

public class CarrinhoActivity extends AppCompatActivity {

    /*FIREBASE*/
    private DatabaseReference mDatabaseReference;

    /*LISTAGEM*/
    private RecyclerView mRecyclerViewItemCarrinho;
    private RecyclerView.Adapter adapter;
    private List<ItemPedido> itens_pedido;
    /*VIEWS*/
    private Toolbar mToolbar;
    private TextView textTotal;
    private TextView textQtdItem;
    private Button continuarComprando;
    private Button confirmarPedido;
    private ImageView imgCardEmpty;
    private Button btCardEmpty;
    //TOTAL
    private double totalCarrinho;
    private TextView textCarrinhoVazio;
    private TextView descCarrinhoVazio;
    /*TIPO DE RECEBIMENTO*/
    private int tipoEntregaSelecionada = 5;

    //PERMISSÔES
    private boolean statusDelivery = true;
    private boolean statusEstabelecimento = true;
    private RadioButton radioDelivery;
    private RadioButton radioRetirada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carrinho);

        itens_pedido = new ArrayList<>();
        CarrinhoDAO crud = new CarrinhoDAO(getBaseContext());
        itens_pedido = crud.getAllItens();

        totalCarrinho = calcularValorTotal();

        mToolbar = findViewById(R.id.toolbar_carrinho);
        mToolbar.setTitle("Meu Carrinho");
        //mToolbar.setNavigationIcon(R.drawable.ic_action_cart);
        setSupportActionBar(mToolbar);

        //CARRINHO VAZIO
        imgCardEmpty = findViewById(R.id.img_carrinho_vazio);
        textCarrinhoVazio = findViewById(R.id.text_carrinho_vazio);
        descCarrinhoVazio = findViewById(R.id.desc_carrinho_vazio);
        confirmarPedido = findViewById(R.id.bt_realizar_pedido);
        btCardEmpty = findViewById(R.id.bt_card_empty);

        if (itens_pedido.size() == 0) {
            imgCardEmpty.setVisibility(View.VISIBLE);
            textCarrinhoVazio.setVisibility(View.VISIBLE);
            descCarrinhoVazio.setVisibility(View.VISIBLE);
            btCardEmpty.setVisibility(View.VISIBLE);
            confirmarPedido.setVisibility(View.GONE);
        }

        mRecyclerViewItemCarrinho = findViewById(R.id.itens_carrinho);
        mRecyclerViewItemCarrinho.setHasFixedSize(true);
        mRecyclerViewItemCarrinho.setLayoutManager(new LinearLayoutManager(this));

        textTotal = findViewById(R.id.text_total_carrinho);
        textTotal.setText("R$ " + String.format(Locale.US, "%.2f", totalCarrinho).replace(".", ","));

        textQtdItem = findViewById(R.id.text_qtd_item);

        if (itens_pedido.size() == 1) {
            textQtdItem.setText(itens_pedido.size() + " Item");
        } else {
            textQtdItem.setText(itens_pedido.size() + " Itens");
        }

        adapter = new ItemCarrinhoAdapter(itens_pedido, this, textTotal, textQtdItem, imgCardEmpty, textCarrinhoVazio, descCarrinhoVazio, confirmarPedido, btCardEmpty);
        mRecyclerViewItemCarrinho.setAdapter(adapter);

        continuarComprando = findViewById(R.id.bt_continuar_comprando);

        /*EVENTO BOTOES*/
        btCardEmpty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CarrinhoActivity.this, CardapioMainActivity.class);
                startActivity(intent);
                finish();
            }
        });

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

                String itemNaoEntregue = null;

                //Verifica Se algum item n pode ser entregue
                for (ItemPedido item : itens_pedido) {
                    if (item.getPermite_entrega() == 2) {
                        itemNaoEntregue = item.getNome();
                    }
                }

                //*Sem Conexão*//
                if (!ConexaoUtil.verificaConectividade(CarrinhoActivity.this)) {

                    OpenDialogUtil.openSimpleDialog("Conexão ruim",
                            "Sua conexão com a internet parece ruim, verifique a conexão e tente novamente.",
                            CarrinhoActivity.this);

                    return;

                }
                //*Estabelecimento Fechado*//
                if (statusEstabelecimento == false) {

                    OpenDialogUtil.openSimpleDialog("Estabelecimento Fechado",
                            "Desculpe, nosso estabelecimento não está recebendo pedidos pelo aplicativo no momento.",
                            CarrinhoActivity.this);

                    return;

                }

                if (!FirebaseAuthApp.podeFazerPedidos()) {

                    OpenDialogUtil.openSimpleDialog("Desculpe",
                            "O estabelecimento o impediu de relizar pedidos, favor o procure-o para prosseguir pedindo.",
                            CarrinhoActivity.this);

                } else {
                    //atualiza itens do pedido
                    CarrinhoDAO.atualizarItens(itens_pedido);

                    AlertDialog.Builder mBilder = new AlertDialog.Builder(CarrinhoActivity.this, R.style.MyDialogTheme);
                    View layoutDialog = getLayoutInflater().inflate(R.layout.dialog_tipo_entrega, null);

                    mBilder.setView(layoutDialog);
                    final AlertDialog dialogEscolherFormEntrega = mBilder.create();
                    dialogEscolherFormEntrega.show();

                    radioDelivery = layoutDialog.findViewById(R.id.radio_delivery);
                    radioRetirada = layoutDialog.findViewById(R.id.radio_retirada);

                    if (statusDelivery == false) {
                        radioDelivery.setVisibility(View.GONE);
                    } else {
                        radioDelivery.setVisibility(View.VISIBLE);
                    }

                    RelativeLayout rlSemDelivery = layoutDialog.findViewById(R.id.rl_sem_delivery);

                    if (statusDelivery == false) {
                        rlSemDelivery.setVisibility(View.VISIBLE);
                    } else if (itemNaoEntregue != null) {
                        TextView infoTipoEntrega = layoutDialog.findViewById(R.id.info_tipo_entrega);
                        infoTipoEntrega.setText("Não entregamos o item: " + itemNaoEntregue);
                        rlSemDelivery.setVisibility(View.VISIBLE);
                        radioDelivery.setVisibility(View.GONE);
                        radioRetirada.setVisibility(View.GONE);
                    }

                    Button btCancel = layoutDialog.findViewById(R.id.bt_cancel_esc_forma_pagamento);
                    Button btConfirm = layoutDialog.findViewById(R.id.bt_confirmar_forma_entrega);

                    btCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialogEscolherFormEntrega.dismiss();
                        }
                    });

                    btConfirm.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if (tipoEntregaSelecionada == 5) {
                                Toast.makeText(CarrinhoActivity.this, "Selecione uma forma de recebimento.", Toast.LENGTH_SHORT).show();
                            } else {

                                if (tipoEntregaSelecionada == 0) {
                                    Intent intent = new Intent(CarrinhoActivity.this, FinalizarPedidoActivity.class);
                                    intent.putExtra("totalPedido", Double.parseDouble(textTotal.getText().toString().replace("R$ ", "").replace(",", ".")));
                                    intent.putExtra("tipoEntrega", tipoEntregaSelecionada);
                                    startActivity(intent);
                                    finish();
                                } else {

                                    Intent intent = new Intent(CarrinhoActivity.this, FinalizarPedidoRetiradaActivity.class);
                                    intent.putExtra("totalPedido", Double.parseDouble(textTotal.getText().toString().replace("R$ ", "").replace(",", ".")));
                                    intent.putExtra("tipoEntrega", tipoEntregaSelecionada);
                                    startActivity(intent);
                                    finish();
                                }
                            }

                        }
                    });

                }
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        if (FirebaseAuthApp.getUsuarioLogado() == null) {
            Intent i = new Intent(CarrinhoActivity.this, LoginActivity.class);
            startActivity(i);
            finish();
        }

        View layoutDialog = getLayoutInflater().inflate(R.layout.dialog_tipo_entrega, null);
        radioDelivery = layoutDialog.findViewById(R.id.radio_delivery);

        mDatabaseReference = FirebaseDatabase.getInstance().getReference();

        mDatabaseReference.child("configuracoes").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Boolean status = Boolean.parseBoolean(dataSnapshot.child("status_delivery").child("status").getValue().toString());
                statusDelivery = status;

                Boolean statusEstab = Boolean.parseBoolean(dataSnapshot.child("status_estabelecimento").child("status").getValue().toString());
                statusEstabelecimento = statusEstab;

                if (statusDelivery == false) {
                    radioDelivery.setVisibility(View.GONE);
                } else {
                    radioDelivery.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(CarrinhoActivity.this, CardapioMainActivity.class);
        startActivity(intent);
        finish();
    }

    /*MENU*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_carrinho, menu);
//        MenuItem menuAddEndereco = menu.getItem(0);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.adicionar_itens:
                //abrir diálog
                Intent i = new Intent(CarrinhoActivity.this, CardapioMainActivity.class);
                startActivity(i);
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

    public void onTipodeEntregaSelected(View view) {
        boolean checked = ((RadioButton) view).isChecked();

        switch (view.getId()) {
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
                tipoEntregaSelecionada = 5;
                break;
        }
    }

}
