package br.com.belongapps.appdelivery.cardapioOnline.adapters;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import br.com.belongapps.appdelivery.R;
import br.com.belongapps.appdelivery.cardapioOnline.model.ItemCardapio;


public class AdicionalAdapter extends ArrayAdapter<ItemCardapio> {

    public AdicionalAdapter(Activity context, ArrayList<ItemCardapio> adicionais) {
        super(context, 0, adicionais);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View listItemView = convertView;

        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_icon_adicionais, parent, false);
        }

        ItemCardapio recheio = getItem(position);

        TextView recheio_nome = (TextView) listItemView.findViewById(R.id.item_nome_adicionais);
        recheio_nome.setText(recheio.getNome());

        TextView recheio_valor_unit = (TextView) listItemView.findViewById(R.id.item_valor_unit_adicionais);
        recheio_valor_unit.setText("R$ " + recheio.getValor_unit() + ",00");

        ImageView item_ref_image = (ImageView) listItemView.findViewById(R.id.item_img_adicionais);
        Picasso.with(getContext()).load(recheio.getRef_img()).into(item_ref_image);

        /*Resources res = getContext().getResources();
        Drawable drawable = res.getDrawable(R.drawable.ic_menu_solicitar_lixeira);*/

        /*ImageView imageView = (ImageView) listItemView.findViewById(R.id.imagem_list_solicitacoes);
        imageView.setImageDrawable(drawable);*/

        return listItemView;
    }

}