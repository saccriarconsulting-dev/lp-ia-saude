package com.axys.redeflexmobile.shared.bd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.axys.redeflexmobile.shared.models.InformacaoCorban;
import com.axys.redeflexmobile.shared.util.Util_DB;
import com.axys.redeflexmobile.shared.util.Util_IO;
import com.axys.redeflexmobile.ui.redeflex.Config;

import java.util.ArrayList;

public class DBInformacaoCorban {
    private Context mContext;

    public DBInformacaoCorban(Context mContext) {
        this.mContext = mContext;
    }

    public void addInformacaoCorban(InformacaoCorban pInformacaoCorban) throws Exception {
        ContentValues values = new ContentValues();
        values.put("idcliente", pInformacaoCorban.getIdcliente());
        values.put("codigocorban", pInformacaoCorban.getCodigocorban());
        values.put("loja", pInformacaoCorban.getLoja());
        values.put("lod", pInformacaoCorban.getLod());
        values.put("situacao", pInformacaoCorban.getSituacao());
        values.put("valor", pInformacaoCorban.getValor());
        values.put("dias", pInformacaoCorban.getDias());
        values.put("dataatualizacao", Util_IO.dateTimeToString(pInformacaoCorban.getDataatualizacao(), Config.FormatDateTimeStringBanco));
        values.put("dataativacao", Util_IO.dateTimeToString(pInformacaoCorban.getDataativacao(), Config.FormatDateTimeStringBanco));
        values.put("dataultimatransacao", Util_IO.dateTimeToString(pInformacaoCorban.getDataultimatransacao(), Config.FormatDateTimeStringBanco));
        if (!Util_DB.isCadastrado(mContext, "InformacaoCorban", "idcliente", Integer.toString(pInformacaoCorban.getIdcliente()))) {
            SimpleDbHelper.INSTANCE.open(mContext).insert("InformacaoCorban", null, values);
        } else
            SimpleDbHelper.INSTANCE.open(mContext).update("InformacaoCorban", values, "[idcliente]=?", new String[]{Integer.toString(pInformacaoCorban.getIdcliente())});
    }

    public ArrayList<InformacaoCorban> getInformacaoCorban(String pCondicao, String[] pCampos) {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("select * from InformacaoCorban");
        sb.appendLine("where 1=1");

        if (pCondicao != null)
            sb.append(pCondicao);

        Cursor cursor = null;
        ArrayList<InformacaoCorban> lista = new ArrayList<>();
        try {
            cursor = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(sb.toString(), pCampos);
            InformacaoCorban informacaoCorban;
            if (cursor.moveToFirst()) {
                do {
                    informacaoCorban = new InformacaoCorban();
                    informacaoCorban.setIdcliente(cursor.getInt(cursor.getColumnIndexOrThrow("idcliente")));
                    informacaoCorban.setCodigocorban(cursor.getInt(cursor.getColumnIndexOrThrow("codigocorban")));
                    informacaoCorban.setLoja(cursor.getString(cursor.getColumnIndexOrThrow("loja")));
                    informacaoCorban.setLod(cursor.getDouble(cursor.getColumnIndexOrThrow("lod")));
                    informacaoCorban.setSituacao(cursor.getString(cursor.getColumnIndexOrThrow("situacao")));
                    informacaoCorban.setValor(cursor.getDouble(cursor.getColumnIndexOrThrow("valor")));
                    informacaoCorban.setDias(cursor.getInt(cursor.getColumnIndexOrThrow("dias")));
                    informacaoCorban.setDataatualizacao(Util_IO.stringToDate(cursor.getString(cursor.getColumnIndexOrThrow("dataatualizacao")), Config.FormatDateStringBanco));
                    informacaoCorban.setDataativacao(Util_IO.stringToDate(cursor.getString(cursor.getColumnIndexOrThrow("dataativacao")), Config.FormatDateStringBanco));
                    informacaoCorban.setDataultimatransacao(Util_IO.stringToDate(cursor.getString(cursor.getColumnIndexOrThrow("dataultimatransacao")), Config.FormatDateStringBanco));
                    lista.add(informacaoCorban);
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
