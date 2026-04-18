package com.axys.redeflexmobile.shared.bd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.axys.redeflexmobile.shared.models.AnexoChamado;
import com.axys.redeflexmobile.shared.models.Chamado;
import com.axys.redeflexmobile.shared.models.InteracaoAnexos;
import com.axys.redeflexmobile.shared.models.Interacoes;
import com.axys.redeflexmobile.shared.models.NovoAnexo;
import com.axys.redeflexmobile.shared.util.Util_DB;
import com.axys.redeflexmobile.shared.util.Util_IO;
import com.axys.redeflexmobile.shared.util.Utilidades;
import com.axys.redeflexmobile.ui.redeflex.Config;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by joao.viana on 07/03/2017.
 */

public class DBChamados {
    private final Context mContext;
    private final String mTabelaChamado = "Chamados";
    private final String mTabelaInteracoes = "ChamadosInteracoes";
    private final String mTabelaAnexo = "ChamadosAnexos";

    public DBChamados(Context pContext) {
        mContext = pContext;
    }

    public boolean addChamados(Chamado pChamado) {
        ContentValues values = new ContentValues();
        values.put("idMobile", pChamado.getId());
        values.put("idChamado", pChamado.getChamadoID());
        if (pChamado.getFilialID() != null)
            values.put("idFilial", pChamado.getFilialID());
        if (pChamado.getDepartamentoID() != null)
            values.put("idDepartamento", pChamado.getDepartamentoID());
        if (pChamado.getSolicitanteID() != null)
            values.put("idSolicitante", pChamado.getSolicitanteID());
        values.put("solicitante", pChamado.getSolicitante());
        values.put("assunto", pChamado.getAssunto());
        values.put("status", pChamado.getStatusID());
        values.put("dataCadastro", Util_IO.dateTimeToString(pChamado.getDataCadastro(), Config.FormatDateTimeStringBanco));
        if (pChamado.getResponsavelID() != null)
            values.put("idAtendente", pChamado.getResponsavelID());
        values.put("atendente", pChamado.getResponsavel());
        if (pChamado.getDataAlteracao() != null)
            values.put("dataAlteracao", Util_IO.dateTimeToString(pChamado.getDataAlteracao(), Config.FormatDateTimeStringBanco));
        values.put("sync", 1);
        if (pChamado.getIdCliente() != null)
            values.put("idCliente", pChamado.getIdCliente());

        boolean bRetorno = false;
        if (pChamado.getIdAppMobile() != null) {
            try (Cursor cursor = SimpleDbHelper.INSTANCE.open(mContext).query(mTabelaChamado, new String[]{"id"}
                    , "[id]=?", new String[]{String.valueOf(pChamado.getIdAppMobile())}, null, null, null, null)) {
                if (cursor == null || cursor.getCount() == 0)
                    SimpleDbHelper.INSTANCE.open(mContext).insert(mTabelaChamado, null, values);
                else {
                    bRetorno = true;
                    SimpleDbHelper.INSTANCE.open(mContext).update(mTabelaChamado, values, "[id]=?", new String[]{String.valueOf(pChamado.getIdAppMobile())});
                }
            }
            updataInteracoesIdChamado(pChamado.getIdAppMobile(), pChamado.getChamadoID());
        } else {
            try {
                bRetorno = Util_DB.isCadastrado(mContext, mTabelaChamado, "idMobile", String.valueOf(pChamado.getId()));
                if (!bRetorno)
                    SimpleDbHelper.INSTANCE.open(mContext).insert(mTabelaChamado, null, values);
                else
                    SimpleDbHelper.INSTANCE.open(mContext).update(mTabelaChamado, values, "[idMobile]=?", new String[]{String.valueOf(pChamado.getId())});
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return bRetorno;
    }

    public void addChamadoMobile(Chamado pChamado, String pDescricao, ArrayList<NovoAnexo> pListAnexos) {
        ContentValues values = new ContentValues();
        values.put("idFilial", pChamado.getFilialID());
        values.put("idDepartamento", pChamado.getDepartamentoID());
        values.put("idSolicitante", pChamado.getSolicitanteID());
        values.put("solicitante", pChamado.getSolicitante());
        values.put("assunto", pChamado.getAssunto());
        values.put("status", pChamado.getStatusID());
        values.put("sync", 0);
        values.put("dataCadastro", Util_IO.dateTimeToString(pChamado.getDataCadastro(), Config.FormatDateTimeStringBanco));
        long codigoChamado = SimpleDbHelper.INSTANCE.open(mContext).insert(mTabelaChamado, null, values);

        values = new ContentValues();
        values.put("descricao", pDescricao);
        values.put("dataCadastro", Util_IO.dateTimeToString(pChamado.getDataCadastro(), Config.FormatDateTimeStringBanco));
        values.put("idUsuario", pChamado.getSolicitanteID());
        values.put("usuario", pChamado.getSolicitante());
        values.put("sync", 0);
        values.put("idChamadoMobile", codigoChamado);
        long codigoInteracao = SimpleDbHelper.INSTANCE.open(mContext).insert(mTabelaInteracoes, null, values);

        if (pListAnexos != null && pListAnexos.size() > 0) {
            for (NovoAnexo anexo : pListAnexos) {
                String nomeArquivo = Utilidades.getFilename(mContext);
                Utilidades.compressImage(anexo.getLocalArquivo(), nomeArquivo);
                values = new ContentValues();
                values.put("idInteracaoMobile", codigoInteracao);
                values.put("idChamadoMobile", codigoChamado);
                values.put("localArquivo", nomeArquivo);
                values.put("nomeArquivo", anexo.getNomeArquivo());
                values.put("tipoArquivo", anexo.getTipoArquivo());
                SimpleDbHelper.INSTANCE.open(mContext).insert(mTabelaAnexo, null, values);
            }
        }
    }

    public void deleteAll() {
        SimpleDbHelper.INSTANCE.open(mContext).delete(mTabelaAnexo, null, null);
        SimpleDbHelper.INSTANCE.open(mContext).delete(mTabelaInteracoes, null, null);
        SimpleDbHelper.INSTANCE.open(mContext).delete(mTabelaChamado, null, null);
    }

    public void deleteAnexo(String pCodigo, String pLocal) {
        try {
            Utilidades.deletaArquivo(pLocal);
            SimpleDbHelper.INSTANCE.open(mContext).delete(mTabelaAnexo, "[id]=? ", new String[]{pCodigo});
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public boolean addInteracoesWebService(Interacoes pInteracoes) {
        Cursor cursor = SimpleDbHelper.INSTANCE.open(mContext).query(mTabelaChamado, new String[]{"id"}
                , "[idChamado]=?", new String[]{String.valueOf(pInteracoes.getChamadoID())}, null, null, null, null);

        try {
            if (cursor == null || cursor.getCount() == 0)
                return false;
        } finally {
            if (cursor != null)
                cursor.close();
        }

        ContentValues values = new ContentValues();
        values.put("idMobile", pInteracoes.getId());
        values.put("idInteracao", pInteracoes.getInteracaoID());
        values.put("idChamado", pInteracoes.getChamadoID());
        values.put("descricao", pInteracoes.getDescricao());
        values.put("dataCadastro", Util_IO.dateTimeToString(pInteracoes.getDataCadastro(), Config.FormatDateTimeStringBanco));
        if (pInteracoes.getIdUsuario() != null)
            values.put("idUsuario", pInteracoes.getIdUsuario());
        values.put("usuario", pInteracoes.getUsuario());
        values.put("sync", 1);
        values.put("idChamadoMobile", "");

        if (pInteracoes.getIdAppMobile() != null) {
            cursor = SimpleDbHelper.INSTANCE.open(mContext).query(mTabelaInteracoes, new String[]{"id"}
                    , "[id]=?", new String[]{String.valueOf(pInteracoes.getIdAppMobile())}, null, null, null, null);

            try {
                if (cursor == null || cursor.getCount() == 0)
                    SimpleDbHelper.INSTANCE.open(mContext).insert(mTabelaInteracoes, null, values);
                else
                    SimpleDbHelper.INSTANCE.open(mContext).update(mTabelaInteracoes, values, "[id]=?", new String[]{String.valueOf(pInteracoes.getIdAppMobile())});
            } finally {
                if (cursor != null)
                    cursor.close();
            }
            updateAnexosIdIntraflex(pInteracoes.getIdAppMobile(), pInteracoes.getChamadoID(), pInteracoes.getInteracaoID());
        } else {
            try {
                if (!Util_DB.isCadastrado(mContext, mTabelaInteracoes, "idMobile", String.valueOf(pInteracoes.getId())))
                    SimpleDbHelper.INSTANCE.open(mContext).insert(mTabelaInteracoes, null, values);
                else
                    SimpleDbHelper.INSTANCE.open(mContext).update(mTabelaInteracoes, values, "[idMobile]=?", new String[]{String.valueOf(pInteracoes.getId())});
            } catch (Exception ex) {
                ex.printStackTrace();
                return false;
            }
        }
        return true;
    }

    public void addInteracoesMobile(Interacoes pInteracoes, ArrayList<NovoAnexo> pListAnexos) {
        ContentValues values = new ContentValues();
        if (pInteracoes.getChamadoID() > 0)
            values.put("idChamado", pInteracoes.getChamadoID());
        else
            values.put("idChamadoMobile", pInteracoes.getIdAppMobile());
        values.put("descricao", pInteracoes.getDescricao());
        values.put("dataCadastro", Util_IO.dateTimeToString(pInteracoes.getDataCadastro(), Config.FormatDateTimeStringBanco));
        values.put("idUsuario", pInteracoes.getIdUsuario());
        values.put("usuario", pInteracoes.getUsuario());
        values.put("sync", 0);
        values.put("idChamadoMotivo", pInteracoes.getIdCallReason());
        long codigoInteracao = SimpleDbHelper.INSTANCE.open(mContext).insert(mTabelaInteracoes, null, values);

        if (pListAnexos != null && pListAnexos.size() > 0) {
            for (NovoAnexo anexo : pListAnexos) {
                String nomeArquivo = Utilidades.getFilename(mContext);
                Utilidades.compressImage(anexo.getLocalArquivo(), nomeArquivo);
                values = new ContentValues();
                values.put("idInteracaoMobile", codigoInteracao);
                if (pInteracoes.getChamadoID() > 0)
                    values.put("idChamado", pInteracoes.getChamadoID());
                else
                    values.put("idChamadoMobile", pInteracoes.getIdAppMobile());
                values.put("localArquivo", nomeArquivo);
                values.put("nomeArquivo", anexo.getNomeArquivo());
                values.put("tipoArquivo", anexo.getTipoArquivo());
                SimpleDbHelper.INSTANCE.open(mContext).insert(mTabelaAnexo, null, values);
            }
        }
    }

    public ArrayList<Chamado> getChamados(int pTipo) {
        Util_IO.StringBuilder sb = retornaQueryChamado();
        if (pTipo == 1)
            sb.appendLine("AND a.idSolicitante = (SELECT id FROM Colaborador)");
        else
            sb.appendLine("AND a.idAtendente = (SELECT id FROM Colaborador)");
        sb.append("ORDER BY a.idChamado DESC");
        Cursor cursor = null;
        ArrayList<Chamado> lista = new ArrayList<>();
        try {
            cursor = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(sb.toString(), null);
            Chamado chamado;
            if (cursor.moveToFirst()) {
                do {
                    chamado = retornaObjChamado(cursor);
                    if (chamado != null)
                        lista.add(chamado);
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

    public Chamado getChamadoById(String pIdChamado) {
        Util_IO.StringBuilder sbSQL = retornaQueryChamado();
        sbSQL.appendLine("AND a.id = ?");

        try (Cursor cursor = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(sbSQL.toString(), new String[]{pIdChamado})) {
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                return retornaObjChamado(cursor);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    public Chamado getChamadoByClienteId(String pIdCliente) {
        Util_IO.StringBuilder sbSQL = retornaQueryChamado();
        sbSQL.appendLine("AND a.idCliente = ?");
        sbSQL.appendLine("AND a.status IN (1,2,3)");
        sbSQL.appendLine("AND (a.dataAgendamento IS NULL OR date(a.dataAgendamento) = date('now','localtime'))");

        try (Cursor cursor = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(sbSQL.toString(), new String[]{pIdCliente})) {
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                return retornaObjChamado(cursor);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    private Util_IO.StringBuilder retornaQueryChamado() {
        Util_IO.StringBuilder sbSQL = new Util_IO.StringBuilder();
        sbSQL.appendLine("SELECT a.idChamado");
        sbSQL.appendLine(",a.assunto");
        sbSQL.appendLine(",a.solicitante");
        sbSQL.appendLine(",a.status");
        sbSQL.appendLine(",a.dataCadastro");
        sbSQL.appendLine(",IFNULL(a.dataAlteracao,a.dataCadastro)");
        sbSQL.appendLine(",a.idMobile");
        sbSQL.appendLine(",a.id");
        sbSQL.appendLine(",IFNULL(a.idSolicitante,0)");
        sbSQL.appendLine(",IFNULL(a.idDepartamento,0)");
        sbSQL.appendLine(",IFNULL(a.idFilial,0)");
        sbSQL.appendLine(",IFNULL(a.idCliente,0)");
        sbSQL.appendLine(",IFNULL(a.dataAgendamento,'')");
        sbSQL.appendLine(",a.data");
        sbSQL.appendLine(",b.nomeFantasia");
        sbSQL.appendLine("FROM Chamados a");
        sbSQL.appendLine("LEFT JOIN Cliente b ON a.idCliente == b.id");
        sbSQL.appendLine("WHERE 1=1");
        return sbSQL;
    }

    private Chamado retornaObjChamado(Cursor cursor) {
        if (cursor != null) {
            Chamado chamado = new Chamado();
            chamado.setChamadoID(cursor.getInt(0));
            chamado.setAssunto(cursor.getString(1));
            chamado.setSolicitante(cursor.getString(2));
            chamado.setStatusID(cursor.getInt(3));
            chamado.setDataCadastro(Util_IO.stringToDate(cursor.getString(4), Config.FormatDateTimeStringBanco));
            chamado.setDataAlteracao(Util_IO.stringToDate(cursor.getString(5), Config.FormatDateTimeStringBanco));
            chamado.setId(cursor.getInt(6));
            chamado.setIdAppMobile(cursor.getInt(7));
            chamado.setSolicitanteID(cursor.getInt(8));
            chamado.setDepartamentoID(cursor.getInt(9));
            chamado.setFilialID(cursor.getInt(10));
            chamado.setIdCliente(cursor.getInt(11));
            chamado.setDataAgendamento(Util_IO.stringToDate(cursor.getString(12), Config.FormatDateTimeStringBanco));
            chamado.setDataAppMobile(Util_IO.stringToDate(cursor.getString(13), Config.FormatDateTimeStringBanco));
            chamado.setNomeFantasiaCliente(cursor.getString(14));
            return chamado;
        } else
            return null;
    }

    public ArrayList<InteracaoAnexos> getInteracoesByChamadoId(int pIdChamado) {
        Util_IO.StringBuilder sbSQL = retornaQueryInteracoes();
        sbSQL.appendLine("AND idChamado = ?");
        sbSQL.appendLine("ORDER BY dataCadastro");
        Cursor cursor = null, cursor2 = null;
        try {
            cursor = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(sbSQL.toString(), new String[]{String.valueOf(pIdChamado)});
            ArrayList<InteracaoAnexos> lista = new ArrayList<>();
            InteracaoAnexos interacaoAnexos;
            if (cursor.moveToFirst()) {
                do {
                    interacaoAnexos = retornaObjInteracoesAnexos(cursor);
                    if (interacaoAnexos != null) {
                        ArrayList<AnexoChamado> listaAnexos = new ArrayList<>();
                        sbSQL = retornaQueryAnexos();
                        if (interacaoAnexos.getInteracaoID() > 0)
                            sbSQL.appendLine("AND idInteracao = " + interacaoAnexos.getInteracaoID());
                        else
                            sbSQL.appendLine("AND idInteracaoMobile = " + interacaoAnexos.getIdAppMobile());
                        sbSQL.appendLine("AND idChamado = ?");
                        cursor2 = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(sbSQL.toString(), new String[]{String.valueOf(interacaoAnexos.getChamadoID())});
                        if (cursor2 != null && cursor2.getCount() > 0) {
                            if (cursor2.moveToFirst()) {
                                do {
                                    AnexoChamado anexoChamado = retornaAnexo(cursor2);
                                    if (anexoChamado != null)
                                        listaAnexos.add(anexoChamado);
                                } while (cursor2.moveToNext());
                            }
                        }
                        interacaoAnexos.setListaAnexos(listaAnexos);
                        lista.add(interacaoAnexos);
                    }
                } while (cursor.moveToNext());
            }
            return lista;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        } finally {
            if (cursor != null)
                cursor.close();
            if (cursor2 != null)
                cursor2.close();
        }
    }

    public ArrayList<InteracaoAnexos> getInteracoesByIdMobile(int pIdChamado) {
        Util_IO.StringBuilder sbSQL = retornaQueryInteracoes();
        sbSQL.appendLine("AND idChamadoMobile = ?");
        sbSQL.appendLine("ORDER BY dataCadastro");
        Cursor cursor = null, cursor2 = null;
        try {
            cursor = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(sbSQL.toString(), new String[]{String.valueOf(pIdChamado)});
            ArrayList<InteracaoAnexos> lista = new ArrayList<>();
            InteracaoAnexos interacaoAnexos;
            if (cursor.moveToFirst()) {
                do {
                    interacaoAnexos = retornaObjInteracoesAnexos(cursor);
                    if (interacaoAnexos != null) {
                        ArrayList<AnexoChamado> listaAnexos = new ArrayList<>();
                        sbSQL = retornaQueryAnexos();
                        sbSQL.appendLine("AND idInteracaoMobile = ?");
                        sbSQL.appendLine("AND idChamadoMobile = ?");
                        cursor2 = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(sbSQL.toString(), new String[]{String.valueOf(interacaoAnexos.getIdAppMobile()), String.valueOf(pIdChamado)});
                        if (cursor2 != null && cursor2.getCount() > 0) {
                            if (cursor2.moveToFirst()) {
                                do {
                                    AnexoChamado anexoChamado = retornaAnexo(cursor2);
                                    if (anexoChamado != null)
                                        listaAnexos.add(anexoChamado);
                                } while (cursor2.moveToNext());
                            }
                        }
                        interacaoAnexos.setListaAnexos(listaAnexos);
                        lista.add(interacaoAnexos);
                    }
                } while (cursor.moveToNext());
            }
            return lista;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        } finally {
            if (cursor != null)
                cursor.close();
            if (cursor2 != null)
                cursor2.close();
        }
    }

    private Util_IO.StringBuilder retornaQueryInteracoes() {
        Util_IO.StringBuilder sbSQL = new Util_IO.StringBuilder();
        sbSQL.appendLine("SELECT IFNULL(usuario,'')");
        sbSQL.appendLine(",IFNULL(descricao,'')");
        sbSQL.appendLine(",dataCadastro");
        sbSQL.appendLine(",IFNULL(idChamado,0)");
        sbSQL.appendLine(",IFNULL(idUsuario,0)");
        sbSQL.appendLine(",id");
        sbSQL.appendLine(",data");
        sbSQL.appendLine(",IFNULL(idInteracao,0)");
        sbSQL.appendLine(", idChamadoMotivo ");
        sbSQL.appendLine("FROM ChamadosInteracoes");
        sbSQL.appendLine("WHERE 1=1");
        return sbSQL;
    }

    private Interacoes retornaObjInteracoes(Cursor cursor) {
        if (cursor != null) {
            Interacoes interacoes = new Interacoes();
            interacoes.setUsuario(cursor.getString(0));
            interacoes.setDescricao(cursor.getString(1));
            interacoes.setDataCadastro(Util_IO.stringToDate(cursor.getString(2), Config.FormatDateTimeStringBanco));
            interacoes.setChamadoID(cursor.getInt(3));
            interacoes.setIdUsuario(cursor.getInt(4));
            interacoes.setIdAppMobile(cursor.getInt(5));
            interacoes.setDataAppMobile(Util_IO.stringToDate(cursor.getString(6), Config.FormatDateTimeStringBanco));
            interacoes.setInteracaoID(cursor.getInt(7));
            interacoes.setIdCallReason(cursor.getInt(8));
            return interacoes;
        } else
            return null;
    }

    private InteracaoAnexos retornaObjInteracoesAnexos(Cursor cursor) {
        if (cursor != null) {
            InteracaoAnexos interacaoAnexos = new InteracaoAnexos();
            interacaoAnexos.setUsuario(cursor.getString(0));
            interacaoAnexos.setDescricao(cursor.getString(1));
            interacaoAnexos.setDataCadastro(Util_IO.stringToDate(cursor.getString(2), Config.FormatDateTimeStringBanco));
            interacaoAnexos.setChamadoID(cursor.getInt(3));
            interacaoAnexos.setIdUsuario(cursor.getInt(4));
            interacaoAnexos.setIdAppMobile(cursor.getInt(5));
            interacaoAnexos.setDataAppMobile(Util_IO.stringToDate(cursor.getString(6), Config.FormatDateTimeStringBanco));
            interacaoAnexos.setInteracaoID(cursor.getInt(7));
            return interacaoAnexos;
        } else
            return null;
    }

    public void updateChamado(int pCodigo, int pStatus) {
        ContentValues values = new ContentValues();
        values.put("status", pStatus);
        values.put("sync", 0);
        SimpleDbHelper.INSTANCE.open(mContext).update(mTabelaChamado, values, "[id]=?", new String[]{String.valueOf(pCodigo)});
    }

    public ArrayList<Interacoes> getInteracoesPendentes() {
        Util_IO.StringBuilder sbSQL = retornaQueryInteracoes();
        sbSQL.appendLine("AND sync = 0");
        sbSQL.appendLine("AND IFNULL(idChamado,0) != 0");
        try (Cursor cursor = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(sbSQL.toString(), null)) {
            ArrayList<Interacoes> lista = new ArrayList<>();
            Interacoes interacoes;
            if (cursor.moveToFirst()) {
                do {
                    interacoes = retornaObjInteracoes(cursor);
                    if (interacoes != null)
                        lista.add(interacoes);
                } while (cursor.moveToNext());
            }
            return lista;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public void updataInteracoesSync(int pCodigo) {
        ContentValues values = new ContentValues();
        values.put("sync", 1);
        SimpleDbHelper.INSTANCE.open(mContext).update(mTabelaInteracoes, values, "[id]=?", new String[]{String.valueOf(pCodigo)});
    }

    private void updataInteracoesIdChamado(int pCodigo, int pIdChamado) {
        ContentValues values = new ContentValues();
        values.put("idChamado", pIdChamado);
        SimpleDbHelper.INSTANCE.open(mContext).update(mTabelaInteracoes, values, "[idChamadoMobile]=?", new String[]{String.valueOf(pCodigo)});
    }

    private void updateAnexosIdIntraflex(int pCodigo, int pIdChamado, int pIdInteracao) {
        ContentValues values = new ContentValues();
        values.put("idChamado", pIdChamado);
        values.put("idInteracao", pIdInteracao);
        SimpleDbHelper.INSTANCE.open(mContext).update(mTabelaAnexo, values, "[idInteracaoMobile]=?", new String[]{String.valueOf(pCodigo)});
    }

    public void updataChamadosSync(int pCodigo) {
        ContentValues values = new ContentValues();
        values.put("sync", 1);
        SimpleDbHelper.INSTANCE.open(mContext).update(mTabelaChamado, values, "id=?", new String[]{String.valueOf(pCodigo)});
    }

    public int retornaCodigo(int pIdMobile) {
        try (Cursor cursor = SimpleDbHelper.INSTANCE.open(mContext).query(mTabelaChamado, new String[]{"id"}
                , "[idMobile]=?", new String[]{String.valueOf(pIdMobile)}, null, null, null, null)) {
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                return cursor.getInt(0);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return 0;
    }

    public int retornaCodigoInteracao(int pIdMobile) {
        try (Cursor cursor = SimpleDbHelper.INSTANCE.open(mContext).query(mTabelaChamado, new String[]{"id"}
                , "[idChamado]=?", new String[]{String.valueOf(pIdMobile)}, null, null, null, null)) {
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                return cursor.getInt(0);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return 0;
    }

    public ArrayList<Chamado> getChamadosPendentes() {
        Util_IO.StringBuilder sbSQL = retornaQueryChamado();
        sbSQL.appendLine("AND a.sync = 0");
        try (Cursor cursor = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(sbSQL.toString(), null)) {
            ArrayList<Chamado> lista = new ArrayList<>();
            Chamado chamado;
            if (cursor.moveToFirst()) {
                do {
                    chamado = retornaObjChamado(cursor);
                    if (chamado != null)
                        lista.add(chamado);
                } while (cursor.moveToNext());
            }
            return lista;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public boolean addAnexosWebService(AnexoChamado pAnexoChamado) {
        Cursor cursor = SimpleDbHelper.INSTANCE.open(mContext).query(mTabelaInteracoes, new String[]{"id"}
                , "[idInteracao]=?", new String[]{String.valueOf(pAnexoChamado.getInteracaoID())}, null, null, null, null);
        try {
            if (cursor == null || cursor.getCount() == 0)
                return false;
        } finally {
            if (cursor != null)
                cursor.close();
        }

        ContentValues values = new ContentValues();
        values.put("idMobile", pAnexoChamado.getId());
        values.put("idInteracao", pAnexoChamado.getInteracaoID());
        values.put("idChamado", pAnexoChamado.getChamadoID());
        values.put("dataCadastro", Util_IO.dateTimeToString(pAnexoChamado.getDataCadastro(), Config.FormatDateTimeStringBanco));
        values.put("localArquivo", pAnexoChamado.getArquivo());
        values.put("nomeArquivo", pAnexoChamado.getNome());
        values.put("tipoArquivo", pAnexoChamado.getTipo());
        values.put("sync", 1);

        if (pAnexoChamado.getIdAppMobile() != null) {
            cursor = SimpleDbHelper.INSTANCE.open(mContext).query(mTabelaAnexo, new String[]{"id"}
                    , "[id]=?", new String[]{String.valueOf(pAnexoChamado.getIdAppMobile())}, null, null, null, null);
            try {
                if (cursor == null || cursor.getCount() == 0)
                    SimpleDbHelper.INSTANCE.open(mContext).insert(mTabelaAnexo, null, values);
                else
                    SimpleDbHelper.INSTANCE.open(mContext).update(mTabelaAnexo, values, "[id]=?", new String[]{String.valueOf(pAnexoChamado.getIdAppMobile())});
            } finally {
                if (cursor != null)
                    cursor.close();
            }
        } else {
            try {
                if (!Util_DB.isCadastrado(mContext, mTabelaAnexo, "idMobile", String.valueOf(pAnexoChamado.getId())))
                    SimpleDbHelper.INSTANCE.open(mContext).insert(mTabelaAnexo, null, values);
                else
                    SimpleDbHelper.INSTANCE.open(mContext).update(mTabelaAnexo, values, "[idMobile]=?", new String[]{String.valueOf(pAnexoChamado.getId())});
            } catch (Exception ex) {
                return false;
            }
        }
        return true;
    }

    private Util_IO.StringBuilder retornaQueryAnexos() {
        Util_IO.StringBuilder sbSQL = new Util_IO.StringBuilder();
        sbSQL.appendLine("SELECT id");
        sbSQL.appendLine(",nomeArquivo");
        sbSQL.appendLine(",localArquivo");
        sbSQL.appendLine(",idInteracao");
        sbSQL.appendLine(",idChamado");
        sbSQL.appendLine(",tipoArquivo");
        sbSQL.appendLine(",dataCadastro");
        sbSQL.appendLine(",data");
        sbSQL.appendLine("FROM [ChamadosAnexos]");
        sbSQL.appendLine("WHERE 1=1");
        return sbSQL;
    }

    private AnexoChamado retornaAnexo(Cursor cursor) {
        if (cursor != null) {
            AnexoChamado anexoChamado = new AnexoChamado();
            anexoChamado.setIdAppMobile(cursor.getInt(0));
            anexoChamado.setNome(cursor.getString(1));
            anexoChamado.setArquivo(cursor.getString(2));
            anexoChamado.setInteracaoID(cursor.getInt(3));
            anexoChamado.setChamadoID(cursor.getInt(4));
            anexoChamado.setTipo(cursor.getString(5));
            anexoChamado.setDataCadastro(Util_IO.stringToDate(cursor.getString(6), Config.FormatDateTimeStringBanco));
            anexoChamado.setDataAppMobile(Util_IO.stringToDate(cursor.getString(7), Config.FormatDateTimeStringBanco));
            return anexoChamado;
        } else
            return null;
    }

    public ArrayList<AnexoChamado> getAnexosPendentes() {
        Util_IO.StringBuilder sbSQL = retornaQueryAnexos();
        sbSQL.appendLine("AND sync = 0");
        sbSQL.appendLine("AND IFNULL(idChamado,0) != 0");
        sbSQL.appendLine("AND IFNULL(idInteracao,0) != 0");
        Cursor cursor = null;
        ArrayList<AnexoChamado> lista = new ArrayList<>();
        try {
            cursor = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(sbSQL.toString(), null);
            AnexoChamado anexoChamado;
            if (cursor.moveToFirst()) {
                do {
                    anexoChamado = retornaAnexo(cursor);
                    if (anexoChamado != null)
                        lista.add(anexoChamado);
                } while (cursor.moveToNext());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return lista;
    }

    public void updataAnexoSync(int pCodigo) {
        ContentValues values = new ContentValues();
        values.put("sync", 1);
        SimpleDbHelper.INSTANCE.open(mContext).update(mTabelaAnexo, values, "[id]=?", new String[]{String.valueOf(pCodigo)});
    }

    public void deleteFinalizados() {
        Cursor cursor = null, cursor2 = null;
        try {
            Util_IO.StringBuilder sbSQL = new Util_IO.StringBuilder();
            sbSQL.appendLine("SELECT [id]");
            sbSQL.appendLine(",[idChamado]");
            sbSQL.appendLine("FROM Chamados");
            sbSQL.appendLine("WHERE IFNULL(dataAlteracao,dataCadastro) < datetime('now', '-7 day')");
            sbSQL.appendLine("AND status IN (6,7) AND sync = 1");

            cursor = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(sbSQL.toString(), null);
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    do {
                        sbSQL = new Util_IO.StringBuilder();
                        sbSQL.appendLine("SELECT [id]");
                        sbSQL.appendLine(",IFNULL([localArquivo],'')");
                        sbSQL.appendLine("FROM ChamadosAnexos");
                        sbSQL.appendLine("WHERE [idChamado] = ?");
                        cursor2 = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(sbSQL.toString(), new String[]{cursor.getString(1)});
                        if (cursor2 != null) {
                            if (cursor2.moveToFirst()) {
                                do {
                                    if (cursor2.getString(1).length() > 0)
                                        Utilidades.deletaArquivo(cursor2.getString(1));
                                    SimpleDbHelper.INSTANCE.open(mContext).delete(mTabelaAnexo, "[id]=? ", new String[]{cursor2.getString(0)});
                                } while (cursor2.moveToNext());
                            }
                        }
                        SimpleDbHelper.INSTANCE.open(mContext).delete(mTabelaInteracoes, "[idChamado]=? ", new String[]{cursor.getString(1)});
                        SimpleDbHelper.INSTANCE.open(mContext).delete(mTabelaChamado, "[id]=? ", new String[]{cursor.getString(0)});
                    } while (cursor.moveToNext());
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
            if (cursor2 != null)
                cursor2.close();
        }
    }

    public List<Interacoes> selectAllCallsByReasonId (int id) {
        Util_IO.StringBuilder sbSQL = retornaQueryInteracoes();
        sbSQL.appendLine(" AND idChamadoMotivo = ? ");

        try (Cursor cursor = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(sbSQL.toString(), new String[]{String.valueOf(id)})) {
            List<Interacoes> list = new ArrayList<>();
            if (cursor.moveToFirst()) {
                do {
                    Interacoes interacoes = retornaObjInteracoes(cursor);
                    if (interacoes != null)
                        list.add(interacoes);
                } while (cursor.moveToNext());
            }
            return list;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
