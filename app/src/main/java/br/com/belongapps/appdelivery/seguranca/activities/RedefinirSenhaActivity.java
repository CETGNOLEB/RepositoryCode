package br.com.belongapps.appdelivery.seguranca.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import br.com.belongapps.appdelivery.R;

public class RedefinirSenhaActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private TextView email_para_redefinir_senha;
    private Button btRedefinirSenha;
    private FirebaseAuth mFirebaseAuth;
    private ProgressDialog mProgressDialog;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redefinir_senha);

        mToolbar = findViewById(R.id.toolbar_redefinir_senha);
        mToolbar.setTitle("Redefinir Senha");

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mFirebaseAuth = FirebaseAuth.getInstance();

        getParametros();
        initViews();
    }

    private void getParametros() {
        email = getIntent().getStringExtra("email");
    }

    private void initViews() {
        email_para_redefinir_senha = findViewById(R.id.email_para_redefinir_senha);
        if (email != null) {
            email_para_redefinir_senha.setText(email);
        }

        btRedefinirSenha = findViewById(R.id.bt_redefinir_senha);

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setTitle("Aguarde");
        mProgressDialog.setMessage("Redefinindo sua senha...");
        mProgressDialog.setCanceledOnTouchOutside(false);

        btRedefinirSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mProgressDialog.show();
                redefinirSenhaDoUsuario();
            }
        });
    }

    private void redefinirSenhaDoUsuario() {
        final String email = email_para_redefinir_senha.getText().toString().toLowerCase().trim();
        mFirebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                mProgressDialog.dismiss();
                mProgressDialog.hide();
                email_para_redefinir_senha.setText("");

                if (task.isSuccessful()) {
                    //MOstrar dialog de sucesso
                    LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                    AlertDialog.Builder mBilder = new AlertDialog.Builder(RedefinirSenhaActivity.this, R.style.MyDialogTheme);
                    View layoutDialog = inflater.inflate(R.layout.dialog_email_enviado, null);

                    Button btEntendi = layoutDialog.findViewById(R.id.bt_entendi_email_enviado);
                    TextView tvTitle = layoutDialog.findViewById(R.id.title_dialog_email_enviado);

                    tvTitle.setText("Enviamos um e-mail para: " + email + ", acesse o link contido no e-mail para redefinir a sua senha!");

                    mBilder.setView(layoutDialog);
                    final AlertDialog dialogemailEnviado = mBilder.create();
                    dialogemailEnviado.show();

                    btEntendi.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialogemailEnviado.dismiss();
                        }
                    });

                } else {
                    Toast.makeText(RedefinirSenhaActivity.this, "Desculpe, não conseguimos redefinir sua senha, tente novamente! Verifique o email informado", Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(RedefinirSenhaActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(RedefinirSenhaActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
        }

        return super.onOptionsItemSelected(item);
    }


}
