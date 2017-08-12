package br.com.belongapps.appdelivery.cardapioOnline.adapters;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

import br.com.belongapps.appdelivery.R;
import br.com.belongapps.appdelivery.cardapioOnline.model.FormadePagamento;
import br.com.belongapps.appdelivery.util.SimpleTextListenerUtil;
import br.com.belongapps.appdelivery.util.StringUtil;

public class FormasdePagamentoAdapter extends RecyclerView.Adapter<FormasdePagamentoAdapter.ViewHolder> {

    private List<FormadePagamento> formasdePagamento;
    private Context context;
    private double totalPedido;

    Locale mLocale = new Locale("pt", "BR");

    public FormasdePagamentoAdapter(List<FormadePagamento> formadePagamento, Context context, double totalPedido) {
        this.formasdePagamento = formadePagamento;
        this.context = context;
        this.totalPedido = totalPedido;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_formar_pagamento, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final FormadePagamento formadePagamento = formasdePagamento.get(position);

        holder.setNome(formadePagamento.getNome());
        holder.setDescForma(formadePagamento.getDescricao());
        holder.setImagem(formadePagamento.getImagem());
        holder.setStatus(formadePagamento.isStatus());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FormadePagamento frmpag = definirFormadePagamento(formadePagamento);

                holder.setStatus(frmpag.isStatus());
                holder.setDescForma(frmpag.getDescricao());

                formasdePagamento.get(position).setStatus(frmpag.isStatus());
                formasdePagamento.get(position).setNome(frmpag.getNome());
                formasdePagamento.get(position).setDescricao(frmpag.getDescricao());

            }
        });

    }

    @Override
    public int getItemCount() {
        return formasdePagamento.size();
    }

    public FormadePagamento definirFormadePagamento(FormadePagamento formadePagamento) {

        if (formadePagamento.getNome().equals("DINHEIRO")) {
            formadePagamento = openDialogDinheiro(formadePagamento);

            return formadePagamento;

        } else if (formadePagamento.getNome().equals("DINHEIRO E CARTÃO")) {
            formadePagamento = openDialogDinheiroeCartao(formadePagamento);
            return formadePagamento;
        } else{
            ativarFormadePagamento(formadePagamento);
            return formadePagamento;
        }

    }

    public FormadePagamento openDialogDinheiro(final FormadePagamento dinheiro) {

        final String txtTotalPedido = "R$" + String.format(Locale.US, "%.2f", totalPedido).replace(".", ",");

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        AlertDialog.Builder mBilder = new AlertDialog.Builder(context);

        View layoutDialog = inflater.inflate(R.layout.dialog_pgm_dinheiro, null);

        final EditText valorEmDinheiro = (EditText) layoutDialog.findViewById(R.id.valor_pagamento_dinheiro);
        final TextView valorPedidoDinheiro = (TextView) layoutDialog.findViewById(R.id.valor_pedido_dinheiro);
        valorPedidoDinheiro.setText(txtTotalPedido);

        final ImageView imgError = (ImageView) layoutDialog.findViewById(R.id.img_error);
        final TextView valorDinheiroInvalido = (TextView) layoutDialog.findViewById(R.id.valor_dinheiro_invalido);

        Button bt_cancelar = (Button) layoutDialog.findViewById(R.id.bt_cancel_pag_dinheiro);
        Button bt_ok = (Button) layoutDialog.findViewById(R.id.bt_confirmar_pag_dinheiro);

        if (dinheiro.getDescricao().equals("") || dinheiro.getDescricao().equals("Sem Troco")) {
            valorEmDinheiro.setText(txtTotalPedido);
        } else {
            valorEmDinheiro.setText(StringUtil.formatToMoeda(dinheiro.getValorDinheiro()));
        }

        valorEmDinheiro.addTextChangedListener(new SimpleTextListenerUtil(valorEmDinheiro, mLocale));

        mBilder.setView(layoutDialog);
        final AlertDialog dialogFormadePagamento = mBilder.create();

        dialogFormadePagamento.show();

        bt_cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogFormadePagamento.dismiss();
            }
        });

        bt_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Double valorInformado = Double.parseDouble(valorEmDinheiro.getText().toString().replace("R$", "").replace(",", ".").trim());

                if (valorInformado < totalPedido) {
                    imgError.setVisibility(View.VISIBLE);
                    valorDinheiroInvalido.setVisibility(View.VISIBLE);
                } else {
                    imgError.setVisibility(View.GONE);
                    valorDinheiroInvalido.setVisibility(View.GONE);

                    dinheiro.setValorDinheiro(StringUtil.formatMoedaToDouble(valorEmDinheiro.getText().toString()));

                    ativarFormadePagamento(dinheiro);
                    notifyDataSetChanged();

                    dialogFormadePagamento.dismiss();
                }
            }
        });

        return dinheiro;
    }


    public FormadePagamento openDialogDinheiroeCartao(final FormadePagamento dinheiroeCartao) {

        final String txtTotalPedido = StringUtil.formatToMoeda(totalPedido);
        final String txtMetadeTotalPedido = StringUtil.formatToMoeda(totalPedido / 2);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        AlertDialog.Builder mBilder = new AlertDialog.Builder(context);

        View layoutDialog = inflater.inflate(R.layout.dialog_pgm_dinheiro_cartao, null);

        final TextView valorTotalPedido = (TextView) layoutDialog.findViewById(R.id.valor_pedido_dinheiro_cartao);
        valorTotalPedido.setText(txtTotalPedido);

        final EditText valorComDinheiro = (EditText) layoutDialog.findViewById(R.id.valor_pgm_dinheiro);
        final EditText valorComCartao = (EditText) layoutDialog.findViewById(R.id.valor_pgm_cartao);

        if (dinheiroeCartao.getDescricao().equals("")) {
            valorComDinheiro.setText(txtMetadeTotalPedido);
            valorComCartao.setText(txtMetadeTotalPedido);
        } else {
            valorComDinheiro.setText(StringUtil.formatToMoeda(dinheiroeCartao.getValorDinheiro()));
            valorComCartao.setText(StringUtil.formatToMoeda(dinheiroeCartao.getValorCartao()));
        }

        Button bt_cancelar = (Button) layoutDialog.findViewById(R.id.bt_cancel_pag_dinheiro_cartao);
        Button bt_ok = (Button) layoutDialog.findViewById(R.id.bt_confirmar_pag_dinheiro_cartao);

        valorComCartao.addTextChangedListener(new SimpleTextListenerUtil(valorComCartao, mLocale));
        valorComDinheiro.addTextChangedListener(new SimpleTextListenerUtil(valorComDinheiro, mLocale));

        mBilder.setView(layoutDialog);
        final AlertDialog dialogFormadePagamento = mBilder.create();

        dialogFormadePagamento.show();

        bt_cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogFormadePagamento.dismiss();
            }
        });

        bt_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dinheiroeCartao.setValorDinheiro(StringUtil.formatMoedaToDouble(valorComDinheiro.getText().toString()));
                dinheiroeCartao.setValorCartao(StringUtil.formatMoedaToDouble(valorComCartao.getText().toString()));

                ativarFormadePagamento(dinheiroeCartao);

                notifyDataSetChanged();
                dialogFormadePagamento.dismiss();
            }
        });

        return dinheiroeCartao;
    }

    private void ativarFormadePagamento(FormadePagamento forma){
        for (FormadePagamento fpgm: formasdePagamento) {
            if (fpgm.getNome().equals(forma.getNome())){
                fpgm.setStatus(true);
            } else {
                fpgm.setStatus(false);
                fpgm.setValorCartao(null);
                fpgm.setValorDinheiro(null);
            }
        }

        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView imgProduto;
        public TextView nomeForma;
        public TextView descForma;
        public ImageView checkForma;


        public View mView;

        public ViewHolder(View itemView) {
            super(itemView);

            mView = itemView;

            imgProduto = (ImageView) itemView.findViewById(R.id.img_frm_pagamento);
            nomeForma = (TextView) itemView.findViewById(R.id.nome_frm_pagamento);
            descForma = (TextView) itemView.findViewById(R.id.desc_frm_pagamento);
            checkForma = (ImageView) itemView.findViewById(R.id.cheked_frm_pagamento);

        }

        public void setNome(String nome) {
            nomeForma.setText(nome);
        }

        public void setDescForma(String descricao) {
            descForma.setText(descricao);

            if (descricao.replace("Troco para ", "").equals(StringUtil.formatToMoeda(totalPedido))) {
                descForma.setText("Sem Troco");
            } else {
                descForma.setVisibility(View.VISIBLE);
            }
        }

        public void setImagem(int url) {
            imgProduto.setImageResource(url);
        }

        public void setStatus(boolean status) {
            if (status == true) {
                checkForma.setVisibility(View.VISIBLE);
            } else {
                checkForma.setVisibility(View.INVISIBLE);
            }

        }

    }


}