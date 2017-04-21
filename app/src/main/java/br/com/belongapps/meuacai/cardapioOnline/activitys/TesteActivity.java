package br.com.belongapps.meuacai.cardapioOnline.activitys;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import br.com.belongapps.meuacai.R;
import br.com.belongapps.meuacai.cardapioOnline.BO.PedidoBO;
import br.com.belongapps.meuacai.cardapioOnline.dao.FirebaseDAO;
import br.com.belongapps.meuacai.cardapioOnline.model.Pedido;

import static android.content.ContentValues.TAG;

public class TesteActivity extends AppCompatActivity {
    Button botaoTeste;
    private DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    List<String> numerosdosPedidos = new ArrayList<>();
    String dia = "11042017";
    private String numero = "0001";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teste);

        botaoTeste = (Button) findViewById(R.id.bt_testes);

        botaoTeste.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               String retorno = FirebaseDAO.ultimoPedidoDoDia("11/04/2017");
               Log.println(Log.ERROR, "RETORNO: ", retorno);
            }
        });

    }

}
