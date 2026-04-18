package com.axys.redeflexmobile.shared.bd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.axys.redeflexmobile.shared.models.Telemetria;
import com.axys.redeflexmobile.shared.util.Util_IO;
import com.axys.redeflexmobile.ui.redeflex.Config;

import java.util.ArrayList;

/**
 * Created by Desenvolvimento on 10/05/2016.
 */
public class DBTelemetria {
    Context mContext;
    private String mTabela = "Telemetria";

    public DBTelemetria(Context pContext) {
        mContext = pContext;
    }

    public void add(Telemetria pTelemetria) {
        ContentValues values = new ContentValues();
        values.put("idVendedor", pTelemetria.getIdVendedor());
        values.put("modeloAparelho", pTelemetria.getModeloAparelho());
        values.put("versaoOs", pTelemetria.getVersaoOs());
        values.put("versaoApp", pTelemetria.getVersaoApp());
        values.put("imei", pTelemetria.getImei());
        values.put("bateria", pTelemetria.getBateria());
        values.put("tipoInternet", pTelemetria.getTipoInternet());
        values.put("latitude", pTelemetria.getLatitude());
        values.put("longitude", pTelemetria.getLongitude());
        values.put("precisao", pTelemetria.getPrecisao());
        values.put("tipo", 0);
        values.put("qtdVisitaPend", pTelemetria.getQtdVisitaPend());
        values.put("qtdVendaPend", pTelemetria.getQtdVendaPend());
        values.put("qtdVendaItensPend", pTelemetria.getQtdVendaItensPend());
        values.put("qtdCodBarraPend", pTelemetria.getQtdCodBarraPend());

        SimpleDbHelper.INSTANCE.open(mContext).insert(mTabela, null, values);
    }

    public ArrayList<Telemetria> getAll() {
        Util_IO.StringBuilder sbSQL = new Util_IO.StringBuilder();
        sbSQL.appendLine("SELECT id");
        sbSQL.appendLine(",idVendedor");
        sbSQL.appendLine(",modeloAparelho");
        sbSQL.appendLine(",versaoOs");
        sbSQL.appendLine(",versaoApp");
        sbSQL.appendLine(",bateria");
        sbSQL.appendLine(",tipoInternet");
        sbSQL.appendLine(",latitude");
        sbSQL.appendLine(",longitude");
        sbSQL.appendLine(",precisao");
        sbSQL.appendLine(",dataGps");
        sbSQL.appendLine(",imei");
        sbSQL.appendLine(",qtdVisitaPend");
        sbSQL.appendLine(",qtdVendaPend");
        sbSQL.appendLine(",qtdVendaItensPend");
        sbSQL.appendLine(",qtdCodBarraPend");
        sbSQL.appendLine("FROM [Telemetria] ");
        sbSQL.appendLine("WHERE IFNULL(tipo,0) = 0");
        sbSQL.appendLine("ORDER BY id");
        ArrayList<Telemetria> lista = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(sbSQL.toString(), null);
            Telemetria telemetria;
            if (cursor.moveToFirst()) {
                do {
                    telemetria = new Telemetria();
                    telemetria.setId(cursor.getInt(0));
                    telemetria.setIdVendedor(cursor.getInt(1));
                    telemetria.setModeloAparelho(cursor.getString(2));
                    telemetria.setVersaoOs(cursor.getString(3));
                    telemetria.setVersaoApp(cursor.getString(4));
                    telemetria.setBateria(cursor.getInt(5));
                    telemetria.setTipoInternet(cursor.getString(6));
                    telemetria.setLatitude(cursor.getDouble(7));
                    telemetria.setLongitude(cursor.getDouble(8));
                    telemetria.setPrecisao(cursor.getDouble(9));
                    telemetria.setDataGps(Util_IO.stringToDate(cursor.getString(10), Config.FormatDateTimeStringBanco));
                    telemetria.setImei(cursor.getString(11));
                    telemetria.setQtdVisitaPend(cursor.getInt(12));
                    telemetria.setQtdVendaPend(cursor.getInt(13));
                    telemetria.setQtdVendaItensPend(cursor.getInt(14));
                    telemetria.setQtdCodBarraPend(cursor.getInt(15));
                    lista.add(telemetria);
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

    public void delete(int id) {
        SimpleDbHelper.INSTANCE.open(mContext).delete(mTabela, "[id]=?", new String[]{String.valueOf(id)});
    }

    public void deleteAll() {
        SimpleDbHelper.INSTANCE.open(mContext).delete(mTabela, null, null);
    }

    public void addNova(Telemetria pTelemetria) {
        ContentValues values = new ContentValues();
        values.put("idVendedor", pTelemetria.getIdVendedor());
        values.put("modeloAparelho", pTelemetria.getModeloAparelho());
        values.put("versaoOs", pTelemetria.getVersaoOs());
        values.put("versaoApp", pTelemetria.getVersaoApp());
        values.put("imei", pTelemetria.getImei());
        values.put("bateria", pTelemetria.getBateria());
        values.put("tipoInternet", pTelemetria.getTipoInternet());
        values.put("latitude", pTelemetria.getLatitude());
        values.put("longitude", pTelemetria.getLongitude());
        values.put("precisao", pTelemetria.getPrecisao());
        values.put("tipo", 1);
        values.put("qtdVisitaPend", pTelemetria.getQtdVisitaPend());
        values.put("qtdVendaPend", pTelemetria.getQtdVendaPend());
        values.put("qtdVendaItensPend", pTelemetria.getQtdVendaItensPend());
        values.put("qtdCodBarraPend", pTelemetria.getQtdCodBarraPend());
        values.put("qtdCadCliPend", pTelemetria.getQtdCadCliPend());
        values.put("qtdDocCliPend", pTelemetria.getQtdDocCliPend());
        values.put("qtdLocCliPend", pTelemetria.getQtdLocCliPend());
        values.put("qtdRemessaPend", pTelemetria.getQtdRemessaPend());
        values.put("qtdItemRemessaPend", pTelemetria.getQtdItemRemessaPend());

        SimpleDbHelper.INSTANCE.open(mContext).insert(mTabela, null, values);
    }

    public ArrayList<Telemetria> getNova() {
        Util_IO.StringBuilder sbSQL = new Util_IO.StringBuilder();
        sbSQL.appendLine("SELECT id");
        sbSQL.appendLine(",idVendedor");
        sbSQL.appendLine(",modeloAparelho");
        sbSQL.appendLine(",versaoOs");
        sbSQL.appendLine(",versaoApp");
        sbSQL.appendLine(",bateria");
        sbSQL.appendLine(",tipoInternet");
        sbSQL.appendLine(",latitude");
        sbSQL.appendLine(",longitude");
        sbSQL.appendLine(",precisao");
        sbSQL.appendLine(",dataGps");
        sbSQL.appendLine(",imei");
        sbSQL.appendLine(",IFNULL(qtdVisitaPend,0)");
        sbSQL.appendLine(",IFNULL(qtdVendaPend,0)");
        sbSQL.appendLine(",IFNULL(qtdVendaItensPend,0)");
        sbSQL.appendLine(",IFNULL(qtdCodBarraPend,0)");
        sbSQL.appendLine(",IFNULL(qtdCadCliPend,0)");
        sbSQL.appendLine(",IFNULL(qtdDocCliPend,0)");
        sbSQL.appendLine(",IFNULL(qtdLocCliPend,0)");
        sbSQL.appendLine(",IFNULL(qtdRemessaPend,0)");
        sbSQL.appendLine(",IFNULL(qtdItemRemessaPend,0)");
        sbSQL.appendLine("FROM [Telemetria] ");
        sbSQL.appendLine("WHERE IFNULL(tipo,0) = 1");
        sbSQL.appendLine("ORDER BY id");

        Cursor cursor = null;
        ArrayList<Telemetria> lista = new ArrayList<>();
        try {
            cursor = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(sbSQL.toString(), null);
            Telemetria telemetria;
            if (cursor.moveToFirst()) {
                do {
                    telemetria = new Telemetria();
                    telemetria.setId(cursor.getInt(0));
                    telemetria.setIdVendedor(cursor.getInt(1));
                    telemetria.setModeloAparelho(cursor.getString(2));
                    telemetria.setVersaoOs(cursor.getString(3));
                    telemetria.setVersaoApp(cursor.getString(4));
                    telemetria.setBateria(cursor.getInt(5));
                    telemetria.setTipoInternet(cursor.getString(6));
                    telemetria.setLatitude(cursor.getDouble(7));
                    telemetria.setLongitude(cursor.getDouble(8));
                    telemetria.setPrecisao(cursor.getDouble(9));
                    telemetria.setDataGps(Util_IO.stringToDate(cursor.getString(10), Config.FormatDateTimeStringBanco));
                    telemetria.setImei(cursor.getString(11));
                    telemetria.setQtdVisitaPend(cursor.getInt(12));
                    telemetria.setQtdVendaPend(cursor.getInt(13));
                    telemetria.setQtdVendaItensPend(cursor.getInt(14));
                    telemetria.setQtdCodBarraPend(cursor.getInt(15));
                    telemetria.setQtdCadCliPend(cursor.getInt(16));
                    telemetria.setQtdDocCliPend(cursor.getInt(17));
                    telemetria.setQtdLocCliPend(cursor.getInt(18));
                    telemetria.setQtdRemessaPend(cursor.getInt(19));
                    telemetria.setQtdItemRemessaPend(cursor.getInt(20));
                    lista.add(telemetria);
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