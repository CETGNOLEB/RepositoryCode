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

public class LoginActivity extends AppCompatActivity {

    EditText editEmail;
    EditText editSenha;
    Button btEntrar;

    Button btGoCadUsuario;
    ProgressDialog mProgressDialog;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);

        mAuth = FirebaseAuth.getInstance();

        initViews();


        btGoCadUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this, CadastrarUsuarioActivity.class);
                startActivity(i);
                finish();
            }
        });

        btEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editEmail.getText().toString();
                String senha = editSenha.getText().toString();

                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(senha)) { //verifica se os campos estão vazios
                    Toast.makeText(LoginActivity.this, "Informe seu email e senha!", Toast.LENGTH_SHORT).show();
                } else {
                    mProgressDialog.setTitle("Entrando");
                    mProgressDialog.setMessage("Aguarde, validando seus dados...");
                    mProgressDialog.setCanceledOnTouchOutside(false);
                    mProgressDialog.show();
                    startSingIn(email, senha);
                }
            }
        });

    }

    private void initViews() {
        mProgressDialog = new ProgressDialog(this);
        editEmail = (EditText) findViewById(R.id.login_email);
        editSenha = (EditText) findViewById(R.id.login_senha);
        btEntrar = (Button) findViewById(R.id.bt_entrar);
        btGoCadUsuario = (Button) findViewById(R.id.bt_init_cad_usuario);
    }

    private void startSingIn(String email, String senha) {

        mAuth.signInWithEmailAndPassword(email, senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    mProgressDialog.dismiss();
                    Intent intent = new Intent(LoginActivity.this, CardapioMainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                } else {
                    mProgressDialog.hide();
                    Toast.makeText(LoginActivity.this, "Usuário não encontrado!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}
