package br.com.belongapps.appdelivery.cardapioOnline.viewHolders;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.Locale;

import br.com.belongapps.appdelivery.R;

public class AcaiViewHolder extends RecyclerView.ViewHolder{

    View mView;

    public AcaiViewHolder(View itemView) {
        super(itemView);
        mView = itemView;

    }

    public void setNome(String nome) {

        TextView item_nome = (TextView) mView.findViewById(R.id.item_nome_acai);
        item_nome.setText(nome);

    }

    public void setDescricao(String descricao) {

        TextView item_descricao = (TextView) mView.findViewById(R.id.item_desc_acai);
        item_descricao.setText(descricao);
    }

    public void setValorUnitario(double valor_unit) {

        TextView item_valor_unit = (TextView) mView.findViewById(R.id.item_valor_unit_acai);
        item_valor_unit.setText(" R$ " +  String.format(Locale.US, "%.2f", valor_unit).replace(".", ","));

    }

    public void setImagem(Context context, String url) {
        ImageView item_ref_image = (ImageView) mView.findViewById(R.id.img_acai);
        Picasso.with(context).load(url).into(item_ref_image);
    }
}
