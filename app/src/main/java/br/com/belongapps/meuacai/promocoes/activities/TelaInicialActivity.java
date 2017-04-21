package br.com.belongapps.meuacai.promocoes.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import br.com.belongapps.meuacai.R;
import br.com.belongapps.meuacai.cardapioOnline.activitys.CardapioMainActivity;
import br.com.belongapps.meuacai.posPedido.activities.MeusPedidosActivity;
import br.com.belongapps.meuacai.promocoes.model.Promocao;

public class TelaInicialActivity extends AppCompatActivity {
    private Button btFazerPedido, btMeusPedidos;
    private ImageView img_promo1;

    private List<Promocao> promocoes;

    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref = database.getReference("/promocoes");

    /*private DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    private ChildEventListener childEventListener;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_inicial);

        btFazerPedido = (Button) findViewById(R.id.bt_realizar_pedido);

        btFazerPedido.setOnClickListener(new View.OnClickListener() {
                                             @Override
                                             public void onClick(View v) {
                                                 Intent i = new Intent(TelaInicialActivity.this, CardapioMainActivity.class);
                                                 startActivity(i);
                                                 finish();
                                             }
                                         }

        );

        btMeusPedidos = (Button) findViewById(R.id.bt_meus_pedidos);

        btMeusPedidos.setOnClickListener(new View.OnClickListener()

                                         {
                                             @Override
                                             public void onClick(View v) {
                                                 Intent i = new Intent(TelaInicialActivity.this, MeusPedidosActivity.class);
                                                 startActivity(i);
                                                 finish();
                                             }
                                         }

        );
    }
}
