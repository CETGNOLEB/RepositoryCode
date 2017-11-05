package br.com.belongapps.appdelivery.cardapioOnline.activitys;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import br.com.belongapps.appdelivery.R;
import br.com.belongapps.appdelivery.cardapioOnline.dao.CarrinhoDAO;
import br.com.belongapps.appdelivery.cardapioOnline.dao.FinalizarPedidoDAO;
import br.com.belongapps.appdelivery.cardapioOnline.model.Cliente;
import br.com.belongapps.appdelivery.cardapioOnline.model.ItemPedido;
import br.com.belongapps.appdelivery.cardapioOnline.model.KeyPedido;
import br.com.belongapps.appdelivery.cardapioOnline.model.Pagamento;
import br.com.belongapps.appdelivery.cardapioOnline.model.Pedido;
import br.com.belongapps.appdelivery.posPedido.activities.AcompanharPedidoActivity;
import br.com.belongapps.appdelivery.util.DataUtil;

import static android.content.ContentValues.TAG;

public class FinalizarPedidoRetiradaActivity extends AppCompatActivity {

    private Toolbar mToolbar;

    //Views
    private TextView tvFormaRecebimento;
    private TextView txtTotalPedido;
    private Button finalizarPedido;

    private double totaldoPedido; //parâmetro recebido
    private int tipoEntrega; //parâmetro recebido

    private Pedido pedido;
    private Cliente cliente;
    private FirebaseAuth mAuth;
    private FirebaseUser usuarioLogado;
    private ProgressDialog mProgressDialog;

    private DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finalizar_pedido_retirada);

        mToolbar = (Toolbar) findViewById(R.id.toolbar_enviar_pedido_retirada);
        mToolbar.setTitle("Finalizar Pedido");

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAuth = FirebaseAuth.getInstance();

        //Recebe Valor total do pedido
        Intent i = getIntent();
        totaldoPedido = i.getDoubleExtra("totalPedido", 0);
        tipoEntrega = i.getIntExtra("tipoEntrega", 5);

        database = FirebaseDatabase.getInstance().getReference();

        populateView();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(FinalizarPedidoRetiradaActivity.this, CarrinhoActivity.class);
        startActivity(intent);

        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(FinalizarPedidoRetiradaActivity.this, CarrinhoActivity.class);
                startActivity(intent);
                finish();
        }

        return super.onOptionsItemSelected(item);
    }

    public void populateView() {

        tvFormaRecebimento = (TextView) findViewById(R.id.tv_forma_recebimento);
        tvFormaRecebimento.setText(setTextFormaRecebimento());

        txtTotalPedido = (TextView) findViewById(R.id.valor_total_pedido_retirada);
        txtTotalPedido.setText("Total: R$ " + String.format(Locale.US, "%.2f", (totaldoPedido)).replace(".", ","));


        finalizarPedido = (Button) findViewById(R.id.bt_finalizar_pedido_retirada);
        /*EVENTS*/
        finalizarPedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openProgressDialog();
                iniciarEnvioDoPedido();

            }
        });

    }

    private String setTextFormaRecebimento() {
        if (tipoEntrega == 1) {
            return "Retirar no Estabelecimento";
        }

        return "Consumir no Estabelecimento";
    }

    private void iniciarEnvioDoPedido() {
        pedido = new Pedido();

        Date data = DataUtil.getCurrenteDate();
        String dataPedido = DataUtil.formatar(data, "dd/MM/yyyy HH:mm");
        String diaPedido = DataUtil.formatar(data, "ddMMyyyy");

        pedido.setData(dataPedido);
        pedido.setStatus_tempo("n" + DataUtil.formatar(data, "HH:mm"));
        pedido.setStatus(0);
        pedido.setEntrega_retirada(tipoEntrega);
        pedido.setItens_pedido(getItensdoPedido());

        pedido.setCliente(cliente); //Adicionar Cliente ao Pedido

        Pagamento pagamento = new Pagamento();
        pagamento.setValorTotal(totaldoPedido);

        pedido.setPagamento(pagamento);

        FinalizarPedidoDAO finalizarPedidoDAO = new FinalizarPedidoDAO(this, mProgressDialog);
        finalizarPedidoDAO.salvarPedido(pedido, diaPedido);

    }

    private void openProgressDialog() {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setTitle("Enviando");
        mProgressDialog.setMessage("Aguarde, estamos enviando seu pedido...");
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();
    }

    public List<ItemPedido> getItensdoPedido() {
        List<ItemPedido> itensAux;
        CarrinhoDAO crud = new CarrinhoDAO(getBaseContext());
        itensAux = crud.getAllItens();
        return itensAux;
    }

    @Override
    protected void onStart() {
        super.onStart();

        usuarioLogado = mAuth.getCurrentUser();

        totaldoPedido = getIntent().getDoubleExtra("totalPedido", 0);

        buscarDadosdoCliente();

    }

    private void buscarDadosdoCliente() {


        String userID = usuarioLogado.getUid();

        Log.println(Log.ERROR, "USUARIO:", userID);

        ValueEventListener dadosClienteListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Cliente c = new Cliente();
                c.setNomeCliente(dataSnapshot.child("nome").getValue(String.class));

                cliente = c;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };

        database.child("clientes").child(userID).addValueEventListener(dadosClienteListener);
    }
}
