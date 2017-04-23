package br.com.belongapps.meuacai.cardapioOnline.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.Locale;

import br.com.belongapps.meuacai.R;
import br.com.belongapps.meuacai.cardapioOnline.activitys.EscolherRecheioActivity;
import br.com.belongapps.meuacai.cardapioOnline.model.ItemCardapio;
import br.com.belongapps.meuacai.cardapioOnline.model.ItemPedido;

public class TabPromocoes extends Fragment {

    private RecyclerView mItemPromoList;
    private DatabaseReference mDatabaseReference;
    private Query mQuery;

    private ProgressBar mProgressBar;
    private ItemPedido itemPedido;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab_promocoes, container, false);

        return rootView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onStart() {
        super.onStart();

        itemPedido = new ItemPedido();

        mProgressBar = (ProgressBar) getActivity().findViewById(R.id.progressbar_itens_promo);

        boolean statusPromocao = true;
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("itens_cardapio").child("1");
        mDatabaseReference.keepSynced(true);

        mQuery = mDatabaseReference.orderByChild("status_promocao").equalTo(statusPromocao);

        mItemPromoList = (RecyclerView) getView().findViewById(R.id.list_itens_promo);
        mItemPromoList.setHasFixedSize(true);
        mItemPromoList.setLayoutManager(new LinearLayoutManager(getActivity()));

        openProgressBar();

        final FirebaseRecyclerAdapter<ItemCardapio, ItemPromoViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<ItemCardapio, ItemPromoViewHolder>(
                ItemCardapio.class, R.layout.card_item_promo, ItemPromoViewHolder.class, mQuery
        ) {

            @Override
            public void onBindViewHolder(final ItemPromoViewHolder viewHolder, final int position) {
                super.onBindViewHolder(viewHolder, position);

                YoYo.with(Techniques.BounceInUp).playOn(viewHolder.card_item_promo);
            }

            @Override
            protected void populateViewHolder(ItemPromoViewHolder viewHolder, final ItemCardapio model, int position) {
                mProgressBar.setVisibility(ProgressBar.INVISIBLE);

                viewHolder.setNome(model.getNome());
                viewHolder.setDescricao(model.getDescricao());
                viewHolder.setValorUnitario(model.getValor_unit());
                viewHolder.setValorPromo(model.getPreco_promocional());
                viewHolder.setImagem(getContext(), model.getRef_img());

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        itemPedido.setNome(model.getNome());
                        itemPedido.setRef_img(model.getRef_img());
                        //itemPedido.setValor_unit(Long.valueOf(model.getValor_unit());

                        Intent intent = new Intent(getActivity(), EscolherRecheioActivity.class);
                        intent.putExtra("ItemPedido", itemPedido);

                        intent.putExtra("qtd_recheios_item_cardapio", model.getQtd_recheios());

                        startActivity(intent);

                        getActivity().finish();
                    }
                });
            }


        };

        mItemPromoList.setAdapter(firebaseRecyclerAdapter);

    }

    public static class ItemPromoViewHolder extends RecyclerView.ViewHolder {

        View mView;
        CardView card_item_promo;

        public ItemPromoViewHolder(final View itemView) {
            super(itemView);

            mView = itemView;
            card_item_promo = (CardView) mView.findViewById(R.id.card_item_promo);

        }

        public void setNome(String nome) {

            TextView item_nome = (TextView) mView.findViewById(R.id.item_nome_item_promo);
            item_nome.setText(nome);

        }

        public void setDescricao(String descricao) {

            TextView item_descricao = (TextView) mView.findViewById(R.id.item_desc_item_promo);
            item_descricao.setText(descricao);
        }

        public void setValorUnitario(double valor_unit) {
            TextView item_valor_unit = (TextView) mView.findViewById(R.id.item_valor_unit_item_promo);
            item_valor_unit.setText(" R$ " + String.format(Locale.US, "%.2f", valor_unit).replace(".", ","));
            item_valor_unit.setPaintFlags(item_valor_unit.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }

        public void setValorPromo(double valor_unit) {
            TextView item_valor_unit = (TextView) mView.findViewById(R.id.item_promo_valor_unit_item_promo);
            item_valor_unit.setText(" R$ " + String.format(Locale.US, "%.2f", valor_unit).replace(".", ","));
        }


        public void setImagem(final Context context, final String url) {
            final ImageView item_ref_image = (ImageView) mView.findViewById(R.id.img__item_promo);

            Picasso.with(context).load(url).networkPolicy(NetworkPolicy.OFFLINE).into(item_ref_image, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {
                    Picasso.with(context).load(url).into(item_ref_image);
                }
            });
        }

    }

    private void openProgressBar() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    private void closeProgressBar() {
        mProgressBar.setVisibility(View.GONE);
    }
}
