package br.com.belongapps.appdelivery.gerencial.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.belongapps.appdelivery.R;
import br.com.belongapps.appdelivery.cardapioOnline.activitys.CardapioMainActivity;
import br.com.belongapps.appdelivery.gerencial.model.Bairro;
import br.com.belongapps.appdelivery.gerencial.model.Endereco;
import br.com.belongapps.appdelivery.seguranca.activities.LoginActivity;
import br.com.belongapps.appdelivery.util.ConexaoUtil;

public class EnderecosActivity extends AppCompatActivity {

    private Toolbar mToolbar;

    private DatabaseReference mDatabaseReference;
    private ProgressBar mProgressBar;
    private RecyclerView mEnderecoList;
    private List<Bairro> bairros;
    private List<String> bairrosNomes;

    private String bairro;

    //Views
    private EditText rua, numero, complemento, cep, nome;
    private Spinner bairroSpinner;

    private FirebaseAuth mAuth;
    private FirebaseUser usuarioLogado;

    //Nenhum endereco cadastrados
    private Button btEndEmpty;
    private View viewEmptyEndereco;

    //Status Conexão
    private CoordinatorLayout snakeBarLayout;
    private Snackbar snackbar;
    private boolean statusConexao;

    //Dialog de Progresso
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enderecos);

        mAuth = FirebaseAuth.getInstance();

        mToolbar = (Toolbar) findViewById(R.id.toolbar_enderecos);
        mToolbar.setTitle("Meus Endereços");

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(EnderecosActivity.this, CardapioMainActivity.class);
        startActivity(intent);

        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_enderecos, menu);
//        MenuItem menuAddEndereco = menu.getItem(0);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(EnderecosActivity.this, CardapioMainActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.adicionar_endereco:
                //abrir diálog
                exibirDilogAddEndereco();
        }

        return super.onOptionsItemSelected(item);
    }

    public void exibirDilogAddEndereco() {

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        AlertDialog.Builder mBilder = new AlertDialog.Builder(this, R.style.MyDialogTheme);
        View layoutDialog = inflater.inflate(R.layout.dialog_add_endereco, null);
        initView(layoutDialog);

        Button btCadastrar = (Button) layoutDialog.findViewById(R.id.bt_cad_endereco);
        Button btFechar = (Button) layoutDialog.findViewById(R.id.bt_fechar_car_endereco);

        mBilder.setView(layoutDialog);
        final AlertDialog dialogCadastrarEndereco = mBilder.create();
        dialogCadastrarEndereco.show();

        btFechar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogCadastrarEndereco.dismiss();
            }
        });

        btCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String campos = camposInvalidos();

                if (campos.equals("")) {
                    Endereco endereco = new Endereco();
                    endereco.setRua(rua.getText().toString());
                    endereco.setNumero(numero.getText().toString());
                    endereco.setBairro(bairro);

                    if (complemento.getText().toString().isEmpty()) {
                        endereco.setComplemento(null);
                    } else {
                        endereco.setComplemento(complemento.getText().toString());
                    }

                    endereco.setNome(nome.getText().toString());

                    salvarEndereco(dialogCadastrarEndereco, endereco);


                } else {
                    Toast.makeText(EnderecosActivity.this, "Informe " + campos, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void exibirDilogEditarEndereco(Endereco endereco, final String key) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        AlertDialog.Builder mBilder = new AlertDialog.Builder(this, R.style.MyDialogTheme);
        View layoutDialog = inflater.inflate(R.layout.dialog_add_endereco, null);
        initView(layoutDialog);

        prencherViews(endereco);

        Button btEditar = (Button) layoutDialog.findViewById(R.id.bt_cad_endereco);
        btEditar.setText("Editar");
        Button btFechar = (Button) layoutDialog.findViewById(R.id.bt_fechar_car_endereco);

        mBilder.setView(layoutDialog);
        final AlertDialog dialogCadastrarEndereco = mBilder.create();
        dialogCadastrarEndereco.show();

        btFechar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogCadastrarEndereco.dismiss();
            }
        });

        btEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String campos = camposInvalidos();

                if (campos.equals("")) {
                    Endereco endereco = new Endereco();
                    endereco.setRua(rua.getText().toString());
                    endereco.setNumero(numero.getText().toString());
                    endereco.setBairro(bairro);

                    if (complemento.getText().toString().isEmpty()) {
                        endereco.setComplemento(null);
                    } else {
                        endereco.setComplemento(complemento.getText().toString());
                    }

                    endereco.setNome(nome.getText().toString());

                    editarEndereco(endereco, key);

                    dialogCadastrarEndereco.dismiss();
                    Toast.makeText(EnderecosActivity.this, "Endereço editado com sucesso", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(EnderecosActivity.this, "Informe " + campos, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    public String camposInvalidos() {
        String campos = "";

        if (rua.getText().toString().equals("")) {
            campos += "a rua,";
        } else if (numero.getText().toString().equals("")) {
            campos += "o numero,";
        } else if (nome.getText().toString().equals("")) {
            campos += "o nome,";
        }

        if (campos.equals("")) {
            return campos;
        } else {
            return campos.substring(0, campos.length() - 1) + ".";
        }

    }

    public void salvarEndereco(final AlertDialog dialogCadastrarEndereco, final Endereco endereco) {

        openProgressDialog("Cadastrando Endereço", "Aguarde, estamos cadastrando o endereço...");

        final String userID = usuarioLogado.getUid();
        final DatabaseReference clienteRef = mDatabaseReference.child("clientes").child(userID);

        clienteRef.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {

                //ATUALIZA TOTAL DE ENDERECOS CADASTRADOS
                Integer total = mutableData.child("total_enderecos").getValue(Integer.class);

                if (total == null) {
                    return Transaction.success(mutableData);
                }

                total++;

                Log.println(Log.ERROR, "TOTAL", "Total: " + total);

                mutableData.child("total_enderecos").setValue(total);

                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b,
                                   DataSnapshot dataSnapshot) {

                //SALVA ENDEREÇO
                String key = clienteRef.child("enderecos").push().getKey();
                Map<String, Object> enderecoValues = endereco.toMap();
                Map<String, Object> childUpdatesEndereco = new HashMap<>();
                childUpdatesEndereco.put(key, enderecoValues);
                clienteRef.child("enderecos").updateChildren(childUpdatesEndereco);

                closeProgressDialog();

                dialogCadastrarEndereco.dismiss();

                Toast.makeText(EnderecosActivity.this, "Endereço cadastrado com sucesso", Toast.LENGTH_SHORT).show();

                buscarEndereços();

                // Transaction completed
            }
        });

    }

    public void editarEndereco(Endereco endereco, String key) {

        String userID = usuarioLogado.getUid();

        Map<String, Object> enderecoValues = endereco.toMap();
        Map<String, Object> childUpdatesEndereco = new HashMap<>();
        childUpdatesEndereco.put("/clientes/" + userID + "/enderecos/" + key, enderecoValues); //PEGAR ID DO USUÁRIO LOGADO

        mDatabaseReference.updateChildren(childUpdatesEndereco);
    }

    public void excluirEndereco(final String keyEndereco) {

        final String userID = usuarioLogado.getUid();

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        AlertDialog.Builder mBilder = new AlertDialog.Builder(EnderecosActivity.this, R.style.MyDialogTheme);
        View layoutDialog = inflater.inflate(R.layout.dialog_cofirm_delete_endereco, null);

        Button btConfirmar = (Button) layoutDialog.findViewById(R.id.bt_confirmar_excluir_endereco);
        Button btCancelar = (Button) layoutDialog.findViewById(R.id.bt_cancel_excluir_endereco);

        mBilder.setView(layoutDialog);
        final AlertDialog dialogExcluirEndereco = mBilder.create();
        dialogExcluirEndereco.show();

        btConfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                decrementarTotaldeEnderecosEExcluir(userID, keyEndereco);
                dialogExcluirEndereco.dismiss();
            }
        });

        btCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogExcluirEndereco.dismiss();
            }
        });

    }

    public void initView(View root) {
        rua = (EditText) root.findViewById(R.id.rua_cad_endereco);
        numero = (EditText) root.findViewById(R.id.numero_cad_endereco);
        complemento = (EditText) root.findViewById(R.id.complemento_cad_endereco);
        nome = (EditText) root.findViewById(R.id.nome_cad_endereco);


        bairroSpinner = (Spinner) root.findViewById(R.id.bairos_spinner);
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, bairrosNomes);
        bairroSpinner.setAdapter(adapter);

        bairroSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                bairro = adapterView.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        /*Dialog de Progresso*/
        mProgressDialog = new ProgressDialog(this);
    }

    public void prencherViews(Endereco endereco) {
        rua.setText(endereco.getRua());
        numero.setText(endereco.getNumero());
        bairroSpinner.setSelection(getPositionItemBairro(endereco.getBairro()));
        nome.setText(endereco.getNome());
    }

    public int getPositionItemBairro(String bairro) {
        for (int i = 0; i < bairrosNomes.size(); i++) {
            String s = bairrosNomes.get(i);
            if (s.equals(bairro)) {
                return i;
            }
        }

        return 0;
    }

    @Override
    protected void onStart() {
        super.onStart();

        usuarioLogado = mAuth.getCurrentUser();

        if (usuarioLogado == null) {
            Intent i = new Intent(EnderecosActivity.this, LoginActivity.class);
            startActivity(i);
            finish();

        } else { //Usuário Logado
            verificarEnderecosCadastrados();
            buscarBairros(); //Buscar Bairros para posterior cadastro de enderecos

        }

        verificaStatusDeConexao();

    }

    //Verifica se existe algum endereço cadastrado
    private void verificarEnderecosCadastrados() {
        viewEmptyEndereco = findViewById(R.id.view_empty_endereco);

        mDatabaseReference = FirebaseDatabase.getInstance().getReference(); //Start database reference

        mProgressBar = (ProgressBar) findViewById(R.id.progressbar_endereco);
        openProgressBar();

        //VERIFICAR ENDEREÇOS CADASTRADOS
        ValueEventListener enderecoListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                closeProgressBar();

                Integer totalEnderecos = dataSnapshot.child("total_enderecos").getValue(Integer.class);

                Log.println(Log.ERROR, "TOTAL END: ", "" + totalEnderecos);

                if (totalEnderecos != null) {
                    if (totalEnderecos == 0) { //Não tem nenhum endereço cadastrado

                        viewEmptyEndereco.setVisibility(View.VISIBLE);

                        btEndEmpty = (Button) findViewById(R.id.bt_sem_enderecos_cad_endereco);
                        btEndEmpty.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                exibirDilogAddEndereco();
                            }
                        });
                    } else { //Existem endereços cadastrados
                        buscarEndereços();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
            }
        };

        mDatabaseReference.child("clientes").child(usuarioLogado.getUid()).addListenerForSingleValueEvent(enderecoListener);

    }

    private void buscarEndereços() {
        viewEmptyEndereco.setVisibility(View.GONE);

        mEnderecoList = (RecyclerView) findViewById(R.id.list_enderecos);
        mEnderecoList.setHasFixedSize(true);
        mEnderecoList.setLayoutManager(new LinearLayoutManager(EnderecosActivity.this));

        final FirebaseRecyclerAdapter<Endereco, EnderecosActivity.EnderecoViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Endereco, EnderecosActivity.EnderecoViewHolder>(
                Endereco.class, R.layout.card_endereco, EnderecosActivity.EnderecoViewHolder.class, mDatabaseReference.child("clientes").child(usuarioLogado.getUid()).child("enderecos")
        ) {

            @Override
            protected void populateViewHolder(final EnderecosActivity.EnderecoViewHolder viewHolder, final Endereco model, int position) {
                closeProgressBar();

                final String enderecoKey = getRef(position).getKey();

                viewHolder.setNome(model.getNome());
                viewHolder.setRua(model.getRua());
                viewHolder.setNumero(model.getNumero());
                viewHolder.setBairro(model.getBairro());
                viewHolder.setComplemento(model.getComplemento());

                viewHolder.optionsMenu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //Exibir popupMen5u
                        PopupMenu popupMenu = new PopupMenu(EnderecosActivity.this, viewHolder.optionsMenu);
                        popupMenu.inflate(R.menu.menu_item_card_endereco);
                        popupMenu.setGravity(Gravity.RIGHT);

                        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                switch (item.getItemId()) {
                                    case R.id.item_cad_endereco_editar:
                                        //abrir popup e editar item
                                        exibirDilogEditarEndereco(model, enderecoKey);

                                        break;
                                    case R.id.item_cad_endereco_excluir:
                                        //Excluir endereco
                                        excluirEndereco(enderecoKey);
                                        break;
                                    default:
                                        break;
                                }

                                return false;
                            }
                        });

                        popupMenu.show();

                    }
                });
            }

        };

        mEnderecoList.setAdapter(firebaseRecyclerAdapter);
    }

    private void buscarBairros() {
        bairros = new ArrayList<>();
        bairrosNomes = new ArrayList<>();

        //ListarBairros
        ValueEventListener bairroListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                try {
                    for (DataSnapshot bairro : dataSnapshot.getChildren()) {
                        Bairro b = bairro.getValue(Bairro.class);
                        bairros.add(b);
                    }
                } catch (Exception n) {
                }

                for (Bairro bairro : bairros) {
                    bairrosNomes.add(bairro.getBairro());
                }
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
            }
        };

        mDatabaseReference.child("configuracoes").child("bairro_taxa").addValueEventListener(bairroListener);
    }

    public void decrementarTotaldeEnderecosEExcluir(final String userID, final String keyEndereco) {


        openProgressDialog("Excluindo", "Aguarde, estamos excluindo o endereço");

        DatabaseReference clienteRef = mDatabaseReference.child("clientes").child(userID);

        clienteRef.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {

                //ATUALIZA TOTAL DE ENDERECOS CADASTRADOS
                Integer total = mutableData.child("total_enderecos").getValue(Integer.class);

                if (total == null) {
                    return Transaction.success(mutableData);
                }

                total--;

                Log.println(Log.ERROR, "TOTAL", "Total: " + total);

                mutableData.child("total_enderecos").setValue(total);

                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b,
                                   DataSnapshot dataSnapshot) {

                /*EXCLUIR ENDEREÇO*/
                mDatabaseReference.child("clientes").child(userID).child("enderecos").child(keyEndereco).removeValue();
                Toast.makeText(EnderecosActivity.this, "Endereço deletado com sucesso", Toast.LENGTH_SHORT).show();

                closeProgressDialog();

                verificarEnderecosCadastrados();

                // Transaction completed
            }
        });
    }

    public void verificaStatusDeConexao() {
        snakeBarLayout = (CoordinatorLayout) findViewById(R.id.layout_snakebar_enderecos);

        snackbar = Snackbar
                .make(snakeBarLayout, "Sem conexão com a internet", Snackbar.LENGTH_INDEFINITE);

        statusConexao = ConexaoUtil.verificaConectividade(this);

        if (statusConexao) {
            snackbar.dismiss();
        } else {
            View snackView = snackbar.getView();
            snackView.setBackgroundColor(ContextCompat.getColor(EnderecosActivity.this, R.color.snakebarColor));
            snackbar.setActionTextColor(getResources().getColor(R.color.textColorPrimary));
            snackbar.show();
        }
    }

    public static class EnderecoViewHolder extends RecyclerView.ViewHolder {

        View mView;
        CardView card_endereco;
        TextView optionsMenu;

        public EnderecoViewHolder(final View itemView) {
            super(itemView);

            mView = itemView;
            card_endereco = (CardView) mView.findViewById(R.id.card_endereco);
            optionsMenu = (TextView) mView.findViewById(R.id.option_menu_endereco);

        }

        public void setNome(String nome) {

            TextView nomeEndereco = (TextView) mView.findViewById(R.id.nome_endereco);
            nomeEndereco.setText(nome);

        }

        public void setRua(String rua) {

            TextView ruaEndereco = (TextView) mView.findViewById(R.id.rua_endereco);
            ruaEndereco.setText(rua);

        }

        public void setNumero(String numero) {

            TextView numeroEndereco = (TextView) mView.findViewById(R.id.numero_endereco);
            numeroEndereco.setText(numero);
        }

        public void setBairro(String bairro) {

            TextView numeroEndereco = (TextView) mView.findViewById(R.id.bairro_endereco);
            numeroEndereco.setText(bairro);
        }

        public void setComplemento(String complemento) {

            TextView complementoEndereco = (TextView) mView.findViewById(R.id.complemento_endereco);

            if (complemento != null) {
                complementoEndereco.setText(complemento);
            } else {
                complementoEndereco.setText("Não informado");
            }

        }

    }

    private void exibirButtonAdicionar(MenuItem menuAddEndereco) {
        menuAddEndereco.setVisible(true);
    }

    private void ocultarButtonAdicionar(MenuItem menuAddEndereco) {
        menuAddEndereco.setVisible(false);
    }

    private void openProgressDialog(String title, String msg) {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setTitle(title);
        mProgressDialog.setMessage(msg);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();
    }

    private void closeProgressDialog() {
        mProgressDialog.dismiss();
    }

    private void openProgressBar() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    private void closeProgressBar() {
        mProgressBar.setVisibility(View.GONE);
    }
}
