package br.com.belongapps.appdelivery.cardapioOnline.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import br.com.belongapps.appdelivery.R;
import br.com.belongapps.appdelivery.cardapioOnline.activitys.CardapioMainActivity;
import br.com.belongapps.appdelivery.cardapioOnline.adapters.PromocoesAdapter;
import br.com.belongapps.appdelivery.cardapioOnline.model.ItemCardapio;
import br.com.belongapps.appdelivery.firebaseAuthApp.FirebaseAuthApp;
import br.com.belongapps.appdelivery.util.Print;

import static android.content.ContentValues.TAG;

public class TabPromocoes extends Fragment {

    private RecyclerView mItemPromoList;
    private DatabaseReference mDatabaseReference;
    private RecyclerView.Adapter adapter;
    private ProgressBar mProgressBar;
    private List<ItemCardapio> itensPromocao;
    private List<ItemCardapio> itensPromocaoAux;
    private boolean statusDelivery = true;
    private boolean statusEstabelecimento = true;

    private boolean permissaoUsuarioFazerPedido = true;

    private View viewSemPromocoes;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.tab_promocoes, container, false);


        return rootView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onStart() {
        super.onStart();

        mDatabaseReference = FirebaseDatabase.getInstance().getReference();

        mProgressBar = (ProgressBar) getView().findViewById(R.id.progressbar_promocoes);

        mItemPromoList = (RecyclerView) getView().findViewById(R.id.list_itens_promo);
        mItemPromoList.setHasFixedSize(true);
        mItemPromoList.setLayoutManager(new LinearLayoutManager(getActivity()));

        itensPromocaoAux = new ArrayList<>();
        openProgressBar();

        //Verifica Status do Delivery e do Estabelecimento
        mDatabaseReference.child("configuracoes").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Boolean statusDlv = Boolean.parseBoolean(dataSnapshot.child("status_delivery").child("status").getValue().toString());
                statusDelivery = statusDlv;

                Boolean statusEstab = Boolean.parseBoolean(dataSnapshot.child("status_estabelecimento").child("status").getValue().toString());
                statusEstabelecimento = statusEstab;

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        buscarPromocoes();

        //Buscar permissão do usuário para realizar pedidos
        if(FirebaseAuthApp.getUsuarioLogado() != null) {
            buscarPermissoesDoUsuario();
        }

    }

    public void buscarPromocoes(){

        viewSemPromocoes = getActivity().findViewById(R.id.view_empty_promocoes);

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                try {
                    for (DataSnapshot item : dataSnapshot.getChildren()) {
                        for (DataSnapshot item2 : item.getChildren()) {
                            ItemCardapio ic = item2.getValue(ItemCardapio.class);
                            if (ic.isStatus_promocao()) {
                                String itemKey = item2.getKey();
                                ic.setItemKey(itemKey);
                                itensPromocaoAux.add(ic);
                            }
                        }
                    }

                } catch (Exception n) {
                }

                itensPromocao = new ArrayList<>();
                itensPromocao.addAll(itensPromocaoAux);
                itensPromocaoAux = new ArrayList<>();

                if (itensPromocao.size() > 0){
                    mItemPromoList.setVisibility(View.VISIBLE);
                    viewSemPromocoes.setVisibility(View.GONE);
                    adapter = new PromocoesAdapter(itensPromocao, getContext(), mProgressBar, statusDelivery, statusEstabelecimento);
                    mItemPromoList.setAdapter(adapter);
                } else{
                    closeProgressBar();
                    mItemPromoList.setVisibility(View.GONE);
                    viewSemPromocoes.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        };

        mDatabaseReference.child("itens_cardapio").addValueEventListener(postListener);
    }

    private void buscarPermissoesDoUsuario(){
        mDatabaseReference.child("clientes").child(FirebaseAuthApp.getUsuarioLogado().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Integer permissao = dataSnapshot.child("permite_ped").getValue(Integer.class);

                Print.logError("Permissão: " + permissao);

                if (permissao == 0) {
                    permissaoUsuarioFazerPedido = false;
                } else {
                    permissaoUsuarioFazerPedido = true;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void openProgressBar() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    private void closeProgressBar() {
        mProgressBar.setVisibility(View.GONE);
    }

}
