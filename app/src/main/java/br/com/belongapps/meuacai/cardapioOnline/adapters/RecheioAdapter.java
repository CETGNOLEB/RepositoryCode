package br.com.belongapps.meuacai.cardapioOnline.adapters;

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

import br.com.belongapps.meuacai.R;
import br.com.belongapps.meuacai.cardapioOnline.model.ItemCardapio;


public class RecheioAdapter extends ArrayAdapter<ItemCardapio> {

    public RecheioAdapter(Activity context, ArrayList<ItemCardapio> recheios) {
        super(context, 0, recheios);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View listItemView = convertView;

        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.card_item_recheios, parent, false);
        }

        ItemCardapio recheio = getItem(position);

        TextView recheio_nome = (TextView) listItemView.findViewById(R.id.item_nome_recheio);
        recheio_nome.setText(recheio.getNome());

        ImageView item_ref_image = (ImageView) listItemView.findViewById(R.id.item_img_recheio);
        Picasso.with(getContext()).load(recheio.getRef_img()).into(item_ref_image);

        /*Resources res = getContext().getResources();
        Drawable drawable = res.getDrawable(R.drawable.ic_menu_solicitar_lixeira);*/

        /*ImageView imageView = (ImageView) listItemView.findViewById(R.id.imagem_list_solicitacoes);
        imageView.setImageDrawable(drawable);*/

        return listItemView;
    }

}