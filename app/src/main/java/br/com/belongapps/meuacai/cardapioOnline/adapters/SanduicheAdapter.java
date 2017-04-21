package br.com.belongapps.meuacai.cardapioOnline.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import br.com.belongapps.meuacai.R;
import br.com.belongapps.meuacai.cardapioOnline.model.ItemCardapio;
import br.com.belongapps.meuacai.cardapioOnline.viewHolders.SanduicheViewHolder;


public class SanduicheAdapter extends RecyclerView.Adapter {

    List<ItemCardapio> sanduiches;
    private Context context;

    public SanduicheAdapter(List<ItemCardapio> sanduiches, Context context) {
        this.sanduiches = sanduiches;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_sanduiches, parent, false);
        SanduicheViewHolder holder = new SanduicheViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        SanduicheViewHolder sanduicheViewHolder = (SanduicheViewHolder) holder;

        ItemCardapio acai  = sanduiches.get(position) ;

        sanduicheViewHolder.setNome(acai.getNome());
        sanduicheViewHolder.setDescricao(acai.getDescricao());
        sanduicheViewHolder.setImagem(context, acai.getRef_img());
        sanduicheViewHolder.setValorUnitario(acai.getValor_unit());

       /* holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, EscolherRecheioActivity.class);
                i.putExtra("nomeItem" , acai.getNome());
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return sanduiches.size();
    }

}