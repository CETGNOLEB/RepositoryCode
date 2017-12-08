package br.com.belongapps.appdelivery.firebaseAuthApp;

import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FirebaseAuthApp {

    public static FirebaseAuth mAuth = FirebaseAuth.getInstance();

    public static FirebaseUser getUsuarioLogado(){

        return mAuth.getInstance().getCurrentUser();
    }

    public static boolean podeFazerPedidos(){

        final boolean[] retorno = {true};

        DatabaseReference mDatabaseReference = FirebaseDatabase.getInstance().getReference();

        mDatabaseReference.child("clientes").child(getUsuarioLogado().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Boolean status = Boolean.parseBoolean(dataSnapshot.child("permite_ped").getValue().toString());

                retorno[0] = status;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return retorno[0];
    }

}
