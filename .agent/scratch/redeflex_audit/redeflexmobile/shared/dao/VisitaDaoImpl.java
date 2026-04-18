package com.axys.redeflexmobile.shared.dao;

import android.content.Context;
import android.database.Cursor;

import com.axys.redeflexmobile.shared.bd.SimpleDbHelper;
import com.axys.redeflexmobile.shared.models.Visita;
import com.axys.redeflexmobile.shared.util.Util_IO;
import com.axys.redeflexmobile.ui.redeflex.Config;

public class VisitaDaoImpl implements VisitaDao {

    private Context context;

    public VisitaDaoImpl(Context context) {
        this.context = context;
    }

    public Visita obterVisitaPorId(int id) {
        String sql = "SELECT t0.id " +
                ",t0.idCliente " +
                ",t0.dataInicio " +
                ",t0.dataFim " +
                ",IFNULL(t0.idMotivo,0) " +
                ",t1.nomeFantasia " +
                ",t1.idclienteintraflex " +
                ",IFNULL(t0.idProjeto,0) " +
                ",CASE UPPER(IFNULL(t1.exibirCodigo,'')) " +
                " WHEN 'SGV' THEN IFNULL(t1.codigoSGV,t1.idclienteintraflex) " +
                " WHEN 'EFRESH' THEN IFNULL(t1.codigoeFresh,t1.idclienteintraflex) " +
                " WHEN 'APLIC' THEN IFNULL(t1.codigoAplic,t1.idclienteintraflex) " +
                " ELSE t1.idclienteintraflex END " +
                ",IFNULL(t0.distancia,0) " +
                "FROM [Visita] t0 " +
                "LEFT OUTER JOIN [Cliente] t1 ON (t1.id = t0.idCliente) " +
                "WHERE t0.id = ? ";

        try (Cursor cursor = SimpleDbHelper.INSTANCE.open(context).rawQuery(sql, new String[]{String.valueOf(id)})) {
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                Visita visita = new Visita();
                visita.setId(cursor.getInt(0));
                visita.setIdCliente(cursor.getString(1));
                visita.setDataInicio(Util_IO.stringToDate(cursor.getString(2), Config.FormatDateTimeStringBanco));
                visita.setDataFim(Util_IO.stringToDate(cursor.getString(3), Config.FormatDateTimeStringBanco));
                visita.setIdMotivo(cursor.getInt(4));
                visita.setNomeFantasia(cursor.getString(5));
                visita.setIdClienteIntraFlex(cursor.getString(6));
                visita.setIdProjetoTrade(cursor.getString(7));
                visita.setCodigoExibirCliente(cursor.getString(8));
                visita.setDistancia(cursor.getDouble(9));
                return visita;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    public Visita retornaUltimaVisitaByClienteId(String pIdCliente) {
        String querySql = "SELECT id,idCliente,dataInicio,dataFim,IFNULL(idMotivo,0),origem FROM [Visita] WHERE id IN ";
        querySql += String.format("(SELECT MAX(id) FROM [Visita] WHERE idCliente = %s)", pIdCliente);

        Cursor cursor = null;
        try {
            cursor = SimpleDbHelper.INSTANCE.open(context).rawQuery(querySql, null);
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                Visita visita = new Visita();
                visita.setId(cursor.getInt(0));
                visita.setIdCliente(cursor.getString(1));
                visita.setDataInicio(Util_IO.stringToDate(cursor.getString(2), Config.FormatDateTimeStringBanco));
                visita.setDataFim(Util_IO.stringToDate(cursor.getString(3), Config.FormatDateTimeStringBanco));
                visita.setIdMotivo(cursor.getInt(4));
                visita.setOrigem(cursor.getInt(5));
                return visita;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }
}
