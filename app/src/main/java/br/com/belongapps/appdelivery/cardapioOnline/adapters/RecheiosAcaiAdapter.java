package br.com.belongapps.appdelivery.cardapioOnline.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;


import java.util.List;

import br.com.belongapps.appdelivery.R;
import br.com.belongapps.appdelivery.cardapioOnline.activitys.DetalhesdoItemActivity;
import br.com.belongapps.appdelivery.cardapioOnline.model.ItemPedido;
import br.com.belongapps.appdelivery.cardapioOnline.model.RecheioAcai;
import br.com.belongapps.appdelivery.util.Print;
import br.com.belongapps.appdelivery.util.StringUtil;


public class RecheiosAcaiAdapter extends RecyclerView.Adapter<RecheiosAcaiAdapter.RecheioViewHolder> {

    private static List<RecheioAcai> todosRecheios;
    private static List<RecheioAcai> recheiosPadrao;
    private static List<String> recheiosAcaiKey;
    private static Button btProximo;
    private TextView tvTotalAcai;
    private static Context context;

    //PARAMS
    private static String nomeAcai;
    private static String imgAcai;
    private static double valorTotal;
    private static String keyAcai;

    public RecheiosAcaiAdapter(String nomeAcai, String imgAcai, double valorTotal, String keyAcai,
                               List<RecheioAcai> recheios, List<String> recheiosAcaiKey, List<RecheioAcai> recheiosPadrao,
                               Button btProximo, TextView tvTotalAcai, final Context context) {

        this.nomeAcai = nomeAcai;
        this.imgAcai = imgAcai;
        this.valorTotal = valorTotal;
        this.keyAcai = keyAcai;

        this.todosRecheios = recheios;
        this.recheiosAcaiKey = recheiosAcaiKey;

        this.recheiosPadrao = recheiosPadrao;

        this.btProximo = btProximo;
        this.tvTotalAcai = tvTotalAcai;
        this.context = context;

        this.btProximo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ItemPedido item = createItemPedido();
                Intent i = new Intent(context, DetalhesdoItemActivity.class);
                i.putExtra("ItemPedido", item);
                i.putExtra("TelaAnterior", "MontagemAcai");
                context.startActivity(i);
                ((Activity) context).finish();
            }
        });
    }

    @Override
    public RecheioViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_icon_recheios, null, false);
        return new RecheioViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecheioViewHolder viewHolder, int position) {
        final RecheioAcai recheio = todosRecheios.get(position);

        viewHolder.setNome(recheio.getNome());
        viewHolder.setImagem(context, recheio.getRef_img());

        //Aumentar Quantidade
        viewHolder.aumentarQuantidade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recheio.setQtd(viewHolder.aumentarQuantidadeDoItem(recheio));
                //viewHolder.mostrarOcultarValorDoItem(mostrarAcressimo);
            }
        });

        viewHolder.diminuirQuantidade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recheio.setQtd(viewHolder.diminuirQuantidadeDoItem(recheio));

               /* boolean mostrarAcressimo = decrementeValorTotalProduto(recheio);
                viewHolder.mostrarOcultarValorDoItem(mostrarAcressimo);*/

            }
        });

        //DEFINE PADRÃO INICIAL NA LISTA
        for (String recheioAcai : recheiosAcaiKey) {
            if (recheio.getItemKey().equals(recheioAcai)) {
                recheio.setQtd(1);
                viewHolder.setQtdInicialRecheio(1);
            }

        }
    }

    @Override
    public int getItemCount() {
        return todosRecheios.size();
    }

    public class RecheioViewHolder extends RecyclerView.ViewHolder {

        View mView;
        CardView card_recheio;
        ImageButton diminuirQuantidade;
        ImageButton aumentarQuantidade;
        TextView qtdItem;

        TextView nomeAdicionais;
        TextView valorAcressimoAdicionais;

        public RecheioViewHolder(final View itemView) {
            super(itemView);

            mView = itemView;
            card_recheio = (CardView) mView.findViewById(R.id.card_recheios);
            aumentarQuantidade = (ImageButton) mView.findViewById(R.id.bt_mais_recheio);
            diminuirQuantidade = (ImageButton) mView.findViewById(R.id.bt_menos_recheio);
            qtdItem = (TextView) itemView.findViewById(R.id.qtd_do_recheio);

            nomeAdicionais = (TextView) itemView.findViewById(R.id.item_nome_adicionais);
            valorAcressimoAdicionais = (TextView) itemView.findViewById(R.id.item_valor_acressimo_adicionais);

        }

        public void setNome(String nome) {

            TextView item_nome = (TextView) mView.findViewById(R.id.item_nome_adicionais);
            item_nome.setText(nome);

        }

        public void setImagem(final Context context, final String url) {
            final ImageView item_ref_image = (ImageView) mView.findViewById(R.id.item_img_recheio);

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

        public void setQtdInicialRecheio(int qtdRecheio) {
            qtdItem.setText(String.valueOf(qtdRecheio));
        }

        public int diminuirQuantidadeDoItem(RecheioAcai recheio) {
            boolean mostrarAcressimo = true;
            int qtd = Integer.valueOf(qtdItem.getText().toString()); //Recupera qtd  selecionada

            if (qtd > 0) {

                Integer qtdPadraoRecheio = buscarQtdPadrao(recheio); //recupera qtd padrao do item no açai

                double valorUnit = (qtd - qtdPadraoRecheio) * recheio.getValor_unit();
                valorAcressimoAdicionais.setText("+ " + StringUtil.formatToMoeda(valorUnit));

                if (qtd > qtdPadraoRecheio) {
                    //Incrementar Valor do Produto
                    valorTotal = valorTotal - recheio.getValor_unit();
                    tvTotalAcai.setText("Total: " + StringUtil.formatToMoeda(valorTotal));
                }

                qtd--;

                Print.logError("QTD P: " + qtdPadraoRecheio);
                Print.logError("QTD: " + qtd);

                if (qtd <= qtdPadraoRecheio) {
                    mostrarAcressimo = false;
                }

                mostrarOcultarValorDoItem(mostrarAcressimo);

                qtdItem.setText(String.valueOf(qtd)); // set qtd na view

            }

            return qtd;
        }

        public int aumentarQuantidadeDoItem(RecheioAcai recheio) {
            boolean mostrarValorDeAcressimo = false;

            int qtd = Integer.valueOf(qtdItem.getText().toString()); //Recupera qtd
            qtd++;

            qtdItem.setText(String.valueOf(qtd)); //Set Qtd na View

            Integer qtdPadraoRecheio = buscarQtdPadrao(recheio); //Buscar qtd padrão do Açai

            if (qtdPadraoRecheio < qtd) {
                double valorUnit = (qtd - qtdPadraoRecheio) * recheio.getValor_unit(); //Calcula valor do acressimo
                valorAcressimoAdicionais.setText("+ " + StringUtil.formatToMoeda(valorUnit)); //set valor do acressimo na view

                //Incrementar Valor do Produto
                valorTotal = valorTotal + recheio.getValor_unit();
                tvTotalAcai.setText("Total: " + StringUtil.formatToMoeda(valorTotal));

                mostrarValorDeAcressimo = true; //Mostra valor do acressimo
            }

            mostrarOcultarValorDoItem(mostrarValorDeAcressimo);

            return qtd;
        }

        public void mostrarOcultarValorDoItem(boolean mostrar) {
            LinearLayout.LayoutParams marginParams = new LinearLayout.LayoutParams(nomeAdicionais.getLayoutParams());

            if (mostrar) {
                //SET MARGENS - NOME
                marginParams.setMargins(10, 10, 0, 0);
                nomeAdicionais.setLayoutParams(marginParams);
                valorAcressimoAdicionais.setVisibility(View.VISIBLE);
            } else {
                marginParams.setMargins(20, 40, 0, 0);
                nomeAdicionais.setLayoutParams(marginParams);
                valorAcressimoAdicionais.setVisibility(View.GONE);
            }
            //setMa
        }
    }

    //MÉTODOS AUXILIARES
    private ItemPedido createItemPedido() {

        ItemPedido item = new ItemPedido();
        item.setNome(nomeAcai);
        item.setRef_img(imgAcai);
        item.setKeyItem(keyAcai);
        Print.logError("KEY DO AçAI: " + keyAcai);
        item.setDescricao(getDescricaoAcai());
        item.setValor_unit(valorTotal);
        item.setCategoria("1"); //CATEGORIA NO CARDAPIO

        return item;
    }

    private String getDescricaoAcai() {
        String descricao = "";

        for (RecheioAcai recheioAcai : todosRecheios) {
            if (recheioAcai.getQtd() != null) {
                if (recheioAcai.getQtd() > 0) {
                    descricao += getDescQtdRecheio(recheioAcai.getQtd()) + recheioAcai.getNome() + ", ";
                }
            }
        }

        if (descricao.equals("")) {
            return "Açai puro";
        }

        return descricao.substring(0, descricao.length() - 2); //removendo , e espaço no final
    }

    public String getDescQtdRecheio(Integer qtd) {
        if (qtd > 1) {
            return qtd + "x ";
        }

        return "";
    }

   /* private boolean incrementeValorTotalProduto(RecheioAcai recheio) {
        boolean incrementado = false;

        Integer qtdPadraoRecheio = buscarQtdPadrao(recheio);

        Log.println(Log.ERROR, "DEC QTD INICIAL", "" + qtdPadraoRecheio);
        Log.println(Log.ERROR, "DEC QTD ATUAL", "" + recheio.getQtd());

        if (recheio.getQtd() > qtdPadraoRecheio) {
            valorTotal = valorTotal + recheio.getValor_unit();
            incrementado = true;
        }

        tvTotalAcai.setText("Total: " + StringUtil.formatToMoeda(valorTotal));

        return incrementado;
    }*/

    private boolean decrementeValorTotalProduto(RecheioAcai recheio) {
        boolean mostrarAcressimo = true;

        Integer qtdPadraoRecheio = buscarQtdPadrao(recheio);

        Log.println(Log.ERROR, "DEC QTD INICIAL", "" + qtdPadraoRecheio);
        Log.println(Log.ERROR, "DEC QTD ATUAL", "" + recheio.getQtd());

        if (qtdPadraoRecheio <= recheio.getQtd()) {
            valorTotal = valorTotal - recheio.getValor_unit();
        }

        if (recheio.getQtd() <= qtdPadraoRecheio) {
            mostrarAcressimo = false;
        }

        tvTotalAcai.setText("Total: " + StringUtil.formatToMoeda(valorTotal));

        return mostrarAcressimo;
    }

    private static Integer buscarQtdPadrao(RecheioAcai recheioAcai) {
        for (String recheiosKey : recheiosAcaiKey) {
            for (RecheioAcai r : recheiosPadrao) {
                if (recheiosKey.equals(recheioAcai.getItemKey())) {
                    return 1;
                }
            }
        }

        return 0;
    }
}