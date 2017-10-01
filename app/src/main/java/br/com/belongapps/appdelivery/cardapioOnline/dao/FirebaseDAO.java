package br.com.belongapps.appdelivery.cardapioOnline.dao;


import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;

import java.util.Date;

import br.com.belongapps.appdelivery.util.DataUtil;

import static android.content.ContentValues.TAG;

public class FirebaseDAO {

    private static DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    private static FirebaseAuth mAuth;

    //ATUALIZAR MÓDULO FINANCEIRO
    public static void atualizarPedidosnaSemana(String mes, String hj, Date data) {
        String diaDaSemana = DataUtil.getDiadaSemana(data);

        DatabaseReference numPedidoRef = database.child("financeiro").child("pedidos_semana").child(mes).child(diaDaSemana);
        numPedidoRef.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                Integer total = mutableData.child("total").getValue(Integer.class);

                if (total == null) {
                    return Transaction.success(mutableData);
                }

                //ATUALIZAR TOTAL
                total++;
                mutableData.child("total").setValue(total); //Atualiza o total de pedidos realizados

                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b,
                                   DataSnapshot dataSnapshot) {
                // Transaction completed
                Log.d(TAG, "postTransaction:onComplete:" + databaseError);
            }
        });

    }

    public static void atualizarPedidosnoMes(String mes) {

        DatabaseReference numPedidoRef = database.child("financeiro").child("pedidos_mes").child(mes);
        numPedidoRef.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                Integer total = mutableData.child("total").getValue(Integer.class);

                if (total == null) {
                    return Transaction.success(mutableData);
                }

                //ATUALIZAR TOTAL
                total++;
                mutableData.child("total").setValue(total); //Atualiza o total de pedidos realizados

                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b,
                                   DataSnapshot dataSnapshot) {
                // Transaction completed
                Log.d(TAG, "postTransaction:onComplete:" + databaseError);
            }
        });

    }

    /*ATUALIZAR DADOS DO CLIENTE*/
    public static void atualizarNumeroPedidosdoCliente(String idDoUsuario) {

        DatabaseReference numPedidoRef = database.child("clientes").child(idDoUsuario);
        numPedidoRef.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                Integer total = mutableData.child("total_pedidos").getValue(Integer.class);

                if (total == null) {
                    return Transaction.success(mutableData);
                }

                //ATUALIZAR TOTAL DE PEDIDOS REALIZADOS
                total++;
                mutableData.child("total_pedidos").setValue(total); //Atualiza o total de pedidos realizados pelo cliente

                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b,
                                   DataSnapshot dataSnapshot) {
                Log.d(TAG, "postTransaction:onComplete:" + databaseError);
            }
        });

    }

    public static boolean temUsuarioLogado() {
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser usuarioLogado = mAuth.getCurrentUser();

        if (usuarioLogado == null) {
            return false;
        }

        return true;
    }


}