package br.com.belongapps.appdelivery.posPedido.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import br.com.belongapps.appdelivery.R;
import br.com.belongapps.appdelivery.cardapioOnline.model.ItemPedido;
import br.com.belongapps.appdelivery.cardapioOnline.model.Pedido;
import br.com.belongapps.appdelivery.posPedido.adapters.PedidosRealizadosAdapter;

import static android.content.ContentValues.TAG;

public class TabPedidosRealizados extends Fragment {

    private RecyclerView mPedidosRealizadosList;
    private DatabaseReference mDatabaseReferenceClientes;
    private DatabaseReference mDatabaseReferencePedidos;
    private RecyclerView.Adapter adapter;

    private ProgressBar mProgressBar;

    private List<String> keypedidosRealizados;
    private List<String> keypedidosRealizadosAux;

    private List<Pedido> pedidosRealizados;
    List<Pedido> pedidosRealizadosAux;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab_pedidos_realizados, container, false);

        mProgressBar = (ProgressBar) rootView.findViewById(R.id.progressbar_pedidos_realizados);

        mPedidosRealizadosList = (RecyclerView) rootView.findViewById(R.id.list_pedidos_realizados);
        mPedidosRealizadosList.setHasFixedSize(true);
        mPedidosRealizadosList.setLayoutManager(new LinearLayoutManager(getActivity()));

        mDatabaseReferenceClientes = FirebaseDatabase.getInstance().getReference().child("clientes");
        mDatabaseReferencePedidos = FirebaseDatabase.getInstance().getReference();

        keypedidosRealizados = new ArrayList<>();
        keypedidosRealizadosAux = new ArrayList<>();

        final String userId = "1"; //PEGAR ID DO USUÀRIO LOGADO
        mDatabaseReferenceClientes.child(userId).child("pedidos").addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get user value
                        for (DataSnapshot cliente : dataSnapshot.getChildren()) {
                            for (DataSnapshot ids : cliente.getChildren()) {
                                String key = ids.getValue().toString();
                                keypedidosRealizadosAux.add(key);
                            }
                        }

                        keypedidosRealizados.addAll(keypedidosRealizadosAux);
                        keypedidosRealizadosAux = new ArrayList<>();

                        buscarPedidosdoCliente(keypedidosRealizados);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w(TAG, "getUser:onCancelled", databaseError.toException());
                        // ...
                    }
                });

        return rootView;

    }

    private void buscarPedidosdoCliente(final List<String> keypedidosRealizados) {

        pedidosRealizadosAux = new ArrayList<>();

        mDatabaseReferencePedidos.child("pedidos").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dias : dataSnapshot.getChildren()) {
                    for (DataSnapshot pedido : dias.getChildren()) {
                        for (String keyPedido : keypedidosRealizados) {
                            if (pedido.getKey().equals(keyPedido)) {
                                Pedido pedidoaux = pedido.getValue(Pedido.class);
                                pedidosRealizadosAux.add(pedidoaux);
                            }
                        }

                    }
                }

                pedidosRealizados = new ArrayList<>();
                pedidosRealizados.addAll(pedidosRealizadosAux);
                pedidosRealizadosAux = new ArrayList<>();

                Collections.reverse(pedidosRealizados);

                adapter = new PedidosRealizadosAdapter(pedidosRealizados, getContext(), mProgressBar);
                mPedidosRealizadosList.setAdapter(adapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onStart() {
        super.onStart();

    }
}
