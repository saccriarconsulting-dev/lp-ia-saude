package com.axys.redeflexmobile.shared.bd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.axys.redeflexmobile.BuildConfig;
import com.axys.redeflexmobile.shared.models.ItemVenda;
import com.axys.redeflexmobile.shared.models.Venda;
import com.axys.redeflexmobile.shared.models.Visita;
import com.axys.redeflexmobile.shared.util.Util_IO;
import com.axys.redeflexmobile.ui.redeflex.Config;

import java.util.ArrayList;
import java.util.Date;

public class DBVisita {
    final private String mTabelaVisita = "Visita";
    final private String mTabelaAuditagemCliente = "AuditagemCliente";
    private Context mContext;

    public DBVisita(Context pContext) {
        this.mContext = pContext;
    }

    public long novaVisita(String idCliente,
                           double latitute,
                           double longitude,
                           double precisao,
                           double distancia,
                           String idProjeto,
                           int origem) {

        ContentValues values = new ContentValues();
        values.put("latitude", latitute);
        values.put("longitude", longitude);
        values.put("precisao", precisao);
        values.put("distancia", distancia);
        values.put("idCliente", idCliente);
        values.put("versaoApp", BuildConfig.VERSION_NAME);
        values.put("origem", origem);

        if (!Util_IO.isNullOrEmpty(idProjeto)) {
            values.put("idProjeto", idProjeto);
        }

        return SimpleDbHelper.INSTANCE.open(mContext).insert(mTabelaVisita, null, values);
    }

    public void updateGeolocalizacaoVisita(Visita visita) {
        ContentValues values = new ContentValues();
        values.put("latitude", visita.getLatitude());
        values.put("longitude", visita.getLongitude());
        values.put("precisao", visita.getPrecisao());

        SimpleDbHelper.INSTANCE.open(mContext).update(mTabelaVisita, values, "[id]=?", new String[]{String.valueOf(visita.getId())});
    }

    public Visita getVisitaAtiva() {
        String selectQuery = "SELECT id,idCliente,dataInicio,dataFim,IFNULL(idMotivo,0) FROM [Visita] WHERE dataFim IS NULL";
        Cursor cursor = null;
        try {
            cursor = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(selectQuery, null);
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                Visita visita = new Visita();
                visita.setId(cursor.getInt(0));
                visita.setIdCliente(cursor.getString(1));
                visita.setDataInicio(Util_IO.stringToDate(cursor.getString(2), Config.FormatDateTimeStringBanco));
                visita.setDataFim(Util_IO.stringToDate(cursor.getString(3), Config.FormatDateTimeStringBanco));
                visita.setIdMotivo(cursor.getInt(4));
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

    public Visita getVisitabyId(int pId) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT t0.id ");
        sb.append(",t0.idCliente ");
        sb.append(",t0.dataInicio ");
        sb.append(",t0.dataFim ");
        sb.append(",IFNULL(t0.idMotivo,0) ");
        sb.append(",t1.nomeFantasia ");
        sb.append(",t1.idclienteintraflex ");
        sb.append(",IFNULL(t0.idProjeto,0) ");
        sb.append(",CASE UPPER(IFNULL(t1.exibirCodigo,'')) ");
        sb.append(" WHEN 'SGV' THEN IFNULL(t1.codigoSGV,t1.idclienteintraflex) ");
        sb.append(" WHEN 'EFRESH' THEN IFNULL(t1.codigoeFresh,t1.idclienteintraflex) ");
        sb.append(" WHEN 'APLIC' THEN IFNULL(t1.codigoAplic,t1.idclienteintraflex) ");
        sb.append(" ELSE t1.idclienteintraflex END ");
        sb.append(",IFNULL(t0.distancia,0) ");
        sb.append(",t0.origem ");
        sb.append("FROM [Visita] t0 ");
        sb.append("LEFT OUTER JOIN [Cliente] t1 ON (t1.id = t0.idCliente) ");
        sb.append("WHERE t0.id = ? ");

        Cursor cursor = null;
        try {
            cursor = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(sb.toString(), new String[]{String.valueOf(pId)});
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
                visita.setOrigem(cursor.getInt(10));
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

    public ArrayList<Visita> getPendentes() {
        ArrayList<Visita> list = new ArrayList<>();
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("SELECT t0.id");
        sb.appendLine(",IFNULL(t0.idServer,0)");
        sb.appendLine(",t0.idMotivo");
        sb.appendLine(",t0.dataInicio");
        sb.appendLine(",t0.dataFim");
        sb.appendLine(",t0.idCliente");
        sb.appendLine(",(SELECT id FROM Colaborador) idVendedor");
        sb.appendLine(",IFNULL(t0.latitude,0)");
        sb.appendLine(",IFNULL(t0.longitude,0)");
        sb.appendLine(",IFNULL(t0.precisao,0)");
        sb.appendLine(",t0.versaoApp");
        sb.appendLine(",IFNULL(t0.distancia,0)");
        sb.appendLine("FROM [Visita] t0");
        sb.appendLine("WHERE t0.dataFim IS NOT NULL AND t0.sync = 0");

        Cursor cursor = null;
        Visita visita;
        try {
            cursor = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(sb.toString(), null);
            if (cursor != null && cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    do {
                        visita = new Visita();
                        visita.setId(cursor.getInt(0));
                        visita.setIdServer(cursor.getInt(1));
                        visita.setIdMotivo(cursor.getInt(2));
                        visita.setDataInicio(Util_IO.stringToDate(cursor.getString(3), Config.FormatDateTimeStringBanco));
                        visita.setDataFim(Util_IO.stringToDate(cursor.getString(4), Config.FormatDateTimeStringBanco));
                        visita.setIdCliente(cursor.getString(5));
                        visita.setIdVendedor(cursor.getString(6));
                        visita.setLatitude(cursor.getDouble(7));
                        visita.setLongitude(cursor.getDouble(8));
                        visita.setPrecisao(cursor.getDouble(9));
                        visita.setVersaoApp(cursor.getString(10));
                        visita.setDistancia(cursor.getDouble(11));
                        list.add(visita);
                    } while (cursor.moveToNext());
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return list;
    }

    public void updateIdServer(String idServer, int idapp) {
        ContentValues values = new ContentValues();
        values.put("idServer", idServer);
        values.put("sync", 1);
        SimpleDbHelper.INSTANCE.open(mContext).update(mTabelaVisita, values, "[id]=?", new String[]{String.valueOf(idapp)});
    }

    public void deleteAll() {
        SimpleDbHelper.INSTANCE.open(mContext).delete(mTabelaVisita, null, null);
    }

    public void deletaVisitas60dias() {
        SimpleDbHelper.INSTANCE.open(mContext).delete(mTabelaVisita, "[dataFim] < datetime('now', '-60 day') AND [sync]=1", null);
    }

    public Visita retornaUltimaVisitaByClienteId(String pIdCliente) {
        String querySql = "SELECT id,idCliente,dataInicio,dataFim,IFNULL(idMotivo,0) FROM [Visita] WHERE id IN ";
        querySql += String.format("(SELECT MAX(id) FROM [Visita] WHERE idCliente = %s)", pIdCliente);
        Cursor cursor = null;
        try {
            cursor = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(querySql, null);
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                Visita visita = new Visita();
                visita.setId(cursor.getInt(0));
                visita.setIdCliente(cursor.getString(1));
                visita.setDataInicio(Util_IO.stringToDate(cursor.getString(2), Config.FormatDateTimeStringBanco));
                visita.setDataFim(Util_IO.stringToDate(cursor.getString(3), Config.FormatDateTimeStringBanco));
                visita.setIdMotivo(cursor.getInt(4));
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

    public long addVisita(Visita visita) {
        ContentValues values = new ContentValues();
        values.put("idMotivo", visita.getIdMotivo());
        values.put("dataInicio", Util_IO.dateTimeToString(visita.getDataInicio(), Config.FormatDateTimeStringBanco));
        values.put("idCliente", visita.getIdCliente());
        values.put("versaoApp", visita.getVersaoApp());
        values.put("latitude", visita.getLatitude());
        values.put("longitude", visita.getLongitude());
        values.put("precisao", visita.getPrecisao());
        values.put("distancia", visita.getDistancia());

        if (visita.getDataFim() != null) {
            values.put("dataFim", Util_IO.dateTimeToString(visita.getDataFim(), Config.FormatDateTimeStringBanco));
        }

        return SimpleDbHelper.INSTANCE.open(mContext).insert(mTabelaVisita, null, values);
    }

    public void encerrarVisita(int pIdVisita) {
        ContentValues values = new ContentValues();
        values.put("idMotivo", 0);
        values.put("dataFim", Util_IO.dateTimeToString(new Date(), Config.FormatDateTimeStringBanco));
        SimpleDbHelper.INSTANCE.open(mContext).update(mTabelaVisita, values, "[id]=?", new String[]{String.valueOf(pIdVisita)});
    }

    public void encerrarVisita(int pIdVisita, int pIdMotivo, boolean pRemoveVenda) {
        if (pRemoveVenda) {
            new DBVenda(mContext).cancelaVendaByIdVisita(pIdVisita);
        } else {
            new DBVenda(mContext).concluiVendaByIdVisita(pIdVisita);
        }

        ContentValues values = new ContentValues();
        values.put("idMotivo", pIdMotivo);
        values.put("dataFim", Util_IO.dateTimeToString(new Date(), Config.FormatDateTimeStringBanco));
        SimpleDbHelper.INSTANCE.open(mContext).update(mTabelaVisita, values, "[id]=?", new String[]{String.valueOf(pIdVisita)});
    }

    public void encerrarVisita(int pIdVisita, int pIdMotivo, boolean pRemoveVenda, String idCliente) {
        DBEstoque dbEstoque = new DBEstoque(mContext);
        // Estava removendo toda auditagem feita no cliente quando finalizava a Visita
        // antes de estar sincronizando as informações
        //dbEstoque.deletaTodaAuditagemCliente(idCliente);

        DBVenda dbVenda = new DBVenda(mContext);
        Venda venda = dbVenda.getVendabyIdVisita(pIdVisita);
        if (venda != null) {
            ArrayList<ItemVenda> items = dbVenda.getItensVendabyIdVenda(venda.getId());
            // TODO: Validar posteriormente, pois estava impactando na producao
//            dbEstoque.atualizaEstoqueVenda(items);
        }

        if (pRemoveVenda) {
            dbVenda.cancelaVendaByIdVisita(pIdVisita);
            excluirMerchandising(pIdVisita);
        } else {
            dbVenda.concluiVendaByIdVisita(pIdVisita);
        }

        ContentValues values = new ContentValues();
        values.put("idMotivo", pIdMotivo);
        values.put("dataFim", Util_IO.dateTimeToString(new Date(), Config.FormatDateTimeStringBanco));
        SimpleDbHelper.INSTANCE.open(mContext).update(mTabelaVisita, values, "[id]=?", new String[]{String.valueOf(pIdVisita)});
    }

    public void encerrarVisitaSemDataFim(int pIdVisita, int pIdMotivo, boolean pRemoveVenda, String idCliente) {
        DBEstoque dbEstoque = new DBEstoque(mContext);
        // Estava removendo toda auditagem feita no cliente quando finalizava a Visita
        // antes de estar sincronizando as informações
        //dbEstoque.deletaTodaAuditagemCliente(idCliente);

        DBVenda dbVenda = new DBVenda(mContext);
        Venda venda = dbVenda.getVendabyIdVisita(pIdVisita);
        if (venda != null) {
            ArrayList<ItemVenda> items = dbVenda.getItensVendabyIdVenda(venda.getId());
            dbEstoque.atualizaEstoqueVenda(items);
        }

        if (pRemoveVenda) {
            dbVenda.cancelaVendaByIdVisita(pIdVisita);
            excluirMerchandising(pIdVisita);
        } else {
            dbVenda.concluiVendaByIdVisita(pIdVisita);
        }

        ContentValues values = new ContentValues();
        values.put("idMotivo", pIdMotivo);
        values.put("dataFim", Util_IO.dateTimeToString(new Date(), Config.FormatDateTimeStringBanco));
        SimpleDbHelper.INSTANCE.open(mContext).update(mTabelaVisita, values, "[id]=?", new String[]{String.valueOf(pIdVisita)});
    }

    public boolean temVisitasHoje() {
        final SQLiteDatabase db = SimpleDbHelper.INSTANCE.open(mContext);
        final String sql = "SELECT * FROM Visita WHERE date(dataInicio) = date()";
        try (Cursor cursor = db.rawQuery(sql, null)) {
            return cursor.getCount() > 0;
        } catch (Exception ignored) {
            return false;
        }
    }

    private void excluirMerchandising(int idVisita) {
        String whereClauseIdMerchandising = "[idMerchandising]=?";
        DBMerchandising dbMerchandising = new DBMerchandising(mContext);

        int idMerchandising = (int) dbMerchandising.obterIdMerchandisingPorIdVisita(idVisita, 0);
        int idMerchandisingPadrao = (int) dbMerchandising.obterIdMerchandisingPadraoPorIdMerchandising(idMerchandising);

        SimpleDbHelper.INSTANCE.open(mContext).delete(DBMerchandising.tabelaMerchandising, "[idVisita]=?", new String[]{String.valueOf(idVisita)});
        SimpleDbHelper.INSTANCE.open(mContext).delete(DBMerchandising.tabelaMerchandisingEnvelopamento, whereClauseIdMerchandising, new String[]{String.valueOf(idMerchandising)});
        SimpleDbHelper.INSTANCE.open(mContext).delete(DBMerchandising.tabelaMerchandisingNenhum, whereClauseIdMerchandising, new String[]{String.valueOf(idMerchandising)});
        SimpleDbHelper.INSTANCE.open(mContext).delete(DBMerchandising.tabelaMerchandisingFachada, whereClauseIdMerchandising, new String[]{String.valueOf(idMerchandising)});
        SimpleDbHelper.INSTANCE.open(mContext).delete(DBMerchandising.tabelaMerchandisingPadrao, whereClauseIdMerchandising, new String[]{String.valueOf(idMerchandising)});
        SimpleDbHelper.INSTANCE.open(mContext).delete(DBMerchandising.tabelaMerchandisingProdutoPadrao, "[idPadrao]=?", new String[]{String.valueOf(idMerchandisingPadrao)});
    }

    public void encerrarVisita(int pIdVisita, int pIdMotivo) {
        ContentValues values = new ContentValues();
        values.put("idMotivo", pIdMotivo);
        values.put("dataFim", Util_IO.dateTimeToString(new Date(), Config.FormatDateTimeStringBanco));
        SimpleDbHelper.INSTANCE.open(mContext).update(mTabelaVisita, values, "[id]=?", new String[]{String.valueOf(pIdVisita)});
    }
}