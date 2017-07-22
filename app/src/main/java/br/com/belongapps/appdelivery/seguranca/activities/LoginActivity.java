package br.com.belongapps.appdelivery.seguranca.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import br.com.belongapps.appdelivery.R;
import br.com.belongapps.appdelivery.cardapioOnline.activitys.CardapioMainActivity;
import br.com.belongapps.appdelivery.promocoes.activities.TelaInicialActivity;

public class LoginActivity extends AppCompatActivity {

    EditText editEmail;
    EditText editSenha;
    Button btEntrar;
    ProgressDialog mProgressDialog;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        mProgressDialog = new ProgressDialog(this, R.style.AppCompatAlertDialogStyle);

        editEmail = (EditText) findViewById(R.id.input_email);
        editSenha = (EditText) findViewById(R.id.input_senha);
        btEntrar = (Button) findViewById(R.id.button_entrar);

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Intent i = new Intent(LoginActivity.this, CardapioMainActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        };

        btEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSingIn();

            }
        });
    }

    private void startSingIn() {
        String email = editEmail.getText().toString();
        String senha = editSenha.getText().toString();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(senha)) { //verifica se os campos estão vazios
            Toast.makeText(LoginActivity.this, "Informe seu email e senha!", Toast.LENGTH_SHORT).show();
        } else {

            mProgressDialog.setMessage("Entrando...");
            mProgressDialog.show();

            mAuth.signInWithEmailAndPassword(email, senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (!task.isSuccessful()) {

                        mProgressDialog.dismiss();

                        Toast.makeText(LoginActivity.this, "Usuário não encontrado!", Toast.LENGTH_SHORT).show();

                    }
                }
            });

        }

    }

    @Override
    protected void onStart() {
        super.onStart();

        mAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthStateListener != null) {
            mAuth.removeAuthStateListener(mAuthStateListener);
        }
    }
}
