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

import com.google.firebase.database.ChildEventListener;
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
import br.com.belongapps.appdelivery.posPedido.PedidoKey;
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

    private List<PedidoKey> pedidosRealizados;
    List<PedidoKey> pedidosRealizadosAux;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab_pedidos_realizados, container, false);

        mProgressBar = (ProgressBar) rootView.findViewById(R.id.progressbar_pedidos_realizados);

        return rootView;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onStart() {
        super.onStart();

        mPedidosRealizadosList = (RecyclerView) getActivity().findViewById(R.id.list_pedidos_realizados);
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

                        for (String key : keypedidosRealizados) {
                            Log.println(Log.ERROR, "KEY", key);
                        }

                        buscarPedidosdoCliente(keypedidosRealizados);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w(TAG, "getUser:onCancelled", databaseError.toException());
                        // ...
                    }
                });

    }

    private void buscarPedidosdoCliente(final List<String> keypedidosRealizados) {

        pedidosRealizadosAux = new ArrayList<>();
        pedidosRealizados = new ArrayList<>();

        for (final String key : keypedidosRealizados) {
            String mesAno = key.substring(0, 6);
            String dia = key.substring(7, 15);
            final String keyPedido = key.substring(16);

            Log.println(Log.ERROR, "mes", mesAno);
            Log.println(Log.ERROR, "dia", dia);
            Log.println(Log.ERROR, "key", keyPedido);

            ChildEventListener buscar = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    /* for (DataSnapshot data : dataSnapshot.getChildren()) {*/

                    Pedido pedido = dataSnapshot.getValue(Pedido.class);
                    Log.println(Log.ERROR, "keynachild", dataSnapshot.getKey());

                    if (dataSnapshot.getKey().equals(keyPedido)) {
                        PedidoKey pedidokey = new PedidoKey(pedido, key);
                        pedidosRealizadosAux.add(pedidokey);
                    }
                    /*}*/

                    pedidosRealizados.addAll(pedidosRealizadosAux);
                    pedidosRealizadosAux = new ArrayList<>();
                    preencherListView(pedidosRealizados);
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    pedidosRealizados = new ArrayList<>();
                    preencherListView(pedidosRealizados);
                    atualizarPedidos();
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };

            mDatabaseReferencePedidos.child("pedidos").child(mesAno).child(dia).addChildEventListener(buscar);
        }

    }

    public void atualizarPedidos(){

        pedidosRealizadosAux = new ArrayList<>();
        pedidosRealizados = new ArrayList<>();

        for (final String key : keypedidosRealizados) {
            String mesAno = key.substring(0, 6);
            String dia = key.substring(7, 15);
            final String keyPedido = key.substring(16);

            Log.println(Log.ERROR, "mes", mesAno);
            Log.println(Log.ERROR, "dia", dia);
            Log.println(Log.ERROR, "key", keyPedido);

            ValueEventListener buscarPedidos = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    for (DataSnapshot data : dataSnapshot.getChildren()) {

                        Pedido pedido = data.getValue(Pedido.class);
                        Log.println(Log.ERROR, "keynachild", data.getKey());

                        if (data.getKey().equals(keyPedido)) {
                            PedidoKey pedidokey = new PedidoKey(pedido, keyPedido);
                            pedidosRealizadosAux.add(pedidokey);
                        }
                    }

                    pedidosRealizados.addAll(pedidosRealizadosAux);
                    pedidosRealizadosAux = new ArrayList<>();
                    preencherListView(pedidosRealizados);
            }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };

            mDatabaseReferencePedidos.child("pedidos").child(mesAno).child(dia).addListenerForSingleValueEvent(buscarPedidos);
        }

    }

    public void preencherListView(List<PedidoKey> pedidos) {
        if (pedidos != null) {
            pedidosRealizados = new ArrayList<>();
            pedidosRealizados.addAll(pedidos);
            //pedidos = new ArrayList<>();

            Collections.sort(pedidosRealizados);

            adapter = new PedidosRealizadosAdapter(pedidosRealizados, getContext(), mProgressBar);
            mPedidosRealizadosList.setAdapter(adapter);
        }

    }

}
