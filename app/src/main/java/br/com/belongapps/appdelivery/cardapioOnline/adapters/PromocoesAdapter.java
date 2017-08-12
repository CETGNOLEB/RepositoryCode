package br.com.belongapps.appdelivery.cardapioOnline.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Locale;

import br.com.belongapps.appdelivery.R;
import br.com.belongapps.appdelivery.cardapioOnline.activitys.DetalhesdoItemActivity;
import br.com.belongapps.appdelivery.cardapioOnline.activitys.EscolherRecheioActivity;
import br.com.belongapps.appdelivery.cardapioOnline.model.ItemCardapio;
import br.com.belongapps.appdelivery.cardapioOnline.model.ItemPedido;


public class PromocoesAdapter extends RecyclerView.Adapter<PromocoesAdapter.ViewHolder> {

    private static List<ItemCardapio> itensemPromocao;
    private Context context;
    private ProgressBar mProgressBar;
    private boolean statusDelivery;
    private boolean statusEstabelecimento;

    public PromocoesAdapter(List<ItemCardapio> itensPedido, Context context, ProgressBar progressBar, boolean statusDelivery, boolean statusEstabelecimento) {
        this.itensemPromocao = itensPedido;
        this.context = context;
        this.mProgressBar = progressBar;
        this.statusDelivery = statusDelivery;
        this.statusEstabelecimento = statusEstabelecimento;
        openProgressBar();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_item_promo, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        closeProgressBar();
        final ItemCardapio item = itensemPromocao.get(position);

        viewHolder.setNome(item.getNome());
        viewHolder.setDescricao(item.getDescricao());
        viewHolder.setValorUnitario(item.getValor_unit());
        viewHolder.setValorPromo(item.getPreco_promocional());
        viewHolder.setImagem(context, item.getRef_img());

        viewHolder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (statusEstabelecimento == false) {

                    LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                    AlertDialog.Builder mBilder = new AlertDialog.Builder(context, R.style.MyDialogTheme);
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


                } else if (statusDelivery == false){

                    LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                    AlertDialog.Builder mBilder = new AlertDialog.Builder(context, R.style.MyDialogTheme);
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
                        public void onClick(View view) {
                            dialogDeliveryFechado.dismiss();
                            selecionarItem(item);
                        }
                    });

                } else {
                    selecionarItem(item);
                }
            }
        });


    }

    public void selecionarItem(ItemCardapio item){

        ItemPedido itemPedido = new ItemPedido();

        itemPedido.setNome(item.getNome());
        itemPedido.setRef_img(item.getRef_img());
        itemPedido.setDescricao(item.getDescricao());
        itemPedido.setCategoria(item.getCategoria_id());

        if (item.isStatus_promocao() == true) {
            itemPedido.setValor_unit(item.getPreco_promocional());
        } else {
            itemPedido.setValor_unit(item.getValor_unit());
        }


        if (item.getCategoria_id().equals("1")) {
            Intent intent = new Intent(context, EscolherRecheioActivity.class);
            intent.putExtra("ItemPedido", itemPedido);
            intent.putExtra("qtd_recheios_item_cardapio", item.getQtd_recheios());
            context.startActivity(intent);
        } else {

            Intent intent = new Intent(context, DetalhesdoItemActivity.class);

            intent.putExtra("ItemPedido", itemPedido);
            intent.putExtra("TelaAnterior", "TabPromocoes");
            context.startActivity(intent);

        }
    }


    @Override
    public int getItemCount() {
        return itensemPromocao.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        View mView;
        CardView card_item_promo;

        public ViewHolder(final View itemView) {
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