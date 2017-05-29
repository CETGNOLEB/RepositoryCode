package br.com.belongapps.appdelivery.cardapioOnline.activitys;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.belongapps.appdelivery.R;
import br.com.belongapps.appdelivery.util.DataUtil;

import static android.content.ContentValues.TAG;

public class TesteActivity extends AppCompatActivity {

    Button botaoTeste;
    private DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    TextView ultimonumero;

    ProgressBar progressBar;

    Date hoje = new Date();
    String dia = DataUtil.formatar(hoje, "ddMMyyyy");
    String ultimo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teste);

        ultimonumero = (TextView) findViewById(R.id.ultimo_pedido);
        botaoTeste = (Button) findViewById(R.id.bt_testes);

        progressBar = (ProgressBar) findViewById(R.id.progressbar_teste);

        botaoTeste.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progressBar.setVisibility(View.VISIBLE);
                ultimo = gerarNumeroPedido(ultimo);
                ultimonumero.setText(ultimo);
                Log.println(Log.ERROR, "Teste Final", ultimo);
            }
        });

    }

    public void updateNumero(List<String> list) {

        if (list.isEmpty()) {
            ultimo = "0001";

        } else {
            ultimo = list.get(list.size() - 1);
            Log.println(Log.ERROR, "Ultimo Pedido: ", list.get(list.size() - 1));
        }
    }

    public String gerarNumeroPedido(String numero) {

        int intnum = Integer.parseInt(numero);
        intnum++;

        NumberFormat formatter = new DecimalFormat("00000");

        numero = formatter.format(intnum);

        return numero;
    }

    @Override
    protected void onStart() {
        super.onStart();

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<String> list = new ArrayList<String>();

                try {
                    for (DataSnapshot pedido : dataSnapshot.getChildren()) {
                        for (DataSnapshot dia : pedido.getChildren()) {
                            String numpedido = dia.child("numero_pedido").getValue().toString();

                            Log.println(Log.ERROR, "NUM: ", numpedido);
                            list.add(numpedido);
                        }
                    }

                    updateNumero(list);
                } catch (NullPointerException n) {
                    list.add("0001");
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
