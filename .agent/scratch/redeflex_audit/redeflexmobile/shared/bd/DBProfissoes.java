package com.axys.redeflexmobile.shared.bd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.axys.redeflexmobile.shared.models.Profissoes;
import com.axys.redeflexmobile.shared.util.Util_DB;
import com.axys.redeflexmobile.shared.util.Util_IO;
import com.axys.redeflexmobile.shared.util.Utilidades;

import java.util.ArrayList;

public class DBProfissoes {
    private Context mContext;
    private String mTabela = "Profissoes";

    public DBProfissoes(Context pContext) {
        mContext = pContext;
    }

    public void addProfissoes(Profissoes pProfissoes) throws Exception {
        ContentValues values = new ContentValues();
        values.put("descricao", pProfissoes.getDescricao());
        values.put("situacao", pProfissoes.getSituacao());
        if (!Util_DB.isCadastrado(mContext, mTabela, "id", String.valueOf(pProfissoes.getId()))) {
            values.put("id", pProfissoes.getId());
            SimpleDbHelper.INSTANCE.open(mContext).insert(mTabela, null, values);
        } else
            SimpleDbHelper.INSTANCE.open(mContext).update(mTabela, values, "[id]=?", new String[]{String.valueOf(pProfissoes.getId())});
    }

    public Profissoes getById(int pId) {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("AND [id] = ?");
        return Utilidades.firstOrDefault(getProfissoes(sb.toString(), new String[]{String.valueOf(pId)}));
    }

    public void deleteById(int pId) {
        SimpleDbHelper.INSTANCE.open(mContext).delete(mTabela, "[id]=?", new String[]{String.valueOf(pId)});
    }

    private ArrayList<Profissoes> getProfissoes(String pCondicao, String[] pCampos) {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("SELECT [id]");
        sb.appendLine(",[descricao]");
        sb.appendLine(",[situacao]");
        sb.appendLine("FROM [Profissoes]");
        sb.appendLine("WHERE 1 = 1");
        if (pCondicao != null)
            sb.append(pCondicao);

        Cursor cursor = null;
        ArrayList<Profissoes> lista = new ArrayList<>();
        try {
            cursor = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(sb.toString(), pCampos);
            Profissoes profissoes;
            if (cursor.moveToFirst()) {
                do {
                    profissoes = new Profissoes();
                    profissoes.setId(cursor.getInt(0));
                    profissoes.setDescricao(cursor.getString(1));
                    profissoes.setSituacao(cursor.getString(2));
                    lista.add(profissoes);
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

    public ArrayList<Profissoes> getProfissoes() {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("SELECT [id]");
        sb.appendLine(",[descricao]");
        sb.appendLine(",[situacao]");
        sb.appendLine("FROM [Profissoes]");
        sb.appendLine("WHERE Situacao = 'A'");

        Cursor cursor = null;
        ArrayList<Profissoes> lista = new ArrayList<>();
        try {
            cursor = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(sb.toString(), null);
            Profissoes profissoes;
            if (cursor.moveToFirst()) {
                do {
                    profissoes = new Profissoes();
                    profissoes.setId(cursor.getInt(0));
                    profissoes.setDescricao(cursor.getString(1));
                    profissoes.setSituacao(cursor.getString(2));
                    lista.add(profissoes);
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
