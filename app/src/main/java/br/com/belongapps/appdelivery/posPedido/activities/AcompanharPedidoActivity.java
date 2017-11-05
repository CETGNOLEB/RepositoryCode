package br.com.belongapps.appdelivery.posPedido.activities;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.Locale;

import br.com.belongapps.appdelivery.R;
import br.com.belongapps.appdelivery.cardapioOnline.model.ItemPedido;
import br.com.belongapps.appdelivery.cardapioOnline.model.Pedido;
import br.com.belongapps.appdelivery.posPedido.adapters.ItensdoPedidoAdapter;
import br.com.belongapps.appdelivery.util.ConexaoUtil;
import br.com.belongapps.appdelivery.util.DataUtil;

public class AcompanharPedidoActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private ProgressBar mProgressBar;

    //Parâmetros
    private String numeroPedido;
    private String dataPedido;
    private String horaPedido;
    private double valorPedido;
    private int statusPedido;
    private int tipoEntrega;
    private String statusTempo;
    private List<ItemPedido> itensdoPedido;
    private String keyPedido;

    //Views
    private TextView txtNumPedido;
    private TextView txtDiaHoraPedido;
    private TextView txtValorPedido;

    private TextView textStatusPedidoConfirmado;
    private ImageView imgStatusPedidoConfirmado;

    private TextView textStatusPedidoProducao;
    private ImageView imgStatusPedidoProducao;

    private TextView textStatusPedidoPronto;
    private ImageView imgStatusPedidoPronto;

    private TextView textStatusPedidoSaiuEntrega;
    private ImageView imgStatusPedidoSaiuEntrega;

    private TextView textStatusPedidoEntregue;
    private ImageView imgStatusPedidoEntregue;

    private RecyclerView mRecyclerViewItensdoPedido;
    private RecyclerView.Adapter adapter;

    private Button chamarAtendente;

    private DatabaseReference database;

    //Status Conexão
    private CoordinatorLayout snakeBarLayout;
    private Snackbar snackbar;
    private boolean statusConexao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acompanhar_pedido);

        mToolbar = (Toolbar) findViewById(R.id.toolbar_acompanhar_pedido);
        mToolbar.setTitle("Detalhes do Pedido");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mProgressBar = (ProgressBar) findViewById(R.id.progressbar_acompanhamento_pedido);

        pegarParametros();
        initViews();

        //SET VIEWS INFO DO PEDIDO
        setViewInfoDoPedido();

        //DEFINIR STATUS DO PEDIDO
        definirStatus(statusPedido, tipoEntrega);

        //PREENCHER LISTA ITENS_PEDIDO
        populateItensdoPedido();


    }

    private void pegarParametros() {
        Intent intent = getIntent();
        numeroPedido = intent.getStringExtra("NumeroPedido");
        dataPedido = intent.getStringExtra("DataPedido");
        horaPedido = intent.getStringExtra("HoraPedido");
        valorPedido = intent.getDoubleExtra("ValorPedido", 0.0);
        statusPedido = intent.getIntExtra("StatusPedido", 0);
        tipoEntrega = intent.getIntExtra("TipoEntrega", 0);
        statusTempo = intent.getStringExtra("StatusTempo");
        itensdoPedido = intent.getParcelableArrayListExtra("ItensPedido");
        keyPedido = intent.getStringExtra("keyPedido");

        Log.println(Log.ERROR, "STATUS", "" + statusPedido);
    }

    private void initViews() {
        txtNumPedido = (TextView) findViewById(R.id.text_num_pedido);
        txtDiaHoraPedido = (TextView) findViewById(R.id.txt_dia_hora_pedido);
        txtValorPedido = (TextView) findViewById(R.id.txt_valor_pedido);

        //CONFIRMADO
        textStatusPedidoConfirmado = (TextView) findViewById(R.id.text_status_pedido_confirmado);
        imgStatusPedidoConfirmado = (ImageView) findViewById(R.id.img_status_pedido_confirmado);

        //EM PRODUÇÃO
        textStatusPedidoProducao = (TextView) findViewById(R.id.text_status_pedido_producao);
        imgStatusPedidoProducao = (ImageView) findViewById(R.id.img_status_pedido_producao);

        //SAIU DA COZINHA
        textStatusPedidoPronto = (TextView) findViewById(R.id.text_status_pedido_pronto);
        imgStatusPedidoPronto = (ImageView) findViewById(R.id.img_status_pedido_pronto);

        //SAIU P/ ENTREGA
        textStatusPedidoSaiuEntrega = (TextView) findViewById(R.id.text_status_pedido_saiu_entrega);
        imgStatusPedidoSaiuEntrega = (ImageView) findViewById(R.id.img_status_pedido_saiu_entrega);

        //ENTREGUE
        textStatusPedidoEntregue = (TextView) findViewById(R.id.text_status_pedido_entregue);
        imgStatusPedidoEntregue = (ImageView) findViewById(R.id.img_status_pedido_entregue);

        mRecyclerViewItensdoPedido = (RecyclerView) findViewById(R.id.list_itens_pedido);
        mRecyclerViewItensdoPedido.setHasFixedSize(true);
        mRecyclerViewItensdoPedido.setLayoutManager(new LinearLayoutManager(this));

        chamarAtendente = (Button) findViewById(R.id.bt_ligar_atendente);
    }

    private void setViewInfoDoPedido(){
        txtNumPedido.setText("Pedido Nº " + numeroPedido);
        txtDiaHoraPedido.setText("Enviado em " + dataPedido + " as " + horaPedido);
        txtValorPedido.setText("Valor do Pedido: R$ " + String.format(Locale.US, "%.2f", valorPedido).replace(".", ","));
    }

    private void definirStatus(int statusPedido, int tipoEntrega) {

        setVisibityStatusPedido(tipoEntrega);

        if (statusPedido == 1) { //Em produção
            textStatusPedidoConfirmado.setText("Confirmado " + getStatusHora(statusTempo, statusPedido));
            imgStatusPedidoConfirmado.setImageResource(R.drawable.ic_check);
            textStatusPedidoProducao.setText("Em produção " + getStatusHora(statusTempo, statusPedido));
            imgStatusPedidoProducao.setImageResource(R.drawable.ic_check);

        } else if (statusPedido == 2) { //Consumo/Retirada
            textStatusPedidoConfirmado.setText("Confirmado " + getStatusHora(statusTempo, 1));
            imgStatusPedidoConfirmado.setImageResource(R.drawable.ic_check);
            textStatusPedidoProducao.setText("Em produção " + getStatusHora(statusTempo, 1));
            imgStatusPedidoProducao.setImageResource(R.drawable.ic_check);

            imgStatusPedidoPronto.setImageResource(R.drawable.ic_check);

            if (tipoEntrega == 1) { //Retirada
                textStatusPedidoPronto.setText("Pronto p/ retirada " + getStatusHora(statusTempo, statusPedido));
            }

            if (tipoEntrega == 2) { //Consumo no estabelecimento
                textStatusPedidoPronto.setText("Pronto p/ consumo " + getStatusHora(statusTempo, statusPedido));
            }

        } else if (statusPedido == 3) { //Pronto P/entrega
            imgStatusPedidoConfirmado.setImageResource(R.drawable.ic_check);
            imgStatusPedidoProducao.setImageResource(R.drawable.ic_check);
            textStatusPedidoConfirmado.setText("Confirmado " + getStatusHora(statusTempo, 1));
            textStatusPedidoProducao.setText("Em produção " + getStatusHora(statusTempo, 1));

            textStatusPedidoPronto.setText("Saiu da Cozinha " + getStatusHora(statusTempo, statusPedido));
            imgStatusPedidoPronto.setImageResource(R.drawable.ic_check);

        } else if (statusPedido == 4) { //Saiu p/ Entrega
            imgStatusPedidoConfirmado.setImageResource(R.drawable.ic_check);
            imgStatusPedidoProducao.setImageResource(R.drawable.ic_check);
            textStatusPedidoConfirmado.setText("Confirmado " + getStatusHora(statusTempo, 1));
            textStatusPedidoProducao.setText("Em produção " + getStatusHora(statusTempo, 1));
            imgStatusPedidoPronto.setImageResource(R.drawable.ic_check);
            textStatusPedidoPronto.setText("Saiu da Cozinha " + getStatusHora(statusTempo, 3));

            imgStatusPedidoSaiuEntrega.setImageResource(R.drawable.ic_check);
            textStatusPedidoSaiuEntrega.setText("Saiu para entrega " + getStatusHora(statusTempo, statusPedido));

        } else if (statusPedido == 5) { //entregue
            imgStatusPedidoConfirmado.setImageResource(R.drawable.ic_check);
            imgStatusPedidoProducao.setImageResource(R.drawable.ic_check);
            textStatusPedidoConfirmado.setText("Confirmado " + getStatusHora(statusTempo, 1));
            textStatusPedidoProducao.setText("Em produção " + getStatusHora(statusTempo, 1));
            imgStatusPedidoPronto.setImageResource(R.drawable.ic_check);
            textStatusPedidoPronto.setText("Saiu da Cozinha " + getStatusHora(statusTempo, 3));
            imgStatusPedidoSaiuEntrega.setImageResource(R.drawable.ic_check);
            textStatusPedidoSaiuEntrega.setText("Saiu para entrega " + getStatusHora(statusTempo, 4));

            imgStatusPedidoEntregue.setImageResource(R.drawable.ic_check);
            textStatusPedidoEntregue.setText("Entregue " + getStatusHora(statusTempo, statusPedido));
        }

    }

    public void populateItensdoPedido(){
        adapter = new ItensdoPedidoAdapter(itensdoPedido, AcompanharPedidoActivity.this);
        mRecyclerViewItensdoPedido.setAdapter(adapter);

        chamarAtendente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + "85991181131"));
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });
    }

    private void setVisibityStatusPedido(int tipoEntrega) {
        if (tipoEntrega == 0) {
            textStatusPedidoPronto.setText("Saiu da cozinha");
            imgStatusPedidoSaiuEntrega.setVisibility(View.VISIBLE);
            textStatusPedidoSaiuEntrega.setVisibility(View.VISIBLE);
            imgStatusPedidoEntregue.setVisibility(View.VISIBLE);
            textStatusPedidoEntregue.setVisibility(View.VISIBLE);
        } else if (tipoEntrega == 1) {
            textStatusPedidoPronto.setText("Pronto p/ retirada");
        } else {
            textStatusPedidoPronto.setText("Pronto p/ consumo");
        }
    }

    private String getStatusHora(String statusTempo, int statusPedido) {
        String retorno = "";

        Log.println(Log.ERROR, "Status_tempo", statusTempo);

        if (statusPedido == 1) { //Em produção
            retorno = "(" + statusTempo.substring(9, 14) + ")";
        } else if (statusPedido == 2) { //Consumo/Retirada
            retorno = "(" + statusTempo.substring(9, 14) + ")";
        } else if (statusPedido == 3) { //Pronto P/entrega
            retorno = "(" + statusTempo.substring(17, 22) + ")";
        } else if (statusPedido == 4) { //Saiu p/ Entrega
            retorno = "(" + statusTempo.substring(25, 30) + ")";
        } else if (statusPedido == 5) { //entregue
            retorno = "(" + statusTempo.substring(33, 38) + ")";
        }

        return retorno;
    }

    public void atualizarAcompanhamento(String key) {

        mProgressBar.setVisibility(View.VISIBLE);

        database = FirebaseDatabase.getInstance().getReference();

        String mesAno = key.substring(0, 6);
        String dia = key.substring(7, 15);
        final String keyPedido = key.substring(16);

        Log.println(Log.ERROR, "mes", mesAno);
        Log.println(Log.ERROR, "dia", dia);
        Log.println(Log.ERROR, "key", keyPedido);

        ValueEventListener buscarPedidos = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mProgressBar.setVisibility(View.GONE);

                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Pedido pedido = data.getValue(Pedido.class);

                    if (data.getKey().equals(keyPedido)) {
                        numeroPedido = pedido.getNumero_pedido();
                        dataPedido = DataUtil.getDataPedido(pedido.getData());
                        horaPedido = DataUtil.getHoraPedido(pedido.getData());
                        valorPedido = pedido.getValor_total();
                        statusPedido = pedido.getStatus();
                        tipoEntrega = pedido.getEntrega_retirada();
                        statusTempo = pedido.getStatus_tempo();
                        itensdoPedido = pedido.getItens_pedido();

                        setViewInfoDoPedido();
                        definirStatus(statusPedido, tipoEntrega);
                        populateItensdoPedido();
                    }
                }

                Toast.makeText(AcompanharPedidoActivity.this, "Atualizado", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        database.child("pedidos").child(mesAno).child(dia).addListenerForSingleValueEvent(buscarPedidos);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_acompanhar_pedido, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(AcompanharPedidoActivity.this, MeusPedidosActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.adicionar_endereco:
                atualizarAcompanhamento(keyPedido);

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(AcompanharPedidoActivity.this, MeusPedidosActivity.class);
        startActivity(intent);

        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();

        verificaStatusDeConexao();

    }

    public void verificaStatusDeConexao(){
        snakeBarLayout = (CoordinatorLayout) findViewById(R.id.layout_snakebar_acompanhar_pedido);

        snackbar = Snackbar
                .make(snakeBarLayout, "Sem conexão com a internet", Snackbar.LENGTH_INDEFINITE);

        statusConexao = ConexaoUtil.verificaConectividade(this);

        if (statusConexao){
            snackbar.dismiss();
        } else{
            View snackView = snackbar.getView();
            snackView.setBackgroundColor(ContextCompat.getColor(AcompanharPedidoActivity.this, R.color.snakebarColor));
            snackbar.setActionTextColor(getResources().getColor(R.color.textColorPrimary));
            snackbar.show();
        }
    }
}
