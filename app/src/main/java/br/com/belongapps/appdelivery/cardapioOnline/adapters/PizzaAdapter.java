package br.com.belongapps.appdelivery.cardapioOnline.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Printer;
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
import br.com.belongapps.appdelivery.cardapioOnline.activitys.DetalhesdoItemActivity;
import br.com.belongapps.appdelivery.cardapioOnline.activitys.DetalhesdoItemPizzaMMActivity;
import br.com.belongapps.appdelivery.cardapioOnline.activitys.EscolherPizzaActivity2;
import br.com.belongapps.appdelivery.cardapioOnline.activitys.EscolherPizzaActivity3;
import br.com.belongapps.appdelivery.cardapioOnline.activitys.EscolherPizzaActivity4;
import br.com.belongapps.appdelivery.cardapioOnline.model.ItemCardapio;
import br.com.belongapps.appdelivery.cardapioOnline.model.ItemPedido;
import br.com.belongapps.appdelivery.util.Print;
import br.com.belongapps.appdelivery.util.StringUtil;


public class PizzaAdapter extends RecyclerView.Adapter<PizzaAdapter.ViewHolder> {

    private static List<ItemCardapio> pizzas;
    private Context context;
    private ProgressBar mProgressBar;
    private ItemPedido itemPedido;
    private int tamPizza;
    private String tipoPizza;
    private int countMetadesSelecionadas;

    public PizzaAdapter(List<ItemCardapio> pizzas, ItemPedido itemPedido ,Context context, ProgressBar progressBar, int tamPizza, String tipoPizza, int countMetadesSelecionadas) {
        this.pizzas = pizzas;
        this.context = context;
        this.mProgressBar = progressBar;
        this.tamPizza = tamPizza;
        this.tipoPizza = tipoPizza;
        this.countMetadesSelecionadas = countMetadesSelecionadas;

        this.itemPedido = itemPedido;
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

        Double valoUniTario;

        viewHolder.setNome(item.getNome());
        viewHolder.setDescricao(getDescMini(item.getDescricao()));
        viewHolder.setStatus(item.getStatus_item());

        Print.logError("TAM PIZZA: " + tamPizza);

        if (tamPizza == 0) { //Pizza Pequena
            valoUniTario =  viewHolder.setValorUnitarioEPromocao(item.getValor_pizza_p(), item.isStatus_promocao(), item.getPromo_pizza_p());
        } else if (tamPizza == 1) { //Pizza Média
            valoUniTario = viewHolder.setValorUnitarioEPromocao(item.getValor_pizza_m(), item.isStatus_promocao(), item.getPromo_pizza_m());
        } else { //Pizza Grande
            valoUniTario = viewHolder.setValorUnitarioEPromocao(item.getValor_pizza_g(), item.isStatus_promocao(), item.getPromo_pizza_g());
        }

        viewHolder.setImagem(context, item.getRef_img());

        if (item.getStatus_item() == 1) {

            final Double finalValoUniTario = valoUniTario;
            viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (tipoPizza.equals("Inteira")) {

                        itemPedido.setNome("Pizza " + item.getNome());
                        itemPedido.setDescricao(item.getDescricao());
                        itemPedido.setRef_img(item.getRef_img());
                        itemPedido.setValor_unit(finalValoUniTario);

                        Intent intent = new Intent(context, DetalhesdoItemActivity.class);
                        intent.putExtra("ItemPedido", itemPedido);
                        intent.putExtra("TelaAnterior", "TabPizza");
                        context.startActivity(intent);
                        ((Activity) context).finish();

                    } else if (tipoPizza.equals("Metade-Metade")) {

                        if (countMetadesSelecionadas == 2){

                            itemPedido.setNomeMetade2(item.getNome());
                            itemPedido.setDescMetade2(item.getDescricao());
                            itemPedido.setImgMetade2(item.getRef_img());
                            itemPedido.setValorMetade2(finalValoUniTario / 2);

                            Intent intent = new Intent(context, DetalhesdoItemPizzaMMActivity.class);
                            intent.putExtra("TamPizza", tamPizza);
                            intent.putExtra("TipoPizza", tipoPizza);
                            intent.putExtra("ItemPedido", itemPedido);
                            context.startActivity(intent);
                            ((Activity) context).finish();

                            Toast.makeText(context, "Pizza " + item.getNome() + " selecionada!", Toast.LENGTH_SHORT).show();

                        } else {

                            itemPedido.setNome(item.getNome());
                            itemPedido.setDescricao(item.getDescricao());
                            itemPedido.setRef_img(item.getRef_img());
                            itemPedido.setValor_unit(finalValoUniTario / 2);

                            Intent intent = new Intent(context, EscolherPizzaActivity2.class);
                            intent.putExtra("TamPizza", tamPizza);
                            intent.putExtra("TipoPizza", tipoPizza);
                            intent.putExtra("ItemPedido", itemPedido);
                            context.startActivity(intent);
                            ((Activity) context).finish();

                            Toast.makeText(context, "Pizza " + item.getNome() + " selecionada!", Toast.LENGTH_SHORT).show();

                        }

                    } else if (tipoPizza.equals("Três Sabores")) {

                        if (countMetadesSelecionadas == 3){

                            itemPedido.setNomeMetade3(item.getNome());
                            itemPedido.setDescMetade3(item.getDescricao());
                            itemPedido.setImgMetade3(item.getRef_img());
                            itemPedido.setValorMetade3(finalValoUniTario / 3);

                            Intent intent = new Intent(context, DetalhesdoItemPizzaMMActivity.class);
                            intent.putExtra("TamPizza", tamPizza);
                            intent.putExtra("TipoPizza", tipoPizza);
                            intent.putExtra("ItemPedido", itemPedido);
                            context.startActivity(intent);
                            ((Activity) context).finish();

                            Toast.makeText(context, "Pizza " + item.getNome() + " selecionada!", Toast.LENGTH_SHORT).show();

                        } else if (countMetadesSelecionadas == 1){

                            itemPedido.setNome(item.getNome());
                            itemPedido.setDescricao(item.getDescricao());
                            itemPedido.setRef_img(item.getRef_img());
                            itemPedido.setValor_unit(finalValoUniTario / 3);

                            Intent intent = new Intent(context, EscolherPizzaActivity2.class);
                            intent.putExtra("TamPizza", tamPizza);
                            intent.putExtra("TipoPizza", tipoPizza);
                            intent.putExtra("ItemPedido", itemPedido);
                            context.startActivity(intent);
                            ((Activity) context).finish();

                            Toast.makeText(context, "Pizza " + item.getNome() + " selecionada!", Toast.LENGTH_SHORT).show();

                        } else{

                            itemPedido.setNomeMetade2(item.getNome());
                            itemPedido.setDescMetade2(item.getDescricao());
                            itemPedido.setImgMetade2(item.getRef_img());
                            itemPedido.setValorMetade2(finalValoUniTario / 3);

                            Intent intent = new Intent(context, EscolherPizzaActivity3.class);
                            intent.putExtra("TamPizza", tamPizza);
                            intent.putExtra("TipoPizza", tipoPizza);
                            intent.putExtra("ItemPedido", itemPedido);
                            context.startActivity(intent);
                            ((Activity) context).finish();

                            Toast.makeText(context, "Pizza " + item.getNome() + " selecionada!", Toast.LENGTH_SHORT).show();

                        }
                    } else if (tipoPizza.equals("Quatro Sabores")) {

                        if (countMetadesSelecionadas == 4){

                            itemPedido.setNomeMetade4(item.getNome());
                            itemPedido.setDescMetade4(item.getDescricao());
                            itemPedido.setImgMetade4(item.getRef_img());
                            itemPedido.setValorMetade4(finalValoUniTario / 4);

                            Intent intent = new Intent(context, DetalhesdoItemPizzaMMActivity.class);
                            intent.putExtra("TamPizza", tamPizza);
                            intent.putExtra("TipoPizza", tipoPizza);
                            intent.putExtra("ItemPedido", itemPedido);
                            context.startActivity(intent);
                            ((Activity) context).finish();

                            Toast.makeText(context, "Pizza " + item.getNome() + " selecionada!", Toast.LENGTH_SHORT).show();

                        } else if (countMetadesSelecionadas == 1){

                            itemPedido.setNome(item.getNome());
                            itemPedido.setDescricao(item.getDescricao());
                            itemPedido.setRef_img(item.getRef_img());
                            itemPedido.setValor_unit(finalValoUniTario / 4);

                            Intent intent = new Intent(context, EscolherPizzaActivity2.class);
                            intent.putExtra("TamPizza", tamPizza);
                            intent.putExtra("TipoPizza", tipoPizza);
                            intent.putExtra("ItemPedido", itemPedido);
                            context.startActivity(intent);
                            ((Activity) context).finish();

                            Toast.makeText(context, "Pizza " + item.getNome() + " selecionada!", Toast.LENGTH_SHORT).show();

                        } else if (countMetadesSelecionadas == 2){

                            itemPedido.setNomeMetade2(item.getNome());
                            itemPedido.setDescMetade2(item.getDescricao());
                            itemPedido.setImgMetade2(item.getRef_img());
                            itemPedido.setValorMetade2(finalValoUniTario / 4);

                            Intent intent = new Intent(context, EscolherPizzaActivity3.class);
                            intent.putExtra("TamPizza", tamPizza);
                            intent.putExtra("TipoPizza", tipoPizza);
                            intent.putExtra("ItemPedido", itemPedido);
                            context.startActivity(intent);
                            ((Activity) context).finish();

                            Toast.makeText(context, "Pizza " + item.getNome() + " selecionada!", Toast.LENGTH_SHORT).show();

                        } else {

                            itemPedido.setNomeMetade3(item.getNome());
                            itemPedido.setDescMetade3(item.getDescricao());
                            itemPedido.setImgMetade3(item.getRef_img());
                            itemPedido.setValorMetade3(finalValoUniTario / 4);

                            Intent intent = new Intent(context, EscolherPizzaActivity4.class);
                            intent.putExtra("TamPizza", tamPizza);
                            intent.putExtra("TipoPizza", tipoPizza);
                            intent.putExtra("ItemPedido", itemPedido);
                            context.startActivity(intent);
                            ((Activity) context).finish();

                            Toast.makeText(context, "Pizza " + item.getNome() + " selecionada!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
        }

    }

    private String getDescMini(String descricao) {
        String desc = descricao;

        if (descricao != null && descricao.length() > 50){
            return desc.substring(0, 30) + " ...";
        }

        return desc;
    }

    public double getValorUnitario(ItemCardapio item) {
        double retorno;

        if (tamPizza == 0) { //Pizza Pequena
            retorno = item.getValor_pizza_p();
        } else if (tamPizza == 1) { //Pizza Média
            retorno = item.getValor_pizza_m();
        } else { //Pizza Grande
            retorno = item.getValor_pizza_g();
        }

        return retorno;
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
            card_sabor_pizza = (CardView) mView.findViewById(R.id.card_sabor_pizzas);

        }

        public void setNome(String nome) {

            TextView item_nome = (TextView) mView.findViewById(R.id.nome_sabor_pizza);
            item_nome.setText(nome);

        }

        public void setDescricao(String descricao) {

            TextView item_descricao = (TextView) mView.findViewById(R.id.desc_sabor_pizza);
            item_descricao.setText(descricao);
        }

        public Double setValorUnitarioEPromocao(double valor_unit, boolean status_promocao, double valor_promocional) {

            TextView item_valor_promo = (TextView) mView.findViewById(R.id.promo_valor_unit_sabor_pizza);
            TextView item_valor_unit = (TextView) mView.findViewById(R.id.valor_unit_sabor_pizza);

            if (status_promocao == true && valor_promocional != 0) {
                item_valor_promo.setText(StringUtil.formatToMoeda(valor_promocional));
                item_valor_unit.setText(StringUtil.formatToMoeda(valor_unit));
                item_valor_unit.setPaintFlags(item_valor_unit.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                item_valor_unit.setVisibility(View.VISIBLE);
                return valor_promocional;
            } else {
                item_valor_promo.setText(StringUtil.formatToMoeda(valor_unit));
                return valor_unit;
            }

        }

        public void setImagem(final Context context, final String url) {
            final ImageView item_ref_image = (ImageView) mView.findViewById(R.id.img_sabor_pizza);

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

        public void setStatus(int status) {
            TextView item_status = (TextView) mView.findViewById(R.id.status_sabor_pizza);

            if (status == 0) { //Se Indisponível
                item_status.setVisibility(View.VISIBLE);
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