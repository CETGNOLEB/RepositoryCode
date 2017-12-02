package br.com.belongapps.appdelivery.posPedido.activities;

import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import br.com.belongapps.appdelivery.R;
import br.com.belongapps.appdelivery.cardapioOnline.activitys.CardapioMainActivity;
import br.com.belongapps.appdelivery.firebaseAuthApp.FirebaseAuthApp;
import br.com.belongapps.appdelivery.posPedido.fragments.TabCartaoFidelidade;
import br.com.belongapps.appdelivery.posPedido.fragments.TabPedidosRealizados;
import br.com.belongapps.appdelivery.seguranca.activities.LoginActivity;
import br.com.belongapps.appdelivery.util.ConexaoUtil;

public class MeusPedidosActivity extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private Toolbar mToolbar;

    //Status Conexão
    private CoordinatorLayout snakeBarLayout;
    private Snackbar snackbar;
    private boolean statusConexao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedidos_realizados);

        mToolbar = (Toolbar) findViewById(R.id.toolbar_meus_pedidos);
        mToolbar.setTitle("Meus Pedidos");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs_meus_pedidos);
        tabLayout.setupWithViewPager(mViewPager);

    }

    public static class PlaceholderFragment extends Fragment {

        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }


        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_pedidos_realizados, container, false);
            return rootView;
        }
    }


    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    TabPedidosRealizados tabPedReal = new TabPedidosRealizados();
                    return tabPedReal;

                case 1:
                    TabCartaoFidelidade tabCartaoFidelidade = new TabCartaoFidelidade();
                    return tabCartaoFidelidade;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "PEDIDOS REALIZADOS";
                case 1:
                    return "CARTÃO FIDELIDADE";
            }
            return null;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(MeusPedidosActivity.this, CardapioMainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(MeusPedidosActivity.this, CardapioMainActivity.class);
                startActivity(intent);
                finish();

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (FirebaseAuthApp.getUsuarioLogado() == null) {
            Intent i = new Intent(MeusPedidosActivity.this, LoginActivity.class);
            startActivity(i);
            finish();
        }

        verificaStatusDeConexao();
    }

    public void verificaStatusDeConexao(){
        snakeBarLayout = (CoordinatorLayout) findViewById(R.id.main_content);

        snackbar = Snackbar
                .make(snakeBarLayout, "Sem conexão com a internet", Snackbar.LENGTH_INDEFINITE);

        statusConexao = ConexaoUtil.verificaConectividade(this);

        if (statusConexao){
            snackbar.dismiss();
        } else{
            View snackView = snackbar.getView();
            snackView.setBackgroundColor(ContextCompat.getColor(MeusPedidosActivity.this, R.color.snakebarColor));
            snackbar.setActionTextColor(getResources().getColor(R.color.textColorPrimary));
            snackbar.show();
        }
    }
}
