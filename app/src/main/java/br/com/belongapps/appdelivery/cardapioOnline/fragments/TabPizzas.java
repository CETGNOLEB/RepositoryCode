package br.com.belongapps.appdelivery.cardapioOnline.fragments;

import android.app.Activity;
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
import android.widget.RadioButton;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import br.com.belongapps.appdelivery.R;
import br.com.belongapps.appdelivery.cardapioOnline.activitys.EscolherPizzaActivity;
import br.com.belongapps.appdelivery.cardapioOnline.model.TamPizza;
import br.com.belongapps.appdelivery.firebaseAuthApp.FirebaseAuthApp;
import br.com.belongapps.appdelivery.seguranca.activities.LoginActivity;
import br.com.belongapps.appdelivery.util.StringUtil;

public class TabPizzas extends Fragment {

    private RecyclerView mTamPizzaList;
    private DatabaseReference mDatabaseReference;
    private ProgressBar mProgressBar;
    private int tipoPizzaSelecionada = 0;
    private boolean statusDelivery = true;
    private boolean statusEstabelecimento = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab_pizzas, container, false);

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


        mProgressBar = (ProgressBar) getActivity().findViewById(R.id.progressbar_escolher_pizzas);
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("tipos_pizzas");
        mDatabaseReference.keepSynced(true);

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

                //YoYo.with(Techniques.BounceInUp).playOn(viewHolder.card_tam_pizza);
            }

            @Override
            protected void populateViewHolder(final TamPizzaViewHolder viewHolder, final TamPizza model, int position) {
                closeProgressBar();

                viewHolder.setNome(model.getNome());
                viewHolder.setApartirDe(model.getApartir_de());
                viewHolder.setImagem(getContext(), model.getRef_img());

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (statusEstabelecimento == false) {

                            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                            AlertDialog.Builder mBilder = new AlertDialog.Builder(getContext(), R.style.MyDialogTheme);
                            View layoutDialog = inflater.inflate(R.layout.dialog_estabelecimento_fechado, null);

                            Button btEntendi = (Button) layoutDialog.findViewById(R.id.bt_entendi_estabeleciemento_fechado);

                            mBilder.setView(layoutDialog);
                            final AlertDialog dialogEstabelecimentoFechado = mBilder.create();
                            dialogEstabelecimentoFechado.show();

                            btEntendi.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialogEstabelecimentoFechado.dismiss();
                                }
                            });

                        } else if (statusDelivery == false) {

                            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                            AlertDialog.Builder mBilder = new AlertDialog.Builder(getContext(), R.style.MyDialogTheme);
                            View layoutDialog = inflater.inflate(R.layout.dialog_delivery_fechado, null);

                            Button btVoltar = (Button) layoutDialog.findViewById(R.id.bt_voltar_delivery_fechado);
                            Button btContinuar = (Button) layoutDialog.findViewById(R.id.bt_continuar_delivery_fechado);

                            mBilder.setView(layoutDialog);
                            final AlertDialog dialogDeliveryFechado = mBilder.create();
                            dialogDeliveryFechado.show();

                            btVoltar.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialogDeliveryFechado.dismiss();
                                }
                            });

                            btContinuar.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialogDeliveryFechado.dismiss();
                                    selecionarItem(model);
                                }
                            });

                        } else {
                            selecionarItem(model);
                        }
                    }
                });
            }
        };

        mTamPizzaList.setAdapter(firebaseRecyclerAdapter);

    }

    public void selecionarItem(final TamPizza model) {
        if (FirebaseAuthApp.getUsuarioLogado() == null) {
            Intent intent = new Intent(getContext(), LoginActivity.class);
            startActivity(intent);
            ((Activity) getContext()).finish();
        } else {

            AlertDialog.Builder mBilder = new AlertDialog.Builder(getContext());
            View layoutDialog = getActivity().getLayoutInflater().inflate(R.layout.dialog_tipo_pizza, null);

            mBilder.setView(layoutDialog);
            final AlertDialog dialogEscolherTipoPizza = mBilder.create();
            dialogEscolherTipoPizza.show();

            Button btConfirmTipoPizza = (Button) layoutDialog.findViewById(R.id.bt_confirmar_tipo_pizza);
            Button btCancelTipoPizza = (Button) layoutDialog.findViewById(R.id.bt_cancel_tipo_pizza);

            btCancelTipoPizza.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogEscolherTipoPizza.dismiss();
                }
            });

            btConfirmTipoPizza.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), EscolherPizzaActivity.class);
                    intent.putExtra("TamPizza", getTamPizza(model.getNome()));
                    intent.putExtra("TipoPizza", tipodaPizza(tipoPizzaSelecionada));
                    startActivity(intent);
                    getActivity().finish();
                }
            });

            RadioButton radioInteira = layoutDialog.findViewById(R.id.radio_inteira);
            radioInteira.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    tipoPizzaSelecionada = 0;
                }
            });

            RadioButton radioMetadeMetade = layoutDialog.findViewById(R.id.radio_metade_metade);
            radioMetadeMetade.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    tipoPizzaSelecionada = 1;
                }
            });

            RadioButton radioTresSabores = layoutDialog.findViewById(R.id.radio_tres_sabores);
            radioTresSabores.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    tipoPizzaSelecionada = 2;
                }
            });

            RadioButton radioQuatroSabores = layoutDialog.findViewById(R.id.radio_quatro_sabores);
            radioQuatroSabores.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    tipoPizzaSelecionada = 3;
                }
            });

        }
    }

    private int getTamPizza(String modelNome) {
        if (modelNome.contains("Pequena")) {
            return 0;
        } else if (modelNome.contains("Média")) {
            return 1;
        } else {
            return 2;
        }
    }

    public String tipodaPizza(int tipoPizzaSelecionada) {
        String tipo = "";
        if (tipoPizzaSelecionada == 0) {
            tipo = "Inteira";
        } else if (tipoPizzaSelecionada == 1) {
            tipo = "Metade-Metade";
        } else if (tipoPizzaSelecionada == 2) {
            tipo = "Três Sabores";
        } else if (tipoPizzaSelecionada == 3) {
            tipo = "Quatro Sabores";
        }

        return tipo;
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

        public void setApartirDe(double valor_unit) {
            TextView apartir_de = (TextView) mView.findViewById(R.id.apartir_de_pizza);
            apartir_de.setText("A partir de " + StringUtil.formatToMoeda(valor_unit));
        }

        public void setImagem(final Context context, final String url) {
            final ImageView item_ref_image = (ImageView) mView.findViewById(R.id.img_tam_pizza);

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
