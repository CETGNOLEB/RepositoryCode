package br.com.belongapps.appdelivery.seguranca.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;

import br.com.belongapps.appdelivery.R;
import br.com.belongapps.appdelivery.cardapioOnline.activitys.CardapioMainActivity;
import br.com.belongapps.appdelivery.util.DataUtil;

public class CadastrarUsuarioActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private TextView nome;
    private TextView email;
    private TextView senha;
    private TextView celular;
    private Button btCadastrarUsuario;

    private ProgressDialog mCadProgress;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_usuario);

        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("clientes");

        mCadProgress = new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();

        mToolbar = (Toolbar) findViewById(R.id.toolbar_cadastar_usuario);
        mToolbar.setTitle("Cadastrar Usuário");

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initViews();

    }

    public void initViews() {
        nome = (TextView) findViewById(R.id.cad_usuario_nome);
        email = (TextView) findViewById(R.id.cad_usuario_email);
        senha = (TextView) findViewById(R.id.cad_usuario_senha);
        celular = (TextView) findViewById(R.id.cad_usuario_celular);

        btCadastrarUsuario = (Button) findViewById(R.id.bt_cad_usuario);

        btCadastrarUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String nomeUsuario = nome.getText().toString();
                String emailUsuario = email.getText().toString().trim();
                String senhaUsuario = senha.getText().toString().trim();
                String celularUsuario = celular.getText().toString().trim();

                if (!TextUtils.isEmpty(nomeUsuario) || TextUtils.isEmpty(emailUsuario) || TextUtils.isEmpty(senhaUsuario)) {
                    mCadProgress.setTitle("Cadastrando Usuário");
                    mCadProgress.setMessage("Aguarde um instante...");
                    mCadProgress.setCanceledOnTouchOutside(false);
                    mCadProgress.show();
                    cadastrarUsuario(nomeUsuario, emailUsuario, senhaUsuario, celularUsuario);
                }

            }
        });
    }

    private void cadastrarUsuario(final String nomeUsuario, String emailUsuario, String senhaUsuario, final String celularUsuario) {
        mAuth.createUserWithEmailAndPassword(emailUsuario, senhaUsuario).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    //Salvar no Banco
                    String userId = mAuth.getCurrentUser().getUid();
                    DatabaseReference referenceUserLogado = mDatabaseReference.child(userId);

                    referenceUserLogado.child("nome").setValue(nomeUsuario);
                    referenceUserLogado.child("data_cadastro").setValue(DataUtil.formatar(new Date(), "dd/MM/yyyy"));
                    referenceUserLogado.child("celular").setValue(celularUsuario);
                    referenceUserLogado.child("permite_ped").setValue(1);
                    referenceUserLogado.child("total_pedidos").setValue(0);
                    referenceUserLogado.child("total_enderecos").setValue(0);

                    mCadProgress.dismiss();

                    Intent intent = new Intent(CadastrarUsuarioActivity.this, CardapioMainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                } else {
                    mCadProgress.hide();
                    Toast.makeText(CadastrarUsuarioActivity.this, "Erro ao cadastrar usuário, o e-mail informado já está sendo usado!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(CadastrarUsuarioActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(CadastrarUsuarioActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

}
