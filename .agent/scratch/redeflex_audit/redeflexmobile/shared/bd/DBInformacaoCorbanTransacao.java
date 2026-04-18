package com.axys.redeflexmobile.shared.bd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.axys.redeflexmobile.shared.models.InformacaoCorbanTransacao;
import com.axys.redeflexmobile.shared.util.Util_DB;
import com.axys.redeflexmobile.shared.util.Util_IO;

import java.util.ArrayList;

public class DBInformacaoCorbanTransacao {
    private Context mContext;

    public DBInformacaoCorbanTransacao(Context mContext) {
        this.mContext = mContext;
    }

    public void addInformacaoCorbanTransacao(InformacaoCorbanTransacao pInformacaoTransacao) throws Exception {
        ContentValues values = new ContentValues();
        values.put("idcliente", pInformacaoTransacao.getIdcliente());
        values.put("anomes", pInformacaoTransacao.getAnomes());
        values.put("qtdtransacao", pInformacaoTransacao.getQtdtransacao());
        if (!Util_DB.isCadastrado(mContext, "InformacaoCorbanTransacao", new String[]{"idCliente", "anomes"}, new String[]{Integer.toString(pInformacaoTransacao.getIdcliente()), Integer.toString(pInformacaoTransacao.getAnomes())})) {
            SimpleDbHelper.INSTANCE.open(mContext).insert("InformacaoCorbanTransacao", null, values);
        } else
            SimpleDbHelper.INSTANCE.open(mContext).update("InformacaoCorbanTransacao", values, "idCliente=? AND anomes=?", new String[]{Integer.toString(pInformacaoTransacao.getIdcliente()), Integer.toString(pInformacaoTransacao.getAnomes())});
    }

    public ArrayList<InformacaoCorbanTransacao> getInformacaoCorbanTransacao(String pCondicao, String[] pCampos) {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("select * from InformacaoCorbanTransacao");
        sb.appendLine("where 1=1");

        if (pCondicao != null)
            sb.append(pCondicao);

        Cursor cursor = null;
        ArrayList<InformacaoCorbanTransacao> lista = new ArrayList<>();
        try {
            cursor = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(sb.toString(), pCampos);
            InformacaoCorbanTransacao informacaoCorbanTransacao;
            if (cursor.moveToFirst()) {
                do {
                    informacaoCorbanTransacao = new InformacaoCorbanTransacao();
                    informacaoCorbanTransacao.setIdcliente(cursor.getInt(cursor.getColumnIndexOrThrow("idcliente")));
                    informacaoCorbanTransacao.setAnomes(cursor.getInt(cursor.getColumnIndexOrThrow("anomes")));
                    informacaoCorbanTransacao.setQtdtransacao(cursor.getInt(cursor.getColumnIndexOrThrow("qtdtransacao")));
                    lista.add(informacaoCorbanTransacao);
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
