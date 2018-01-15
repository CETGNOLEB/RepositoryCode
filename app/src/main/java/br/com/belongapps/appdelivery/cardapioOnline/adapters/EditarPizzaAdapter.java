package br.com.belongapps.appdelivery.cardapioOnline.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Locale;

import br.com.belongapps.appdelivery.R;
import br.com.belongapps.appdelivery.cardapioOnline.activitys.DetalhesdoItemPizzaMMActivity;
import br.com.belongapps.appdelivery.cardapioOnline.model.ItemCardapio;
import br.com.belongapps.appdelivery.cardapioOnline.model.ItemPedido;


public class EditarPizzaAdapter extends RecyclerView.Adapter<EditarPizzaAdapter.ViewHolder> {

    private static List<ItemCardapio> pizzas;
    private Context context;
    private ProgressBar mProgressBar;
    private ItemPedido itemPedido;
    private int tamPizza;
    private String tipoPizza;
    private String metade;

    public EditarPizzaAdapter(List<ItemCardapio> pizzas, ItemPedido itemPedido, Context context, ProgressBar progressBar, int tamPizza, String tipoPizza, String metade) {
        this.pizzas = pizzas;
        this.context = context;
        this.mProgressBar = progressBar;
        this.tamPizza = tamPizza;
        this.tipoPizza = tipoPizza;
        this.itemPedido = itemPedido;
        this.metade = metade;
        openProgressBar();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_sabor_pizzas, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        closeProgressBar();
        final ItemCardapio item = pizzas.get(position);

        viewHolder.setNome(item.getNome());
        viewHolder.setDescricao(item.getDescricao());
        viewHolder.setStatus(item.getStatus_item(), item.getPermite_entrega());

        if (tamPizza == 0) { //Pizza Pequena
            viewHolder.setValorUnitarioEPromocao(item.getValor_pizza_p(), item.isStatus_promocao(), item.getPromo_pizza_p());
        } else if (tamPizza == 1) { //Pizza Média
            viewHolder.setValorUnitarioEPromocao(item.getValor_pizza_m(), item.isStatus_promocao(), item.getPromo_pizza_m());
        } else { //Pizza Grande
            viewHolder.setValorUnitarioEPromocao(item.getValor_pizza_g(), item.isStatus_promocao(), item.getPromo_pizza_g());
        }

        viewHolder.setImagem(context, item.getRef_img());

        viewHolder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (item.getStatus_item() == 1) { //Se Disponível no Cardápio

                    switch (metade) {

                        case "01":
                            itemPedido.setNome(item.getNome());
                            itemPedido.setDescricao(item.getDescricao());
                            itemPedido.setRef_img(item.getRef_img());
                            itemPedido.setValor_unit(getValorUnitario(item));
                            break;
                        case "02":
                            itemPedido.setNomeMetade2(item.getNome());
                            itemPedido.setDescMetade2(item.getDescricao());
                            itemPedido.setImgMetade2(item.getRef_img());
                            itemPedido.setValorMetade2(getValorUnitario(item));
                            break;
                        case "03":
                            itemPedido.setNomeMetade3(item.getNome());
                            itemPedido.setDescMetade3(item.getDescricao());
                            itemPedido.setImgMetade3(item.getRef_img());
                            itemPedido.setValorMetade3(getValorUnitario(item));
                            break;
                        case "04":
                            itemPedido.setNomeMetade4(item.getNome());
                            itemPedido.setDescMetade4(item.getDescricao());
                            itemPedido.setImgMetade4(item.getRef_img());
                            itemPedido.setValorMetade4(getValorUnitario(item));
                            break;
                    }

                    Intent intent = new Intent(context, DetalhesdoItemPizzaMMActivity.class);
                    intent.putExtra("ItemPedido", itemPedido);
                    intent.putExtra("TamPizza", tamPizza);
                    intent.putExtra("TipoPizza", tipoPizza);
                    context.startActivity(intent);
                    ((Activity) context).finish();
                } else {
                    Toast.makeText(context, "Produto Indisponível", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    public double getValorUnitario(ItemCardapio item) {
        double retorno;

        if (tamPizza == 0) { //Pizza Pequena
            retorno = getValorPorTipo(item.getValor_pizza_p());
        } else if (tamPizza == 1) { //Pizza Média
            retorno = getValorPorTipo(item.getValor_pizza_m());
        } else { //Pizza Grande
            retorno = getValorPorTipo(item.getValor_pizza_g());
        }

        return retorno;
    }

    public double getValorPorTipo(double valorUnit) {

        if (tipoPizza.equals("Metade-Metade")) {
            return valorUnit;
        } else if (tipoPizza.equals("Três Sabores")) {
            return valorUnit;
        } else {
            return valorUnit;
        }

    }

    @Override
    public int getItemCount() {
        return pizzas.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        View mView;
        CardView card_sabor_pizza;

        public ViewHolder(final View itemView) {
            super(itemView);

            mView = itemView;
            card_sabor_pizza = mView.findViewById(R.id.card_sabor_pizzas);

        }

        public void setNome(String nome) {

            TextView item_nome = mView.findViewById(R.id.nome_sabor_pizza);
            item_nome.setText(nome);

        }

        public void setDescricao(String descricao) {

            TextView item_descricao = mView.findViewById(R.id.desc_sabor_pizza);
            item_descricao.setText(descricao);
        }

        public void setValorUnitarioEPromocao(double valor_unit, boolean status_promocao, double valor_promocional) {

            TextView item_valor_promo = mView.findViewById(R.id.promo_valor_unit_sabor_pizza);
            TextView item_valor_unit = mView.findViewById(R.id.valor_unit_sabor_pizza);

            if (status_promocao == true) {
                item_valor_promo.setText(" R$ " + String.format(Locale.US, "%.2f", valor_promocional).replace(".", ","));
                item_valor_unit.setText(" R$ " + String.format(Locale.US, "%.2f", valor_unit).replace(".", ","));
                item_valor_unit.setPaintFlags(item_valor_unit.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                item_valor_unit.setVisibility(View.VISIBLE);
                Log.println(Log.ERROR, "teste", "O CARAI ENTROU AQUI");
            } else {
                item_valor_promo.setText(" R$ " + String.format(Locale.US, "%.2f", valor_unit).replace(".", ","));
            }

        }

        public void setImagem(final Context context, final String url) {
            final ImageView item_ref_image = mView.findViewById(R.id.img_sabor_pizza);

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
            TextView item_status = mView.findViewById(R.id.status_sabor_pizza);

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