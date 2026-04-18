package com.axys.redeflexmobile.shared.bd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.axys.redeflexmobile.shared.models.Cnae;
import com.axys.redeflexmobile.shared.util.Util_DB;
import com.axys.redeflexmobile.shared.util.Util_IO;
import com.axys.redeflexmobile.shared.util.Utilidades;

import java.util.ArrayList;

/**
 * Created by joao.viana on 10/01/2018.
 */

public class DBCnae {
    private Context mContext;
    private String mTabela = "Cnae";

    public DBCnae(Context pContext) {
        mContext = pContext;
    }

    public void addCnae(Cnae pCnae) throws Exception {
        ContentValues values = new ContentValues();
        values.put("ramoAtividade", pCnae.getRamoAtividade());
        values.put("mcc", pCnae.getMcc());
        values.put("situacao", pCnae.getSituacao());
        if (!Util_DB.isCadastrado(mContext, mTabela, "id", pCnae.getCodigo())) {
            values.put("id", pCnae.getCodigo());
            SimpleDbHelper.INSTANCE.open(mContext).insert(mTabela, null, values);
        } else
            SimpleDbHelper.INSTANCE.open(mContext).update(mTabela, values, "[id]=?", new String[]{pCnae.getCodigo()});
    }

    public Cnae getById(String pId) {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("AND [id] = ?");
        sb.appendLine("AND [situacao] = 'A'");
        return Utilidades.firstOrDefault(getCnaes(sb.toString(), new String[]{pId}));
    }

    public void deleteById(String pId) {
        SimpleDbHelper.INSTANCE.open(mContext).delete(mTabela, "[id]=?", new String[]{pId});
    }

    public boolean existeCnae() {
        Cursor cursor = null;
        try {
            cursor = SimpleDbHelper.INSTANCE.open(mContext).rawQuery("SELECT id FROM [Cnae]", null);
            return (cursor != null && cursor.getCount() > 0);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return false;
    }

    private ArrayList<Cnae> getCnaes(String pCondicao, String[] pCampos) {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("SELECT [id]");
        sb.appendLine(",[ramoAtividade]");
        sb.appendLine(",[mcc]");
        sb.appendLine(",[situacao]");
        sb.appendLine("FROM [Cnae]");
        sb.appendLine("WHERE 1=1");
        if (pCondicao != null)
            sb.append(pCondicao);

        Cursor cursor = null;
        ArrayList<Cnae> lista = new ArrayList<>();
        try {
            cursor = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(sb.toString(), pCampos);
            Cnae cnae;
            if (cursor.moveToFirst()) {
                do {
                    cnae = new Cnae();
                    cnae.setCodigo(cursor.getString(0));
                    cnae.setId(cursor.getString(0));
                    cnae.setRamoAtividade(cursor.getString(1));
                    cnae.setMcc(cursor.getString(2));
                    cnae.setSituacao(cursor.getString(3));
                    lista.add(cnae);
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