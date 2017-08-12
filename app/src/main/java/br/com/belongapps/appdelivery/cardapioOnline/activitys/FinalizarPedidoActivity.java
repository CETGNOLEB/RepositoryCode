package br.com.belongapps.appdelivery.cardapioOnline.activitys;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
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
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.text.NumberFormat;
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
import br.com.belongapps.appdelivery.cardapioOnline.model.Cliente;
import br.com.belongapps.appdelivery.cardapioOnline.model.FormadePagamento;
import br.com.belongapps.appdelivery.cardapioOnline.model.ItemPedido;
import br.com.belongapps.appdelivery.cardapioOnline.model.ItemQtdPedido;
import br.com.belongapps.appdelivery.cardapioOnline.model.KeyPedido;
import br.com.belongapps.appdelivery.cardapioOnline.model.Pagamento;
import br.com.belongapps.appdelivery.cardapioOnline.model.Pedido;
import br.com.belongapps.appdelivery.gerencial.model.Bairro;
import br.com.belongapps.appdelivery.gerencial.model.Endereco;
import br.com.belongapps.appdelivery.posPedido.activities.AcompanharPedidoActivity;
import br.com.belongapps.appdelivery.util.DataUtil;
import br.com.belongapps.appdelivery.util.StringUtil;

import static android.content.ContentValues.TAG;

public class FinalizarPedidoActivity extends AppCompatActivity {

    private Toolbar mToolbar;

    //Views Pagamento
    private TextView tvtaxadeEntrega;
    private TextView txtTotalPedido;
    private TextView txtTotalDosItens;
    private Button finalizarPedido;
    private double taxadeEntrega;

    //Dialogs
    AlertDialog dialogPedidoEnviado;
    AlertDialog.Builder mBilder;

    private double totaldoPedido; //parâmetro recebido

    //ESCOLHER FORMA DE PAGAMENTO
    private RecyclerView mRecyclerViewFormasdePagamento;
    private RecyclerView.Adapter adapter;

    private List<FormadePagamento> formasDePagamento;

    DatabaseReference database = FirebaseDatabase.getInstance().getReference();

    private String numerodopedido = "";

    private Pedido pedido = new Pedido();

    //  STATUS FORMA PAGAMENTO
    private boolean statusDinheiro = false;
    private boolean statusCartao = false;
    private boolean statusDinheiroCartao = false;
    private List<FormadePagamento> formasDePagamentoaux;

    private ProgressDialog dialog;
    private ProgressBar mProgressBar;

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
    private TextView aeTextRua, aeTextNumero, aeTextBairro;
    private TextView aeTvRuaEndereco, aeTvNumeroEndereco, aeTvBairroEndereco;
    private Spinner enderecoSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finalizar_pedido);

        mBilder = new AlertDialog.Builder(FinalizarPedidoActivity.this);

        mToolbar = (Toolbar) findViewById(R.id.toolbar_enviar_pedido);
        mToolbar.setTitle("Finalizar Pedido");

        mProgressBar = (ProgressBar) findViewById(R.id.progressbar_finalizar_pedido);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Recebe Valor total do pedido
        totaldoPedido = getIntent().getDoubleExtra("totalPedido", 0);

        populateViewValores();

        finalizarPedido = (Button) findViewById(R.id.bt_finalizar_pedido);

        finalizarPedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enviarPedido();
            }
        });

        //Cadastrar Novo Endereço
        btcadastrarEndereco = (Button) findViewById(R.id.cadastrar_endereco);

        btAlterarEndereco = (Button) findViewById(R.id.bt_alterar_endereco);
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

    public void enviarPedido() {

        boolean frmPagValida = verificarFormadePagamento();

        //verificar o endereço

        if (frmPagValida && endereco != null) {
            realizarEnvioDoPedido();
           // Toast.makeText(FinalizarPedidoActivity.this, "Pode enviar", Toast.LENGTH_SHORT).show();
        } else {
            naoEnviarPedido(frmPagValida);
        }
    }

    private void realizarEnvioDoPedido() {
        pedido = new Pedido();

        Date data = DataUtil.getCurrenteDate();
        String dataPedido = DataUtil.formatar(data, "dd/MM/yyyy HH:mm");
        String diaPedido = DataUtil.formatar(data, "ddMMyyyy");

        pedido.setData(dataPedido);
        pedido.setStatus_tempo("n" + DataUtil.formatar(data, "HH:mm"));
        pedido.setNumero_pedido(gerarNumeroPedido(numerodopedido));
        pedido.setStatus(0);
        pedido.setEntrega_retirada(getIntent().getIntExtra("tipoEntrega", 0));
        pedido.setItens_pedido(getItensdoPedido());

        //Pegar usuário logado
        Cliente cliente = new Cliente();
        cliente.setNomeCliente("Thiago Oliveira");

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
        pagamento.setValorTotal(totaldoPedido);
        pagamento.setDescricaoPagemento(formadePagamento.getDescricao());

        pedido.setPagamento(pagamento);

        new exibirDialogdeEnvio().execute((Void[]) null); //Executar dialog de status de envio
        salvarPedido(pedido, diaPedido);

    }


    /*METÓDOS PARA ENVIO DO PEDIDO -----------------------*/

    private void salvarPedido(Pedido pedido, String hj) {
        String key = database.child("pedidos").push().getKey();
        Pedido pedidoAux = pedido;
        Map<String, Object> pedidoValues = pedidoAux.toMap();

        Log.println(Log.ERROR, "NODO:", hj);

        Map<String, Object> childUpdatesPedido = new HashMap<>();
        childUpdatesPedido.put("/pedidos/" + StringUtil.mesdoPedido(hj) + "/" + key, pedidoValues);

        database.updateChildren(childUpdatesPedido);

        //ATUALIZA PEDIDOS DO USUÁRIO LOGADO
        atualizarPedidosdoCliente(key);

        //ATUALIZAR NÚMERO ITENS PEDIDO
        buscarTotaldePedidosDoItem(pedido.getItens_pedido());

        //LIMPAR CARRINHO
       CarrinhoDAO dao = new CarrinhoDAO(this);
        dao.deleteAll();
    }


    public void atualizarPedidosdoCliente(String keyPedido) {
        KeyPedido keyp = new KeyPedido(keyPedido);
        keyp.setId(keyPedido);

        String key = database.child("clientes").child("1").push().getKey();//pegar id do usuário logado

        database.child("clientes").child("1").child("pedidos").child(key).setValue(keyp);
    }

    public void buscarTotaldePedidosDoItem(final List<ItemPedido> itensdoPedido){

        final List<ItemQtdPedido> itensqtd = new ArrayList<>();

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (ItemPedido item : itensdoPedido) {

                    if (item.getKeyItem() != null) {
                        //recupera qtd pedido do item
                        Integer qtdPedido = dataSnapshot.child(item.getCategoria()).child(item.getKeyItem()).child("qtdPedido").getValue(Integer.class);

                        if (qtdPedido == null) {
                            qtdPedido = 0;
                        }

                        ItemQtdPedido itemQtd = new ItemQtdPedido(item.getCategoria(), item.getKeyItem(), qtdPedido, item.getQuantidade());
                        itensqtd.add(itemQtd);
                    }
                }

                incrementarTotalPedidodosItens(this, itensqtd);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        database.child("itens_cardapio").addValueEventListener(valueEventListener);
    }

    public void incrementarTotalPedidodosItens(ValueEventListener event, List<ItemQtdPedido> itensQtd){

        database.child("itens_cardapio").removeEventListener(event);

        /*USAR TRANSAÇÃO*/
        for (ItemQtdPedido item: itensQtd) {

            if (item.getKeyItem() != null) {
                database.child("itens_cardapio")
                        .child(item.getKeycategoria())
                        .child(item.getKeyItem())
                        .child("qtdPedido")
                        .setValue(item.getQtdPedido() + item.getQuantidade());
            }
        }
    }

    private class exibirDialogdeEnvio extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ProgressDialog(FinalizarPedidoActivity.this, R.style.AppCompatAlertDialogStyle);
            dialog.setMessage("Estamos enviando seu pedido!!");
            dialog.show();
        }

        protected Void doInBackground(Void... param) {
            try {
                Thread.currentThread();
                Thread.sleep(4000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Void param) {
            dialog.dismiss();

            View layoutDialog = exibirDialogPedidoEnviado();
            mBilder.setView(layoutDialog);
            dialogPedidoEnviado = mBilder.create();
            dialogPedidoEnviado.show();
        }
    }

    private View exibirDialogPedidoEnviado() {
        mBilder = new AlertDialog.Builder(FinalizarPedidoActivity.this);
        View layoutDialog = getLayoutInflater().inflate(R.layout.dialog_pedido_finalizado, null);

        Button bt_ok = (Button) layoutDialog.findViewById(R.id.bt_ok_pedido_enviado);
        Button acompanhar = (Button) layoutDialog.findViewById(R.id.bt_acompanhar_pedido_enviado);

        bt_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(FinalizarPedidoActivity.this, CardapioMainActivity.class);
                startActivity(i);
                finish();
            }
        });

        acompanhar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(FinalizarPedidoActivity.this, AcompanharPedidoActivity.class);
                i.putExtra("NumeroPedido", pedido.getNumero_pedido());
                i.putExtra("DataPedido", DataUtil.getDataPedido(pedido.getData()));
                i.putExtra("HoraPedido", DataUtil.getHoraPedido(pedido.getData()));
                i.putExtra("ValorPedido", totaldoPedido);
                i.putExtra("StatusPedido", pedido.getStatus());
                i.putExtra("TipoEntrega", pedido.getEntrega_retirada());
                i.putExtra("StatusTempo", pedido.getStatus_tempo());

                ArrayList<ItemPedido> itens = new ArrayList<>();
                for (ItemPedido item : pedido.getItens_pedido()) {
                    itens.add(item);
                }
                i.putParcelableArrayListExtra("ItensPedido", itens);

                startActivity(i);
                finish();

            }
        });

        return layoutDialog;
    }

    public void populateViewValores() {

        tvtaxadeEntrega = (TextView) findViewById(R.id.taxa_de_entrega);
        tvtaxadeEntrega.setText("Taxa de Entrega: " + StringUtil.formatToMoeda(taxadeEntrega));

        txtTotalDosItens = (TextView) findViewById(R.id.valor_pedido);
        txtTotalDosItens.setText("Valor do Pedido: R$ " + String.format(Locale.US, "%.2f", totaldoPedido).replace(".", ","));

        txtTotalPedido = (TextView) findViewById(R.id.valor_total_pedido);
        txtTotalPedido.setText("Subtotal: R$ " + String.format(Locale.US, "%.2f", (totaldoPedido + taxadeEntrega)).replace(".", ","));

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

                adapter = new FormasdePagamentoAdapter(formasDePagamento, FinalizarPedidoActivity.this, totaldoPedido + taxadeEntrega);
                mRecyclerViewFormasdePagamento.setAdapter(adapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

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

    public void cadastrarEndereco(){
        buscarBairros();
        exibirDilogAddEndereco();
    }

    public void alterarEndereco(){
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

    public boolean verificarFormadePagamento(){
        for (FormadePagamento fpgm : formasDePagamento) {
            if (fpgm.isStatus()) {
                return true;
            }
        }

        return false;
    }

    public void updateNumero(List<Integer> list) {

        if (list.isEmpty()) {
            numerodopedido = "0";

        } else {

            Collections.sort(list);

            numerodopedido = list.get(list.size() - 1).toString();
            Log.println(Log.ERROR, "Ultimo Pedido: ", list.get(list.size() - 1).toString());
        }

    }

    public List<ItemPedido> getItensdoPedido() {
        List<ItemPedido> itensAux;
        CarrinhoDAO crud = new CarrinhoDAO(getBaseContext());
        itensAux = crud.getAllItens();
        return itensAux;
    }

    public String gerarNumeroPedido(String numero) {

        Log.println(Log.ERROR, "PEDIDO ANTERIOR:", numero);

        int intnum = Integer.parseInt(numero);
        intnum++;

        if (intnum < 10) {
            NumberFormat formatter = new DecimalFormat("00");

            numero = formatter.format(intnum);
        } else {
            numero = String.valueOf(intnum);
        }

        Log.println(Log.ERROR, "NUMERO DO PEDIDO:", numero);

        return numero;
    }

    public void buscarBairros(){

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

                    salvarEndereco(enderecoAux);

                    endereco = enderecoAux;
                    atualizarViewEnderecoeTaxa(endereco);

                    dialogCadastrarEndereco.dismiss();
                    Toast.makeText(FinalizarPedidoActivity.this, "Endereço cadastrado com sucesso", Toast.LENGTH_SHORT).show();

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
    }

    public void initViewAlterarEndereco(final View root) {

        enderecoSpinner = (Spinner) root.findViewById(R.id.enderecos_spinner);
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

    public void updateViewsAlterarEndereco(View root, Endereco endereco){

        aeTvRuaEndereco = (TextView) root.findViewById(R.id.ae_tv_rua_endereco);
        aeTvRuaEndereco.setText(endereco.getRua());

        aeTvNumeroEndereco = (TextView) root.findViewById(R.id.ae_tv_numero_endereco);
        aeTvNumeroEndereco.setText(endereco.getNumero());

        aeTvBairroEndereco = (TextView) root.findViewById(R.id.ae_tv_bairro_endereco);
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

    public void salvarEndereco(Endereco endereco) {

        DatabaseReference enderecoref = database.child("clientes").child("1").child("enderecos"); //PEGAR ID DO USUÁRIO LOGADO
        String key = enderecoref.push().getKey();
        Map<String, Object> enderecoValues = endereco.toMap();
        Map<String, Object> childUpdatesEndereco = new HashMap<>();
        childUpdatesEndereco.put("/clientes/1/enderecos/" + key, enderecoValues); //PEGAR ID DO USUÁRIO LOGADO

        database.updateChildren(childUpdatesEndereco);
    }

    public void atualizarViewEnderecoeTaxa(Endereco endereco){
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

    public void buscarTaxaPeloBairro(final String bairroAux){
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
                   if (bairro.getBairro().equals(bairroAux)){
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

        bairrosNomes = new ArrayList<>();
        totaldoPedido = getIntent().getDoubleExtra("totalPedido", 0);

        buscarNumerodoPedido();
        buscarFormasdePagamento();
        buscarEnderecosdoUsuario();
        buscarBairros();
        populateFormasdePagamento();

    }

    public void buscarNumerodoPedido(){
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Integer> list = new ArrayList<>();

                try {
                    for (DataSnapshot pedido : dataSnapshot.getChildren()) {
                        for (DataSnapshot dia : pedido.getChildren()) {
                            String numpedido = dia.child("numero_pedido").getValue().toString();
                            list.add(Integer.parseInt(numpedido));
                        }
                    }

                    updateNumero(list);
                } catch (Exception n) {
                    updateNumero(list);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };

        database.child("pedidos").addValueEventListener(postListener);
    }

    public void buscarFormasdePagamento(){

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

    public void buscarEnderecosdoUsuario(){

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

        database.child("clientes").child("1").child("enderecos").addValueEventListener(enderecoListener); //PEGAR ID DO USUÁRIO
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
}