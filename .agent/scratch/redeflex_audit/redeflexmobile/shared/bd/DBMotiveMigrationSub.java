package com.axys.redeflexmobile.shared.bd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.axys.redeflexmobile.shared.models.migracao.MotiveMigrationSub;
import com.axys.redeflexmobile.shared.util.Util_DB;
import com.axys.redeflexmobile.shared.util.Util_IO;
import com.axys.redeflexmobile.shared.util.Utilidades;

import java.util.ArrayList;

import timber.log.Timber;

/**
 * @author Lucas Marciano on 30/03/2020
 */
public class DBMotiveMigrationSub {
    private Context context;
    private String mTabela = "MotivoMigracaoSub";

    public DBMotiveMigrationSub(Context pContext) {
        context = pContext;
    }

    public void add(MotiveMigrationSub item) throws Exception {
        ContentValues values = new ContentValues();
        values.put("id", item.getId());
        values.put("descricao", item.getDescricao());
        values.put("ativo", item.isAtivo());

        if (!Util_DB.isCadastrado(context, mTabela, "id", String.valueOf(item.getId())))
            SimpleDbHelper.INSTANCE.open(context).insert(mTabela, null, values);
        else
            SimpleDbHelper.INSTANCE.open(context).update(mTabela, values, "[id]=?",
                    new String[]{String.valueOf(item.getId())});
    }

    public MotiveMigrationSub getById(String id) {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("AND [id] = ?");
        return Utilidades.firstOrDefault(getAll(sb.toString(), new String[]{id}));
    }

    public void deleteById(String id) {
        SimpleDbHelper.INSTANCE.open(context).delete(mTabela, "[id]=?", new String[]{id});
    }

    public void deleteAll() {
        SimpleDbHelper.INSTANCE.open(context).delete(mTabela, null, null);
    }

    public ArrayList<MotiveMigrationSub> getAll(String pCondicao, String[] pCampos) {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("SELECT id");
        sb.appendLine(", descricao ");
        sb.appendLine(", ativo ");
        sb.appendLine("FROM [" + mTabela + "]");
        sb.appendLine("WHERE 1 = 1");
        if (pCondicao != null)
            sb.append(pCondicao);

        return moverCursorDb(sb, pCampos);
    }

    private ArrayList<MotiveMigrationSub> moverCursorDb(Util_IO.StringBuilder sb, String[] args) {
        ArrayList<MotiveMigrationSub> lista = new ArrayList<>();
        SQLiteDatabase db = SimpleDbHelper.INSTANCE.open(context);
        try (Cursor cursor = db.rawQuery(sb.toString(), args)) {
            while (cursor.moveToNext()) {
                MotiveMigrationSub item = new MotiveMigrationSub();
                item.setId(cursor.getInt(0));
                item.setDescricao(cursor.getString(1));
                item.setAtivo(cursor.getInt(2) == 1);

                lista.add(item);
            }
        } catch (Exception e) {
            Timber.e(e);
        }

        return lista;
    }
}
