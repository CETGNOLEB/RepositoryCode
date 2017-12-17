package br.com.belongapps.appdelivery.posPedido.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import br.com.belongapps.appdelivery.R;
import br.com.belongapps.appdelivery.cardapioOnline.model.ItemPedido;
import br.com.belongapps.appdelivery.cardapioOnline.model.Pedido;
import br.com.belongapps.appdelivery.posPedido.PedidoKey;
import br.com.belongapps.appdelivery.posPedido.activities.AcompanharPedidoActivity;


public class PedidosRealizadosAdapter extends RecyclerView.Adapter<PedidosRealizadosAdapter.ViewHolder> {

    private static List<PedidoKey> pedidosRealizados;
    private Context context;
    private ProgressBar mProgressBar;

    public PedidosRealizadosAdapter(List<PedidoKey> pedidosRealizados, Context context, ProgressBar progressBar) {
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

        final Pedido pedido = pedidosRealizados.get(position).getPedido();
        final String key = pedidosRealizados.get(position).getKey();

        viewHolder.setNumerodoPedido(pedido.getNumero_pedido());
        viewHolder.setDatadoPedido(getData(pedido.getData()));
        viewHolder.setHora(getHora(pedido.getData()));
        viewHolder.setTipoEntrega(pedido.getEntrega_retirada());
        viewHolder.setValorPedido(pedido.getValor_total());
        viewHolder.setImagemStatus(pedido.getStatus(), pedido.getEntrega_retirada());
        viewHolder.setStatus(pedido.getStatus(), pedido.getEntrega_retirada());

        viewHolder.detalhesPedidoRealizado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AcompanharPedidoActivity.class);
                intent.putExtra("NumeroPedido", pedido.getNumero_pedido());
                intent.putExtra("DataPedido", getData(pedido.getData()));
                intent.putExtra("HoraPedido", getHora(pedido.getData()));
                intent.putExtra("ValorPedido", pedido.getValor_total());
                intent.putExtra("StatusPedido", pedido.getStatus());
                intent.putExtra("TipoEntrega", pedido.getEntrega_retirada());
                intent.putExtra("StatusTempo", pedido.getStatus_tempo());
                intent.putExtra("KeyPedido", key);

                ArrayList<ItemPedido> itensdoPedido = new ArrayList<>();
                for (ItemPedido pedidoaux: pedido.getItens_pedido() ) {
                    itensdoPedido.add(pedidoaux);
                }

                intent.putParcelableArrayListExtra("ItensPedido", itensdoPedido);
                context.startActivity(intent);
            }
        });

    }

    private String getData(String data) {
        String retorno = data.substring(0, 10);
        return retorno;
    }

    private String getHora(String data) {
        String retorno = data.trim().substring(10, data.length());
        return retorno;
    }

    @Override
    public int getItemCount() {
        return pedidosRealizados.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        View mView;
        CardView card_pedido_realizado;
        TextView detalhesPedidoRealizado;

        public ViewHolder(final View itemView) {
            super(itemView);

            mView = itemView;
            card_pedido_realizado = (CardView) mView.findViewById(R.id.card_pedido_realizado);
            detalhesPedidoRealizado = (TextView) mView.findViewById(R.id.detalhes_pedido_realizado);
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
            if (tipoEntrega == 0) {
                tipoEntregaPedido.setText("Entrega em Domicílio");
            } else if (tipoEntrega == 1) {
                tipoEntregaPedido.setText("Retirar no Estabelecimento");
            } else {
                tipoEntregaPedido.setText("Consumir no Estabelecimento");
            }
        }

        public void setValorPedido(double valor) {
            TextView valorPedido = (TextView) mView.findViewById(R.id.valor_pedido_realizado);
            valorPedido.setText("Valor Total: R$ " + String.format(Locale.US, "%.2f", valor).replace(".", ","));
        }

        public void setStatus(int status, int tipoEntrega) {
            TextView statusPedido = (TextView) mView.findViewById(R.id.status_pedido_realizado);

            if (status == 0) {
                statusPedido.setText("ENVIADO");
            } else if (status == 1) {
                statusPedido.setText("EM PRODUÇÃO");

                /*NotificationCompat.Builder mBuilder =
                        new NotificationCompat.Builder(context)
                                .setContentTitle("Em Produção")
                                .setContentText("Hello World!");

                NotificationManager mNotificationManager =
                        (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
// mId allows you to update the notification later on.
                mNotificationManager.notify(1, mBuilder.build());*/


            } else if (status == 2) {
                if (tipoEntrega == 1) {
                    statusPedido.setText("PRONTO P/ RETIRADA");
                }

                if (tipoEntrega == 2) {
                    statusPedido.setText("PRONTO P/ CONSUMO");
                }
            } else if (status == 3) {
                statusPedido.setText("SAIU DA COZINHA");
            } else if (status == 4) {
                statusPedido.setText("SAIU P/ ENTREGA");
            } else {
                statusPedido.setText("ENTREGUE");
            }

        }

        public void setImagemStatus(int status, int tipoEntrega) {
            ImageView imgPedido = (ImageView) mView.findViewById(R.id.img_pedido_realizado);

            if (status == 0) {
                imgPedido.setImageResource(R.drawable.img_pedido_enviado);
            } else if (status == 1) {
                imgPedido.setImageResource(R.drawable.img_pedido_em_producao);
            } else if (status == 2) {
                if (tipoEntrega == 1) {
                    imgPedido.setImageResource(R.drawable.img_pedido_em_producao);
                }

                if (tipoEntrega == 2) {
                    imgPedido.setImageResource(R.drawable.img_pedido_pronto);
                }
            } else if (status == 3) {
                imgPedido.setImageResource(R.drawable.img_pedido_pronto);
            } else if (status == 4) {
                imgPedido.setImageResource(R.drawable.img_pedido_saiu_entrega);
            } else {
                imgPedido.setImageResource(R.drawable.ic_check_all);
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