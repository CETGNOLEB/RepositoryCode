package br.com.belongapps.meuacai.cardapioOnline.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import br.com.belongapps.meuacai.R;
import br.com.belongapps.meuacai.cardapioOnline.activitys.CarrinhoActivity;
import br.com.belongapps.meuacai.cardapioOnline.model.ItemCardapio;
import br.com.belongapps.meuacai.cardapioOnline.viewHolders.SalgadoViewHolder;


public class SalgadoAdapter extends RecyclerView.Adapter {

    List<ItemCardapio> salgados;
    private Context context;

    public SalgadoAdapter(List<ItemCardapio> salgados, Context context) {
        this.salgados = salgados;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_salgados, parent, false);
        SalgadoViewHolder holder = new SalgadoViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        SalgadoViewHolder salgadoViewHolder = (SalgadoViewHolder) holder;

        final ItemCardapio salgado  = salgados.get(position) ;

        salgadoViewHolder.setNome(salgado.getNome());
        salgadoViewHolder.setDescricao(salgado.getDescricao());
        salgadoViewHolder.setImagem(context, salgado.getRef_img());
        salgadoViewHolder.setValorUnitario(salgado.getValor_unit());

        salgadoViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, CarrinhoActivity.class);
                i.putExtra("nomeItem" , salgado.getNome());
                context.startActivity(i);

            }
        });
    }

    @Override
    public int getItemCount() {
        return salgados.size();
    }

}