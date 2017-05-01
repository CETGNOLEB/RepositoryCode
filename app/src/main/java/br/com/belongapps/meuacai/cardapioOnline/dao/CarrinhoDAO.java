package br.com.belongapps.meuacai.cardapioOnline.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import br.com.belongapps.meuacai.cardapioOnline.model.ItemPedido;
import br.com.belongapps.meuacai.util.CriaBanco;

public class CarrinhoDAO {

    private static SQLiteDatabase db;
    private static CriaBanco banco;

    public CarrinhoDAO(Context context) {
        banco = new CriaBanco(context);
    }

    public String salvarItem(ItemPedido itemPedido) {
        ContentValues valores;
        long resultado;

        db = banco.getWritableDatabase();
        valores = new ContentValues();

        valores.put(banco.NOME, itemPedido.getNome());
        valores.put(banco.DESCRICAO, itemPedido.getDescricao());
        valores.put(banco.OBSERVACAO, itemPedido.getObservacao());
        valores.put(banco.REF_IMG, itemPedido.getRef_img());
        valores.put(banco.VALOR_UNIT, itemPedido.getValor_unit());

        valores.put(banco.QUANTIDADE, itemPedido.getQuantidade());
        valores.put(banco.VALOR_TOTAL, itemPedido.getValor_total());

        valores.put(banco.NOME_METADE_2, itemPedido.getNomeMetade2());
        valores.put(banco.DESCRICAO_METADE_2, itemPedido.getDescMetade2());
        valores.put(banco.OBSERVACAO_METADE_2, itemPedido.getObsMetade2());
        valores.put(banco.REF_IMG_METADE_2, itemPedido.getImgMetade2());
        valores.put(banco.VALOR_UNIT_METADE_2, itemPedido.getValorMetade2());

        resultado = db.insert(CriaBanco.TABELA, null, valores);
        db.close();

        if (resultado == -1)
            return "Erro ao adicionar item no carrinho";
        else
            return "Item adicionado com sucesso";

    }

    public List<ItemPedido> getAllItens() {
        db = banco.getWritableDatabase();
        String selectQuery = "SELECT * FROM itemPedido";
        Cursor cursor = db.rawQuery(selectQuery, null);

        List<ItemPedido> itens = new ArrayList<>();

        try {
            if (cursor.moveToFirst()) {
                do {

                    ItemPedido item = new ItemPedido();
                    item.setNome(cursor.getString(cursor.getColumnIndex(banco.NOME)));
                    item.setDescricao(cursor.getString(cursor.getColumnIndex(banco.DESCRICAO)));
                    item.setObservacao(cursor.getString(cursor.getColumnIndex(banco.OBSERVACAO)));
                    item.setRef_img(cursor.getString(cursor.getColumnIndex(banco.REF_IMG)));
                    item.setValor_unit(cursor.getDouble(cursor.getColumnIndex(banco.VALOR_UNIT)));

                    item.setQuantidade(cursor.getInt(cursor.getColumnIndex(banco.QUANTIDADE)));
                    item.setValor_total(cursor.getDouble(cursor.getColumnIndex(banco.VALOR_TOTAL)));

                    item.setNomeMetade2(cursor.getString(cursor.getColumnIndex(banco.NOME_METADE_2)));
                    item.setDescMetade2(cursor.getString(cursor.getColumnIndex(banco.DESCRICAO_METADE_2)));
                    item.setObsMetade2(cursor.getString(cursor.getColumnIndex(banco.OBSERVACAO_METADE_2)));
                    item.setImgMetade2(cursor.getString(cursor.getColumnIndex(banco.REF_IMG_METADE_2)));
                    item.setValorMetade2(cursor.getDouble(cursor.getColumnIndex(banco.VALOR_UNIT_METADE_2)));

                    // Adding contact to list
                    itens.add(item);
                } while (cursor.moveToNext());
            }
        } catch (SQLiteException e) {
            Log.d("SQL Error", e.getMessage());
            return null;
        } finally {
            cursor.close();
            db.close();
        }
        return itens;
    }

    public Cursor buscarItemPorId(int id) {
        db = banco.getWritableDatabase();

        Cursor cursor;
        String[] campos = {
                banco.ID,
                banco.NOME,
                banco.DESCRICAO,
                banco.OBSERVACAO,
                banco.VALOR_UNIT,

                banco.QUANTIDADE,
                banco.VALOR_TOTAL,

                banco.NOME_METADE_2,
                banco.DESCRICAO_METADE_2,
                banco.OBSERVACAO_METADE_2,
                banco.REF_IMG_METADE_2,
                banco.VALOR_UNIT_METADE_2,
        };


        String where = CriaBanco.ID + "=" + id;
        cursor = db.query(CriaBanco.TABELA, campos, where, null, null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }
        db.close();
        return cursor;
    }

    public static void updateItem(String id, ItemPedido item) {
        ContentValues valores;
        String where;

        db = banco.getWritableDatabase();

        where = CriaBanco.ID + " = " + id;

        valores = new ContentValues();
        valores.put(CriaBanco.QUANTIDADE, item.getQuantidade());
        valores.put(CriaBanco.VALOR_UNIT, item.getValor_unit());
        valores.put(CriaBanco.VALOR_TOTAL, item.getValor_total());

        Log.println(Log.ERROR, "ITEM:", "" + valores.getAsString(CriaBanco.QUANTIDADE));

        db.update(CriaBanco.TABELA, valores, where, null);
        db.close();
    }

    public static void atualizarItens(List<ItemPedido> itens_pedido) {
        db = banco.getWritableDatabase();
        String selectQuery = "SELECT * FROM itemPedido";
        Cursor cursor = db.rawQuery(selectQuery, null);
        String codigo;

        for (int i = 0; i < itens_pedido.size(); i++) {
            cursor.moveToPosition(i);
            codigo = cursor.getString(cursor.getColumnIndexOrThrow("_id"));

            Log.println(Log.ERROR, "ID: ", codigo);
            updateItem(codigo, itens_pedido.get(i));
        }
    }

    public void deletarItem(int position, List<ItemPedido> itens) {
        db = banco.getWritableDatabase();
        String selectQuery = "SELECT * FROM itemPedido";
        Cursor cursor = db.rawQuery(selectQuery, null);

        cursor.moveToPosition(position);
        String codigo = cursor.getString(cursor.getColumnIndexOrThrow("_id"));

        Log.println(Log.ERROR, "ID: ", codigo);

        String where = CriaBanco.ID + " = " + codigo;
        db = banco.getReadableDatabase();
        db.delete(CriaBanco.TABELA, where, null);
        db.close();
    }

}
