package br.com.belongapps.appdelivery.cardapioOnline.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import br.com.belongapps.appdelivery.R;
import br.com.belongapps.appdelivery.cardapioOnline.activitys.EscolherRecheioActivity;
import br.com.belongapps.appdelivery.cardapioOnline.model.ItemCardapio;
import br.com.belongapps.appdelivery.cardapioOnline.viewHolders.AcaiViewHolder;


public class AcaiAdapter extends RecyclerView.Adapter {

    List<ItemCardapio> acais;
    private Context context;

    public AcaiAdapter(List<ItemCardapio> acais, Context context) {
        this.acais = acais;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(context).inflate(R.layout.card_acai, parent, false);
        final AcaiViewHolder holder = new AcaiViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        AcaiViewHolder acaiViewHolder = (AcaiViewHolder) holder;

        final ItemCardapio acai  = acais.get(position) ;

        acaiViewHolder.setNome(acai.getNome());
        acaiViewHolder.setDescricao(acai.getDescricao());
        acaiViewHolder.setImagem(context, acai.getRef_img());
        acaiViewHolder.setValorUnitario(acai.getValor_unit());

        acaiViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, EscolherRecheioActivity.class);
                i.putExtra("nomeItem" , acai.getNome());
                i.putExtra("qtdRecheio", acai.getQtd_recheios());
                context.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return acais.size();
    }

}