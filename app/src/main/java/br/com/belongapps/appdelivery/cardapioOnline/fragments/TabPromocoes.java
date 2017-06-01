package br.com.belongapps.appdelivery.cardapioOnline.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import br.com.belongapps.appdelivery.cardapioOnline.adapters.PromocoesAdapter;
import br.com.belongapps.appdelivery.cardapioOnline.model.ItemCardapio;

import static android.content.ContentValues.TAG;

public class TabPromocoes extends Fragment {

    private RecyclerView mItemPromoList;
    private DatabaseReference mDatabaseReference;
    private RecyclerView.Adapter adapter;

    private ProgressBar mProgressBar;

    private List<ItemCardapio> itensPromocao;
    List<ItemCardapio> itensPromocaoAux;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.tab_promocoes, container, false);

        mProgressBar = (ProgressBar) rootView.findViewById(R.id.progressbar_promocoes);

        mItemPromoList = (RecyclerView) rootView.findViewById(R.id.list_itens_promo);
        mItemPromoList.setHasFixedSize(true);
        mItemPromoList.setLayoutManager(new LinearLayoutManager(getActivity()));

        mDatabaseReference = FirebaseDatabase.getInstance().getReference();

        itensPromocaoAux = new ArrayList<>();

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

                adapter = new PromocoesAdapter(itensPromocao, getContext(), mProgressBar);
                mItemPromoList.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        };

        mDatabaseReference.child("itens_cardapio").addValueEventListener(postListener);

        return rootView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onStart() {
        super.onStart();

    }

}
