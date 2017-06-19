package br.com.belongapps.appdelivery.cardapioOnline.activitys;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

import br.com.belongapps.appdelivery.R;
import br.com.belongapps.appdelivery.cardapioOnline.dao.CarrinhoDAO;
import br.com.belongapps.appdelivery.cardapioOnline.fragments.TabBebidas;
import br.com.belongapps.appdelivery.cardapioOnline.fragments.TabPizzas;
import br.com.belongapps.appdelivery.cardapioOnline.fragments.TabPromocoes;
import br.com.belongapps.appdelivery.cardapioOnline.fragments.TabSucos;
import br.com.belongapps.appdelivery.cardapioOnline.fragments.TabVitaminas;
import br.com.belongapps.appdelivery.cardapioOnline.model.ItemPedido;
import br.com.belongapps.appdelivery.gerencial.activities.EnderecosActivity;
import br.com.belongapps.appdelivery.helpAbout.activities.AjudaActivity;
import br.com.belongapps.appdelivery.helpAbout.activities.SobreActivity;
import br.com.belongapps.appdelivery.posPedido.activities.MeusPedidosActivity;
import br.com.belongapps.appdelivery.promocoes.activities.TelaInicialActivity;
import br.com.belongapps.appdelivery.cardapioOnline.fragments.TabAcai;
import br.com.belongapps.appdelivery.cardapioOnline.fragments.TabCombos;
import br.com.belongapps.appdelivery.cardapioOnline.fragments.TabSalgados;
import br.com.belongapps.appdelivery.cardapioOnline.fragments.TabSanduiches;
import br.com.belongapps.appdelivery.seguranca.activities.LoginActivity;

public class CardapioMainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private CardapioMainActivity.SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private TextView nomeusuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cardapio);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF")); //Cor do Titulo da Tela
        setSupportActionBar(toolbar);

        //Exibir nome do usuário
        nomeusuario = (TextView) findViewById(R.id.nome_usuario_menu);

        setIconCarrinho();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_drawer);
        navigationView.setNavigationItemSelectedListener(this);

        mSectionsPagerAdapter = new CardapioMainActivity.SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Intent intent = new Intent(CardapioMainActivity.this, TelaInicialActivity.class);
            startActivity(intent);
            finish();

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_item_cardapio, menu);

        MenuItem menuItem = menu.getItem(0);

        //buscar tamanho da lista do carrinho
        CarrinhoDAO crud = new CarrinhoDAO(getBaseContext());
        List<ItemPedido> itens_pedido = crud.getAllItens();
        int size = itens_pedido.size();

        //Alterar icon
        if(size == 1){
            menuItem.setIcon(R.drawable.ic_cart_one);
        } else if(size == 2){
            menuItem.setIcon(R.drawable.ic_cart_two);
        } else if(size == 3){
            menuItem.setIcon(R.drawable.ic_cart_three);
        } else if(size == 4){
            menuItem.setIcon(R.drawable.ic_cart_four);
        } else if(size == 5){
            menuItem.setIcon(R.drawable.ic_cart_five);
        } else if(size == 6){
            menuItem.setIcon(R.drawable.ic_cart_six);
        } else if(size == 7){
            menuItem.setIcon(R.drawable.ic_cart_seven);
        } else if(size == 8){
            menuItem.setIcon(R.drawable.ic_cart_eight);
        } else if(size == 9){
            menuItem.setIcon(R.drawable.ic_cart_nine);
        } else {
            menuItem.setIcon(R.drawable.ic_action_cart);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_carrinho) {

            Intent i = new Intent(CardapioMainActivity.this, CarrinhoActivity.class);
            startActivity(i);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_cardapio) {
            Intent i = new Intent(CardapioMainActivity.this, CardapioMainActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_carrinho) {
            Intent i = new Intent(CardapioMainActivity.this, CarrinhoActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_meuspedidos) {
            Intent i = new Intent(CardapioMainActivity.this, MeusPedidosActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_enderecos) {
            Intent i = new Intent(CardapioMainActivity.this, EnderecosActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_sobre) {
            Intent i = new Intent(CardapioMainActivity.this, SobreActivity.class);
            startActivity(i);
       /* } else if (id == R.id.nav_ajuda) {
            Intent i = new Intent(CardapioMainActivity.this, AjudaActivity.class);
            startActivity(i);*/
        } else if (id == R.id.nav_sair) {
            FirebaseAuth.getInstance().signOut();
            Intent i = new Intent(CardapioMainActivity.this, LoginActivity.class);
            startActivity(i);

            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                    TabPromocoes tabPromocoes = new TabPromocoes();
                    return tabPromocoes;

                case 1:
                    TabPizzas tabPizzas = new TabPizzas();
                    return tabPizzas;

                case 2:
                    TabSanduiches tabSanduiches = new TabSanduiches();
                    return tabSanduiches;

                case 3:
                    TabSalgados tabSalgados = new TabSalgados();
                    return tabSalgados;

                case 4:
                    TabCombos tabCombos = new TabCombos();
                    return tabCombos;

                case 5:

                    TabAcai tabAcai = new TabAcai();
                    return tabAcai;

                case 6:

                    TabVitaminas tabVitaminas = new TabVitaminas();
                    return tabVitaminas;

                case 7:

                    TabSucos tabSucos = new TabSucos();
                    return tabSucos;

                case 8:

                    TabBebidas tabBebidas = new TabBebidas();
                    return tabBebidas;

                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 9;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "PROMOÇÕES";
                case 1:
                    return "PIZZAS";
                case 2:
                    return "SANDUICHES";
                case 3:
                    return "SALGADOS";
                case 4:
                    return "COMBOS";
                case 5:
                    return "AÇAI";
                case 6:
                    return "VITAMINAS";
                case 7:
                    return "SUCOS";
                case 8:
                    return "BEBIDAS";
            }

            return null;
        }
    }

    public void setIconCarrinho(){

    }
}
