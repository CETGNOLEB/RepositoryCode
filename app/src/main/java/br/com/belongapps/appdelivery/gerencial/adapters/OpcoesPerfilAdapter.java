package br.com.belongapps.appdelivery.gerencial.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

import br.com.belongapps.appdelivery.R;
import br.com.belongapps.appdelivery.gerencial.activities.EnderecosActivity;
import br.com.belongapps.appdelivery.gerencial.model.OpcoesPerfil;
import br.com.belongapps.appdelivery.posPedido.activities.MeusPedidosActivity;
import br.com.belongapps.appdelivery.promocoes.activities.TelaInicialActivity;


public class OpcoesPerfilAdapter extends RecyclerView.Adapter<OpcoesPerfilAdapter.ViewHolderOpcoes> {

    private static List<OpcoesPerfil> opcoes;
    private static Context context;

    public OpcoesPerfilAdapter(List<OpcoesPerfil> opcoes, Context context) {
        this.opcoes = opcoes;
        this.context = context;
    }

    @Override
    public ViewHolderOpcoes onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_icon_opcoes_perfi, null, false);
        return new ViewHolderOpcoes(view);
    }

    @Override
    public void onBindViewHolder(ViewHolderOpcoes holder, int position) {
        final OpcoesPerfil opcao = opcoes.get(position);

        holder.icon.setImageResource(opcao.getIcon());
        holder.descOpcao.setText(opcao.getNomeOpcao());

        if (position == opcoes.size() - 1){
            holder.divider.setVisibility(View.INVISIBLE);
        }

        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;

                if (opcao.getNomeOpcao().equals("Meus Pedidos")){
                    intent = new Intent(context, MeusPedidosActivity.class);
                    context.startActivity(intent);
                    ((Activity)context).finish();

                } else if(opcao.getNomeOpcao().equals("Endereços de Entrega")){

                    intent = new Intent(context, EnderecosActivity.class);
                    context.startActivity(intent);
                    ((Activity)context).finish();
                } else { //Sair
                    FirebaseAuth.getInstance().signOut();
                    intent = new Intent(context, TelaInicialActivity.class);
                    context.startActivity(intent);
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
            icon = (ImageView) itemView.findViewById(R.id.icon_opcoes_perfil);
            descOpcao = (TextView) itemView.findViewById(R.id.desc_opcoes_perfil);
            divider = itemView.findViewById(R.id.divider_opcoes_perfil);
        }
    }
}