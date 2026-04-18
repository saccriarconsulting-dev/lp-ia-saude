package com.axys.redeflexmobile.shared.bd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import com.axys.redeflexmobile.shared.models.Motivo;
import com.axys.redeflexmobile.shared.util.Util_DB;
import com.axys.redeflexmobile.shared.util.Util_IO;

import java.util.ArrayList;

/**
 * Created by joao.viana on 10/01/2018.
 */

public class DBMotivo {
    private Context mContext;
    private String mTabela = "Motivo";

    public DBMotivo(Context pContext) {
        mContext = pContext;
    }

    public void addMotivo(Motivo pMotivo) throws Exception {
        ContentValues values = new ContentValues();
        values.put("descricao", pMotivo.getDescricao());
        values.put("ativo", pMotivo.getAtivo());
        values.put("tipo", pMotivo.getTipo());
        if (!Util_DB.isCadastrado(mContext, mTabela, "id", String.valueOf(pMotivo.getId()))) {
            values.put("id", pMotivo.getId());
            SimpleDbHelper.INSTANCE.open(mContext).insert(mTabela, null, values);
        } else
            SimpleDbHelper.INSTANCE.open(mContext).update(mTabela, values, "[id]=?", new String[]{String.valueOf(pMotivo.getId())});
    }

/*    public Motivo getById(int pId) {
        try {
            ArrayList<Parametro> pmts = new ArrayList<>();
            pmts.add(new Parametro("id", String.valueOf(pId), Operador.Igual));
            return Util.firstOrDefault(this.mGeneric.returnList(Motivo.class, pmts));
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }*/

    public void deleteById(int pId) {
        SimpleDbHelper.INSTANCE.open(mContext).delete(mTabela, "[id]=?", new String[]{String.valueOf(pId)});
    }

    public ArrayList<Motivo> getMotivos() {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("SELECT [id]");
        sb.appendLine(",[tipo]");
        sb.appendLine(",[descricao]");
        sb.appendLine(",[ativo]");
        sb.appendLine("FROM [Motivo]");
        sb.appendLine("WHERE ativo='S' AND tipo >= -1 AND id != 15");

        Cursor cursor = null;
        ArrayList<Motivo> lista = new ArrayList<>();
        try {
            cursor = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(sb.toString(), new String[]{});
            Motivo m;
            if (cursor.moveToFirst()) {
                do {
                    m = new Motivo();
                    m.setId(cursor.getInt(0));
                    m.setTipo(cursor.getInt(1));
                    m.setDescricao(cursor.getString(2));
                    m.setAtivo(cursor.getString(3));
                    lista.add(m);
                } while (cursor.moveToNext());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return lista;
    }

    public void deleteAll() {
        SimpleDbHelper.INSTANCE.open(mContext).delete(mTabela, null, null);
    }
}