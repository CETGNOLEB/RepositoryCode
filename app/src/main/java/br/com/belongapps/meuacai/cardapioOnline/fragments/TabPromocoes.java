package br.com.belongapps.meuacai.cardapioOnline.fragments;

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

import br.com.belongapps.meuacai.R;
import br.com.belongapps.meuacai.cardapioOnline.adapters.PromocoesAdapter;
import br.com.belongapps.meuacai.cardapioOnline.model.ItemCardapio;

import static android.content.ContentValues.TAG;

public class TabPromocoes extends Fragment {

    private RecyclerView mItemPromoList;
    private DatabaseReference mDatabaseReference;
    private RecyclerView.Adapter adapter;

    private ProgressBar mProgressBar;

    private List<ItemCardapio> itensPromocao;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab_promocoes, container, false);

        itensPromocao = getPromocoes();

        Log.println(Log.ERROR, "SIZE LIST:" ,"" + itensPromocao.size());

        mItemPromoList = (RecyclerView) rootView.findViewById(R.id.list_itens_promo);
        mItemPromoList.setHasFixedSize(true);
        mItemPromoList.setLayoutManager(new LinearLayoutManager(getActivity()));

        adapter = new PromocoesAdapter(itensPromocao, getContext());
        mItemPromoList.setAdapter(adapter);

        return rootView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        itensPromocao = getPromocoes();
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    private List<ItemCardapio> getPromocoes() {

        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        final ArrayList<ItemCardapio> list = new ArrayList<>();

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                try {
                    for (DataSnapshot item : dataSnapshot.getChildren()) {
                        for (DataSnapshot item2 : item.getChildren()) {
                            ItemCardapio ic = item2.getValue(ItemCardapio.class);
                            if (ic.isStatus_promocao()) {
                                list.add(ic);
                            }
                        }
                    }

                    for (ItemCardapio item :
                            itensPromocao) {

                        Log.println(Log.ERROR, "NOME:" , item.getNome());
                    }

                } catch (Exception n) {
                }

                adapter = new PromocoesAdapter(itensPromocao, getContext());
                mItemPromoList.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };

        mDatabaseReference.child("itens_cardapio").addValueEventListener(postListener);

        return list;
    }

    private void openProgressBar() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    private void closeProgressBar() {
        mProgressBar.setVisibility(View.GONE);
    }
}
