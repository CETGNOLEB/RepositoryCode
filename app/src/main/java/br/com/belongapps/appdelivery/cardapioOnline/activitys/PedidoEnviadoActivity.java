package br.com.belongapps.appdelivery.cardapioOnline.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import br.com.belongapps.appdelivery.R;
import br.com.belongapps.appdelivery.cardapioOnline.model.ItemPedido;
import br.com.belongapps.appdelivery.posPedido.activities.AcompanharPedidoActivity;

public class PedidoEnviadoActivity extends AppCompatActivity {

    //Parms a receber
    private String numeroPedido;
    private String dataPedido;
    private String horaPedido;
    private double valorPedido;
    private int statusPedido;
    private int tipoEntrega;
    private String statusTempo;
    private List<ItemPedido> itensdoPedido;
    private String keyPedido;

    //View
    private Button btAcompanharPedidoEnviado;
    private Button btVoltarPedidoEnviado;
    private TextView tempoProntoOuEntrega;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedido_enviado);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar_pedido_enviado);
        mToolbar.setTitle("Pedido Enviado");

        getParametros();
        initViews();
    }

    private void initViews(){

        tempoProntoOuEntrega = (TextView) findViewById(R.id.tempo_pronto_ou_entrega);

        if (tipoEntrega == 0 ){
            tempoProntoOuEntrega.setText("Tempo para entrega: 30 a 40 minutos");
        } else{
            tempoProntoOuEntrega.setText("Fica pronto em: 30 a 40 minutos");
        }

        btAcompanharPedidoEnviado = (Button) findViewById(R.id.bt_acompanhar_pedido_enviado);
        btVoltarPedidoEnviado = (Button) findViewById(R.id.bt_voltar_pedido_enviado);

        //EVENTS
        btAcompanharPedidoEnviado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(PedidoEnviadoActivity.this, AcompanharPedidoActivity.class); //Acompanhar Pedido

                i.putExtra("NumeroPedido", numeroPedido);
                i.putExtra("DataPedido", dataPedido);
                i.putExtra("HoraPedido", horaPedido);
                i.putExtra("ValorPedido", valorPedido);
                i.putExtra("StatusPedido", statusPedido);
                i.putExtra("TipoEntrega", tipoEntrega);
                i.putExtra("StatusTempo",statusTempo);
                i.putExtra("keyPedido", keyPedido);

                ArrayList<ItemPedido> itens = new ArrayList<>();
                for (ItemPedido item : itensdoPedido) {
                    itens.add(item);
                }

                i.putParcelableArrayListExtra("ItensPedido", itens);

                startActivity(i);
                finish();
            }
        });

        btVoltarPedidoEnviado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PedidoEnviadoActivity.this, CardapioMainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        getParametros();
    }

    private void getParametros(){
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
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(PedidoEnviadoActivity.this, CardapioMainActivity.class);
        startActivity(intent);
        finish();
    }
}
