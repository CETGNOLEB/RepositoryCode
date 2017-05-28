package br.com.belongapps.meuacai.cardapioOnline.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
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
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;


import java.util.Locale;

import br.com.belongapps.meuacai.R;
import br.com.belongapps.meuacai.cardapioOnline.activitys.EscolherRecheioActivity;
import br.com.belongapps.meuacai.cardapioOnline.model.ItemCardapio;
import br.com.belongapps.meuacai.cardapioOnline.model.ItemPedido;

public class TabAcai extends Fragment {

    private RecyclerView mAcaiList;
    private DatabaseReference mDatabaseReference;

    private ProgressBar mProgressBar;
    private ItemPedido itemPedido;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab_acai, container, false);

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

        mProgressBar = (ProgressBar) getActivity().findViewById(R.id.progressbar_escolher_acai);
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("itens_cardapio").child("1");
        mDatabaseReference.keepSynced(true);

        mAcaiList = (RecyclerView) getView().findViewById(R.id.list_acai);
        mAcaiList.setHasFixedSize(true);
        mAcaiList.setLayoutManager(new LinearLayoutManager(getActivity()));

        openProgressBar();


        final FirebaseRecyclerAdapter<ItemCardapio, TabAcai.AcaiViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<ItemCardapio, AcaiViewHolder>(
                ItemCardapio.class, R.layout.card_acai, TabAcai.AcaiViewHolder.class, mDatabaseReference
        ) {

            @Override
            public void onBindViewHolder(final AcaiViewHolder viewHolder, final int position) {
                super.onBindViewHolder(viewHolder, position);

                YoYo.with(Techniques.BounceInUp).playOn(viewHolder.card_acai);
            }

            @Override
            protected void populateViewHolder(TabAcai.AcaiViewHolder viewHolder, final ItemCardapio model, int position) {
                mProgressBar.setVisibility(ProgressBar.INVISIBLE);

                viewHolder.setNome(model.getNome());
                viewHolder.setDescricao(model.getDescricao());
                viewHolder.setValorUnitarioEPromocao(model.getValor_unit(), model.isStatus_promocao(), model.getPreco_promocional());
                viewHolder.setImagem(getContext(), model.getRef_img());

                if (model.getStatus_item().equals("Ativado")) {

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
            }

        };

        mAcaiList.setAdapter(firebaseRecyclerAdapter);

    }

    public static class AcaiViewHolder extends RecyclerView.ViewHolder {

        View mView;
        CardView card_acai;

        public AcaiViewHolder(final View itemView) {
            super(itemView);

            mView = itemView;
            card_acai = (CardView) mView.findViewById(R.id.card_acai);

        }

        public void setNome(String nome) {

            TextView item_nome = (TextView) mView.findViewById(R.id.item_nome_acai);
            item_nome.setText(nome);

        }

        public void setDescricao(String descricao) {

            TextView item_descricao = (TextView) mView.findViewById(R.id.item_desc_acai);
            item_descricao.setText(descricao);
        }

        public void setValorUnitarioEPromocao(double valor_unit, boolean status_promocao, double valor_promocional) {

            TextView item_valor_promo = (TextView) mView.findViewById(R.id.item_valor_promo_acai);
            TextView item_valor_unit = (TextView) mView.findViewById(R.id.item_valor_unit_acai);

            if (status_promocao == true){
                item_valor_promo.setText(" R$ " +  String.format(Locale.US, "%.2f", valor_promocional).replace(".", ","));
                item_valor_unit.setText(" R$ " +  String.format(Locale.US, "%.2f", valor_unit).replace(".", ","));
                item_valor_unit.setPaintFlags(item_valor_unit.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                item_valor_unit.setVisibility(View.VISIBLE);
            } else{
                item_valor_promo.setText(" R$ " +  String.format(Locale.US, "%.2f", valor_unit).replace(".", ","));
            }

        }

        public void setImagem(final Context context, final String url) {
            final ImageView item_ref_image = (ImageView) mView.findViewById(R.id.img_acai);

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
