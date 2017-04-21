package br.com.belongapps.meuacai.cardapioOnline.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import br.com.belongapps.meuacai.R;
import br.com.belongapps.meuacai.cardapioOnline.activitys.CardapioMainActivity;
import br.com.belongapps.meuacai.cardapioOnline.activitys.EscolherPizzaActivity;
import br.com.belongapps.meuacai.cardapioOnline.activitys.EscolherRecheioActivity;
import br.com.belongapps.meuacai.cardapioOnline.model.ItemCardapio;
import br.com.belongapps.meuacai.cardapioOnline.model.TamPizza;

public class TabPizzas extends Fragment {

    private RecyclerView mTamPizzaList;
    private DatabaseReference mDatabaseReference;

    private ProgressBar mProgressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab1_pizzas, container, false);

        return rootView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onStart() {
        super.onStart();

        mProgressBar = (ProgressBar) getActivity().findViewById(R.id.progressbar_escolher_pizzas);
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("tipos_pizzas");

        mTamPizzaList = (RecyclerView) getView().findViewById(R.id.list_pizzas);
        mTamPizzaList.setHasFixedSize(true);
        mTamPizzaList.setLayoutManager(new LinearLayoutManager(getActivity()));

        openProgressBar();


        final FirebaseRecyclerAdapter<TamPizza, TamPizzaViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<TamPizza, TamPizzaViewHolder>(
                TamPizza.class, R.layout.card_tam_pizzas, TamPizzaViewHolder.class, mDatabaseReference
        ) {

            @Override
            public void onBindViewHolder(final TamPizzaViewHolder viewHolder, final int position) {
                super.onBindViewHolder(viewHolder, position);

                YoYo.with(Techniques.BounceInUp).playOn(viewHolder.card_tam_pizza);
            }

            @Override
            protected void populateViewHolder(TamPizzaViewHolder viewHolder, final TamPizza model, int position) {
                viewHolder.setNome(model.getNome());
                viewHolder.setApartirDe(model.getApartir_de());
                viewHolder.setImagem(getContext(), model.getRef_img());

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        AlertDialog.Builder mBilder = new AlertDialog.Builder(getContext());
                        View layoutDialog = getActivity().getLayoutInflater().inflate(R.layout.dialog_tipo_pizza, null);

                        Button btInteira = (Button) layoutDialog.findViewById(R.id.bt_pizza_inteira);
                        Button btMetadeMetade = (Button) layoutDialog.findViewById(R.id.bt_pizza_metade_metade);

                        btInteira.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent i = new Intent(getActivity(), EscolherPizzaActivity.class);
                                i.putExtra("tipoPizza", "Inteira");
                                i.putExtra("tamPizza", model.getNome());
                                startActivity(i);

                            }
                        });

                        btMetadeMetade.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent i = new Intent(getActivity(), EscolherPizzaActivity.class);
                                i.putExtra("tipoPizza", "MetadeMetade");
                                i.putExtra("tamPizza", model.getNome());
                                startActivity(i);
                            }
                        });

                        mBilder.setView(layoutDialog);
                        AlertDialog dialogEscolherTamPizza = mBilder.create();
                        dialogEscolherTamPizza.show();
                    }
                });
            }
        };

        mTamPizzaList.setAdapter(firebaseRecyclerAdapter);

        closeProgressBar();
    }

    public static class TamPizzaViewHolder extends RecyclerView.ViewHolder {

        View mView;
        CardView card_tam_pizza;

        public TamPizzaViewHolder(final View itemView) {
            super(itemView);

            mView = itemView;
            card_tam_pizza = (CardView) mView.findViewById(R.id.card_tam_pizzas);

        }

        public void setNome(String nome) {

            TextView item_nome = (TextView) mView.findViewById(R.id.item_nome_pizza);
            item_nome.setText(nome);

        }

        public void setApartirDe(long valor_unit) {
            NumberFormat formatodor = new DecimalFormat(".##");

            TextView apartir_de = (TextView) mView.findViewById(R.id.apartir_de_pizza);
            apartir_de.setText("Apartir de R$ " + valor_unit + ",00");

        }

        public void setImagem(Context context, String url) {
            ImageView item_ref_image = (ImageView) mView.findViewById(R.id.img_tam_pizza);
            Picasso.with(context).load(url).into(item_ref_image);
        }

    }

    private void openProgressBar() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    private void closeProgressBar() {
        mProgressBar.setVisibility(View.GONE);
    }
}
