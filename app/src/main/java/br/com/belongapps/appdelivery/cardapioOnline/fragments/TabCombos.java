package br.com.belongapps.appdelivery.cardapioOnline.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
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
import android.widget.Toast;

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
import br.com.belongapps.appdelivery.cardapioOnline.activitys.DetalhesdoItemActivity;
import br.com.belongapps.appdelivery.cardapioOnline.model.ItemCardapio;
import br.com.belongapps.appdelivery.cardapioOnline.model.ItemPedido;
import br.com.belongapps.appdelivery.util.StringUtil;


public class TabCombos extends Fragment {

    private RecyclerView mCombosList;
    private DatabaseReference mDatabaseReference;
    private ProgressBar mProgressBar;
    private ItemPedido itemPedido;
    private boolean statusDelivery = true;
    private boolean statusEstabelecimento = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab_combos, container, false);

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

                Boolean status = Boolean.parseBoolean(dataSnapshot.child("status_delivery").child("status").getValue().toString());
                statusDelivery = status;

                Boolean statusEstab = Boolean.parseBoolean(dataSnapshot.child("status_estabelecimento").child("status").getValue().toString());
                statusEstabelecimento = statusEstab;

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        // verificarSeExisteCombos();

        itemPedido = new ItemPedido();

        mProgressBar = getActivity().findViewById(R.id.progressbar_escolher_combo);
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("itens_cardapio").child("11");

        mCombosList = getView().findViewById(R.id.list_combos);
        mCombosList.setHasFixedSize(true);
        mCombosList.setLayoutManager(new LinearLayoutManager(getActivity()));

        openProgressBar();

        final FirebaseRecyclerAdapter<ItemCardapio, ComboViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<ItemCardapio, ComboViewHolder>(
                ItemCardapio.class, R.layout.card_combos, ComboViewHolder.class, mDatabaseReference
        ) {

            @Override
            public void onBindViewHolder(final ComboViewHolder viewHolder, final int position) {
                super.onBindViewHolder(viewHolder, position);

                //YoYo.with(Techniques.BounceInUp).playOn(viewHolder.card_combo);
            }

            @Override
            protected void populateViewHolder(ComboViewHolder viewHolder, final ItemCardapio model, int position) {
                closeProgressBar();

                final String key = getRef(position).getKey();

                viewHolder.setNome(model.getNome());
                viewHolder.setValorUnitarioEPromocao(model.getValor_unit(), model.isStatus_promocao(), model.getPreco_promocional());
                viewHolder.setImagem(getContext(), model.getRef_img());
                viewHolder.setStatus(model.getStatus_item(), model.getPermite_entrega());

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (model.getStatus_item() == 1) { //Disponível no Cardápio
                            if (statusEstabelecimento == false) {
                                LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                                AlertDialog.Builder mBilder = new AlertDialog.Builder(getContext(), R.style.MyDialogTheme);
                                View layoutDialog = inflater.inflate(R.layout.dialog_estabelecimento_fechado, null);

                                Button btEntendi = layoutDialog.findViewById(R.id.bt_entendi_estabeleciemento_fechado);

                                mBilder.setView(layoutDialog);
                                final AlertDialog dialogEstabelecimentoFechado = mBilder.create();
                                dialogEstabelecimentoFechado.show();

                                btEntendi.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialogEstabelecimentoFechado.dismiss();
                                    }
                                });
                            } else if (model.getPermite_entrega() == 2){

                                LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                                AlertDialog.Builder mBilder = new AlertDialog.Builder(getContext(), R.style.MyDialogTheme);
                                View layoutDialog = inflater.inflate(R.layout.dialog_nao_permite_entrega, null);

                                Button btVoltar = layoutDialog.findViewById(R.id.bt_voltar_item_sem_entrega);
                                Button btContinuar = layoutDialog.findViewById(R.id.bt_continuar_item_sem_entrega);

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
                                        selecionarItem(model, key);
                                    }
                                });

                            } else if (statusDelivery == false) {
                                LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                                AlertDialog.Builder mBilder = new AlertDialog.Builder(getContext(), R.style.MyDialogTheme);
                                View layoutDialog = inflater.inflate(R.layout.dialog_delivery_fechado, null);

                                Button btVoltar = layoutDialog.findViewById(R.id.bt_voltar_delivery_fechado);
                                Button btContinuar = layoutDialog.findViewById(R.id.bt_continuar_delivery_fechado);

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
                                        selecionarItem(model, key);
                                    }
                                });

                            } else {
                                selecionarItem(model, key);
                            }
                        } else {
                            Toast.makeText(getContext(), "Produto Indisponível", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }

        };

        mCombosList.setAdapter(firebaseRecyclerAdapter);

    }

    /*private void verificarSeExisteCombos(){
        ValueEventListener verificarCombos = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()){ //Tem Combos
                    buscarCombos();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        mDatabaseReference.child("itens_cardapio").child("11");
    }*/


    public void selecionarItem(final ItemCardapio model, String key) {

        itemPedido.setNome("Combo " + model.getNome());
        itemPedido.setDescricao(model.getDescricao());
        itemPedido.setRef_img(model.getRef_img());
        itemPedido.setCategoria(model.getCategoria_id());
        itemPedido.setPermite_entrega(model.getPermite_entrega());
        itemPedido.setEntrega_gratis(model.getEntrega_gratis());

        if (model.isStatus_promocao() == true) {
            itemPedido.setValor_unit(model.getPreco_promocional());
        } else {
            itemPedido.setValor_unit(model.getValor_unit());
        }

        itemPedido.setKeyItem(key);

        Intent intent = new Intent(getActivity(), DetalhesdoItemActivity.class);

        intent.putExtra("ItemPedido", itemPedido);
        intent.putExtra("TelaAnterior", "TabVitaminas");
        startActivity(intent);

        getActivity().finish();
    }

    public static class ComboViewHolder extends RecyclerView.ViewHolder {

        View mView;
        CardView card_combo;

        public ComboViewHolder(final View itemView) {
            super(itemView);

            mView = itemView;
            card_combo = mView.findViewById(R.id.card_combos);

        }

        public void setNome(String nome) {

            TextView item_nome = mView.findViewById(R.id.item_nome_combo);
            item_nome.setText(nome);

        }

        public void setValorUnitarioEPromocao(double valor_unit, boolean status_promocao, double valor_promocional) {

            TextView item_valor_promo = mView.findViewById(R.id.item_valor_promo_combo);
            TextView item_valor_unit = mView.findViewById(R.id.item_valor_unit_combo);

            if (status_promocao == true) {
                item_valor_promo.setText(StringUtil.formatToMoeda(valor_promocional));
                item_valor_unit.setText(StringUtil.formatToMoeda(valor_unit));
                item_valor_unit.setPaintFlags(item_valor_unit.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                item_valor_unit.setVisibility(View.VISIBLE);
            } else {
                item_valor_promo.setText(StringUtil.formatToMoeda(valor_unit));
                item_valor_unit.setVisibility(View.INVISIBLE);
            }

        }

        public void setImagem(final Context context, final String url) {
            final ImageView item_ref_image = mView.findViewById(R.id.img_combos);
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

        public void setStatus(int status, int permiteEntrega) {
            TextView item_status = mView.findViewById(R.id.status_combo);

            //Não permite entrega
            if (permiteEntrega == 2){
                item_status.setText("Não Entregamos");
            }

            if (status == 0 || permiteEntrega == 2) { //Se Indisponível ou nao faz entrega
                item_status.setVisibility(View.VISIBLE);
                if (status == 0){
                    item_status.setText("Produto Indisponível");
                }
            } else{
                item_status.setVisibility(View.INVISIBLE);
            }

        }

    }

    private void openProgressBar() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    private void closeProgressBar() {
        mProgressBar.setVisibility(View.GONE);
    }

}
