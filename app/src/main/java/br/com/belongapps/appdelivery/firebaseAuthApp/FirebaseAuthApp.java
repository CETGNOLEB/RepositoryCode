package br.com.belongapps.appdelivery.firebaseAuthApp;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FirebaseAuthApp {

    public static FirebaseAuth mAuth = FirebaseAuth.getInstance();

    public static FirebaseUser getUsuarioLogado(){

        return mAuth.getInstance().getCurrentUser();
    }

}
