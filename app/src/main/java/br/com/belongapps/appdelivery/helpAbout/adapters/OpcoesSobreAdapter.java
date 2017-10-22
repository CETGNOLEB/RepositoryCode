package br.com.belongapps.appdelivery.helpAbout.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import br.com.belongapps.appdelivery.R;
import br.com.belongapps.appdelivery.helpAbout.activities.SobreAplicativoActivity;
import br.com.belongapps.appdelivery.helpAbout.model.OpcoesSobre;


public class OpcoesSobreAdapter extends RecyclerView.Adapter<OpcoesSobreAdapter.ViewHolderOpcoes> {

    private static List<OpcoesSobre> opcoes;
    private static Context context;

    public OpcoesSobreAdapter(List<OpcoesSobre> opcoes, Context context) {
        this.opcoes = opcoes;
        this.context = context;
    }

    @Override
    public ViewHolderOpcoes onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_icon_opcoes, null, false);
        return new ViewHolderOpcoes(view);
    }

    @Override
    public void onBindViewHolder(ViewHolderOpcoes holder, int position) {
        final OpcoesSobre opcao = opcoes.get(position);

        holder.icon.setImageResource(opcao.getIcon());
        holder.descOpcao.setText(opcao.getNomeOpcao());

        if (position == opcoes.size() - 1) {
            holder.divider.setVisibility(View.INVISIBLE);
        }

        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (opcao.getNomeOpcao().equals("Sobre o aplicativo")) {

                    exibirActivityDadosDoAplicativo();

                } else if (opcao.getNomeOpcao().equals("Política de privacidade")) {

                    Toast.makeText(context, "Link para a Política de privacidade", Toast.LENGTH_SHORT).show();

                } else if (opcao.getNomeOpcao().equals("Nosso site")) {

                    Toast.makeText(context, "Link para o site", Toast.LENGTH_SHORT).show();

                } else { //Sair
                    Toast.makeText(context, "Link para a lista de Versão 1.0.0", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return opcoes.size();
    }

    public class ViewHolderOpcoes extends RecyclerView.ViewHolder {

        ImageView icon;
        TextView descOpcao;
        View divider;

        View item;

        public ViewHolderOpcoes(View itemView) {
            super(itemView);

            item = itemView;
            icon = (ImageView) itemView.findViewById(R.id.icon_opcoes);
            descOpcao = (TextView) itemView.findViewById(R.id.desc_opcoes);
            divider = itemView.findViewById(R.id.divider_opcoes);
        }
    }


    //MÉTODOS AUXILIÁRES
    private void exibirActivityDadosDoAplicativo() {
        Intent intent = new Intent(context, SobreAplicativoActivity.class);
        context.startActivity(intent);
        ((Activity)context).finish();
    }

}