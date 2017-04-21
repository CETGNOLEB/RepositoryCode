package br.com.belongapps.meuacai.posPedido.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import br.com.belongapps.meuacai.R;
import br.com.belongapps.meuacai.posPedido.activities.AcompanharPedidoActivity;

public class TabPedidosRealizados extends Fragment {

    Button acompanharPedido;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab_pedidos_realizados, container, false);

        return rootView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onStart() {
        super.onStart();

        acompanharPedido = (Button) getActivity().findViewById(R.id.bt_acompanhar_pedido);
        acompanharPedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), AcompanharPedidoActivity.class);
                startActivity(i);
                getActivity().finish();
            }
        });

    }
}
