package com.axys.redeflexmobile.shared.bd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.axys.redeflexmobile.shared.models.FaixaDeFaturamentoMensal;
import com.axys.redeflexmobile.shared.util.Util_DB;
import com.axys.redeflexmobile.shared.util.Util_IO;
import com.axys.redeflexmobile.shared.util.Utilidades;

import java.util.ArrayList;

public class DBFaixaDeFaturamentoMensal {
    private Context mContext;
    private String mTabela = "FaixaDeFaturamentoMensal";

    public DBFaixaDeFaturamentoMensal(Context pContext) {
        mContext = pContext;
    }

    public void addFaixaDeFaturamentoMensal(FaixaDeFaturamentoMensal pFaixaDeFaturamentoMensal) throws Exception {
        ContentValues values = new ContentValues();
        values.put("descricao", pFaixaDeFaturamentoMensal.getDescricao());
        values.put("situacao", pFaixaDeFaturamentoMensal.getSituacao());
        if (!Util_DB.isCadastrado(mContext, mTabela, "id", String.valueOf(pFaixaDeFaturamentoMensal.getId()))) {
            values.put("id", pFaixaDeFaturamentoMensal.getId());
            SimpleDbHelper.INSTANCE.open(mContext).insert(mTabela, null, values);
        } else
            SimpleDbHelper.INSTANCE.open(mContext).update(mTabela, values, "[id]=?", new String[]{String.valueOf(pFaixaDeFaturamentoMensal.getId())});
    }

    public FaixaDeFaturamentoMensal getById(int pId) {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("AND [id] = ?");
        return Utilidades.firstOrDefault(getFaixaDeFaturamentoMensal(sb.toString(), new String[]{String.valueOf(pId)}));
    }

    public void deleteById(int pId) {
        SimpleDbHelper.INSTANCE.open(mContext).delete(mTabela, "[id]=?", new String[]{String.valueOf(pId)});
    }

    private ArrayList<FaixaDeFaturamentoMensal> getFaixaDeFaturamentoMensal(String pCondicao, String[] pCampos) {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("SELECT [id]");
        sb.appendLine(",[descricao]");
        sb.appendLine(",[situacao]");
        sb.appendLine("FROM [FaixaDeFaturamentoMensal]");
        sb.appendLine("WHERE 1 = 1 ");
        if (pCondicao != null)
            sb.append(pCondicao);

        Cursor cursor = null;
        ArrayList<FaixaDeFaturamentoMensal> lista = new ArrayList<>();
        try {
            cursor = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(sb.toString(), pCampos);
            FaixaDeFaturamentoMensal faixadefaturamentoMensal;
            if (cursor.moveToFirst()) {
                do {
                    faixadefaturamentoMensal = new FaixaDeFaturamentoMensal();
                    faixadefaturamentoMensal.setId(cursor.getInt(0));
                    faixadefaturamentoMensal.setDescricao(cursor.getString(1));
                    faixadefaturamentoMensal.setSituacao(cursor.getString(2));
                    lista.add(faixadefaturamentoMensal);
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

    public ArrayList<FaixaDeFaturamentoMensal> getFaixaDeFaturamentoMensal() {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("SELECT [id]");
        sb.appendLine(",[descricao]");
        sb.appendLine(",[situacao]");
        sb.appendLine("FROM [FaixaDeFaturamentoMensal]");
        sb.appendLine("WHERE Situacao = 'A'");

        Cursor cursor = null;
        ArrayList<FaixaDeFaturamentoMensal> lista = new ArrayList<>();
        try {
            cursor = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(sb.toString(), null);
            FaixaDeFaturamentoMensal faixadefaturamentoMensal;
            if (cursor.moveToFirst()) {
                do {
                    faixadefaturamentoMensal = new FaixaDeFaturamentoMensal();
                    faixadefaturamentoMensal.setId(cursor.getInt(0));
                    faixadefaturamentoMensal.setDescricao(cursor.getString(1));
                    faixadefaturamentoMensal.setSituacao(cursor.getString(2));
                    lista.add(faixadefaturamentoMensal);
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
