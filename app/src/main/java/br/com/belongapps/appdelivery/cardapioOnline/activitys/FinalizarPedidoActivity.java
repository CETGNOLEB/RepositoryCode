package br.com.belongapps.appdelivery.cardapioOnline.activitys;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import br.com.belongapps.appdelivery.R;
import br.com.belongapps.appdelivery.cardapioOnline.adapters.FormasdePagamentoAdapter;
import br.com.belongapps.appdelivery.cardapioOnline.dao.CarrinhoDAO;
import br.com.belongapps.appdelivery.cardapioOnline.dao.FinalizarPedidoDAO;
import br.com.belongapps.appdelivery.cardapioOnline.model.Cliente;
import br.com.belongapps.appdelivery.cardapioOnline.model.FormadePagamento;
import br.com.belongapps.appdelivery.cardapioOnline.model.ItemPedido;
import br.com.belongapps.appdelivery.cardapioOnline.model.Pagamento;
import br.com.belongapps.appdelivery.cardapioOnline.model.Pedido;
import br.com.belongapps.appdelivery.firebaseAuthApp.FirebaseAuthApp;
import br.com.belongapps.appdelivery.gerencial.model.Bairro;
import br.com.belongapps.appdelivery.gerencial.model.Endereco;
import br.com.belongapps.appdelivery.util.ConexaoUtil;
import br.com.belongapps.appdelivery.util.DataUtil;
import br.com.belongapps.appdelivery.util.Print;
import br.com.belongapps.appdelivery.util.StringUtil;

public class FinalizarPedidoActivity extends AppCompatActivity {

    private Toolbar mToolbar;

    //Views Pagamento
    private TextView tvtaxadeEntrega;
    private TextView txtTotalPedido;
    private TextView txtTotalDosItens;
    private Button finalizarPedido;
    private double taxadeEntrega;

    private double totaldoPedido; //parâmetro recebido

    //ESCOLHER FORMA DE PAGAMENTO
    private RecyclerView mRecyclerViewFormasdePagamento;
    private RecyclerView.Adapter adapter;

    private List<FormadePagamento> formasDePagamento;

    private DatabaseReference database = FirebaseDatabase.getInstance().getReference();

    private Pedido pedido = new Pedido();

    //  STATUS FORMA PAGAMENTO
    private boolean statusDinheiro = false;
    private boolean statusCartao = false;
    private boolean statusDinheiroCartao = false;
    private List<FormadePagamento> formasDePagamentoaux;

    private ProgressDialog mProgressDialog;

    // ENDEREÇO
    private List<Endereco> enderecos;
    private Endereco endereco;
    private List<String> bairrosNomes;
    private List<String> enderecosNomes;

    //Views Endereço
    private TextView textRua, tvRuaEndereco; //Rua
    private TextView textNumero, tvNumeroEndereco; //Numero
    private TextView textBairro, tvBairroEndereco; // Bairro
    private GridLayout glEndereco;
    private Button btAlterarEndereco;
    private Button btcadastrarEndereco;

    //View Cadastro Endereço
    private EditText rua, numero, nome, complemento;
    private Spinner bairroSpinner;
    private String bairro;
    private Endereco enderecoAuxAlterar;

    //View Editar Endereço
    private TextView aeTvRuaEndereco, aeTvNumeroEndereco, aeTvBairroEndereco;
    private Spinner enderecoSpinner;

    private Button btVoltarFinalizar;

    private Cliente cliente;
    private Boolean statusDelivery = true;
    private Boolean statusEstabelecimento = true;

    private Boolean entregaGratis = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finalizar_pedido);

        mToolbar = (Toolbar) findViewById(R.id.toolbar_enviar_pedido);
        mToolbar.setTitle("Finalizar Pedido");

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Recebe Valor total do pedido
        totaldoPedido = getIntent().getDoubleExtra("totalPedido", 0);

        populateViewValores();

        finalizarPedido = findViewById(R.id.bt_finalizar_pedido);

        finalizarPedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validarEnvioDoPedido();
            }
        });

        btVoltarFinalizar = findViewById(R.id.bt_voltar_finalizar);
        btVoltarFinalizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(FinalizarPedidoActivity.this, CardapioMainActivity.class);
                startActivity(i);
                finish();
            }
        });

        //Cadastrar Novo Endereço
        btcadastrarEndereco = findViewById(R.id.cadastrar_endereco);

        btAlterarEndereco = findViewById(R.id.bt_alterar_endereco);
        btcadastrarEndereco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cadastrarEndereco();
            }
        });

        //Alterar o endereço
        btAlterarEndereco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alterarEndereco();
            }
        });

        populateFormasdePagamento();

    }

    public void validarEnvioDoPedido() {

        /*Verificar Conexão*/
        if (!ConexaoUtil.verificaConectividade(FinalizarPedidoActivity.this)) {

            exibirDilogSemConexao();

        } else if (!statusEstabelecimento) { //Estabelecimento Fechado

            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            AlertDialog.Builder mBilder = new AlertDialog.Builder(FinalizarPedidoActivity.this, R.style.MyDialogTheme);
            View layoutDialog = inflater.inflate(R.layout.dialog_estabelecimento_fechado, null);

            Button btEntendi = layoutDialog.findViewById(R.id.bt_entendi_estabeleciemento_fechado);

            mBilder.setView(layoutDialog);
            final AlertDialog dialogEstabelecimentoFechado = mBilder.create();
            dialogEstabelecimentoFechado.show();

            btEntendi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogEstabelecimentoFechado.dismiss();
                }
            });

        } else if (!statusDelivery) { //Delivery Frechado

            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            AlertDialog.Builder mBilder = new AlertDialog.Builder(FinalizarPedidoActivity.this, R.style.MyDialogTheme);
            View layoutDialog = inflater.inflate(R.layout.dialog_delivery_fechado, null);

            TextView descDialogDeliveryFechado = layoutDialog.findViewById(R.id.desc_dialog_delivery_fechado);
            descDialogDeliveryFechado.setText("Nosso delivery está fechado, volte e selecione outra forma de recebimento!");

            Button btVoltar = layoutDialog.findViewById(R.id.bt_voltar_delivery_fechado);
            btVoltar.setVisibility(View.INVISIBLE);
            Button btContinuar = layoutDialog.findViewById(R.id.bt_continuar_delivery_fechado);
            btContinuar.setText("Entendi");

            mBilder.setView(layoutDialog);
            final AlertDialog dialogDeliveryFechado = mBilder.create();
            dialogDeliveryFechado.show();

            btContinuar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogDeliveryFechado.dismiss();
                }
            });

        } else { //Conectado

            boolean frmPagValida = verificarFormadePagamento();

            //verificar se endereço e form pag. selecionados
            if (frmPagValida && endereco != null) {
                try {
                    openProgressDialog(); //Exibir status de envio
                    iniciarEnvioDoPedido();
                    //new exibirDialogdeEnvio().execute((Void[]) null); //Executar dialog de status de envio]
                } catch (Exception e) {
                    e.printStackTrace();
                    //Exibir Dialog de Erro ao enviar o pedido
                    exibirErroAoEnviarPedido();
                }
            } else {
                naoEnviarPedido(frmPagValida);
            }
        }

    }

    private void openProgressDialog() {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setTitle("Enviando");
        mProgressDialog.setMessage("Aguarde, estamos enviando seu pedido...");
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();
    }

    private void iniciarEnvioDoPedido() {
        pedido = new Pedido();

        Date data = DataUtil.getCurrenteDate();
        String dataPedido = DataUtil.formatar(data, "dd/MM/yyyy HH:mm");
        String diaPedido = DataUtil.formatar(data, "ddMMyyyy");

        pedido.setData(dataPedido);
        pedido.setStatus_tempo("n" + DataUtil.formatar(data, "HH:mm"));
        pedido.setStatus(0);
        pedido.setEntrega_retirada(getIntent().getIntExtra("tipoEntrega", 0));
        pedido.setItens_pedido(getItensdoPedido());

        //setar apenas se a entrega for delivery
        if (pedido.getEntrega_retirada() == 0) {
            cliente.setRuaEndCliente(endereco.getRua());
            cliente.setNumeroEndCliente(endereco.getNumero());
            cliente.setBairroEndCliente(endereco.getBairro());
            cliente.setComplementoEndCliente(endereco.getComplemento());
        }



        pedido.setCliente(cliente); //Adicionar Cliente ao Pedido

        FormadePagamento formadePagamento = getFormaPagamento();

        Pagamento pagamento = new Pagamento();
        pagamento.setFormaPagamento(formadePagamento.getNome());


        if (entregaGratis || contemItemComEntragaGratis()) {
            pagamento.setValorTotal(totaldoPedido);
        } else {
            pagamento.setValorTotal(totaldoPedido + taxadeEntrega);

            //SALVAR TAXA DE ENTREGA
            pedido.setTaxa_entrega(taxadeEntrega);
        }

        pagamento.setDescricaoPagemento(formadePagamento.getDescricao());
        pagamento.setValorPago(formadePagamento.getValorDinheiro());

        pedido.setPagamento(pagamento);

        FinalizarPedidoDAO finalizarPedidoDAO = new FinalizarPedidoDAO(this, mProgressDialog);
        finalizarPedidoDAO.salvarPedido(pedido, diaPedido);

    }

    private boolean contemItemComEntragaGratis() {
        List<ItemPedido> itens_pedido;
        CarrinhoDAO crud = new CarrinhoDAO(getBaseContext());
        itens_pedido = crud.getAllItens();

        for (ItemPedido item : itens_pedido) {
            Print.logError("ITEM: " + item.getNome());
            Print.logError("ENTREGA GRATIS: " + item.getEntrega_gratis());

            if (item.getEntrega_gratis() == 1){
                return true;
            }
        }

        return false;
    }

    private void exibirDilogSemConexao() {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        AlertDialog.Builder mBilder = new AlertDialog.Builder(FinalizarPedidoActivity.this, R.style.MyDialogTheme);
        View layoutDialog = inflater.inflate(R.layout.dialog_sem_conexao, null);

        Button btEntendi = (Button) layoutDialog.findViewById(R.id.bt_entendi_sem_conexao);

        mBilder.setView(layoutDialog);
        final AlertDialog dialogSemConexao = mBilder.create();
        dialogSemConexao.show();

        btEntendi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogSemConexao.dismiss();
            }
        });
    }

    public void exibirErroAoEnviarPedido() {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        AlertDialog.Builder mBilder = new AlertDialog.Builder(this, R.style.MyDialogTheme);
        View layoutDialog = inflater.inflate(R.layout.dialog_erro_ao_enviar_pedido, null);
        initView(layoutDialog);

        Button btEntendi = (Button) layoutDialog.findViewById(R.id.bt_entendi_erro);

        mBilder.setView(layoutDialog);
        final AlertDialog dialogErro = mBilder.create();
        dialogErro.show();

        btEntendi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogErro.dismiss();
            }
        });
    }

    public void populateViewValores() {

        tvtaxadeEntrega = findViewById(R.id.taxa_de_entrega);
        tvtaxadeEntrega.setText("Taxa de Entrega: " + StringUtil.formatToMoeda(taxadeEntrega));

        txtTotalDosItens = findViewById(R.id.valor_pedido);
        txtTotalDosItens.setText("Valor do Pedido: " + StringUtil.formatToMoeda(totaldoPedido));

        txtTotalPedido = findViewById(R.id.valor_total_pedido);
        txtTotalPedido.setText("Subtotal: " + StringUtil.formatToMoeda(totaldoPedido + taxadeEntrega));

        if (entregaGratis || contemItemComEntragaGratis()) {
            tvtaxadeEntrega.setPaintFlags(tvtaxadeEntrega.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            txtTotalPedido.setText("Subtotal: " + StringUtil.formatToMoeda(totaldoPedido));
        }
    }

    public void populateFormasdePagamento() {

        formasDePagamentoaux = new ArrayList<>();

        database.child("configuracoes").child("forma_pagamento").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot forma : dataSnapshot.getChildren()) {
                    if (forma.getKey().equals("dinheiro")) {
                        statusDinheiro = forma.getValue(boolean.class);
                        if (statusDinheiro == true) {
                            FormadePagamento dinheiro = new FormadePagamento();
                            dinheiro.setCod(1);
                            dinheiro.setNome("DINHEIRO");
                            dinheiro.setDescricao("");
                            dinheiro.setImagem(R.drawable.ic_money);
                            dinheiro.setStatus(false);

                            formasDePagamentoaux.add(dinheiro);
                        }
                    } else if (forma.getKey().equals("cartao")) {
                        statusCartao = forma.getValue(boolean.class);
                        if (statusCartao == true) {
                            FormadePagamento cartao = new FormadePagamento();
                            cartao.setCod(2);
                            cartao.setNome("CARTÃO");
                            cartao.setDescricao("");
                            cartao.setImagem(R.drawable.ic_credit_card);
                            cartao.setStatus(false);

                            formasDePagamentoaux.add(cartao);
                        }
                    } else {
                        statusDinheiroCartao = forma.getValue(boolean.class);
                        if (statusDinheiroCartao == true) {
                            FormadePagamento dinheiroCartao = new FormadePagamento();
                            dinheiroCartao.setCod(3);
                            dinheiroCartao.setNome("DINHEIRO E CARTÃO");
                            dinheiroCartao.setDescricao("");
                            dinheiroCartao.setImagem(R.drawable.ic_credit_money);
                            dinheiroCartao.setStatus(false);


                            formasDePagamentoaux.add(dinheiroCartao);
                        }
                    }
                }

                formasDePagamento = new ArrayList<>();
                formasDePagamento.addAll(formasDePagamentoaux);
                formasDePagamentoaux = new ArrayList<>();

                Collections.sort(formasDePagamento, new FormadePagamento());

                if (entregaGratis || contemItemComEntragaGratis()) {
                    adapter = new FormasdePagamentoAdapter(formasDePagamento, FinalizarPedidoActivity.this, totaldoPedido);
                    mRecyclerViewFormasdePagamento.setAdapter(adapter);
                } else {
                    adapter = new FormasdePagamentoAdapter(formasDePagamento, FinalizarPedidoActivity.this, totaldoPedido + taxadeEntrega);
                    mRecyclerViewFormasdePagamento.setAdapter(adapter);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void buscarDadosdoCliente(String userID) {

        Log.println(Log.ERROR, "USUARIO:", userID);

        ValueEventListener dadosClienteListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Cliente c = new Cliente();
                c.setNomeCliente(dataSnapshot.child("nome").getValue(String.class));
                c.setCelular(dataSnapshot.child("celular").getValue(String.class));

                cliente = c;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };

        database.child("clientes").child(userID).addValueEventListener(dadosClienteListener);
    }

    public void naoEnviarPedido(boolean formadepagamento) {

        AlertDialog.Builder mBilder = new AlertDialog.Builder(FinalizarPedidoActivity.this, R.style.MyDialogTheme);
        View layoutDialog = getLayoutInflater().inflate(R.layout.dialog_entendi, null);

        mBilder.setView(layoutDialog);
        final AlertDialog dialogEscolhaFormPag = mBilder.create();
        dialogEscolhaFormPag.show();

        TextView textDialod = (TextView) layoutDialog.findViewById(R.id.text_dialog_entendi);

        if (formadepagamento == false && endereco == null) {
            textDialod.setText("Você precisa cadastrar um endereço e selecionar uma forma de pagamento.");
        } else if (formadepagamento == false) {
            textDialod.setText("Você precisa selecionar uma forma de pagamento.");
        } else {
            textDialod.setText("Você precisa cadastrar um endereço.");
        }


        Button btCancel = (Button) layoutDialog.findViewById(R.id.bt_entendi);


        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogEscolhaFormPag.dismiss();
            }
        });
    }

    public void cadastrarEndereco() {
        buscarBairros();
        exibirDilogAddEndereco();
    }

    public void alterarEndereco() {
        exibirDilogAlterarEndereco();
    }

    //Métodos Auxiliares

    public FormadePagamento getFormaPagamento() {
        FormadePagamento fpgm = new FormadePagamento();

        for (FormadePagamento forma :
                formasDePagamento) {
            if (forma.isStatus() == true) {
                fpgm = forma;
            }
        }

        return fpgm;
    }

    public boolean verificarFormadePagamento() {
        for (FormadePagamento fpgm : formasDePagamento) {
            if (fpgm.isStatus()) {
                return true;
            }
        }
        return false;
    }

    public List<ItemPedido> getItensdoPedido() {
        List<ItemPedido> itensAux;
        CarrinhoDAO crud = new CarrinhoDAO(getBaseContext());
        itensAux = crud.getAllItens();
        return itensAux;
    }

    public void buscarBairros() {

        final List<Bairro> bairros = new ArrayList<>();

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
            }
        };

        database.child("configuracoes").child("bairro_taxa").addValueEventListener(bairroListener);
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
                    Endereco enderecoAux = new Endereco();
                    enderecoAux.setRua(rua.getText().toString());
                    enderecoAux.setNumero(numero.getText().toString());
                    enderecoAux.setBairro(bairro);

                    if (complemento.getText().toString().isEmpty()) {
                        enderecoAux.setComplemento(null);
                    } else {
                        enderecoAux.setComplemento(complemento.getText().toString());
                    }

                    enderecoAux.setNome(nome.getText().toString());

                    salvarEndereco(enderecoAux, dialogCadastrarEndereco);

                } else {
                    Toast.makeText(FinalizarPedidoActivity.this, "Informe " + campos, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void exibirDilogAlterarEndereco() {

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        AlertDialog.Builder mBilder = new AlertDialog.Builder(this, R.style.MyDialogTheme);
        View layoutDialog = inflater.inflate(R.layout.dialog_alterar_endereco, null);
        initViewAlterarEndereco(layoutDialog);

        Button btAlterar = (Button) layoutDialog.findViewById(R.id.bt_alterar_endereco);
        Button btFechar = (Button) layoutDialog.findViewById(R.id.bt_fechar_alterar_endereco);

        mBilder.setView(layoutDialog);
        final AlertDialog dialogCadastrarEndereco = mBilder.create();
        dialogCadastrarEndereco.show();

        btFechar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogCadastrarEndereco.dismiss();
            }
        });

        btAlterar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogCadastrarEndereco.dismiss();
                endereco = enderecoAuxAlterar;
                atualizarViewEnderecoeTaxa(endereco);
                populateFormasdePagamento();
            }
        });
    }

    public void initView(View root) {

        rua = root.findViewById(R.id.rua_cad_endereco);
        numero = root.findViewById(R.id.numero_cad_endereco);
        complemento = root.findViewById(R.id.complemento_cad_endereco);
        nome = root.findViewById(R.id.nome_cad_endereco);

        bairroSpinner = root.findViewById(R.id.bairos_spinner);
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
    }

    public void initViewAlterarEndereco(final View root) {

        enderecoSpinner = root.findViewById(R.id.enderecos_spinner);
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, enderecos);
        enderecoSpinner.setAdapter(adapter);

        enderecoSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                enderecoAuxAlterar = (Endereco) adapterView.getItemAtPosition(position);
                updateViewsAlterarEndereco(root, enderecoAuxAlterar);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void updateViewsAlterarEndereco(View root, Endereco endereco) {

        aeTvRuaEndereco = root.findViewById(R.id.ae_tv_rua_endereco);
        aeTvRuaEndereco.setText(endereco.getRua());

        aeTvNumeroEndereco = root.findViewById(R.id.ae_tv_numero_endereco);
        aeTvNumeroEndereco.setText(endereco.getNumero());

        aeTvBairroEndereco = root.findViewById(R.id.ae_tv_bairro_endereco);
        aeTvBairroEndereco.setText(endereco.getBairro());

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

    public void salvarEndereco(final Endereco enderecoAux, final AlertDialog dialogCadastrarEndereco) {
        openProgressDialog("Cadastrando Endereço", "Aguarde, estamos cadastrando o endereço...");

        final String userID = FirebaseAuthApp.getUsuarioLogado().getUid();
        final DatabaseReference clienteRef = database.child("clientes").child(userID);

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
                Map<String, Object> enderecoValues = enderecoAux.toMap();
                Map<String, Object> childUpdatesEndereco = new HashMap<>();
                childUpdatesEndereco.put(key, enderecoValues);
                clienteRef.child("enderecos").updateChildren(childUpdatesEndereco);

                closeProgressDialog();

                endereco = enderecoAux;
                atualizarViewEnderecoeTaxa(enderecoAux);

                dialogCadastrarEndereco.dismiss();

                Toast.makeText(FinalizarPedidoActivity.this, "Endereço cadastrado com sucesso", Toast.LENGTH_SHORT).show();

                // Transaction completed
            }
        });

        /*String userId = usuarioLogado.getUid();

        DatabaseReference enderecoref = database.child("clientes").child(userId).child("enderecos"); //PEGAR ID DO USUÁRIO LOGADO
        String key = enderecoref.push().getKey();
        Map<String, Object> enderecoValues = enderecoAux.toMap();
        Map<String, Object> childUpdatesEndereco = new HashMap<>();
        childUpdatesEndereco.put("/clientes/" + userId + "/enderecos/" + key, enderecoValues); //PEGAR ID DO USUÁRIO LOGADO

        database.updateChildren(childUpdatesEndereco);*/
    }

    public void atualizarViewEnderecoeTaxa(Endereco endereco) {
        //taxa de entrega
        tvtaxadeEntrega = (TextView) findViewById(R.id.taxa_de_entrega);

        //Endereco
        if (endereco != null) {
            //Ocultar botão de cadastro
            btcadastrarEndereco.setVisibility(View.GONE);

            //Exibir Valores
            glEndereco.setVisibility(View.VISIBLE);
            btAlterarEndereco.setVisibility(View.VISIBLE); //Exibor

            textRua = (TextView) findViewById(R.id.text_rua);
            textRua.setVisibility(View.VISIBLE);

            tvRuaEndereco = (TextView) findViewById(R.id.tv_rua_endereco);
            tvRuaEndereco.setText(endereco.getRua());
            tvRuaEndereco.setVisibility(View.VISIBLE);

            textNumero = (TextView) findViewById(R.id.text_numero);
            textNumero.setVisibility(View.VISIBLE);

            tvNumeroEndereco = (TextView) findViewById(R.id.tv_numero_endereco);
            tvNumeroEndereco.setText(endereco.getNumero());
            tvNumeroEndereco.setVisibility(View.VISIBLE);

            textBairro = (TextView) findViewById(R.id.text_bairro);
            textBairro.setVisibility(View.VISIBLE);

            tvBairroEndereco = (TextView) findViewById(R.id.tv_bairro_endereco);
            tvBairroEndereco.setText(endereco.getBairro());
            tvBairroEndereco.setVisibility(View.VISIBLE);

            //Setar a Taxa
            buscarTaxaPeloBairro(endereco.getBairro());
        }
    }

    public void buscarTaxaPeloBairro(final String bairroAux) {
        final List<Bairro> bairroList = new ArrayList<>();

        database.child("configuracoes").child("bairro_taxa").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    for (DataSnapshot bairro : dataSnapshot.getChildren()) {
                        Bairro b = bairro.getValue(Bairro.class);
                        bairroList.add(b);
                    }
                } catch (Exception n) {
                }

                for (Bairro bairro : bairroList) {
                    if (bairro.getBairro().equals(bairroAux)) {
                        taxadeEntrega = bairro.getTaxa();
                        populateViewValores();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        verificarEntregaGratis();

        bairrosNomes = new ArrayList<>();
        totaldoPedido = getIntent().getDoubleExtra("totalPedido", 0);

        buscarStatusEstabelecimentoDelivery();
        buscarFormasdePagamento();
        buscarEnderecosdoUsuario();
        buscarBairros();
        populateFormasdePagamento();
        buscarDadosdoCliente(FirebaseAuthApp.getUsuarioLogado().getUid());

    }

    private void buscarStatusEstabelecimentoDelivery() {
        database = FirebaseDatabase.getInstance().getReference();

        database.child("configuracoes").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Boolean status = Boolean.parseBoolean(dataSnapshot.child("status_delivery").child("status").getValue().toString());
                statusDelivery = status;

                Boolean statusEstab = Boolean.parseBoolean(dataSnapshot.child("status_estabelecimento").child("status").getValue().toString());
                statusEstabelecimento = statusEstab;

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void buscarFormasdePagamento() {

        mRecyclerViewFormasdePagamento = (RecyclerView) findViewById(R.id.formas_de_pagamento);
        mRecyclerViewFormasdePagamento.setHasFixedSize(true);
        mRecyclerViewFormasdePagamento.setLayoutManager(new LinearLayoutManager(this));

        formasDePagamento = new ArrayList<>();

        database.child("configuracoes").child("forma_pagamento").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot forma : dataSnapshot.getChildren()) {
                    if (forma.getKey().equals("dinheiro")) {
                        statusDinheiro = forma.getValue(boolean.class);
                        if (statusDinheiro == true) {
                            FormadePagamento dinheiro = new FormadePagamento();
                            dinheiro.setNome("DINHEIRO");
                            dinheiro.setDescricao("");
                            dinheiro.setImagem(R.drawable.ic_money);
                            dinheiro.setStatus(false);

                            formasDePagamentoaux.add(dinheiro);
                        }
                    } else if (forma.getKey().equals("cartao")) {
                        statusCartao = forma.getValue(boolean.class);
                        if (statusCartao == true) {
                            FormadePagamento cartao = new FormadePagamento();
                            cartao.setCod(2);
                            cartao.setNome("CARTÃO");
                            cartao.setDescricao("");
                            cartao.setImagem(R.drawable.ic_credit_card);
                            cartao.setStatus(false);

                            formasDePagamentoaux.add(cartao);
                        }
                    } else {
                        statusDinheiroCartao = forma.getValue(boolean.class);
                        if (statusDinheiroCartao == true) {
                            FormadePagamento dinheiroCartao = new FormadePagamento();
                            dinheiroCartao.setCod(3);
                            dinheiroCartao.setNome("DINHEIRO E CARTÃO");
                            dinheiroCartao.setDescricao("");
                            dinheiroCartao.setImagem(R.drawable.ic_credit_money);
                            dinheiroCartao.setStatus(false);


                            formasDePagamentoaux.add(dinheiroCartao);
                        }
                    }


                }

                formasDePagamento = new ArrayList<>();
                formasDePagamento.addAll(formasDePagamentoaux);
                formasDePagamentoaux = new ArrayList<>();

                Collections.sort(formasDePagamento, new FormadePagamento());

                adapter = new FormasdePagamentoAdapter(formasDePagamento, FinalizarPedidoActivity.this, totaldoPedido + taxadeEntrega);
                mRecyclerViewFormasdePagamento.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void buscarEnderecosdoUsuario() {

        String userId = FirebaseAuthApp.getUsuarioLogado().getUid();

        btAlterarEndereco = (Button) findViewById(R.id.bt_alterar_endereco);
        glEndereco = (GridLayout) findViewById(R.id.gl_endereco);
        btcadastrarEndereco = (Button) findViewById(R.id.cadastrar_endereco);
        enderecos = new ArrayList<>();
        enderecosNomes = new ArrayList<>();

        ValueEventListener enderecoListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                try {
                    for (DataSnapshot endereco : dataSnapshot.getChildren()) {
                        Endereco e = endereco.getValue(Endereco.class);
                        enderecos.add(e);
                        enderecosNomes.add(e.getNome());
                    }

                } catch (Exception n) {
                }

                if (enderecos.size() > 0) {
                    endereco = enderecos.get(0);
                }

                atualizarViewEnderecoeTaxa(endereco);
                populateFormasdePagamento();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };

        database.child("clientes").child(userId).child("enderecos").addValueEventListener(enderecoListener); //PEGAR ID DO USUÁRIO
    }

    private void verificarEntregaGratis() {
        ValueEventListener entregaGratisListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Boolean taxaGratis = dataSnapshot.child("taxa_gratis").getValue(Boolean.class);
                entregaGratis = taxaGratis;

                populateViewValores();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };

        database.child("configuracoes").child("taxa_entrega").addValueEventListener(entregaGratisListener);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(FinalizarPedidoActivity.this, CarrinhoActivity.class);
        startActivity(intent);

        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(FinalizarPedidoActivity.this, CarrinhoActivity.class);
                startActivity(intent);
                finish();
        }

        return super.onOptionsItemSelected(item);
    }

    //OPEN/CLOSE PROGRESS DIALOG
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
}