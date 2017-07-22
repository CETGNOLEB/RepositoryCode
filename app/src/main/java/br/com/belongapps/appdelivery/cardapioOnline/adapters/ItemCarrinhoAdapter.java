package br.com.belongapps.appdelivery.cardapioOnline.adapters;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Locale;

import br.com.belongapps.appdelivery.R;
import br.com.belongapps.appdelivery.cardapioOnline.dao.CarrinhoDAO;
import br.com.belongapps.appdelivery.cardapioOnline.model.ItemPedido;


public class ItemCarrinhoAdapter extends RecyclerView.Adapter<ItemCarrinhoAdapter.ViewHolder> {

    private static List<ItemPedido> itensPedido;
    private Context context;
    public TextView totalPedido;
    public TextView txtQtdItens;
    ImageView imgCarrinhoVazio;
    public TextView textCarrinhoVazio;
    public TextView descCarrinhoVazio;
    Button btFinalizar;
    Button btCardEmpty;

    public ItemCarrinhoAdapter(List<ItemPedido> itensPedido,Context context, TextView textTotal, TextView txtQtdItens,
                               ImageView imgCarrinhoVazio, TextView textCarrinhoVazio, TextView descCarrinhoVazio,
                               Button btFinalizar, Button btCardEmpty) {
        this.itensPedido = itensPedido;
        this.context = context;
        this.totalPedido = textTotal;
        this.txtQtdItens = txtQtdItens;
        this.imgCarrinhoVazio = imgCarrinhoVazio;
        this.textCarrinhoVazio = textCarrinhoVazio;
        this.descCarrinhoVazio = descCarrinhoVazio;
        this.btFinalizar = btFinalizar;
        this.btCardEmpty = btCardEmpty;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_item_carrinho, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        final ItemPedido itemPedido = itensPedido.get(position);

        holder.setNome(itemPedido.getNome());
        holder.setDescricao(itemPedido.getDescricao());
        holder.setValorUnitario(itemPedido.getValor_unit());
        holder.setValorTotalProduto(itemPedido.getValor_total());
        holder.setQuantidadeProduto(itemPedido.getQuantidade());
        holder.setImagem(context, itemPedido.getRef_img());

        holder.aumentarQuantidade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double vtotal = holder.aumentarQuantidadeDoItem(itemPedido.getValor_unit());

                itensPedido.get(position).setValor_total(vtotal);
                itensPedido.get(position).setQuantidade(Integer.parseInt(holder.qtdItem.getText().toString()));

                updateValorPedido();

            }
        });

        holder.diminuirQuantidade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double vtotal = holder.diminuirQuantidadeDoItem(itemPedido.getValor_unit());

                itensPedido.get(position).setValor_total(vtotal);
                itensPedido.get(position).setQuantidade(Integer.parseInt(holder.qtdItem.getText().toString()));

                updateValorPedido();
            }
        });

        holder.removerItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                AlertDialog.Builder mBilder = new AlertDialog.Builder(context, R.style.MyDialogTheme);
                View layoutDialog = inflater.inflate(R.layout.dialog_cofirm_delete_item_carrinho, null);

                Button btConfirmar = (Button) layoutDialog.findViewById(R.id.bt_confirmar_excluir_item_carrinho);
                Button btCancelar = (Button) layoutDialog.findViewById(R.id.bt_cancel_excluir_item_carrinho);

                mBilder.setView(layoutDialog);
                final AlertDialog dialogConfirmExcItem = mBilder.create();
                dialogConfirmExcItem.show();

                btCancelar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogConfirmExcItem.dismiss();
                    }
                });

                btConfirmar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CarrinhoDAO dao = new CarrinhoDAO(context);
                        dao.deletarItem(position);

                        itensPedido.remove(itemPedido);
                        notifyDataSetChanged();

                        updateValorPedido();
                        updateQtdItens();

                        if (itensPedido.size() == 0) {
                            exibirCarrinhoVazio();
                        }

                        dialogConfirmExcItem.dismiss();

                        Toast.makeText(context, "Item removido!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }

    public void updateValorPedido() {

        double valorTotalPedido = 0.0;
        for (ItemPedido item : itensPedido) {
            Log.println(Log.ERROR, "Valor Total: ", "" + item.getValor_total());
            valorTotalPedido += item.getValor_total();
        }

        if (itensPedido.size() == 0) {
            totalPedido.setText("");
        } else {
            totalPedido.setText("R$ " + String.format(Locale.US, "%.2f", valorTotalPedido).replace(".", ","));
        }
    }

    public void updateQtdItens() {
        if (itensPedido.size() == 1) {
            txtQtdItens.setText(itensPedido.size() + " Item");
        } else {
            txtQtdItens.setText(itensPedido.size() + " Itens");
        }
    }

    @Override
    public int getItemCount() {
        return itensPedido.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView imgProduto;
        public ImageButton diminuirQuantidade;
        public ImageButton aumentarQuantidade;
        public TextView qtdItem;
        public TextView valorTotalProduto;

        public ImageButton removerItem;

        public View mView;

        public ViewHolder(View itemView) {
            super(itemView);

            mView = itemView;

            imgProduto = (ImageView) itemView.findViewById(R.id.img_item_carrinho);
            diminuirQuantidade = (ImageButton) itemView.findViewById(R.id.bt_diminuir_qtd_item_carrinho);
            aumentarQuantidade = (ImageButton) itemView.findViewById(R.id.bt_aumentar_qtd_item_carrinho);

            qtdItem = (TextView) itemView.findViewById(R.id.qtd_item_carrinho);
            valorTotalProduto = (TextView) mView.findViewById(R.id.valor_total_item_carrinho);

            removerItem = (ImageButton) itemView.findViewById(R.id.bt_excluir_item_carrinho);
        }

        public void setNome(String nome) {

            TextView nomeProduto = (TextView) mView.findViewById(R.id.nome_item_carrinho);
            nomeProduto.setText(nome);

        }

        public void setDescricao(String descricao) {
            TextView descProduto = (TextView) mView.findViewById(R.id.desc_item_carrinho);
            if (descricao != null){
                descProduto.setText(descricao);
            } else{
                descProduto.setVisibility(View.GONE);
            }

        }

        public void setValorUnitario(double valorUnit) {

            TextView valorUnitProduto = (TextView) mView.findViewById(R.id.valor_unit_item_carrinho);
            valorUnitProduto.setText(" R$ " + String.format(Locale.US, "%.2f", valorUnit).replace(".", ","));

        }

        public void setValorTotalProduto(double valorTotal) {
            valorTotalProduto.setText(" R$ " + String.format(Locale.US, "%.2f", valorTotal).replace(".", ","));
        }

        public void setQuantidadeProduto(int quantidadeProduto) {
            qtdItem.setText(String.valueOf(quantidadeProduto));
        }

        public void setImagem(final Context context, final String url) {
            final ImageView imgProduto = (ImageView) mView.findViewById(R.id.img_item_carrinho);

            Picasso.with(context).load(url).networkPolicy(NetworkPolicy.OFFLINE).into(imgProduto, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {
                    Picasso.with(context).load(url).into(imgProduto);
                }
            });
        }

        public double diminuirQuantidadeDoItem(double valorUni) {
            int qtd = Integer.valueOf(qtdItem.getText().toString());

            if (qtd > 1) {
                qtd--;
            }

            qtdItem.setText(String.valueOf(qtd));

            double valorTotal;

            valorTotal = qtd * valorUni;
            setValorTotalProduto(valorTotal);

            return valorTotal;
        }

        public double aumentarQuantidadeDoItem(double valorUni) {
            int qtd = Integer.valueOf(qtdItem.getText().toString());
            qtd++;

            qtdItem.setText(String.valueOf(qtd));

            double valorTotal;
            valorTotal = qtd * valorUni;
            setValorTotalProduto(valorTotal);

            return valorTotal;
        }

    }

    private void exibirCarrinhoVazio(){
        imgCarrinhoVazio.setVisibility(View.VISIBLE);
        textCarrinhoVazio.setVisibility(View.VISIBLE);
        descCarrinhoVazio.setVisibility(View.VISIBLE);
        btCardEmpty.setVisibility(View.VISIBLE);
        btFinalizar.setVisibility(View.GONE);
    }
}