package br.com.belongapps.appdelivery.cardapioOnline.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

import br.com.belongapps.appdelivery.R;
import br.com.belongapps.appdelivery.cardapioOnline.activitys.DetalhesdoItemActivity;
import br.com.belongapps.appdelivery.cardapioOnline.activitys.EscolherItemComboActivity;
import br.com.belongapps.appdelivery.cardapioOnline.activitys.MontagemAcaiActivity;
import br.com.belongapps.appdelivery.cardapioOnline.model.ItemCardapio;
import br.com.belongapps.appdelivery.cardapioOnline.model.ItemPedido;
import br.com.belongapps.appdelivery.util.StringUtil;


public class ItemComboAdapter extends RecyclerView.Adapter<ItemComboAdapter.ViewHolder> {

    private static List<ItemCardapio> itens;
    private Context context;
    private ProgressBar mProgressBar;

    private ItemPedido itemPedido;
    private String itemSelecionado;

    public ItemComboAdapter(List<ItemCardapio> itens, Context context, ProgressBar progressBar, String itemSelcionado, ItemPedido itemPedido) {
        this.itens = itens;
        this.context = context;
        this.mProgressBar = progressBar;

        this.itemPedido = itemPedido;
        this.itemSelecionado = itemSelcionado;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_item_combo, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        closeProgressBar();

        final ItemCardapio item = itens.get(position);

        viewHolder.setNome(item.getNome());
        viewHolder.setDescricao(item.getDescricao());
        /*viewHolder.setValorUnitario(item.getValor_unit());
        viewHolder.setValorPromo(item.getPreco_promocional());*/
        viewHolder.setImagem(context, item.getRef_img());
        viewHolder.setStatusEntrega(item.getPermite_entrega());

        viewHolder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemSelecionado = item.getNome();

                Intent i = new Intent(context, DetalhesdoItemActivity.class);
                i.putExtra("ItemPedido", itemPedido);
                i.putExtra("ItemSelecionado", itemSelecionado);
                i.putExtra("Combo", "Combo");
                context.startActivity(i);
                ((Activity) context).finish();
            }
        });

    }


    @Override
    public int getItemCount() {
        return itens.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        View mView;
        CardView card_item_combo;

        public ViewHolder(final View itemView) {
            super(itemView);

            mView = itemView;
            card_item_combo = mView.findViewById(R.id.card_item_combo);

        }

        public void setNome(String nome) {

            TextView item_nome = mView.findViewById(R.id.item_nome_item_combo);
            item_nome.setText(nome);

        }

        public void setDescricao(String descricao) {

            TextView item_descricao = mView.findViewById(R.id.item_desc_item_combo);
            item_descricao.setText(descricao);
        }

        /*public void setValorUnitario(double valor_unit) {
            TextView item_valor_unit = mView.findViewById(R.id.item_valor_unit_item_combo);
            item_valor_unit.setText(StringUtil.formatToMoeda(valor_unit));
            item_valor_unit.setPaintFlags(item_valor_unit.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }

        public void setValorPromo(double valor_unit) {
            TextView item_valor_unit = mView.findViewById(R.id.item_promo_valor_unit_item_combo);
            item_valor_unit.setText(StringUtil.formatToMoeda(valor_unit));
        }*/

        public void setImagem(final Context context, final String url) {
            final ImageView item_ref_image = mView.findViewById(R.id.img_item_combo);

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

        public void setStatusEntrega(int permiteEntrega) {
            TextView item_status =  mView.findViewById(R.id.status_entrega_combo);

            if (permiteEntrega == 2) { //Se Indisponível ou nao faz entrega
                item_status.setVisibility(View.VISIBLE);
            } else{
                item_status.setVisibility(View.INVISIBLE);
            }


        }

    }

    private void closeProgressBar() {
        mProgressBar.setVisibility(View.GONE);
    }

}