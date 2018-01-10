package br.com.belongapps.appdelivery.mensagens.service;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import br.com.belongapps.appdelivery.R;
import br.com.belongapps.appdelivery.firebaseAuthApp.FirebaseAuthApp;

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    private static final String TAG = "MyFirebaseIIDService";

    /**
     * Chamado se o token InstanceID for atualizado.
     * Isso pode ocorrer se a segurança de o token anterior tinha sido comprometido.
     * Observe que isso é chamado quando o token do InstanceID é gerado inicialmente, então é aqui que é recuperado o token.
     */
    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Atualizar token: " + refreshedToken);

        if (FirebaseAuthApp.getUsuarioLogado() != null) {
            sendRegistrationToServer(refreshedToken);
        }
    }


    /**
     * Modifique este método para associar o token FCM InstanceID do usuário com qualquer conta do lado do servidor
     * mantida pelo seu aplicativo.
     *
     * @param token Novo Token.
     */
    private void sendRegistrationToServer(String token) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child(getString(R.string.dbnode_users))
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child(getString(R.string.field_messaging_token))
                .setValue(token);
    }
}













