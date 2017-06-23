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
        mProgressBar.setVisibility(View.VISIBLE);

        //Verifica Status do Delivery e do Estabelecimento
        mDatabaseReference.child("configuracoes").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Boolean statusDlv = Boolean.parseBoolean(dataSnapshot.child("status_delivery").child("status").getValue().toString());
                statusDelivery = statusDlv;

                Boolean statusEstab = Boolean.parseBoolean(dataSnapshot.child("status_estabelecimento").child("status").getValue().toString());
                statusEstabelecimento = statusEstab;

                ValueEventListener postListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        try {
                            for (DataSnapshot item : dataSnapshot.getChildren()) {
                                for (DataSnapshot item2 : item.getChildren()) {
                                    ItemCardapio ic = item2.getValue(ItemCardapio.class);
                                    if (ic.isStatus_promocao()) {
                                        itensPromocaoAux.add(ic);
                                    }
                                }
                            }

                        } catch (Exception n) {
                        }

                        itensPromocao = new ArrayList<>();
                        itensPromocao.addAll(itensPromocaoAux);
                        itensPromocaoAux = new ArrayList<>();

                        adapter = new PromocoesAdapter(itensPromocao, getContext(), mProgressBar, statusDelivery, statusEstabelecimento);
                        mItemPromoList.setAdapter(adapter);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                    }
                };

                mDatabaseReference.child("itens_cardapio").addValueEventListener(postListener);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

}
