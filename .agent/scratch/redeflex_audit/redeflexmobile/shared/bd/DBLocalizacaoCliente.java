package com.axys.redeflexmobile.shared.bd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.axys.redeflexmobile.shared.models.LocalizacaoCliente;
import com.axys.redeflexmobile.shared.util.Util_IO;
import com.axys.redeflexmobile.shared.util.Utilidades;
import com.axys.redeflexmobile.ui.redeflex.Config;

import java.util.ArrayList;

/**
 * Created by joao.viana on 24/10/2016.
 */

public class DBLocalizacaoCliente {
    private Context mContext;
    private String mTabela = "LocalizacaoCliente";

    public DBLocalizacaoCliente(Context pContext) {
        mContext = pContext;
    }

    public void addLocalizacao(LocalizacaoCliente obj) {
        ContentValues values = new ContentValues();
        values.put("latitude", obj.getLatitude());
        values.put("longitude", obj.getLongitude());
        values.put("idCliente", obj.getIdCliente());
        values.put("precisao", obj.getPrecisao());
        values.put("localArquivo", obj.getLocalArquivo());
        values.put("tipoId", obj.getTipoId());

        SimpleDbHelper.INSTANCE.open(mContext).insert(mTabela, null, values);
    }

    private Util_IO.StringBuilder retornaQuery() {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("SELECT [id]");
        sb.appendLine(",[latitude]");
        sb.appendLine(",[longitude]");
        sb.appendLine(",[idCliente]");
        sb.appendLine(",[precisao]");
        sb.appendLine(",[datagps]");
        sb.appendLine(",[sync]");
        sb.appendLine(",[anexo]");
        sb.appendLine(",[localArquivo]");
        sb.appendLine(",[tipoId]");
        sb.appendLine("FROM [LocalizacaoCliente]");
        sb.appendLine("WHERE 1=1");
        return sb;
    }

    public boolean jaCapturouCoordenadas(String pIdCliente) {
        Util_IO.StringBuilder sb = retornaQuery();
        sb.appendLine("AND [idCliente] = ?");
        sb.appendLine("AND date([datagps]) = date('now','localtime')");
        Cursor cursor = null;

        try {
            cursor = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(sb.toString(), new String[]{pIdCliente});
            return (cursor != null && cursor.getCount() > 0);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return false;
    }

    public ArrayList<LocalizacaoCliente> getLocalizacaoPendente() {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("SELECT id");
        sb.appendLine(",latitude");
        sb.appendLine(",longitude");
        sb.appendLine(",idCliente");
        sb.appendLine(",precisao");
        sb.appendLine(",datagps");
        sb.appendLine(",(SELECT id FROM Colaborador) idVendedor");
        sb.appendLine(",localArquivo");
        sb.appendLine(",tipoId");
        sb.appendLine("FROM [LocalizacaoCliente]");
        sb.appendLine("WHERE sync = 0");

        ArrayList<LocalizacaoCliente> lista = new ArrayList<>();
        Cursor cursor = null;

        try {
            cursor = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(sb.toString(), null);
            LocalizacaoCliente obj;
            if (cursor.moveToFirst()) {
                do {
                    obj = new LocalizacaoCliente();
                    obj.setId(cursor.getInt(0));
                    obj.setLatitude(cursor.getDouble(1));
                    obj.setLongitude(cursor.getDouble(2));
                    obj.setIdCliente(cursor.getString(3));
                    obj.setPrecisao(cursor.getDouble(4));
                    obj.setData(Util_IO.stringToDate(cursor.getString(5), Config.FormatDateTimeStringBanco));
                    obj.setIdVendedor(cursor.getString(6));
                    obj.setLocalArquivo(cursor.getString(7));
                    obj.setTipoId(cursor.getInt(8));
                    lista.add(obj);
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

    public void updateSync(int pCodigo) {
        ContentValues values = new ContentValues();
        values.put("sync", 1);
        SimpleDbHelper.INSTANCE.open(mContext).update(mTabela, values, "[id]=?", new String[]{String.valueOf(pCodigo)});
    }

    public void deleteOk() {
        Cursor cursor = null;
        try {
            Util_IO.StringBuilder sb = retornaQuery();
            sb.appendLine("AND sync = 1");
            sb.appendLine("AND date([datagps]) < date('now', '-10 day')");
            cursor = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(sb.toString(), null);
            if (cursor.moveToFirst()) {
                do {
                    Utilidades.deletaArquivo(cursor.getString(8));
                    SimpleDbHelper.INSTANCE.open(mContext).delete(mTabela, "[id]=? ", new String[]{cursor.getString(0)});
                } while (cursor.moveToNext());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
    }

    public void deleteAll() {
        SimpleDbHelper.INSTANCE.open(mContext).delete(mTabela, null, null);
    }

    public void deleteById(int id) {
        SimpleDbHelper.INSTANCE.open(mContext).delete(mTabela, "[id]=?", new String[]{String.valueOf(id)});
    }
}