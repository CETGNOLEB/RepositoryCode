package br.com.belongapps.appdelivery.helpAbout.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import br.com.belongapps.appdelivery.R;
import br.com.belongapps.appdelivery.helpAbout.model.OpcoesSocial;


public class OpcoesSocialAdapter extends RecyclerView.Adapter<OpcoesSocialAdapter.ViewHolderOpcoes> {

    private static List<OpcoesSocial> opcoes;
    private static Context context;

    public OpcoesSocialAdapter(List<OpcoesSocial> opcoes, Context context) {
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
        final OpcoesSocial opcao = opcoes.get(position);

        holder.icon.setImageResource(opcao.getIcon());
        holder.descOpcao.setText(opcao.getNomeOpcao());

        if (position == opcoes.size() - 1) {
            holder.divider.setVisibility(View.INVISIBLE);
        }

        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (opcao.getNomeOpcao().equals("Envie Comentários")) {

                    Toast.makeText(context, "Enviar Comentário", Toast.LENGTH_SHORT).show();

                } else if (opcao.getNomeOpcao().equals("Gostou do aplicativo? Avalie!")) {

                    avaliarNaPlayStore();

                } else if (opcao.getNomeOpcao().equals("Curta nosso Facebook")) {

                    curtirNoFacebook();

                } else { //Compartilhar aplicativo

                    compartilharLinkdoAplicativo();
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

    public void avaliarNaPlayStore(){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=br.com.belongapps.appdelivery"));
        context.startActivity(intent);
    }

    public void curtirNoFacebook(){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("https://www.facebook.com/belongtecnologia/?ref=br_rs"));
        context.startActivity(intent);
    }

    public void compartilharLinkdoAplicativo() {
        Intent share = new Intent(android.content.Intent.ACTION_SEND);
        share.setType("text/plain");
        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

        share.putExtra(Intent.EXTRA_SUBJECT, "Titulo do compartilhamento");
        share.putExtra(Intent.EXTRA_TEXT, "Dá uma olhada nesse app! https://play.google.com/store/apps/details?id=br.com.belongapps.appdelivery");
        context.startActivity(Intent.createChooser(share, "Complete a ação usando:"));
    }
}