package br.com.belongapps.appdelivery.cardapioOnline.dao;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.belongapps.appdelivery.R;
import br.com.belongapps.appdelivery.cardapioOnline.activitys.CardapioMainActivity;
import br.com.belongapps.appdelivery.cardapioOnline.activitys.PedidoEnviadoActivity;
import br.com.belongapps.appdelivery.cardapioOnline.model.ItemPedido;
import br.com.belongapps.appdelivery.cardapioOnline.model.ItemQtdPedido;
import br.com.belongapps.appdelivery.cardapioOnline.model.KeyPedido;
import br.com.belongapps.appdelivery.cardapioOnline.model.Pedido;
import br.com.belongapps.appdelivery.firebaseService.FirebaseAuthApp;
import br.com.belongapps.appdelivery.posPedido.activities.AcompanharPedidoActivity;
import br.com.belongapps.appdelivery.util.DataUtil;
import br.com.belongapps.appdelivery.util.StringUtil;

import static android.content.ContentValues.TAG;

public class FinalizarPedidoDAO {

    private static DatabaseReference database;
    private static FirebaseUser usuarioLogado;
    private static Context context;
    private static ProgressDialog mProgressDialog;

    public FinalizarPedidoDAO(Context context, ProgressDialog mProgressDialog) {
        this.context = context;
        this.mProgressDialog = mProgressDialog;
        database = FirebaseDatabase.getInstance().getReference();
        usuarioLogado = FirebaseAuthApp.getUsuarioLogado();
    }

    //SALVAR PEDIDO
    /*METÓDOS PARA ENVIO DO PEDIDO -----------------------*/
    public void salvarPedido(final Pedido pedido, final String dataPedido) {
        final String mesPedido = StringUtil.mesdoPedido(dataPedido);
        final String keyPedido = database.child("pedidos").push().getKey();

        //Atualizar total de pedidos e cadastrar o Pedido
        DatabaseReference numPedidoRef = database.child("pedidos");
        numPedidoRef.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                Integer ultimoPedido = mutableData.child("ultimo_pedido").getValue(Integer.class);

                if (ultimoPedido == null) {
                    return Transaction.success(mutableData);
                }

                Log.println(Log.ERROR, "ULTIMO PEDIDO:", "" + ultimoPedido);

                //SET NUMERO DO PEDIDO
                ultimoPedido++;

                Log.println(Log.ERROR, "PEDIDO ATUAL:", "" + ultimoPedido);

                pedido.setNumero_pedido(String.valueOf(ultimoPedido));

                mutableData.child("ultimo_pedido").setValue(ultimoPedido); //Atualiza o ultimoPedido de pedidos realizados

                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b,
                                   DataSnapshot dataSnapshot) {

                //CADASTRAR PEDIDO
                cadastrarPedido(pedido, dataPedido, mesPedido, keyPedido);

                //ATUALIZA PEDIDOS DO USUÁRIO LOGADO
                atualizarPedidosdoCliente(dataPedido, keyPedido, pedido);

                //ATUALIZA NÚMERO DE PEDIDOS DO USUÁRIO LOGADO
                buscarEAtualizarTotaldePedidosDoItem(pedido.getItens_pedido());

                //ATUALIZAR MÓDULO FINANCEIRO
                //Atualizar Total de Pedidos na Semana
                atualizarPedidosNaSemana(mesPedido);

                //Atualizar Total de Pedidos no Mes
                atualizarPedidosnoMes(mesPedido, pedido);

            }
        });

    }

    public static void cadastrarPedido(Pedido pedido, String dataHoje, String mesPedido, String keyPedido) {
        Pedido pedidoAux = pedido;
        Map<String, Object> pedidoValues = pedidoAux.toMap();

        Log.println(Log.ERROR, "MÊS:", mesPedido);
        Log.println(Log.ERROR, "DIA:", dataHoje);

        Map<String, Object> childUpdatesPedido = new HashMap<>();
        childUpdatesPedido.put("/pedidos/" + mesPedido + "/" + dataHoje + "/" + keyPedido, pedidoValues);

        database.updateChildren(childUpdatesPedido);
    }

    //ATUALIZAR MÓDULO FINANCEIRO
    public static void atualizarPedidosNaSemana(String mesPedido) {
        Date dataAtual = new Date();
        String diaDaSemana = DataUtil.getDiadaSemana(dataAtual);

        DatabaseReference numPedidoRef = database.child("financeiro").child("pedidos_semana").child(mesPedido).child(diaDaSemana);
        numPedidoRef.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                Integer total = mutableData.child("total").getValue(Integer.class);

                if (total == null) {
                    return Transaction.success(mutableData);
                }

                //ATUALIZAR TOTAL
                total++;
                mutableData.child("total").setValue(total); //Atualiza o total de pedidos realizados

                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b,
                                   DataSnapshot dataSnapshot) {
                // Transaction completed
                Log.d(TAG, "postTransaction:onComplete:" + databaseError);
            }
        });

    }

    //ATUALIZAR TOTAL PEDIDOS DO MES
    public static void atualizarPedidosnoMes(String mes, final Pedido pedido) {

        DatabaseReference numPedidoRef = database.child("financeiro").child("pedidos_mes").child(mes);
        numPedidoRef.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                Integer total = mutableData.child("total").getValue(Integer.class);

                if (total == null) {
                    return Transaction.success(mutableData);
                }

                //ATUALIZAR TOTAL
                total++;
                mutableData.child("total").setValue(total); //Atualiza o total de pedidos realizados

                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b,
                                   DataSnapshot dataSnapshot) {
                // Transaction completed
                Log.d(TAG, "postTransaction:onComplete:" + databaseError);

                limparCarrinho();

                //Fecha o dialog
                mProgressDialog.hide();
                mProgressDialog.dismiss();

                //Redirecina para a tela de Sucesso
                Intent i = new Intent(context, PedidoEnviadoActivity.class); //exibir tela de sucesso

                i.putExtra("NumeroPedido", pedido.getNumero_pedido());
                i.putExtra("DataPedido", DataUtil.getDataPedido(pedido.getData()));
                i.putExtra("HoraPedido", DataUtil.getHoraPedido(pedido.getData()));
                i.putExtra("ValorPedido", pedido.getValor_total());
                i.putExtra("StatusPedido", pedido.getStatus());
                i.putExtra("TipoEntrega", pedido.getEntrega_retirada());
                i.putExtra("StatusTempo", pedido.getStatus_tempo());

                ArrayList<ItemPedido> itens = new ArrayList<>();
                for (ItemPedido item : pedido.getItens_pedido()) {
                    itens.add(item);
                }

                i.putParcelableArrayListExtra("ItensPedido", itens);

                context.startActivity(i);
                ((Activity) context).finish();

            }
        });

    }

    /*ATUALIZAR TOTAL PEDIDOS DO CLIENTE*/
    public static void atualizarNumeroPedidosdoCliente(String idDoUsuario, final Pedido pedido) {

        DatabaseReference numPedidoRef = database.child("clientes").child(idDoUsuario);
        numPedidoRef.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                Integer total = mutableData.child("total_pedidos").getValue(Integer.class);

                if (total == null) {
                    return Transaction.success(mutableData);
                }

                //ATUALIZAR TOTAL DE PEDIDOS REALIZADOS
                total++;
                mutableData.child("total_pedidos").setValue(total); //Atualiza o total de pedidos realizados pelo cliente

                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b,
                                   DataSnapshot dataSnapshot) {
                Log.d(TAG, "postTransaction:onComplete:" + databaseError);

            }
        });

    }

    //ATUALIZAR OS PEDIDOS DO CLIENTE
    public static void atualizarPedidosdoCliente(String hj, String keyPedido, Pedido pedido) {

        KeyPedido keyp = new KeyPedido(keyPedido);
        keyp.setId(StringUtil.mesdoPedido(hj) + "/" + hj + "/" + keyPedido);

        String key = database.child("clientes").child(usuarioLogado.getUid()).push().getKey();

        database.child("clientes").child(usuarioLogado.getUid()).child("pedidos").child(key).setValue(keyp);

        atualizarNumeroPedidosdoCliente(usuarioLogado.getUid(), pedido);
    }

    //ATUALIZAR TOTAL PEDIDO DO ITEM
    public static void buscarEAtualizarTotaldePedidosDoItem(final List<ItemPedido> itensdoPedido) {

        final List<ItemQtdPedido> itensqtd = new ArrayList<>();

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (ItemPedido item : itensdoPedido) {
                    if (item.getKeyItem() != null) {

                        Log.println(Log.ERROR, "ITEM P/ ATUALIZAR", item.getCategoria() + "/" + item.getKeyItem());

                        //recupera qtd pedido do item
                        Integer qtdPedido = dataSnapshot.child(item.getCategoria()).child(item.getKeyItem()).child("qtdPedido").getValue(Integer.class);
                        ItemQtdPedido itemQtd = new ItemQtdPedido(item.getCategoria(), item.getKeyItem(), qtdPedido, item.getQuantidade());
                        itensqtd.add(itemQtd);
                    }
                }

                incrementarTotalPedidodosItens(itensqtd);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        database.child("itens_cardapio").addListenerForSingleValueEvent(valueEventListener);
    }

    public static void incrementarTotalPedidodosItens(List<ItemQtdPedido> itensQtd) {

        for (ItemQtdPedido item : itensQtd) {

            DatabaseReference itemCardapioRef = database.child("itens_cardapio").child(item.getKeycategoria()).child(item.getKeyItem());
            itemCardapioRef.runTransaction(new Transaction.Handler() {
                @Override
                public Transaction.Result doTransaction(MutableData mutableData) {
                    Integer total = mutableData.child("qtdPedido").getValue(Integer.class);

                    if (total == null) {
                        return Transaction.success(mutableData);
                    }

                    //ATUALIZAR TOTAL DE PEDIDOS REALIZADOS
                    total++;
                    mutableData.child("qtdPedido").setValue(total); //Atualiza o total Pedido no Item

                    return Transaction.success(mutableData);
                }

                @Override
                public void onComplete(DatabaseError databaseError, boolean b,
                                       DataSnapshot dataSnapshot) {
                    Log.d(TAG, "postTransaction:onComplete:" + databaseError);
                }
            });

        }
    }

    //EXIBIR MENSAGEM PEDIDO ENVIADO COM SUCESSO
    public void exibirMensagemDeSucesso(final Pedido pedido) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        AlertDialog.Builder mBilder = new AlertDialog.Builder(context, R.style.MyDialogTheme);
        View layoutDialog = inflater.inflate(R.layout.dialog_pedido_finalizado, null);
        Button bt_ok = (Button) layoutDialog.findViewById(R.id.bt_ok_pedido_enviado);
        Button acompanhar = (Button) layoutDialog.findViewById(R.id.bt_acompanhar_pedido_enviado);

        mBilder.setView(layoutDialog);
        final AlertDialog dialogpedidoEnviado = mBilder.create();
        dialogpedidoEnviado.show();

        bt_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, CardapioMainActivity.class);
                context.startActivity(i);
                ((Activity) context).finish();
            }
        });

        acompanhar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, AcompanharPedidoActivity.class);
                i.putExtra("NumeroPedido", pedido.getNumero_pedido());
                i.putExtra("DataPedido", DataUtil.getDataPedido(pedido.getData()));
                i.putExtra("HoraPedido", DataUtil.getHoraPedido(pedido.getData()));
                i.putExtra("ValorPedido", pedido.getValor_total());
                i.putExtra("StatusPedido", pedido.getStatus());
                i.putExtra("TipoEntrega", pedido.getEntrega_retirada());
                i.putExtra("StatusTempo", pedido.getStatus_tempo());

                ArrayList<ItemPedido> itens = new ArrayList<>();
                for (ItemPedido item : pedido.getItens_pedido()) {
                    itens.add(item);
                }
                i.putParcelableArrayListExtra("ItensPedido", itens);

                context.startActivity(i);
                ((Activity) context).finish();

            }
        });

    }

    private static void limparCarrinho() {
        CarrinhoDAO dao = new CarrinhoDAO(context);
        dao.deleteAll();
    }

    public static boolean temUsuarioLogado() {
        if (usuarioLogado == null) {
            return false;
        }

        return true;
    }


}