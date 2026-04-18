package com.axys.redeflexmobile.shared.bd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.axys.redeflexmobile.shared.models.SolicitacaoPrecoDiferenciado;
import com.axys.redeflexmobile.shared.models.SolicitacaoPrecoDiferenciadoDetalhe;
import com.axys.redeflexmobile.shared.util.Util_DB;
import com.axys.redeflexmobile.shared.util.Util_IO;
import com.axys.redeflexmobile.shared.util.Utilidades;
import com.axys.redeflexmobile.ui.redeflex.Config;

import java.util.ArrayList;
import java.util.Date;

public class BDSolicitacaoPrecoDiferenciado {
    private final Context mContext;
    private final String mTabela = "SolicitacaoPrecoDiferenciado";

    public BDSolicitacaoPrecoDiferenciado(Context pContext) {
        mContext = pContext;
    }

    public long addSolicitacao(SolicitacaoPrecoDiferenciado pSolicitacao) throws Exception {
        ContentValues values = new ContentValues();
        values.put("IdServerSolicitacao", pSolicitacao.getIdServerSolicitacao());
        values.put("IdVendedor", pSolicitacao.getIdVendedor());
        values.put("NomeSolicitante", pSolicitacao.getNomeSolicitante());
        values.put("DataSolicitacao", Util_IO.dateTimeToString(pSolicitacao.getDataSolicitacao(), Config.FormatDateTimeStringBanco));
        values.put("DataInicial", Util_IO.dateTimeToString(pSolicitacao.getDataInicial(), Config.FormatDateTimeStringBanco));
        values.put("DataFinal", Util_IO.dateTimeToString(pSolicitacao.getDataFinal(), Config.FormatDateTimeStringBanco));
        values.put("SituacaoId", pSolicitacao.getSituacaoId());
        values.put("TipoId", pSolicitacao.getTipoId());
        values.put("DataCriacao", Util_IO.dateTimeToString(pSolicitacao.getDataCriacao(), Config.FormatDateTimeStringBanco));
        values.put("DataAtualizacao", Util_IO.dateTimeToString(pSolicitacao.getDataAtualizacao(), Config.FormatDateTimeStringBanco));
        values.put("Sync", pSolicitacao.getSync());
        values.put("DataSincronizacao", Util_IO.dateTimeToString(pSolicitacao.getDataSincronizacao(), Config.FormatDateTimeStringBanco));
        long codigoSolicitacao = 0;

        if (pSolicitacao.getIdServerSolicitacao() > 0) {
            if (!Util_DB.isCadastrado(mContext, mTabela, "IdServerSolicitacao", String.valueOf(pSolicitacao.getIdServerSolicitacao())))
                codigoSolicitacao = SimpleDbHelper.INSTANCE.open(mContext).insert(mTabela, null, values);
            else {
                SimpleDbHelper.INSTANCE.open(mContext).update(mTabela, values, "[IdServerSolicitacao]=?", new String[]{String.valueOf(pSolicitacao.getIdServerSolicitacao())});

                codigoSolicitacao = getByIdServer(String.valueOf(pSolicitacao.getIdServerSolicitacao())).getId();
            }
        } else {
            if (pSolicitacao.getId() <= 0)
                codigoSolicitacao = SimpleDbHelper.INSTANCE.open(mContext).insert(mTabela, null, values);
            else if (!Util_DB.isCadastrado(mContext, mTabela, "Id", String.valueOf(pSolicitacao.getId())))
                codigoSolicitacao = SimpleDbHelper.INSTANCE.open(mContext).insert(mTabela, null, values);
            else
                codigoSolicitacao = SimpleDbHelper.INSTANCE.open(mContext).update(mTabela, values, "[Id]=?", new String[]{String.valueOf(pSolicitacao.getId())});
        }

        return codigoSolicitacao;
    }

    public void deleteById(int pId) {
        SimpleDbHelper.INSTANCE.open(mContext).delete(mTabela, "[Id]=?", new String[]{String.valueOf(pId)});
    }

    public SolicitacaoPrecoDiferenciado getById(String pId) {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("AND [id] = ?");
        return Utilidades.firstOrDefault(getSolicitacao(sb.toString(), new String[]{pId}));
    }

    public SolicitacaoPrecoDiferenciado getByIdServer(String pIdServer) {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("AND [IdServerSolicitacao] = ?");
        return Utilidades.firstOrDefault(getSolicitacao(sb.toString(), new String[]{pIdServer}));
    }

    public ArrayList<SolicitacaoPrecoDiferenciado> getSolicitacao(String pCondicao, String[] pCampos) {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("Select Id, IdServerSolicitacao, IdVendedor, NomeSolicitante, DataSolicitacao, DataInicial, DataFinal,");
        sb.appendLine("SituacaoId, TipoId, DataCriacao, DataAtualizacao, Sync, DataSincronizacao");
        sb.appendLine("from SolicitacaoPrecoDiferenciado");
        sb.appendLine("WHERE 1=1");
        if (pCondicao != null)
            sb.append(pCondicao);
        sb.appendLine(" Order by DataSolicitacao desc");
        return moverCursorDb(sb, pCampos);
    }

    public ArrayList<SolicitacaoPrecoDiferenciado> getSolicitacaoPendente() {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine(" and [Sync] = 0");
        ArrayList<SolicitacaoPrecoDiferenciado> listaSolicitacao = getSolicitacao(sb.toString(), null);
        for (int aa = 0; aa < listaSolicitacao.size(); aa++) {
            ArrayList<SolicitacaoPrecoDiferenciadoDetalhe> itens =
                    new BDSolicitacaoPrecoDiferenciadoDetalhe(mContext)
                            .getSolicitacaoDetalhes(" and IdSolicitacao = " + listaSolicitacao.get(aa).getId(), null);
            listaSolicitacao.get(aa).setItensPrecoDif(itens);
        }
        return listaSolicitacao;
    }

    public void updateSyncSolicitacao(SolicitacaoPrecoDiferenciado pSolicitacao) {
        try {
            ContentValues values = new ContentValues();

            for (int aa = 0; aa < pSolicitacao.getItensPrecoDif().size(); aa++) {
                values = new ContentValues();
                values.put("IdServer", pSolicitacao.getItensPrecoDif().get(aa).getIdServer());
                SimpleDbHelper.INSTANCE.open(mContext).update(
                        "SolicitacaoPrecoDiferenciadoDetalhe",
                        values,
                        "[id]=?",
                        new String[]{String.valueOf(pSolicitacao.getItensPrecoDif().get(aa).getId())});
            }
            values = new ContentValues();
            values.put("IdServerSolicitacao", pSolicitacao.getIdServerSolicitacao());

            values.put("SituacaoId", pSolicitacao.getSituacaoId());

            values.put("DataSincronizacao",
                    Util_IO.dateTimeToString(new Date(), Config.FormatDateTimeStringBanco));
            values.put("Sync", 1);

            SimpleDbHelper.INSTANCE.open(mContext).update(
                    "SolicitacaoPrecoDiferenciado",
                    values,
                    "[id]=?",
                    new String[]{String.valueOf(pSolicitacao.getId())});
        } catch (Exception ex) {
            Log.d("Roni", "updateSyncConsignado: " + ex.getMessage());
        }
    }

    private ArrayList<SolicitacaoPrecoDiferenciado> moverCursorDb(Util_IO.StringBuilder sb, String[] args) {
        Cursor cursor = null;
        ArrayList<SolicitacaoPrecoDiferenciado> lista = new ArrayList<>();
        try {
            cursor = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(sb.toString(), args);
            SolicitacaoPrecoDiferenciado solicitacao;
            if (cursor.moveToFirst()) {
                do {
                    solicitacao = new SolicitacaoPrecoDiferenciado();
                    solicitacao.setId(cursor.getInt(cursor.getColumnIndexOrThrow("Id")));
                    solicitacao.setIdServerSolicitacao(cursor.getInt(cursor.getColumnIndexOrThrow("IdServerSolicitacao")));
                    solicitacao.setIdVendedor(cursor.getInt(cursor.getColumnIndexOrThrow("IdVendedor")));
                    solicitacao.setNomeSolicitante(cursor.getString(cursor.getColumnIndexOrThrow("NomeSolicitante")));
                    solicitacao.setDataSolicitacao(Util_IO.stringToDate(cursor.getString(cursor.getColumnIndexOrThrow("DataSolicitacao")), Config.FormatDateTimeStringBanco));
                    solicitacao.setDataInicial(Util_IO.stringToDate(cursor.getString(cursor.getColumnIndexOrThrow("DataInicial")), Config.FormatDateTimeStringBanco));
                    solicitacao.setDataFinal(Util_IO.stringToDate(cursor.getString(cursor.getColumnIndexOrThrow("DataFinal")), Config.FormatDateTimeStringBanco));
                    solicitacao.setSituacaoId(cursor.getInt(cursor.getColumnIndexOrThrow("SituacaoId")));
                    solicitacao.setTipoId(cursor.getInt(cursor.getColumnIndexOrThrow("TipoId")));
                    solicitacao.setDataCriacao(Util_IO.stringToDate(cursor.getString(cursor.getColumnIndexOrThrow("DataCriacao")), Config.FormatDateTimeStringBanco));
                    solicitacao.setDataAtualizacao(Util_IO.stringToDate(cursor.getString(cursor.getColumnIndexOrThrow("DataAtualizacao")), Config.FormatDateTimeStringBanco));
                    solicitacao.setSync(cursor.getInt(cursor.getColumnIndexOrThrow("Sync")));
                    solicitacao.setDataSincronizacao(Util_IO.stringToDate(cursor.getString(cursor.getColumnIndexOrThrow("DataSincronizacao")), Config.FormatDateTimeStringBanco));
                    lista.add(solicitacao);
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