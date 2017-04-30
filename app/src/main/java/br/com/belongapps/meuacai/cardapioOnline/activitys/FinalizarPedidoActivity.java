package br.com.belongapps.meuacai.cardapioOnline.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import br.com.belongapps.meuacai.R;
import br.com.belongapps.meuacai.cardapioOnline.adapters.FormasdePagamentoAdapter;
import br.com.belongapps.meuacai.cardapioOnline.adapters.ItemCarrinhoAdapter;
import br.com.belongapps.meuacai.cardapioOnline.dao.CarrinhoDAO;
import br.com.belongapps.meuacai.cardapioOnline.dao.FormardePagamentoDAO;
import br.com.belongapps.meuacai.cardapioOnline.model.Cliente;
import br.com.belongapps.meuacai.cardapioOnline.model.FormadePagamento;
import br.com.belongapps.meuacai.cardapioOnline.model.ItemCardapio;
import br.com.belongapps.meuacai.cardapioOnline.model.ItemPedido;
import br.com.belongapps.meuacai.cardapioOnline.model.Pagamento;
import br.com.belongapps.meuacai.cardapioOnline.model.Pedido;
import br.com.belongapps.meuacai.util.DataUtil;

import static android.content.ContentValues.TAG;

public class FinalizarPedidoActivity extends AppCompatActivity {

    private Toolbar mToolbar;

    //Views
    private TextView txtTotalPedido;
    private TextView txtTotalDosItens;
    private Button finalizarPedido;

    //Dialogs
    AlertDialog dialogPedidoEnviado;
    AlertDialog.Builder mBilder;

    private double totaldoPedido; //parâmetro recebido

    //ESCOLHER FORMA DE PAGAMENTO
    private RecyclerView mRecyclerViewFormasdePagamento;
    private RecyclerView.Adapter adapter;

    private List<FormadePagamento> formasDePagamento = FormardePagamentoDAO.getFormasdePagamento();

    DatabaseReference database = FirebaseDatabase.getInstance().getReference();

    String numerodopedido = "";

    private Pedido pedido = new Pedido();

    private ArrayList<ItemPedido> itensdoPedido = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finalizar_pedido);

        mBilder = new AlertDialog.Builder(FinalizarPedidoActivity.this);

        mToolbar = (Toolbar) findViewById(R.id.toolbar_enviar_pedido);
        mToolbar.setTitle("Finalizar Pedido");

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        populateFormasdePagamento();

        //Recebe Valor total do pedido
        Intent i = getIntent();
        totaldoPedido = i.getDoubleExtra("totalPedido", 0);

        populateView();

        finalizarPedido = (Button) findViewById(R.id.bt_finalizar_pedido);

        finalizarPedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                beforeEnviarPedido();

                View layoutDialog = createDialog();

                mBilder.setView(layoutDialog);
                dialogPedidoEnviado = mBilder.create();
                dialogPedidoEnviado.show();

            }
        });

    }

    private View createDialog() {
        mBilder = new AlertDialog.Builder(FinalizarPedidoActivity.this);
        View layoutDialog = getLayoutInflater().inflate(R.layout.dialog_pedido_finalizado, null);

        Button bt_ok = (Button) layoutDialog.findViewById(R.id.bt_entendi_dialog_pedido_enviado);
        bt_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(FinalizarPedidoActivity.this, CardapioMainActivity.class);
                startActivity(i);
                finish();
            }
        });

        return layoutDialog;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(FinalizarPedidoActivity.this, CarrinhoActivity.class);
        startActivity(intent);

        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(FinalizarPedidoActivity.this, CarrinhoActivity.class);
                startActivity(intent);
                finish();
        }

        return super.onOptionsItemSelected(item);
    }

    public void populateView() {

        txtTotalDosItens = (TextView) findViewById(R.id.valor_pedido);
        txtTotalDosItens.setText("Valor do Pedido: R$ " + String.format(Locale.US, "%.2f", totaldoPedido).replace(".", ","));

        txtTotalPedido = (TextView) findViewById(R.id.valor_total_pedido);
        txtTotalPedido.setText("Subtotal: R$ " + String.format(Locale.US, "%.2f", (totaldoPedido + 1)).replace(".", ","));

    }

    public void populateFormasdePagamento() {
        mRecyclerViewFormasdePagamento = (RecyclerView) findViewById(R.id.formas_de_pagamento);
        mRecyclerViewFormasdePagamento.setHasFixedSize(true);
        mRecyclerViewFormasdePagamento.setLayoutManager(new LinearLayoutManager(this));

        adapter = new FormasdePagamentoAdapter(formasDePagamento, this);
        mRecyclerViewFormasdePagamento.setAdapter(adapter);

    }

    private void beforeEnviarPedido() {
        Pedido pedido = new Pedido();

        Date data = DataUtil.getCurrenteDate();
        Date hj = DataUtil.getCurrenteDate();
        String dataPedido = DataUtil.formatar(data, "dd/MM/yyyy HH:mm");
        String diaPedido = DataUtil.formatar(data, "ddMMyyyy");

        pedido.setData(dataPedido);
        pedido.setNumero_pedido(gerarNumeroPedido(numerodopedido));
        pedido.setStatus(0);
        pedido.setEntrega_retirada(getIntent().getIntExtra("tipoEntrega", 0));
        pedido.setItens_pedido(getItensdoPedido());

        //Pegar usuário logado
        Cliente cliente = new Cliente();
        cliente.setNomeCliente("Thiago Oliveira");

        //setar apenas se a entrega for delivery
        if (pedido.getEntrega_retirada() == 1) {
            cliente.setRuaEndCliente("Tv. Aristides Gonçalves");
            cliente.setNumeroEndCliente("179");
            cliente.setBairroEndCliente("Rodoviária");
            cliente.setComplementoEndCliente("Ap 202");
        }

        pedido.setCliente(cliente); //Adicionar Cliente ao Pedido

        FormadePagamento formadePagamento = getFormaPagamento();

        Pagamento pagamento = new Pagamento();
        pagamento.setFormaPagamento(formadePagamento.getNome());
        pagamento.setValorTotal(totaldoPedido);
        pagamento.setDescricaoPagemento(formadePagamento.getDescricao());

        pedido.setPagamento(pagamento);

        salvarPedido(pedido,diaPedido);

    }

    private void salvarPedido(Pedido pedido, String hj) {
        String key = database.child("pedidos").push().getKey();
        Pedido pedidoAux = pedido;
        Map<String, Object> pedidoValues = pedidoAux.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/pedidos/" + hj + "/" + key, pedidoValues);
        childUpdates.put("/clientes/" + 1 + "/pedidos/" + key, key); //pegar id do usuário logado

        database.updateChildren(childUpdates);
    }

    public FormadePagamento getFormaPagamento() {
        FormadePagamento fpgm = new FormadePagamento();

        for (FormadePagamento forma :
                formasDePagamento) {
            if (forma.isStatus() == true) {
                fpgm = forma;
            }
        }

        return fpgm;
    }

    public void updateNumero(List<String> list) {

        if (list.isEmpty()) {
            numerodopedido = "0";

        } else {
            numerodopedido = list.get(list.size() - 1);
            Log.println(Log.ERROR, "Ultimo Pedido: ", list.get(list.size() - 1));
        }



    }

    public List<ItemPedido> getItensdoPedido() {
        List<ItemPedido> itensAux = new ArrayList<>();
        CarrinhoDAO crud = new CarrinhoDAO(getBaseContext());
        itensAux = crud.getAllItens();
        return itensAux;
    }

    public String gerarNumeroPedido(String numero) {

        int intnum = Integer.parseInt(numero);
        intnum++;

        if (intnum < 10) {
            NumberFormat formatter = new DecimalFormat("00");

            numero = formatter.format(intnum);
        }

        return numero;
    }

    @Override
    protected void onStart() {
        super.onStart();

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<String> list = new ArrayList<>();

                try {
                    for (DataSnapshot pedido : dataSnapshot.getChildren()) {
                        for (DataSnapshot dia : pedido.getChildren()) {
                            String numpedido = dia.child("numero_pedido").getValue().toString();
                            list.add(numpedido);
                        }
                    }

                    updateNumero(list);
                } catch (Exception n) {
                    updateNumero(list);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };

        database.child("pedidos").addValueEventListener(postListener);

    }
}
