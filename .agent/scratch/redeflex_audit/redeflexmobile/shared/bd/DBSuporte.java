package com.axys.redeflexmobile.shared.bd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.axys.redeflexmobile.BuildConfig;
import com.axys.redeflexmobile.shared.models.Colaborador;
import com.axys.redeflexmobile.shared.models.ComandoExecutado;
import com.axys.redeflexmobile.shared.models.ComandoExecutar;
import com.axys.redeflexmobile.shared.models.ErroSync;
import com.axys.redeflexmobile.shared.models.Pendencias;
import com.axys.redeflexmobile.shared.util.Util_DB;
import com.axys.redeflexmobile.shared.util.Util_IO;
import com.axys.redeflexmobile.ui.redeflex.Config;

import java.util.ArrayList;

/**
 * Created by Desenvolvimento on 02/05/2016.
 */
public class DBSuporte {
    private Context mContext;
    private String mTabela = "ComandoExecutar";
    private String mTabelaErroSync = "ErroSync";

    public DBSuporte(Context pContext) {
        mContext = pContext;
    }

    public void insertComandoExecutar(ComandoExecutar pComandoExecutar) throws Exception {
        ContentValues values = new ContentValues();
        values.put("id", pComandoExecutar.getId());
        values.put("comando", pComandoExecutar.getComando());
        values.put("basedados", pComandoExecutar.getBasedados());
        if (!Util_DB.isCadastrado(mContext, mTabela, "id", pComandoExecutar.getId()))
            SimpleDbHelper.INSTANCE.open(mContext).insert(mTabela, null, values);
        else
            SimpleDbHelper.INSTANCE.open(mContext).update(mTabela, values, "[id]=?", new String[]{pComandoExecutar.getId()});
    }

    public void deleteComandoExecutar(String pId) {
        SimpleDbHelper.INSTANCE.open(mContext).delete(mTabela, "[id]=?", new String[]{pId});
    }

    public void executarComandos() {
        String query = "SELECT [id],[comando],[basedados] FROM [ComandoExecutar] WHERE dataExecutado IS NULL";
        Cursor cursor = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(query, null);
        String cmd;
        String id;
        String mensagem;
        try {
            if (cursor.moveToFirst()) {
                do {
                    try {
                        id = cursor.getString(0);
                        cmd = cursor.getString(1);
                        try {
                            SimpleDbHelper.INSTANCE.open(mContext).execSQL(cmd);
                            mensagem = "Query executada sem erros.";
                        } catch (Exception ex) {
                            mensagem = ex.toString().replace("'", "");
                        }
                        query = "UPDATE [ComandoExecutar] SET mensagem = '" + mensagem + "', dataExecutado = datetime('now','localtime') WHERE id = '" + id + "'";
                        SimpleDbHelper.INSTANCE.open(mContext).execSQL(query);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                } while (cursor.moveToNext());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
    }

    public void addErroSync(String pIdColaborador, String pEntidade, int pCodigo) {
        if (pIdColaborador == null) {
            Colaborador colaborador = new DBColaborador(mContext).get();
            if (colaborador != null)
                pIdColaborador = String.valueOf(colaborador.getId());
        }
        ContentValues values = new ContentValues();
        values.put("idColaborador", pIdColaborador);
        values.put("entidade", pEntidade);
        values.put("idEntidade", pCodigo);
        SimpleDbHelper.INSTANCE.open(mContext).insert(mTabelaErroSync, null, values);
    }

    public ArrayList<ComandoExecutado> getComandosExecutados() {
        String selectQuery = " SELECT [id],[mensagem],[dataExecutado] FROM [ComandoExecutar] WHERE dataExecutado IS NOT NULL";
        Cursor cursor = null;
        ArrayList<ComandoExecutado> lista = new ArrayList<>();
        try {
            cursor = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    ComandoExecutado comandoExecutado = new ComandoExecutado();
                    comandoExecutado.setId(cursor.getString(0));
                    comandoExecutado.setMensagem(cursor.getString(1));
                    comandoExecutado.setDataExecucao(Util_IO.stringToDate(cursor.getString(2), Config.FormatDateTimeStringBanco));
                    lista.add(comandoExecutado);
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

    public ArrayList<Pendencias> getPendencias() {
        StringBuilder sbSQL = new StringBuilder();
        sbSQL.append("SELECT * FROM ( ");
        sbSQL.append("SELECT 'Auditagem de Estoque' AS Nome, COUNT(DISTINCT id) AS Qtd FROM [AuditagemEstoque] WHERE [sync] = 0 AND [confirmado] = 'S' UNION ");
        sbSQL.append("SELECT 'Auditagem de Estoque - Cód. Barra' AS Nome, COUNT(DISTINCT t0.id) AS Qtd ");
        sbSQL.append("FROM [AuditagemEstoqueCodigoBarra] t0 JOIN [AuditagemEstoque] t1 ON (t0.[idAuditagem] = t1.[id]) ");
        sbSQL.append("WHERE t0.[sync] = 0 AND t1.[confirmado] = 'S' UNION ");
        sbSQL.append("SELECT 'Cadastro Cliente', COUNT(DISTINCT id) FROM [ClienteCadastro] WHERE [sync] = 0 UNION ");
        sbSQL.append("SELECT 'Cadastro Cliente - Anexos', COUNT(DISTINCT id) FROM [ClienteCadastroAnexo] WHERE [sync] = 0 UNION ");
        sbSQL.append("SELECT 'Venda - Produtos Cód. Barra', COUNT(DISTINCT t2.id) FROM [ItemVendaCodigoBarra] t2 ");
        sbSQL.append("JOIN [Venda] t0 ON (t0.[id] = t2.[idVenda]) JOIN [Visita] t1 ON (t0.[idVisita] = t1.[id]) ");
        sbSQL.append("WHERE t2.cancelado = 0 AND t2.[sync] = 0 AND t1.[dataFim] IS NOT NULL UNION ");
        sbSQL.append("SELECT 'Venda - Produtos', COUNT(DISTINCT t2.id) FROM [ItemVenda] t2 ");
        sbSQL.append("JOIN [Venda] t0 ON (t0.[id] = t2.[idVenda]) JOIN [Visita] t1 ON (t0.[idVisita] = t1.[id]) ");
        sbSQL.append("WHERE t0.status = 3 AND t2.[sync] = 0 AND t1.[dataFim] IS NOT NULL UNION ");
        sbSQL.append("SELECT 'Localização Cliente', COUNT(DISTINCT id) FROM [LocalizacaoCliente] WHERE [sync] = 0 UNION ");
        sbSQL.append("SELECT 'OS', COUNT(DISTINCT id) FROM OS WHERE (([VisualizacaoSync] = 0 AND [DataVisualizacao] IS NOT NULL) ");
        sbSQL.append("OR ([AgendamentoSync] = 0 AND [DataAgendamento] IS NOT NULL) ");
        sbSQL.append("OR ([AtendimentoSync] = 0 AND [DataAtendimento] IS NOT NULL)) UNION ");
        sbSQL.append("SELECT 'Remessa', COUNT(DISTINCT id) FROM [Remessa] WHERE [sync] = 0 AND [dataconfirmacao] IS NOT NULL UNION ");
        sbSQL.append("SELECT 'Remessa - Produtos', COUNT(DISTINCT t0.id) FROM [RemessaItem] t0 JOIN [Remessa] t1 ON (t0.[idRemessa] = t1.[id]) ");
        sbSQL.append("WHERE t0.[sync] = 0 AND t1.[dataconfirmacao] IS NOT NULL UNION ");
        sbSQL.append("SELECT 'Senha Mobile', COUNT(DISTINCT id) FROM [SenhaVenda] WHERE [sync] = 0 UNION ");
        sbSQL.append("SELECT 'Telemetria', COUNT(1) FROM [Telemetria] UNION ");
        sbSQL.append("SELECT 'Venda', COUNT(DISTINCT t0.id) FROM [Venda] t0 JOIN [Visita] t1 ON (t0.[idVisita] = t1.[id]) ");
        sbSQL.append("WHERE t0.[sync] = 0 AND t0.status = 3 AND t1.[dataFim] IS NOT NULL UNION ");
        sbSQL.append("SELECT 'Remessa - Comprovante', COUNT(DISTINCT id) FROM [Remessa] ");
        sbSQL.append("WHERE [localArquivo] IS NOT NULL AND [syncArquivo] = 0 AND [dataconfirmacao] IS NOT NULL UNION ");
        sbSQL.append("SELECT 'Visita', COUNT(DISTINCT id) FROM [Visita] WHERE [sync] = 0 AND [dataFim] IS NOT NULL UNION ");
        sbSQL.append("SELECT 'Cliente', COUNT(DISTINCT id) FROM [Cliente] WHERE [sync] = 0 UNION ");
        sbSQL.append("SELECT 'Cliente - Auditagem', COUNT(DISTINCT id) FROM [AuditagemCliente] WHERE confirmado = 'S' AND sync = 0 UNION ");
        sbSQL.append("SELECT 'Cliente - Auditagem Cód. Barra', COUNT(DISTINCT t0.[id]) FROM [AuditagemClienteCodBarra] t0 ");
        sbSQL.append("JOIN [AuditagemCliente] t1 ON (t0.[idAuditagem] = t1.[id]) WHERE t1.[confirmado] = 'S' AND t0.[sync] = 0 UNION ");
        sbSQL.append("SELECT 'Chamados', COUNT(DISTINCT id) FROM [Chamados] WHERE [sync] = 0 UNION ");
        sbSQL.append("SELECT 'Chamados - Interações', COUNT(DISTINCT id) FROM [ChamadosInteracoes] WHERE [sync] = 0 UNION ");
        sbSQL.append("SELECT 'Chamados - Anexos', COUNT(DISTINCT id) FROM [ChamadosAnexos] WHERE [sync] = 0 UNION ");
        sbSQL.append("SELECT 'Solicitação de Suporte', COUNT(DISTINCT id)  FROM [Suporte] WHERE [sync] = 0 UNION ");
        sbSQL.append("SELECT 'Envio anexos', COUNT(DISTINCT id)  FROM [ComprovanteSkyTa] WHERE [sync] = 0 UNION ");
        sbSQL.append("SELECT 'Solicitação de Mercadoria ', COUNT(DISTINCT id) FROM ( ");
        sbSQL.append("        SELECT t0.id,t0.idServer,t0.dataCriacao,(SELECT id FROM Colaborador) AS idVendedor,t1.[idStatus],t1.data ");
        sbSQL.append("FROM [SolicMerc]  t0 ");
        sbSQL.append("JOIN (SELECT * FROM [SituacaoSolicitacao] ta ");
        sbSQL.append("        WHERE EXISTS (SELECT * FROM (SELECT MAX(idStatus) AS idStatus, idSolicMerc ");
        sbSQL.append("                FROM [SituacaoSolicitacao] GROUP BY idSolicMerc) tb ");
        sbSQL.append("                WHERE tb.idStatus = ta.idStatus AND tb.idSolicMerc = ta.idSolicMerc) ");
        sbSQL.append(") t1 ON (t1.idSolicMerc = t0.id) WHERE IFNULL(t1.[idStatus],0) = 0) UNION ");
        sbSQL.append("SELECT 'Devolução', COUNT(DISTINCT id) FROM Devolucao WHERE sync = 0 UNION ");
        sbSQL.append("SELECT 'Devolução - Produtos', COUNT(DISTINCT id) FROM DevolucaoItens WHERE sync = 0 UNION ");
        sbSQL.append("SELECT 'Visita Adquirência', COUNT(DISTINCT id) FROM VisitaAdquirencia WHERE id_server IS NULL UNION ");
        sbSQL.append("SELECT 'Visita Adquirência - Anexo', COUNT(DISTINCT id) FROM VisitaAdqurenciaAnexo WHERE sync = 0 UNION ");
        sbSQL.append("SELECT 'Visita Adquirência - Qualidade', COUNT(DISTINCT id) FROM VisitaAdquirenciaQualidade WHERE sync = 0");
        sbSQL.append(" ) WHERE Qtd > 0 ");
        sbSQL.append("ORDER BY Nome ");
        Cursor cursor = null;
        ArrayList<Pendencias> lista = new ArrayList<>();
        Pendencias pendencias;
        try {
            cursor = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(sbSQL.toString(), null);
            if (cursor.moveToFirst()) {
                do {
                    pendencias = new Pendencias();
                    pendencias.setDescricao(cursor.getString(0));
                    pendencias.setQtd(cursor.getInt(1));
                    lista.add(pendencias);
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

    public ArrayList<ErroSync> getErroSync() {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("SELECT id");
        sb.appendLine(",idColaborador");
        sb.appendLine(",entidade");
        sb.appendLine(",idEntidade");
        sb.appendLine(",datetime('now', 'localtime')");
        sb.appendLine("FROM [ErroSync]");
        Cursor cursor = null;
        ArrayList<ErroSync> lista = new ArrayList<>();
        try {
            cursor = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(sb.toString(), null);
            if (cursor.moveToFirst()) {
                do {
                    ErroSync erroSync = new ErroSync();
                    erroSync.setSyncPendenciasId(cursor.getInt(0));
                    erroSync.setAjustado(false);
                    erroSync.setIdColaborador(cursor.getInt(1));
                    erroSync.setEntidade(cursor.getString(2));
                    erroSync.setIdAppMobile(cursor.getInt(3));
                    erroSync.setDataHora(Util_IO.stringToDate(cursor.getString(4), Config.FormatDateTimeStringBanco));
                    erroSync.setVersaoApp(BuildConfig.VERSION_NAME);
                    lista.add(erroSync);
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

    public void deletarErroSync(int pId) {
        SimpleDbHelper.INSTANCE.open(mContext).delete(mTabelaErroSync, "[id]=?", new String[]{String.valueOf(pId)});
    }

    public void deleteAll() {
        SimpleDbHelper.INSTANCE.open(mContext).delete(mTabela, null, null);
        SimpleDbHelper.INSTANCE.open(mContext).delete(mTabelaErroSync, null, null);
    }
}