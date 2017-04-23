package br.com.belongapps.meuacai.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class CriaBanco extends SQLiteOpenHelper{

    private static final String NOME_BANCO = "belong.db";

    public static final String TABELA = "itemPedido";
    public static final String ID = "_id";
    public static final String NOME = "nome";
    public static final String DESCRICAO = "descricao";
    public static final String OBSERVACAO = "observacao";
    public static final String REF_IMG = "ref_img";
    public static final String VALOR_UNIT = "valor_unit";

    public static final String QUANTIDADE = "quantidade";
    public static final String VALOR_TOTAL = "valor_total";

    public static final String NOME_METADE_2 = "nomeMetade2";
    public static final String DESCRICAO_METADE_2 = "descricaoMetade2";
    public static final String OBSERVACAO_METADE_2 = "observacaoMetade2";
    public static final String REF_IMG_METADE_2 = "ref_imgMetade2";
    public static final String VALOR_UNIT_METADE_2 = "valor_unit_metade_2";

    private static final int VERSAO = 1;

    public CriaBanco(Context context){
        super(context,NOME_BANCO,null,VERSAO);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql = "CREATE TABLE "+TABELA+"("
                + ID + " integer primary key autoincrement,"
                + NOME + " text, "
                + DESCRICAO + " text, "
                + OBSERVACAO + " text, "
                + REF_IMG + " text, "
                + VALOR_UNIT + " real, "

                + QUANTIDADE + " integer, "
                + VALOR_TOTAL + " real, "

                + NOME_METADE_2 + " text, "
                + DESCRICAO_METADE_2 + " text, "
                + OBSERVACAO_METADE_2 + " text, "
                + REF_IMG_METADE_2 + " text, "
                + VALOR_UNIT_METADE_2 + " real "
                +")";
        sqLiteDatabase.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS" + TABELA);
        onCreate(sqLiteDatabase);
    }

}
