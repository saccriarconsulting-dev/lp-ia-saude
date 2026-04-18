package com.axys.redeflexmobile.shared.bd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.axys.redeflexmobile.shared.models.SolicitacaoPid;
import com.axys.redeflexmobile.shared.models.SolicitacaoPidRetorno;
import com.axys.redeflexmobile.shared.models.SolicitacaoPidStatus;
import com.axys.redeflexmobile.shared.util.DataSyncManager;
import com.axys.redeflexmobile.shared.util.DataSyncValidator;
import com.axys.redeflexmobile.shared.util.Util_DB;
import com.axys.redeflexmobile.shared.util.Util_IO;
import com.axys.redeflexmobile.shared.util.Utilidades;

import java.util.ArrayList;
import java.util.Date;

public class BDSolicitacaoPid {
    @NonNull private final Context mContext;
    @NonNull private final String mTabela = "SolicitacaoPid";

    @NonNull
    private final java.util.List<Runnable> dataSyncFixQueue = new java.util.ArrayList<>();

    public BDSolicitacaoPid(@NonNull Context pContext) {
        mContext = pContext;
    }

    public long addSolicitacaoPid(@NonNull SolicitacaoPid pSolicitacao) throws Exception {
        ContentValues values = new ContentValues();
        values.put("Id", pSolicitacao.getId());
        values.put("Id_Server", pSolicitacao.getId_Server());
        values.put("TipoPessoa", pSolicitacao.getTipoPessoa());
        values.put("TipoCliente", pSolicitacao.getTipoCliente());
        values.put("IdVendedor", pSolicitacao.getIdVendedor());
        values.put("Latitude", pSolicitacao.getLatitude());
        values.put("Longitude", pSolicitacao.getLongitude());
        values.put("Precisao", pSolicitacao.getPrecisao());
        values.put("CodigoSGV", pSolicitacao.getCodigoSGV());
        values.put("CodigoAdquirencia", pSolicitacao.getCodigoAdquirencia());
        values.put("CpfCnpj", pSolicitacao.getCpfCnpj());
        values.put("RazaoSocial", pSolicitacao.getRazaoSocial());
        values.put("NomeFantasia", pSolicitacao.getNomeFantasia());
        values.put("Observacao", pSolicitacao.getObservacao());
        values.put("MCCPrincipal", pSolicitacao.getMCCPrincipal());
        values.put("DDD", pSolicitacao.getDDD());
        values.put("Rede", pSolicitacao.getRede());
        values.put("NDeLojas", pSolicitacao.getNDeLojas());
        values.put("FaturamentoPrevisto", pSolicitacao.getFaturamentoPrevisto());
        values.put("NomeConcorrente", pSolicitacao.getNomeConcorrente());

        if (pSolicitacao.getDistribuicaoDebito().isPresent())
            values.put("DistribuicaoDebito", pSolicitacao.getDistribuicaoDebito().get());
        if (pSolicitacao.getDistribuicaoCredito().isPresent())
            values.put("DistribuicaoCredito", pSolicitacao.getDistribuicaoCredito().get());
        if (pSolicitacao.getDistribuicaoCredito6x().isPresent())
            values.put("DistribuicaoCredito6x", pSolicitacao.getDistribuicaoCredito6x().get());
        if (pSolicitacao.getDistribuicaoCredito12x().isPresent())
            values.put("DistribuicaoCredito12x", pSolicitacao.getDistribuicaoCredito12x().get());

        values.put("Origem", pSolicitacao.getOrigem());
        values.put("Renegociacao", pSolicitacao.getRenegociacao());
        values.put("Contraproposta", pSolicitacao.getContraproposta());
        values.put("Reprecificada", pSolicitacao.getReprecificada());
        values.put("Sincronizado", pSolicitacao.getSincronizado());
        values.put("Status", pSolicitacao.getStatus());
        values.put("PropostaId", pSolicitacao.getPropostaId());

        String dsStr = DataSyncValidator.toDbIfValid(pSolicitacao.getDataSync());
        if (dsStr != null) {
            values.put("DataSync", dsStr);
        } else {
            values.putNull("DataSync");
            if (pSolicitacao.getDataSync() != null) {
                Log.w("BDSolicitacaoPid", "[DATASYNC] DataSync inválido na gravação; definindo como NULL. idLocal=" + pSolicitacao.getId());
            }
        }

        @Nullable String dsCadastro = DataSyncManager.toDbString(pSolicitacao.getDataCadastro());
        if (dsCadastro != null) values.put("DataCadastro", dsCadastro); else values.putNull("DataCadastro");

        @Nullable String dsAvaliacao = DataSyncManager.toDbString(pSolicitacao.getDataAvaliacao());
        if (dsAvaliacao != null) values.put("DataAvaliacao", dsAvaliacao); else values.putNull("DataAvaliacao");

        @Nullable String dsEnvioTermo = DataSyncManager.toDbString(pSolicitacao.getDataEnvioTermo());
        if (dsEnvioTermo != null) values.put("DataEnvioTermo", dsEnvioTermo); else values.putNull("DataEnvioTermo");

        long codigoSolicitacao;
        if (pSolicitacao.getId_Server() != null && pSolicitacao.getId_Server() > 0) {
            if (!Util_DB.isCadastrado(mContext, mTabela, "idServer", String.valueOf(pSolicitacao.getId_Server()))) {
                codigoSolicitacao = SimpleDbHelper.INSTANCE.open(mContext).insert(mTabela, null, values);
            } else {
                codigoSolicitacao = SimpleDbHelper.INSTANCE.open(mContext).update(
                        mTabela, values, "[Id_Server]=?", new String[]{String.valueOf(pSolicitacao.getId_Server())});
            }
        } else {
            if (!Util_DB.isCadastrado(mContext, mTabela, "id", String.valueOf(pSolicitacao.getId()))) {
                codigoSolicitacao = SimpleDbHelper.INSTANCE.open(mContext).insert(mTabela, null, values);
            } else {
                codigoSolicitacao = SimpleDbHelper.INSTANCE.open(mContext).update(
                        mTabela, values, "[id]=?", new String[]{String.valueOf(pSolicitacao.getId())});
            }
        }
        return codigoSolicitacao;
    }

    public void deleteById(String pId) {
        SimpleDbHelper.INSTANCE.open(mContext).delete(mTabela, "[id]=?", new String[]{pId});
    }

    public void deleteAll() {
        SimpleDbHelper.INSTANCE.open(mContext).delete(mTabela, null, null);
    }

    public ArrayList<SolicitacaoPid> getSolicitacaoPid(String pCondicao, String[] pCampos) {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("Select * FROM [SolicitacaoPid]");
        sb.appendLine("WHERE 1=1");
        if (pCondicao != null) sb.append(pCondicao);
        sb.appendLine(" Order by Id");
        return moverCursorDb(sb, pCampos);
    }

    private ArrayList<SolicitacaoPid> moverCursorDb(Util_IO.StringBuilder sb, String[] args) {
        Cursor cursor = null;
        ArrayList<SolicitacaoPid> lista = new ArrayList<>();
        try {
            cursor = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(sb.toString(), args);
            if (cursor != null && cursor.moveToFirst()) {
                final int idxId = cursor.getColumnIndexOrThrow("Id");
                final int idxIdServer = cursor.getColumnIndexOrThrow("Id_Server");
                final int idxCodigoSGV = cursor.getColumnIndexOrThrow("CodigoSGV");
                final int idxCodigoAdq = cursor.getColumnIndexOrThrow("CodigoAdquirencia");
                final int idxObs = cursor.getColumnIndexOrThrow("Observacao");
                final int idxDDD = cursor.getColumnIndexOrThrow("DDD");
                final int idxFaturPrev = cursor.getColumnIndexOrThrow("FaturamentoPrevisto");
                final int idxConc = cursor.getColumnIndexOrThrow("NomeConcorrente");
                final int idxDataSync = cursor.getColumnIndexOrThrow("DataSync");
                final int idxDataCadastro = cursor.getColumnIndexOrThrow("DataCadastro");
                final int idxDataAvaliacao = cursor.getColumnIndexOrThrow("DataAvaliacao");
                final int idxDataEnvioTermo = cursor.getColumnIndexOrThrow("DataEnvioTermo");
                final int idxPropostaId = cursor.getColumnIndexOrThrow("PropostaId");
                final int idxDeb = cursor.getColumnIndexOrThrow("DistribuicaoDebito");
                final int idxCred = cursor.getColumnIndexOrThrow("DistribuicaoCredito");
                final int idxCred6 = cursor.getColumnIndexOrThrow("DistribuicaoCredito6x");
                final int idxCred12 = cursor.getColumnIndexOrThrow("DistribuicaoCredito12x");
                do {
                    SolicitacaoPid solicitacaoPid = new SolicitacaoPid();

                    final int localId = cursor.getInt(idxId);
                    solicitacaoPid.setId(localId);

                    if (!cursor.isNull(idxIdServer)) {
                        solicitacaoPid.setId_Server(cursor.getInt(idxIdServer));
                    }
                    if (!Util_IO.isNullOrEmpty(cursor.getString(idxCodigoSGV))) {
                        solicitacaoPid.setCodigoSGV(cursor.getString(idxCodigoSGV));
                    }
                    if (!cursor.isNull(idxCodigoAdq)) {
                        solicitacaoPid.setCodigoAdquirencia(cursor.getInt(idxCodigoAdq));
                    }
                    if (!Util_IO.isNullOrEmpty(cursor.getString(idxObs))) {
                        solicitacaoPid.setObservacao(cursor.getString(idxObs));
                    }
                    if (!cursor.isNull(idxDDD)) {
                        solicitacaoPid.setDDD(cursor.getInt(idxDDD));
                    }
                    if (!cursor.isNull(idxFaturPrev)) {
                        solicitacaoPid.setFaturamentoPrevisto(cursor.getDouble(idxFaturPrev));
                    }
                    if (!Util_IO.isNullOrEmpty(cursor.getString(idxConc))) {
                        solicitacaoPid.setNomeConcorrente(cursor.getString(idxConc));
                    }
                    java.util.Date parsedDs = null;
                    if (!cursor.isNull(idxDataSync)) {
                        String rawDs = cursor.getString(idxDataSync);
                        parsedDs = DataSyncManager.parse(rawDs);
                        if (parsedDs == null) {
                            DataSyncValidator.warnInvalid(rawDs, "SolicitacaoPid", "Id", String.valueOf(localId));
                            enqueueDataSyncFix(localId);
                        }
                    }
                    solicitacaoPid.setDataSync(parsedDs);

                    if (!cursor.isNull(idxDataCadastro)) {
                        solicitacaoPid.setDataCadastro(DataSyncManager.parse(cursor.getString(idxDataCadastro)));
                    }
                    if (!cursor.isNull(idxDataAvaliacao)) {
                        solicitacaoPid.setDataAvaliacao(DataSyncManager.parse(cursor.getString(idxDataAvaliacao)));
                    }
                    if (!cursor.isNull(idxDataEnvioTermo)) {
                        solicitacaoPid.setDataEnvioTermo(DataSyncManager.parse(cursor.getString(idxDataEnvioTermo)));
                    }
                    if (!cursor.isNull(idxPropostaId)) {
                        solicitacaoPid.setPropostaId(cursor.getInt(idxPropostaId));
                    }
                    if (!cursor.isNull(idxDeb)) {
                        solicitacaoPid.setDistribuicaoDebito(cursor.getDouble(idxDeb));
                    }
                    if (!cursor.isNull(idxCred)) {
                        solicitacaoPid.setDistribuicaoCredito(cursor.getDouble(idxCred));
                    }
                    if (!cursor.isNull(idxCred6)) {
                        solicitacaoPid.setDistribuicaoCredito6x(cursor.getDouble(idxCred6));
                    }
                    if (!cursor.isNull(idxCred12)) {
                        solicitacaoPid.setDistribuicaoCredito12x(cursor.getDouble(idxCred12));
                    }

                    // Demais campos de tabela
                    solicitacaoPid.setTipoPessoa(cursor.getString(cursor.getColumnIndexOrThrow("TipoPessoa")));
                    solicitacaoPid.setTipoCliente(cursor.getString(cursor.getColumnIndexOrThrow("TipoCliente")));
                    solicitacaoPid.setIdVendedor(cursor.getInt(cursor.getColumnIndexOrThrow("IdVendedor")));
                    solicitacaoPid.setLatitude(cursor.getDouble(cursor.getColumnIndexOrThrow("Latitude")));
                    solicitacaoPid.setLongitude(cursor.getDouble(cursor.getColumnIndexOrThrow("Longitude")));
                    solicitacaoPid.setPrecisao(cursor.getDouble(cursor.getColumnIndexOrThrow("Precisao")));
                    solicitacaoPid.setCpfCnpj(cursor.getString(cursor.getColumnIndexOrThrow("CpfCnpj")));
                    solicitacaoPid.setRazaoSocial(cursor.getString(cursor.getColumnIndexOrThrow("RazaoSocial")));
                    solicitacaoPid.setNomeFantasia(cursor.getString(cursor.getColumnIndexOrThrow("NomeFantasia")));
                    solicitacaoPid.setMCCPrincipal(cursor.getInt(cursor.getColumnIndexOrThrow("MCCPrincipal")));
                    solicitacaoPid.setRede(cursor.getInt(cursor.getColumnIndexOrThrow("Rede")));
                    solicitacaoPid.setNDeLojas(cursor.getInt(cursor.getColumnIndexOrThrow("NDeLojas")));
                    solicitacaoPid.setOrigem(cursor.getString(cursor.getColumnIndexOrThrow("Origem")));
                    solicitacaoPid.setRenegociacao(cursor.getInt(cursor.getColumnIndexOrThrow("Renegociacao")));
                    solicitacaoPid.setContraproposta(cursor.getInt(cursor.getColumnIndexOrThrow("Contraproposta")));
                    solicitacaoPid.setReprecificada(cursor.getInt(cursor.getColumnIndexOrThrow("Reprecificada")));
                    solicitacaoPid.setSincronizado(cursor.getInt(cursor.getColumnIndexOrThrow("Sincronizado")));
                    solicitacaoPid.setStatus(cursor.getString(cursor.getColumnIndexOrThrow("Status")));

                    lista.add(solicitacaoPid);
                } while (cursor.moveToNext());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (cursor != null) cursor.close();
        }
        runDataSyncFixQueue();
        return lista;
    }

    public SolicitacaoPid getById(String pId) {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("AND [id] = ?");
        return Utilidades.firstOrDefault(getSolicitacaoPid(sb.toString(), new String[]{pId}));
    }

    public SolicitacaoPid getSolicitacaoPidIdServer(int pIdSolicitacaoServer) {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine(" AND [Id_Server] = " + pIdSolicitacaoServer);
        SolicitacaoPid solicitacao = Utilidades.firstOrDefault(getSolicitacaoPid(sb.toString(), null));

        if (solicitacao != null) {
            solicitacao.setProduto(new BDSolicitacaoPidProduto(mContext)
                    .getSolicitacaoPidProduto(" and IdSolicitacaoPid = " + solicitacao.getId(), null));
            solicitacao.setAnexos(new BDSolicitacaoPidAnexo(mContext)
                    .getSolicitacaoPidAnexo(" and IdSolicitacaoPid = " + solicitacao.getId(), null));
            solicitacao.setListaPOS(new BDSolicitacaoPidPOS(mContext)
                    .getSolicitacaoPidPOS(" and IdSolicitacaoPid = " + solicitacao.getId(), null));
            solicitacao.setListaRedes(new BDSolicitacaoPidRede(mContext)
                    .getSolicitacaoPidRede(" and IdSolicitacaoPid = " + solicitacao.getId(), null));
            solicitacao.setListaTaxas(new BDSolicitacaoPidTaxaMDR(mContext)
                    .getSolicitacaoPidTaxaMDR(" and IdSolicitacaoPid = " + solicitacao.getId(), null));
        }
        return solicitacao;
    }

    public ArrayList<SolicitacaoPid> getAll() {
        return getSolicitacaoPid(null, null);
    }

    public ArrayList<Integer> getServerIds() {
        String query = "Select Id_Server FROM [SolicitacaoPid] WHERE Id_Server IS NOT NULL";
        Cursor cursor = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(query, new String[]{});
        ArrayList<Integer> serverIds = new ArrayList<>();
        try {
            if (cursor.moveToFirst()) {
                do {
                    serverIds.add(cursor.getInt(cursor.getColumnIndexOrThrow("Id_Server")));
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null) cursor.close();
        }
        return serverIds;
    }

    private void enqueueDataSyncFix(final int id) {
        dataSyncFixQueue.add(() -> {
            ContentValues fix = new ContentValues();
            fix.putNull("DataSync");
            try {
                SimpleDbHelper.INSTANCE.open(mContext)
                        .update(mTabela, fix, "[Id]=?", new String[]{ String.valueOf(id) });
                Log.i("BDSolicitacaoPid", "[DATASYNC] DataSync fix aplicado para id=" + id);
            } catch (Exception e) {
                Log.e("BDSolicitacaoPid", "[DATASYNC] Erro ao aplicar fix DataSync id=" + id, e);
            }
        });
    }

    private void runDataSyncFixQueue() {
        for (Runnable r : dataSyncFixQueue) {
            try {
                r.run();
            } catch (Exception e) {
                Log.e("BDSolicitacaoPid", "[DATASYNC] Erro ao executar correção de DataSync", e);
            }
        }
        dataSyncFixQueue.clear();
    }

    public void updateSyncSolicitacaoPid(final int pId, @NonNull final SolicitacaoPidRetorno solRetorno) {
        SQLiteDatabase db = null;
        try {
            if (pId <= 0) {
                throw new IllegalArgumentException("Id local inválido para atualização de sincronismo.");
            }

            final int idServer = solRetorno.getIdSolicitacaoPid();
            if (idServer <= 0) {
                throw new IllegalArgumentException("Id_Server inválido retornado pelo backend.");
            }

            db = SimpleDbHelper.INSTANCE.open(mContext);
            db.beginTransaction();

            ContentValues values = new ContentValues();
            values.put("Sincronizado", 1);
            values.put("Id_Server", idServer);
            String dsNow = DataSyncValidator.toDbIfValid(new Date());
            if (dsNow != null) {
                values.put("DataSync", dsNow);
            } else {
                values.putNull("DataSync");
                Log.w("BDSolicitacaoPid", "[DATASYNC] Falha ao formatar DataSync em updateSyncSolicitacaoPid; gravando NULL. idLocal=" + pId);
            }

            final int rows = db.update("SolicitacaoPid", values, "[id]=?", new String[]{ String.valueOf(pId) });
            if (rows <= 0) {
                throw new IllegalStateException("Falha ao atualizar SolicitacaoPid id=" + pId);
            }

            Log.i("BDSolicitacaoPid", "[SYNC][SolicitacaoPid] id=" + pId + " idServer=" + idServer + " dsAfter=" + (dsNow != null ? dsNow : "NULL"));

            db.setTransactionSuccessful();
        } catch (Exception ex) {
            Log.e("BDSolicitacaoPid", "updateSyncSolicitacaoPid error", ex);
            throw (ex instanceof RuntimeException) ? (RuntimeException) ex
                    : new IllegalStateException("Não foi possível concluir a sincronização da solicitação.", ex);
        } finally {
            if (db != null && db.inTransaction()) db.endTransaction();
        }
    }

    public void updateStatusSyncSolicitacaoPid(SolicitacaoPidStatus solStatusRetorno) {
        try {
            ContentValues values = new ContentValues();
            values.put("Status", solStatusRetorno.getStatus());
            SimpleDbHelper.INSTANCE.open(mContext)
                    .update("SolicitacaoPid", values, "[Id_Server]=?", new String[]{String.valueOf(solStatusRetorno.getIdSolicitacaoPid())});
        } catch (Exception ex) {
            Log.d("Roni", "updateStatusSyncSolicitacaoPid: " + ex.getMessage());
        }
    }

    public ArrayList<SolicitacaoPid> getSolicitacaoPidFiltro(String pParametro) {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("Select * FROM [SolicitacaoPid]");
        sb.appendLine("WHERE 1=1");
        if (pParametro != null)
            sb.appendLine("AND ([RazaoSocial] LIKE '%" + pParametro + "%' OR [NomeFantasia] LIKE '%" + pParametro + "%' OR [CpfCnpj] LIKE '%" + pParametro + "%')");
        sb.appendLine(" Order by Id desc");
        return moverCursorDb(sb, null);
    }
}