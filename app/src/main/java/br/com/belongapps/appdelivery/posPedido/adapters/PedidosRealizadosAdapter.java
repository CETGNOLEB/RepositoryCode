package br.com.belongapps.appdelivery.posPedido.adapters;

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
import java.util.Locale;

import br.com.belongapps.appdelivery.R;
import br.com.belongapps.appdelivery.cardapioOnline.activitys.DetalhesdoItemActivity;
import br.com.belongapps.appdelivery.cardapioOnline.activitys.EscolherRecheioActivity;
import br.com.belongapps.appdelivery.cardapioOnline.model.ItemPedido;
import br.com.belongapps.appdelivery.cardapioOnline.model.Pedido;


public class PedidosRealizadosAdapter extends RecyclerView.Adapter<PedidosRealizadosAdapter.ViewHolder> {

    private static List<Pedido> pedidosRealizados;
    private Context context;
    private ProgressBar mProgressBar;

    public PedidosRealizadosAdapter(List<Pedido> pedidosRealizados, Context context, ProgressBar progressBar) {
        this.pedidosRealizados = pedidosRealizados;
        this.context = context;
        this.mProgressBar = progressBar;

        openProgressBar();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_pedido_realizado, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        closeProgressBar();

        final Pedido pedido = pedidosRealizados.get(position);

        viewHolder.setNumerodoPedido(pedido.getNumero_pedido());
        viewHolder.setDatadoPedido(getData(pedido.getData()));
        viewHolder.setHora(getHora(pedido.getData()));
        viewHolder.setTipoEntrega(pedido.getEntrega_retirada());
        viewHolder.setValorPedido(pedido.getValor_total());
        viewHolder.setImagemStatus(pedido.getStatus());
        viewHolder.setStatus(pedido.getStatus());

        viewHolder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    private String getData(String data) {
        String retorno = data.substring(0,10);
        return retorno;
    }

    private String getHora(String data) {
        String retorno = data.trim().substring(10,data.length());
        return retorno;
    }

    @Override
    public int getItemCount() {
        return pedidosRealizados.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        View mView;
        CardView card_pedido_realizado;

        public ViewHolder(final View itemView) {
            super(itemView);

            mView = itemView;
            card_pedido_realizado = (CardView) mView.findViewById(R.id.card_pedido_realizado);

        }

        public void setNumerodoPedido(String numero) {

            TextView numeroPedido = (TextView) mView.findViewById(R.id.numero_pedido_realizado);
            numeroPedido.setText("Pedido Nº " + numero);

        }

        public void setDatadoPedido(String data) {

            TextView dataPedido = (TextView) mView.findViewById(R.id.data_pedido_realizado);
            dataPedido.setText("Data: " + data);
        }

        public void setHora(String hora) {
            TextView horaPedido = (TextView) mView.findViewById(R.id.hora_pedido_realizado);
            horaPedido.setText("Hora: " + hora);
        }

        public void setTipoEntrega(int tipoEntrega) {
            TextView tipoEntregaPedido = (TextView) mView.findViewById(R.id.entrega_pedido_realizado);
            if (tipoEntrega == 0){
                tipoEntregaPedido.setText("Entrega em Domicílio");
            } else if(tipoEntrega == 1){
                tipoEntregaPedido.setText("Retirar no Estabelecimento");
            } else{
                tipoEntregaPedido.setText("Consumir no Estabelecimento");
            }
        }

        public void setValorPedido(double valor){
            TextView valorPedido = (TextView) mView.findViewById(R.id.valor_pedido_realizado);
            valorPedido.setText("Valor Total: R$ " +  String.format(Locale.US, "%.2f", valor).replace(".", ","));
        }

        public void setStatus(int status) {
            TextView statusPedido = (TextView) mView.findViewById(R.id.status_pedido_realizado);

            if (status == 0){
                statusPedido.setText("ENVIADO");
            } else if(status == 1){
                statusPedido.setText("EM PRODUÇÃO");
            } else if (status == 2){
                statusPedido.setText("SAIU P/ ENTREGA");
            } else{
                statusPedido.setText("CANCELADO");
            }
        }

       public void setImagemStatus(int status) {
            ImageView imgPedido = (ImageView) mView.findViewById(R.id.img_pedido_realizado);

            if (status == 0){
                imgPedido.setImageResource(R.drawable.img_pedido_enviado);
            } else if (status == 1){
                imgPedido.setImageResource(R.drawable.img_pedido_em_producao);
            } else if (status == 2){
                imgPedido.setImageResource(R.drawable.img_pedido_saiu_entrega);
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