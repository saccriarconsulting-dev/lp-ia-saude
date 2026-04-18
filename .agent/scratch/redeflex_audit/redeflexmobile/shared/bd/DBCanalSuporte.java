package com.axys.redeflexmobile.shared.bd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.axys.redeflexmobile.shared.models.CanalSuporte;
import com.axys.redeflexmobile.shared.util.Util_IO;
import com.axys.redeflexmobile.shared.util.Utilidades;
import com.axys.redeflexmobile.ui.redeflex.Config;

import java.util.ArrayList;

/**
 * Created by joao.viana on 10/01/2018.
 */

public class DBCanalSuporte {
    private Context mContext;
    private String mTabela = "Suporte";

    public DBCanalSuporte(Context pContext) {
        mContext = pContext;
    }

    public void add(CanalSuporte pCanalSuporte) throws Exception {
        ContentValues values = new ContentValues();
        values.put("problema", pCanalSuporte.getProblema());
        values.put("versao", pCanalSuporte.getVersaoApp());
        if (!Util_IO.isNullOrEmpty(pCanalSuporte.getDescricao()))
            values.put("descricao", pCanalSuporte.getDescricao());
        if (!Util_IO.isNullOrEmpty(pCanalSuporte.getLocalArquivo()))
            values.put("localArquivo", pCanalSuporte.getLocalArquivo());
        SimpleDbHelper.INSTANCE.open(mContext).insert(mTabela, null, values);
    }

    public CanalSuporte getById(int pId) {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("AND [id] = ?");
        return Utilidades.firstOrDefault(getCanalSuportes(sb.toString(), new String[]{String.valueOf(pId)}));
    }

    public void deleteById(int pId) {
        SimpleDbHelper.INSTANCE.open(mContext).delete(mTabela, "[id]=?", new String[]{String.valueOf(pId)});
    }

    private ArrayList<CanalSuporte> getCanalSuportes(String pCondicao, String[] pCampos) {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("SELECT [id]");
        sb.appendLine(",[problema]");
        sb.appendLine(",[data]");
        sb.appendLine(",[versao]");
        sb.appendLine(",[descricao]");
        sb.appendLine(",[localArquivo]");
        sb.appendLine(",[sync]");
        sb.appendLine(",(SELECT id FROM Colaborador) idVendedor");
        sb.appendLine("FROM [Suporte]");
        sb.appendLine("WHERE 1=1");
        if (pCondicao != null)
            sb.append(pCondicao);

        Cursor cursor = null;
        ArrayList<CanalSuporte> lista = new ArrayList<>();
        try {
            cursor = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(sb.toString(), pCampos);
            CanalSuporte canalsuporte;
            if (cursor.moveToFirst()) {
                do {
                    canalsuporte = new CanalSuporte();
                    canalsuporte.setCodigo(cursor.getInt(0));
                    canalsuporte.setProblema(cursor.getString(1));
                    canalsuporte.setDataHora(Util_IO.stringToDate(cursor.getString(2), Config.FormatDateTimeStringBanco));
                    canalsuporte.setVersaoApp(cursor.getString(3));
                    canalsuporte.setDescricao(cursor.getString(4));
                    canalsuporte.setLocalArquivo(cursor.getString(5));
                    canalsuporte.setIdVendedor(cursor.getInt(7));
                    lista.add(canalsuporte);
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

    public ArrayList<CanalSuporte> getPendente() {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("AND [sync] = 0");
        return getCanalSuportes(sb.toString(), null);
    }

    public void deleteAll() {
        SimpleDbHelper.INSTANCE.open(mContext).delete(mTabela, null, null);
    }
}