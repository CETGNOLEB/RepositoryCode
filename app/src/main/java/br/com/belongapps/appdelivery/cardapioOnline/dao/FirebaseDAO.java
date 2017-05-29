package br.com.belongapps.appdelivery.cardapioOnline.dao;


import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.belongapps.appdelivery.cardapioOnline.model.Pedido;

import static android.content.ContentValues.TAG;

public class FirebaseDAO {

    private static DatabaseReference database = FirebaseDatabase.getInstance().getReference();

    static List<String> numerosdosPedidos = new ArrayList<>();
    String numero = "0001";

    private void enviarPedido(Pedido pedido) {

        String key = database.child("pedidos").push().getKey();
        Map<String, Object> postValues = pedido.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/pedidos/" + key, postValues);

        database.updateChildren(childUpdates);

        DatabaseReference mChildRef = database.child("pedidos").child(key).push();

        /*for (ItemPedido item:
             itens_pedidos) {
            mChildRef.setValue(item);
        }*/
    }

    public static void ultimoPedidoDoDia(String dia) {

        dia = dia.replace("/", "");
        Log.println(Log.ERROR, "Dia: ", dia);
        String numero = "0001";

        database.child("pedidos").child(dia).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        ArrayList<String> list = new ArrayList<String>();

                        for (DataSnapshot pedido : dataSnapshot.getChildren()) {
                            String numpedido = pedido.child("numero_pedido").getValue().toString();
                            list.add(numpedido);
                        }

                        if (numerosdosPedidos.isEmpty()){

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w(TAG, "getUser:onCancelled", databaseError.toException());
                    }
                });

    }

    public static String updateList(List<String> pedidos){
        numerosdosPedidos = pedidos;

        if (numerosdosPedidos.isEmpty()){
            return "0001";
        }

        return numerosdosPedidos.get(numerosdosPedidos.size() - 1);
    }
}