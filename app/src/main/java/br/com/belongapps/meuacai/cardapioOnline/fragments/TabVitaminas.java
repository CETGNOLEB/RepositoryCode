package br.com.belongapps.meuacai.cardapioOnline.fragments;

import android.content.Context;
import android.content.Intent;
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
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.Locale;

import br.com.belongapps.meuacai.R;
import br.com.belongapps.meuacai.cardapioOnline.activitys.DetalhesdoItemActivity;
import br.com.belongapps.meuacai.cardapioOnline.model.ItemCardapio;


public class TabVitaminas extends Fragment {

    private RecyclerView mVitaminasList;
    private DatabaseReference mDatabaseReference;

    private ProgressBar mProgressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab_vitaminas, container, false);

        return rootView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onStart() {
        super.onStart();

        mProgressBar = (ProgressBar) getActivity().findViewById(R.id.progressbar_escolher_vitaminas);
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("itens_cardapio").child("9");

        mVitaminasList = (RecyclerView) getView().findViewById(R.id.list_vitaminas);
        mVitaminasList.setHasFixedSize(true);
        mVitaminasList.setLayoutManager(new LinearLayoutManager(getActivity()));

        openProgressBar();


        final FirebaseRecyclerAdapter<ItemCardapio, VitaminasViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<ItemCardapio, VitaminasViewHolder>(
                ItemCardapio.class, R.layout.card_vitaminas, VitaminasViewHolder.class, mDatabaseReference
        ) {

            @Override
            public void onBindViewHolder(final VitaminasViewHolder viewHolder, final int position) {
                super.onBindViewHolder(viewHolder, position);

                YoYo.with(Techniques.BounceInUp).playOn(viewHolder.card_vitaminas);
            }

            @Override
            protected void populateViewHolder(VitaminasViewHolder viewHolder, final ItemCardapio model, int position) {
                viewHolder.setNome(model.getNome());
                viewHolder.setDescricao(model.getDescricao());
                viewHolder.setValorUnitario(model.getValor_unit());
                viewHolder.setImagem(getContext(), model.getRef_img());

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), DetalhesdoItemActivity.class);
                        intent.putExtra("NomeProduto", model.getNome());
                        intent.putExtra("DescProduto", model.getDescricao());
                        intent.putExtra("ImgProduto", model.getRef_img());
                        intent.putExtra("ValorProduto", model.getValor_unit());

                        intent.putExtra("TelaAnterior", "TabVitaminas");
                        startActivity(intent);

                        getActivity().finish();
                    }
                });
            }


        };

        mVitaminasList.setAdapter(firebaseRecyclerAdapter);

        closeProgressBar();
    }

    public static class VitaminasViewHolder extends RecyclerView.ViewHolder {

        View mView;
        CardView card_vitaminas;

        public VitaminasViewHolder(final View itemView) {
            super(itemView);

            mView = itemView;
            card_vitaminas = (CardView) mView.findViewById(R.id.card_vitaminas);

        }

        public void setNome(String nome) {

            TextView item_nome = (TextView) mView.findViewById(R.id.item_nome_vitamina);
            item_nome.setText(nome);

        }

        public void setDescricao(String descricao) {

            TextView item_descricao = (TextView) mView.findViewById(R.id.item_desc_vitamina);
            item_descricao.setText(descricao);
        }

        public void setValorUnitario(double valor_unit) {

            TextView item_valor_unit = (TextView) mView.findViewById(R.id.item_valor_unit_vitamina);
            item_valor_unit.setText(" R$ " +  String.format(Locale.US, "%.2f", valor_unit).replace(".", ","));

        }

        public void setImagem(final Context context, final String url) {
            final ImageView item_ref_image = (ImageView) mView.findViewById(R.id.img_vitamina);
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
