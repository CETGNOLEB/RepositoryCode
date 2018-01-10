package br.com.belongapps.appdelivery.firebaseAuthApp;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FirebaseAuthApp {

    private static FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private static Integer permite_pedidos = 1;

    public static FirebaseUser getUsuarioLogado() {

        return mAuth.getInstance().getCurrentUser();
    }

    public static boolean podeFazerPedidos() {

        DatabaseReference mDatabaseReference = FirebaseDatabase.getInstance().getReference();


        ValueEventListener permisaoValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Integer permisao = dataSnapshot.child("permite_ped").getValue(Integer.class);
                if (permisao != null) {
                    permite_pedidos = permisao;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        mDatabaseReference.child("clientes").child(getUsuarioLogado().getUid()).addValueEventListener(permisaoValueEventListener);

        if (permite_pedidos == 0){
            return false;
        }

        return true;
    }

}
