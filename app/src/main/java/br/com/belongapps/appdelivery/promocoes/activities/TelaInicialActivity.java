package br.com.belongapps.appdelivery.promocoes.activities;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import br.com.belongapps.appdelivery.R;
import br.com.belongapps.appdelivery.cardapioOnline.activitys.CardapioMainActivity;
import br.com.belongapps.appdelivery.posPedido.activities.MeusPedidosActivity;
import br.com.belongapps.appdelivery.promocoes.adapter.ViewPagerAdapter;
import br.com.belongapps.appdelivery.promocoes.model.Promocao;

public class TelaInicialActivity extends AppCompatActivity {
    private Button btFazerPedido, btMeusPedidos;

    private List<Promocao> promocoes;
    private List<Promocao> promocoesAux;

    private DatabaseReference mDatabaseReference;

    ViewPager viewPager;
    LinearLayout promoDots;
    private int promoCount;
    private ImageView[] dots;

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

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();
    }

    public class TempodeExibicaoPromo extends TimerTask {

        @Override
        public void run() {
            TelaInicialActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    if (viewPager.getCurrentItem() == 0) {
                        viewPager.setCurrentItem(1);
                    } else if (viewPager.getCurrentItem() == 1) {
                        viewPager.setCurrentItem(2);
                    } else {
                        viewPager.setCurrentItem(0);
                    }

                }
            });
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

       /* promocoes = new ArrayList<>();*/

        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        promocoesAux = new ArrayList<>();

        mDatabaseReference.child("promocoes").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot promo : dataSnapshot.getChildren()) {
                    Promocao promocao = promo.getValue(Promocao.class);
                    promocoesAux.add(promocao);
                }

                promocoes = new ArrayList<>();

                promocoes.addAll(promocoesAux);

                viewPager = (ViewPager) findViewById(R.id.slider_promo);
                promoDots = (LinearLayout) findViewById(R.id.promo_dots);

                ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(TelaInicialActivity.this, promocoes);
                viewPager.setAdapter(viewPagerAdapter);

                promoCount = promocoesAux.size();
                dots = new ImageView[promoCount];

                promocoesAux = new ArrayList<>();

                promoDots.removeAllViews();

                for (int i = 0; i < promoCount; i++) {
                    dots[i] = new ImageView(TelaInicialActivity.this);
                    dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.promo_nonactive_dot));

                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    params.setMargins(8, 0, 8, 0);

                    promoDots.addView(dots[i], params);
                }

                if (dots != null) {

                    dots[0].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.promo_active_dot));
                    viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                        @Override
                        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                        }

                        @Override
                        public void onPageSelected(int position) {
                            for (int i = 0; i < promoCount; i++) {
                                dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.promo_nonactive_dot));
                            }

                            dots[position].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.promo_active_dot));
                        }

                        @Override
                        public void onPageScrollStateChanged(int state) {

                        }
                    });


                }
/*
                Timer timer = new Timer();
                timer.scheduleAtFixedRate(new TempodeExibicaoPromo(), 2000, 4000);*/

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
