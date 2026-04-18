package com.axys.redeflexmobile.shared.bd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.axys.redeflexmobile.shared.models.Cobranca;
import com.axys.redeflexmobile.shared.util.Util_DB;
import com.axys.redeflexmobile.shared.util.Util_IO;
import com.axys.redeflexmobile.ui.redeflex.Config;

import java.util.ArrayList;

/**
 * Created by joao.viana on 14/07/2016.
 */
public class DBCobranca {
    private Context mContext;
    private String mTabela = "Boleto";

    public DBCobranca(Context pContext) {
        this.mContext = pContext;
    }

    public void addCobranca(Cobranca pCobranca) throws Exception {
        ContentValues values = new ContentValues();
        values.put("idServer", pCobranca.getId());
        values.put("NumBoleto", pCobranca.getNumBoleto());
        values.put("LinhaDigitavel", pCobranca.getLinhaDigitavel());
        values.put("DataVencimento", Util_IO.dateTimeToString(pCobranca.getDataVencimento(), Config.FormatDateTimeStringBanco));
        values.put("Valor", pCobranca.getValor());
        values.put("Situacao", pCobranca.getSituacao());
        if (!Util_DB.isCadastrado(mContext, mTabela, "NumBoleto", String.valueOf(pCobranca.getNumBoleto())))
            SimpleDbHelper.INSTANCE.open(mContext).insert(mTabela, null, values);
        else
            SimpleDbHelper.INSTANCE.open(mContext).update(mTabela, values, "[NumBoleto]=?", new String[]{String.valueOf(pCobranca.getNumBoleto())});
    }

    public ArrayList<Cobranca> getCobrancas() {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("SELECT [id]");
        sb.appendLine(",[idServer]");
        sb.appendLine(",[NumBoleto]");
        sb.appendLine(",[LinhaDigitavel]");
        sb.appendLine(",[DataVencimento]");
        sb.appendLine(",[Valor]");
        sb.appendLine(",(CASE WHEN [DataVencimento] < date('now','localtime') AND Situacao = 1 THEN 0 ELSE [Situacao] END) AS [Situacao]");
        sb.appendLine("FROM [Boleto]");
        sb.appendLine("ORDER BY [DataVencimento] DESC");

        Cursor cursor = null;
        ArrayList<Cobranca> lista = new ArrayList<>();
        try {
            cursor = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(sb.toString(), null);
            Cobranca cobranca;
            if (cursor.moveToFirst()) {
                do {
                    cobranca = new Cobranca();
                    cobranca.setId(cursor.getLong(1));
                    cobranca.setNumBoleto(cursor.getLong(2));
                    cobranca.setLinhaDigitavel(cursor.getString(3));
                    cobranca.setDataVencimento(Util_IO.stringToDate(cursor.getString(4), Config.FormatDateTimeStringBanco));
                    cobranca.setValor(cursor.getDouble(5));
                    cobranca.setSituacao(cursor.getInt(6));
                    lista.add(cobranca);
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

    public void deletePagos() {
        SimpleDbHelper.INSTANCE.open(mContext).delete(mTabela, "[Situacao]<>? AND [DataVencimento]<date('now', '-7 day')", new String[]{"1"});
    }
}