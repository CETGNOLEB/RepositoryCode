package br.com.belongapps.appdelivery.cardapioOnline.adapters;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import br.com.belongapps.appdelivery.R;
import br.com.belongapps.appdelivery.cardapioOnline.model.FormadePagamento;
import br.com.belongapps.appdelivery.util.MaskNumberUtil;


public class FormasdePagamentoAdapter extends RecyclerView.Adapter<FormasdePagamentoAdapter.ViewHolder> {

    private List<FormadePagamento> formasdePagamento;
    private Context context;

    public FormasdePagamentoAdapter(List<FormadePagamento> formadePagamento, Context context) {
        this.formasdePagamento = formadePagamento;
        this.context = context;
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
        } else {
            if (formadePagamento.isStatus() == true) {
                formadePagamento.setStatus(false);
            } else {
                formadePagamento.setStatus(true);
            }
        }

        return formadePagamento;
    }


    public FormadePagamento openDialogDinheiro(final FormadePagamento dinheiro) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        AlertDialog.Builder mBilder = new AlertDialog.Builder(context);

        View layoutDialog = inflater.inflate(R.layout.dialog_pag_dinheiro, null);

        final EditText valorEmDinheiro = (EditText) layoutDialog.findViewById(R.id.valor_do_dinheiro);

        Button bt_cancelar = (Button) layoutDialog.findViewById(R.id.bt_cancel_fpag_dinheiro);
        Button bt_ok = (Button) layoutDialog.findViewById(R.id.bt_confirmar_fpag_dinheiro);

        valorEmDinheiro.addTextChangedListener(new MaskNumberUtil(valorEmDinheiro));

        if (!dinheiro.getDescricao().equals("")){
            valorEmDinheiro.setText(dinheiro.getDescricao().replace("Troco para:", "").trim());
        }

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

                dialogFormadePagamento.dismiss();
                dinheiro.setDescricao("Troco para: " +  valorEmDinheiro.getText().toString());
                dinheiro.setStatus(true);

                notifyDataSetChanged();
            }
        });

        return dinheiro;
    }


    public FormadePagamento openDialogDinheiroeCartao(final FormadePagamento dinheiroeCartao) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        AlertDialog.Builder mBilder = new AlertDialog.Builder(context);

        View layoutDialog = inflater.inflate(R.layout.dialog_pag_dinheiro_e_cartao, null);

        final EditText valorComDinheiro = (EditText) layoutDialog.findViewById(R.id.valor_com_dinheiro);
        final EditText valorComCartao = (EditText) layoutDialog.findViewById(R.id.valor_com_cartao);

        Button bt_cancelar = (Button) layoutDialog.findViewById(R.id.bt_cancel_fpag_dinheiro_e_cartao);
        Button bt_ok = (Button) layoutDialog.findViewById(R.id.bt_confirmar_fpag_dinheiro_e_cartao);

        valorComCartao.addTextChangedListener(new MaskNumberUtil(valorComCartao));
        valorComDinheiro.addTextChangedListener(new MaskNumberUtil(valorComDinheiro));

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
                dialogFormadePagamento.dismiss();
                dinheiroeCartao.setStatus(true);
                dinheiroeCartao.setDescricao("Dinheiro: " + valorComDinheiro.getText() + " Cartão: " + valorComCartao.getText());

                notifyDataSetChanged();
            }
        });

        return dinheiroeCartao;
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

        public void setDescForma(String descricao){
            descForma.setText(descricao);

            if (!descricao.equals("")){
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