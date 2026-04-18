package com.axys.redeflexmobile.shared.bd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.axys.redeflexmobile.shared.models.FaixaSalarial;
import com.axys.redeflexmobile.shared.models.IccidOperadora;
import com.axys.redeflexmobile.shared.util.Util_DB;
import com.axys.redeflexmobile.shared.util.Util_IO;
import com.axys.redeflexmobile.shared.util.Utilidades;

import java.util.ArrayList;

public class BDIccidOperadora {
    private Context mContext;
    private String mTabela = "IccidOperadora";

    public BDIccidOperadora(Context pContext) {
        mContext = pContext;
    }

    public void addIccidOperadora(IccidOperadora pIccidOperadora) throws Exception {
        ContentValues values = new ContentValues();
        values.put("id", pIccidOperadora.getId());
        values.put("OperadoraId", pIccidOperadora.getOperadoraId());
        values.put("InicioCodBarra", pIccidOperadora.getInicioCodBarra());
        values.put("Ativo", pIccidOperadora.getAtivo());
        if (!Util_DB.isCadastrado(mContext, mTabela, "id", String.valueOf(pIccidOperadora.getId()))) {
            values.put("id", pIccidOperadora.getId());
            SimpleDbHelper.INSTANCE.open(mContext).insert(mTabela, null, values);
        } else
            SimpleDbHelper.INSTANCE.open(mContext).update(mTabela, values, "[id]=?", new String[]{String.valueOf(pIccidOperadora.getId())});
    }

    public IccidOperadora getById(int pId) {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("AND [id] = ?");
        return Utilidades.firstOrDefault(getIccidOperadora(sb.toString(), new String[]{String.valueOf(pId)}));
    }

    public IccidOperadora getByInicioBarra(String pInicioBarra) {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("AND InicioCodBarra = '" + pInicioBarra + "'");
        return Utilidades.firstOrDefault(getIccidOperadora(sb.toString(), null));
    }

    public void deleteById(int pId) {
        SimpleDbHelper.INSTANCE.open(mContext).delete(mTabela, "[id]=?", new String[]{String.valueOf(pId)});
    }

    public void deleteAll() {
        SimpleDbHelper.INSTANCE.open(mContext).delete(mTabela, null, null);
    }

    private ArrayList<IccidOperadora> getIccidOperadora(String pCondicao, String[] pCampos) {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("select Id, OperadoraId, InicioCodBarra, Ativo");
        sb.appendLine("from IccidOperadora");
        sb.appendLine("WHERE 1 = 1 ");
        if (pCondicao != null)
            sb.append(pCondicao);

        Cursor cursor = null;
        ArrayList<IccidOperadora> lista = new ArrayList<>();
        try {
            cursor = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(sb.toString(), pCampos);
            IccidOperadora iccidOperadora;
            if (cursor.moveToFirst()) {
                do {
                    iccidOperadora = new IccidOperadora();
                    iccidOperadora.setId(cursor.getInt(0));
                    iccidOperadora.setOperadoraId(cursor.getInt(1));
                    iccidOperadora.setInicioCodBarra(cursor.getString(2));
                    iccidOperadora.setAtivo(cursor.getInt(3));
                    lista.add(iccidOperadora);
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
}
