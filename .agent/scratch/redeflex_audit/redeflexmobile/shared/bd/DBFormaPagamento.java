package com.axys.redeflexmobile.shared.bd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.axys.redeflexmobile.shared.models.FormaPagamento;
import com.axys.redeflexmobile.shared.util.Util_DB;
import com.axys.redeflexmobile.shared.util.Util_IO;
import com.axys.redeflexmobile.shared.util.Utilidades;

import java.util.ArrayList;

/**
 * Created by joao.viana on 10/01/2018.
 */

public class DBFormaPagamento {
    private Context mContext;
    private String mTabela = "FormaPagamento";

    public DBFormaPagamento(Context pContext) {
        mContext = pContext;
    }

    public void addFormaPagamento(FormaPagamento pFormaPagamento) throws Exception {
        ContentValues values = new ContentValues();
        values.put("descricao", pFormaPagamento.getDescricao());
        values.put("ativo", pFormaPagamento.getAtivo());
        if (!Util_DB.isCadastrado(mContext, mTabela, "id", String.valueOf(pFormaPagamento.getId()))) {
            values.put("id", pFormaPagamento.getId());
            SimpleDbHelper.INSTANCE.open(mContext).insert(mTabela, null, values);
        } else
            SimpleDbHelper.INSTANCE.open(mContext).update(mTabela, values, "[id]=?", new String[]{String.valueOf(pFormaPagamento.getId())});
    }

    public boolean existeFormaPagamento() {
        Cursor cursor = null;
        try {
            cursor = SimpleDbHelper.INSTANCE.open(mContext).rawQuery("SELECT id FROM [FormaPagamento]", null);
            return (cursor != null && cursor.getCount() > 0);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return false;
    }

    public FormaPagamento getFormaPagamentoById(int pId) {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("AND [id] = ?");
        return Utilidades.firstOrDefault(getFormaPagamentos(sb.toString(), new String[]{String.valueOf(pId)}));
    }

    public void deleteById(int pId) {
        SimpleDbHelper.INSTANCE.open(mContext).delete(mTabela, "[id]=?", new String[]{String.valueOf(pId)});
    }

    private ArrayList<FormaPagamento> getFormaPagamentos(String pCondicao, String[] pCampos) {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("SELECT [id]");
        sb.appendLine(",[descricao]");
        sb.appendLine(",[ativo]");
        sb.appendLine("FROM [FormaPagamento]");
        sb.appendLine("WHERE 1=1");
        if (pCondicao != null)
            sb.append(pCondicao);

        Cursor cursor = null;
        ArrayList<FormaPagamento> lista = new ArrayList<>();
        try {
            cursor = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(sb.toString(), pCampos);
            FormaPagamento formapagamento;
            if (cursor.moveToFirst()) {
                do {
                    formapagamento = new FormaPagamento();
                    formapagamento.setId(cursor.getInt(0));
                    formapagamento.setDescricao(cursor.getString(1));
                    formapagamento.setAtivo(cursor.getString(2));
                    lista.add(formapagamento);
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

    public ArrayList<FormaPagamento> getFormaPagamentos() {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("AND [ativo] = 'S'");

        return getFormaPagamentos(sb.toString(), null);
    }

    public ArrayList<FormaPagamento> getFormaPagamentos_Avista() {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("AND ([ativo] = 'S' OR ID = 1)");
        return getFormaPagamentos(sb.toString(), null);
    }

    public void deleteAll() {
        SimpleDbHelper.INSTANCE.open(mContext).delete(mTabela, null, null);
    }
}