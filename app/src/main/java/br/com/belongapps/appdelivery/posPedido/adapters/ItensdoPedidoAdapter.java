package br.com.belongapps.appdelivery.posPedido.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import br.com.belongapps.appdelivery.R;
import br.com.belongapps.appdelivery.cardapioOnline.model.ItemPedido;
import br.com.belongapps.appdelivery.posPedido.activities.AcompanharPedidoActivity;


public class ItensdoPedidoAdapter extends RecyclerView.Adapter<ItensdoPedidoAdapter.ViewHolder> {

    private static List<ItemPedido> itemPedidos;
    private Context context;

    public ItensdoPedidoAdapter(List<ItemPedido> itemPedidos, Context context) {
        this.itemPedidos = itemPedidos;
        this.context = context;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_itens_do_pedido, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {

        final ItemPedido item = itemPedidos.get(position);

        viewHolder.setNomeItem(item.getNome());
        viewHolder.setImagem(context, item.getRef_img());
        viewHolder.setQtdItem(item.getQuantidade());
        viewHolder.setValorItem(item.getValor_unit());

    }

    @Override
    public int getItemCount() {
        return itemPedidos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        View mView;
        CardView card_item_do_pedido;

        public ViewHolder(final View itemView) {
            super(itemView);

            mView = itemView;
            card_item_do_pedido = (CardView) mView.findViewById(R.id.card_itens_do_pedido);
        }

        public void setNomeItem(String nome) {

            TextView nomePedido = (TextView) mView.findViewById(R.id.item_nome);
            nomePedido.setText(nome);

        }

        public void setQtdItem(int qtdItem){
            TextView qtd = (TextView) mView.findViewById(R.id.item_qtd);
            qtd.setText(qtdItem + " x");
        }

        public void setValorItem(double valor_unit) {

            TextView item_valor_unit = (TextView) mView.findViewById(R.id.item_valor);
            item_valor_unit.setText(" R$ " +  String.format(Locale.US, "%.2f", valor_unit).replace(".", ","));

        }

        public void setImagem(final Context context, final String url) {
            final ImageView item_ref_image = (ImageView) mView.findViewById(R.id.item_img);

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

}