package com.axys.redeflexmobile.shared.bd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.axys.redeflexmobile.BuildConfig;
import com.axys.redeflexmobile.shared.enums.EnumStatusConsignado;
import com.axys.redeflexmobile.shared.models.CodBarra;
import com.axys.redeflexmobile.shared.models.Consignado;
import com.axys.redeflexmobile.shared.models.ConsignadoItem;
import com.axys.redeflexmobile.shared.models.ConsignadoItemCodBarra;
import com.axys.redeflexmobile.shared.models.Visita;
import com.axys.redeflexmobile.shared.util.CodigoBarra;
import com.axys.redeflexmobile.shared.util.Util_DB;
import com.axys.redeflexmobile.shared.util.Util_IO;
import com.axys.redeflexmobile.shared.util.Utilidades;
import com.axys.redeflexmobile.shared.util.DataSyncManager;
import com.axys.redeflexmobile.shared.util.DataSyncValidator;
import com.axys.redeflexmobile.ui.redeflex.Config;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;

import io.reactivex.annotations.NonNull;

public class BDConsignado {
    private final Context mContext;
    private final String mTabela = "Consignado";

    private final java.util.List<Runnable> dataSyncFixQueue = new java.util.ArrayList<>();

    public BDConsignado(Context pContext) {
        mContext = pContext;
    }

    public long addConsignado(Consignado pConsignado) throws Exception {
        ContentValues values = new ContentValues();
        values.put("IdVendedor", pConsignado.getIdVendedor());
        values.put("TipoConsignado", pConsignado.getTipoConsignado());
        values.put("IdCliente", pConsignado.getIdCliente());
        values.put("IdServer", pConsignado.getIdServer());
        values.put("IdVisita", pConsignado.getIdVisita());
        values.put("DataEmissao", Util_IO.dateTimeToString(pConsignado.getDataEmissao(), Config.FormatDateTimeStringBanco));
        values.put("ValorTotal", pConsignado.getValorTotal());

        String dsStr = DataSyncValidator.toDbIfValid(pConsignado.getDataSync());
        if (dsStr != null) {
            values.put("DataSync", dsStr);
        } else {
            values.putNull("DataSync");
            if (pConsignado.getDataSync() != null) {
                Log.w("BDConsignado",
                        "[DATASYNC] Invalid DataSync during insert; setting NULL. id=" + pConsignado.getId());
            }
        }

        values.put("Latitude", pConsignado.getLatitude());
        values.put("Longitude", pConsignado.getLongitude());
        values.put("Precisao", pConsignado.getPrecisao());
        values.put("VersaoApp", BuildConfig.VERSION_NAME);
        values.put("IdConsignadoRefer", pConsignado.getIdConsignadoRefer());
        values.put("Status", pConsignado.getStatus());

        long codigoConsignado;
        if (pConsignado.getIdServer() > 0) {
            if (!Util_DB.isCadastrado(mContext, mTabela, "idServer", String.valueOf(pConsignado.getIdServer()))) {
                codigoConsignado = SimpleDbHelper.INSTANCE.open(mContext).insert(mTabela, null, values);
            } else {
                codigoConsignado = SimpleDbHelper.INSTANCE.open(mContext).update(
                        mTabela, values, "[idServer]=?", new String[]{String.valueOf(pConsignado.getIdServer())});
            }
        } else {
            if (!Util_DB.isCadastrado(mContext, mTabela, "id", String.valueOf(pConsignado.getId()))) {
                codigoConsignado = SimpleDbHelper.INSTANCE.open(mContext).insert(mTabela, null, values);
            } else {
                codigoConsignado = SimpleDbHelper.INSTANCE.open(mContext).update(
                        mTabela, values, "[id]=?", new String[]{String.valueOf(pConsignado.getId())});
            }
        }
        return codigoConsignado;
    }

    public long novoConsignado(Visita pVisita, int pIdVendedor, int pIdConsignadoRefer) {
        ContentValues values = new ContentValues();
        values.put("IdVisita", pVisita.getId());
        values.put("IdCliente", pVisita.getIdCliente());
        values.put("IdVendedor", pIdVendedor);
        values.put("TipoConsignado", "CON");
        values.put("DataEmissao", Util_IO.dateTimeToString(new Date(), Config.FormatDateTimeStringBanco));
        values.put("ValorTotal", 0.00);
        values.put("VersaoApp", BuildConfig.VERSION_NAME);
        values.put("Status", EnumStatusConsignado.ANDAMENTO.getValue());
        if (pIdConsignadoRefer != 0) {
            values.put("IdConsignadoRefer", pIdConsignadoRefer);
        }
        return SimpleDbHelper.INSTANCE.open(mContext).insert(mTabela, null, values);
    }

    public void atualizaStatusConsignado(String pIdConsignacao, double pValorTotal, int pStatus) {
        String query = "UPDATE [Consignado] SET status = " + pStatus + ", ValorTotal = " + pValorTotal + "  WHERE id = " + pIdConsignacao;
        SimpleDbHelper.INSTANCE.open(mContext).execSQL(query);
    }

    public void atualizaStatusConsignado(String pIdConsignacao, String pStatus) {
        String query = "UPDATE [Consignado] SET status = " + pStatus + "  WHERE id = " + pIdConsignacao;
        SimpleDbHelper.INSTANCE.open(mContext).execSQL(query);
    }

    public void atualizaStatusConsignadoIdServer(String pIdServer, String pStatus) {
        String query = "UPDATE [Consignado] SET status = " + pStatus + "  WHERE idServer = " + pIdServer;
        SimpleDbHelper.INSTANCE.open(mContext).execSQL(query);
    }

    public double getByValorTotalItens(String pIdConsignado) {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("SELECT Sum(ValorUnit * Qtd) as ValorTotal");
        sb.appendLine("FROM ConsignadoItem");
        sb.appendLine("Where IdConsignado = " + pIdConsignado);
        Cursor cursor = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(sb.toString(), null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            return cursor.getDouble(0);
        }
        else
            return 0.00;
    }

    public Consignado getById(String pId) {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("AND [id] = ?");
        return Utilidades.firstOrDefault(getConsignado(sb.toString(), new String[]{pId}));
    }

    public Consignado getByIdCLiente(String pIdCliente, int pStatus) {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("AND [IdCliente] = ? AND [Status] = " + pStatus + " AND [TipoConsignado] = 'CON'");
        return Utilidades.firstOrDefault(getConsignado(sb.toString(), new String[]{pIdCliente}));
    }

    public Consignado getByIdClienteConsigAtivo(String pIdCliente) {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("AND [IdCliente] = ? AND [Status] = '0' AND [TipoConsignado] = 'CON' AND [DataEmissao] < date('now','localtime')");
        return Utilidades.firstOrDefault(getConsignado(sb.toString(), new String[]{pIdCliente}));
    }

    public Consignado getConsignadobyIdVisita(String pIdVisita) {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("AND [IdVisita] = ?");
        return Utilidades.firstOrDefault(getConsignado(sb.toString(), new String[]{pIdVisita}));
    }

    public void deleteById(String pId) {
        SimpleDbHelper.INSTANCE.open(mContext).delete(mTabela, "[id]=?", new String[]{pId});
    }

    public void deleteAll() {
        SimpleDbHelper.INSTANCE.open(mContext).delete(mTabela, null, null);
    }

    public ArrayList<Consignado> getAll() {
        return getConsignado(null, null);
    }

    private ArrayList<Consignado> getConsignado(String pCondicao, String[] pCampos) {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("Select Id, IdVendedor, TipoConsignado, IdCliente, IdServer, IdVisita");
        sb.appendLine(",DataEmissao, ValorTotal, DataSync, Latitude, Longitude");
        sb.appendLine(",Precisao, VersaoApp, IdConsignadoRefer, Status");
        sb.appendLine("FROM [Consignado]");
        sb.appendLine("WHERE 1=1");
        if (pCondicao != null)
            sb.append(pCondicao);
        sb.appendLine("Order by Id");
        return moverCursorDb(sb, pCampos);
    }

    private ArrayList<Consignado> moverCursorDb(Util_IO.StringBuilder sb, String[] args) {
        Cursor cursor = null;
        ArrayList<Consignado> lista = new ArrayList<>();
        try {
            cursor = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(sb.toString(), args);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    Consignado consignado = new Consignado();
                    consignado.setId(cursor.getInt(0));
                    consignado.setIdVendedor(cursor.getInt(1));
                    consignado.setTipoConsignado(cursor.getString(2));
                    consignado.setIdCliente(cursor.getInt(3));
                    consignado.setIdServer(cursor.getInt(4));
                    consignado.setIdVisita(cursor.getInt(5));

                    if (!cursor.isNull(6)) {
                        String de = cursor.getString(6);
                        if (!Util_IO.isNullOrEmpty(de)) {
                            consignado.setDataEmissao(Util_IO.stringToDate(de, Config.FormatDateTimeStringBanco));
                        }
                    }

                    consignado.setValorTotal(cursor.getDouble(7));

                    String rawDs = cursor.isNull(8) ? null : cursor.getString(8);
                    java.util.Date parsedDs = null;
                    if (rawDs != null) {
                        parsedDs = DataSyncManager.parse(rawDs);
                        if (parsedDs == null) {
                            DataSyncValidator.warnInvalid(rawDs, "Consignado", "Id", String.valueOf(cursor.getInt(0)));
                            enqueueDataSyncFix(cursor.getInt(0));
                        }
                    }
                    consignado.setDataSync(parsedDs);

                    consignado.setLatitude(cursor.getDouble(9));
                    consignado.setLongitude(cursor.getDouble(10));
                    consignado.setPrecisao(cursor.getDouble(11));
                    consignado.setVersaoApp(cursor.getString(12));

                    if (!cursor.isNull(13)) {
                        consignado.setIdConsignadoRefer(cursor.getInt(13));
                    }
                    consignado.setStatus(cursor.getString(14));

                    lista.add(consignado);
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

    private void enqueueDataSyncFix(final int id) {
        dataSyncFixQueue.add(() -> {
            ContentValues fix = new ContentValues();
            fix.putNull("DataSync");
            try {
                SimpleDbHelper.INSTANCE.open(mContext)
                        .update("Consignado", fix, "[id]=?", new String[]{String.valueOf(id)});
                Log.i("BDConsignado", "DataSync fix aplicado para id=" + id);
            } catch (Exception e) {
                Log.e("BDConsignado", "Erro ao aplicar fix DataSync para id=" + id, e);
            }
        });
    }

    private void runDataSyncFixQueue() {
        for (Runnable r : dataSyncFixQueue) {
            try {
                r.run();
            } catch (Exception e) {
                Log.e("BDConsignado", "Erro ao executar correção de DataSync", e);
            }
        }
        dataSyncFixQueue.clear();
    }

    public boolean iccidVendido(CodBarra pCodBarra) {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("SELECT id");
        sb.appendLine("FROM [ConsignadoItemCodBarra]");
        sb.appendLine("WHERE cancelado = 0");
        if (pCodBarra != null) {
            if (pCodBarra.getIndividual()) {
                sb.appendLine("AND ([CodigoBarraIni] = '" + pCodBarra.getCodBarraInicial() + "'");
                sb.appendLine("OR [CodigoBarraFim] = '" + pCodBarra.getCodBarraInicial() + "')");
            } else {
                sb.appendLine("AND (([CodigoBarraIni] = '" + pCodBarra.getCodBarraInicial() + "'");
                sb.appendLine("OR [CodigoBarraFim] = '" + pCodBarra.getCodBarraInicial() + "')");
                sb.appendLine("OR ([CodigoBarraIni] = '" + pCodBarra.getCodBarraFinal() + "'");
                sb.appendLine("OR [CodigoBarraFim] = '" + pCodBarra.getCodBarraFinal() + "'))");
            }
        } else
            return false;
        Cursor cursor = null;
        try {
            cursor = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(sb.toString(), null);
            return (cursor != null && cursor.getCount() > 0);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }

        return false;
    }

    public boolean iccidVendido(CodBarra pCodBarra, int pIdConsignado) {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("SELECT id");
        sb.appendLine("FROM [ConsignadoItemCodBarra]");
        sb.appendLine("WHERE cancelado is null and IdConsignado = " + pIdConsignado);
        if (pCodBarra != null) {
            if (pCodBarra.getIndividual()) {
                sb.appendLine("AND ([CodigoBarraIni] = '" + pCodBarra.getCodBarraInicial() + "'");
                sb.appendLine("OR [CodigoBarraFim] = '" + pCodBarra.getCodBarraInicial() + "')");
            } else {
                sb.appendLine("AND (([CodigoBarraIni] <= '" + pCodBarra.getCodBarraInicial() + "'");
                sb.appendLine("OR [CodigoBarraFim] = '" + pCodBarra.getCodBarraInicial() + "')");
                sb.appendLine("OR ([CodigoBarraIni] = '" + pCodBarra.getCodBarraFinal() + "'");
                sb.appendLine("OR [CodigoBarraFim] = '" + pCodBarra.getCodBarraFinal() + "'))");
            }
        } else
            return false;
        Cursor cursor = null;
        try {
            cursor = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(sb.toString(), null);
            return (cursor != null && cursor.getCount() > 0);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }

        return false;
    }

    public ArrayList<Consignado> getConsignadoPendente() {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("AND [DataSync] is null and [Status] = 0");
        ArrayList<Consignado> listaConsignado = getConsignado(sb.toString(), null);
        for (int aa=0; aa < listaConsignado.size();aa++)
        {
            ArrayList<ConsignadoItem> itens = new BDConsignadoItem(mContext).getByIdCConsignado(String.valueOf(listaConsignado.get(aa).getId()));
            for (int bb=0; bb<itens.size();bb++)
            {
                ArrayList<ConsignadoItemCodBarra> codBarras = new BDConsignadoItemCodBarra(mContext).getByIdConsignadoItens(String.valueOf(itens.get(bb).getId()));
                itens.get(bb).setListaCodigoBarra(codBarras);
            }

            listaConsignado.get(aa).setItens(itens);
        }
        return listaConsignado;
    }

    public void updateSyncConsignado(@NonNull Consignado pConsignado) {
        SQLiteDatabase db = null;
        try {
            db = SimpleDbHelper.INSTANCE.open(mContext);
            db.beginTransaction();

            if (pConsignado.getItens() == null) {
                throw new IllegalStateException("Itens nulos; manter DataSync NULL e reprocessar no próximo ciclo.");
            }

            for (int i = 0; i < pConsignado.getItens().size(); i++) {
                final ConsignadoItem item = pConsignado.getItens().get(i);

                ContentValues valuesItem = new ContentValues();
                valuesItem.put("IdServer", item.getIdServer());
                int rItem = db.update("ConsignadoItem", valuesItem, "[id]=?", new String[]{ String.valueOf(item.getId()) });
                if (rItem <= 0) {
                    throw new IllegalStateException("Falha ao atualizar ConsignadoItem id=" + item.getId());
                }

                if (item.getListaCodigoBarra() != null) {
                    for (int j = 0; j < item.getListaCodigoBarra().size(); j++) {
                        final ConsignadoItemCodBarra cb = item.getListaCodigoBarra().get(j);
                        ContentValues valuesCod = new ContentValues();
                        valuesCod.put("IdServer", cb.getIdServer());
                        int rCb = db.update("ConsignadoItemCodBarra", valuesCod, "[id]=?", new String[]{ String.valueOf(cb.getId()) });
                        if (rCb <= 0) {
                            throw new IllegalStateException("Falha ao atualizar ConsignadoItemCodBarra id=" + cb.getId());
                        }
                    }
                }
            }

            ContentValues valuesHead = new ContentValues();
            valuesHead.put("IdServer", pConsignado.getIdServer());
            // Use DataSyncValidator to generate ISO string for DataSync
            String dsTmp = DataSyncValidator.toDbIfValid(pConsignado.getDataSync());
            if (dsTmp != null) {
                valuesHead.put("DataSync", dsTmp);
            } else {
                valuesHead.putNull("DataSync");
                if (pConsignado.getDataSync() != null) {
                    Log.w("BDConsignado", "[DATASYNC] DataSync inválido ao atualizar consignado id=" + pConsignado.getId());
                }
            }

            int rHead = db.update("Consignado", valuesHead, "[id]=?", new String[]{ String.valueOf(pConsignado.getId()) });
            if (rHead <= 0) {
                throw new IllegalStateException("Falha ao atualizar cabeçalho Consignado id=" + pConsignado.getId());
            }

            Log.i("BDConsignado", "[SYNC][Consignado] id=" + pConsignado.getId() + " dsAfter=" + (dsTmp != null ? dsTmp : "NULL"));

            db.setTransactionSuccessful();

        } catch (Exception ex) {
            Log.e("BDConsignado", "updateSyncConsignado error", ex);
            throw new IllegalStateException("Não foi possível concluir a sincronização do consignado.", ex);
        } finally {
            if (db != null && db.inTransaction()) db.endTransaction();
        }
    }

    public ArrayList<CodBarra> listCodigosbyConsignado(int pIdConsignado) {
        ArrayList<CodBarra> list = new ArrayList<>();
        Util_IO.StringBuilder sbSql = new Util_IO.StringBuilder();
        sbSql.appendLine("SELECT t0.id");
        sbSql.appendLine(",IFNULL(t0.CodigoBarraIni,'')");
        sbSql.appendLine(",IFNULL(t0.CodigoBarraFim,'')");
        sbSql.appendLine(",t0.Qtd");
        sbSql.appendLine(",IFNULL(t2.grupo,0)");
        sbSql.appendLine(",t2.id");
        sbSql.appendLine("FROM [ConsignadoItemCodBarra] t0");
        sbSql.appendLine("left join ConsignadoItem t1 on t1.Id = t0.IdConsignadoItem");
        sbSql.appendLine("left join Produto t2 on t2.id = t1.IdProduto");
        sbSql.appendLine("WHERE (t0.cancelado = 0 or t0.cancelado is null)");
        sbSql.appendLine("AND t0.IdConsignado = ?");

        Cursor cursor = null;
        CodBarra codBarra;
        try {
            cursor = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(sbSql.toString(), new String[]{String.valueOf(pIdConsignado)});
            if (cursor != null && cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    do {
                        codBarra = new CodBarra();
                        codBarra.setIndividual(cursor.getString(2).length() == 0);
                        codBarra.setGrupoProduto(cursor.getInt(4));
                        codBarra.setIdProduto(cursor.getString(5));
                        if (!codBarra.getIndividual()) {
                            if (cursor.getInt(4) == 100 || cursor.getInt(4) == 229 || cursor.getInt(4) == 235) {
                                BigInteger cinicial = CodigoBarra.retornaICCID(cursor.getString(1), cursor.getInt(4));
                                BigInteger cfinal = CodigoBarra.retornaICCID(cursor.getString(2), cursor.getInt(4));
                                if (cfinal.compareTo(cinicial) > 0) {
                                    codBarra.setCodBarraInicial(cursor.getString(1));
                                    codBarra.setCodBarraFinal(cursor.getString(2));
                                } else {
                                    codBarra.setCodBarraInicial(cursor.getString(2));
                                    codBarra.setCodBarraFinal(cursor.getString(1));
                                }
                            } else {
                                BigInteger cinicial = new BigInteger(cursor.getString(1));
                                BigInteger cfinal = new BigInteger(cursor.getString(2));
                                Long retorno = cfinal.subtract(cinicial).longValue();
                                if (retorno < 0) {
                                    codBarra.setCodBarraInicial(cursor.getString(1));
                                    codBarra.setCodBarraFinal(cursor.getString(2));
                                } else {
                                    codBarra.setCodBarraInicial(cursor.getString(2));
                                    codBarra.setCodBarraFinal(cursor.getString(1));
                                }
                            }
                        } else {
                            codBarra.setCodBarraInicial(cursor.getString(1));
                            codBarra.setCodBarraFinal(cursor.getString(2));
                        }
                        list.add(codBarra);
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

    public ArrayList<CodBarra> listCodigosbyConsignadoProduto(int pIdConsignado, String pIdProduto) {
        ArrayList<CodBarra> list = new ArrayList<>();
        Util_IO.StringBuilder sbSql = new Util_IO.StringBuilder();
        sbSql.appendLine("SELECT t0.id");
        sbSql.appendLine(",IFNULL(t0.CodigoBarraIni,'')");
        sbSql.appendLine(",IFNULL(t0.CodigoBarraFim,'')");
        sbSql.appendLine(",t0.Qtd");
        sbSql.appendLine(",IFNULL(t2.grupo,0)");
        sbSql.appendLine(",t2.id");
        sbSql.appendLine("FROM [ConsignadoItemCodBarra] t0");
        sbSql.appendLine("left join ConsignadoItem t1 on t1.Id = t0.IdConsignadoItem");
        sbSql.appendLine("left join Produto t2 on t2.id = t1.IdProduto");
        sbSql.appendLine("WHERE (t0.cancelado = 0 or t0.cancelado is null)");
        sbSql.appendLine("AND t0.IdConsignado = ? and t1.IdProduto = '" + pIdProduto + "'");

        Cursor cursor = null;
        CodBarra codBarra;
        try {
            cursor = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(sbSql.toString(), new String[]{String.valueOf(pIdConsignado)});
            if (cursor != null && cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    do {
                        codBarra = new CodBarra();
                        codBarra.setIndividual(cursor.getString(2).length() == 0);
                        codBarra.setGrupoProduto(cursor.getInt(4));
                        codBarra.setIdProduto(cursor.getString(5));
                        if (!codBarra.getIndividual()) {
                            if (cursor.getInt(4) == 100 || cursor.getInt(4) == 229 || cursor.getInt(4) == 235) {
                                BigInteger cinicial = CodigoBarra.retornaICCID(cursor.getString(1), cursor.getInt(4));
                                BigInteger cfinal = CodigoBarra.retornaICCID(cursor.getString(2), cursor.getInt(4));
                                if (cfinal.compareTo(cinicial) > 0) {
                                    codBarra.setCodBarraInicial(cursor.getString(1));
                                    codBarra.setCodBarraFinal(cursor.getString(2));
                                } else {
                                    codBarra.setCodBarraInicial(cursor.getString(2));
                                    codBarra.setCodBarraFinal(cursor.getString(1));
                                }
                            } else {
                                BigInteger cinicial = new BigInteger(cursor.getString(1));
                                BigInteger cfinal = new BigInteger(cursor.getString(2));
                                Long retorno = cfinal.subtract(cinicial).longValue();
                                if (retorno < 0) {
                                    codBarra.setCodBarraInicial(cursor.getString(1));
                                    codBarra.setCodBarraFinal(cursor.getString(2));
                                } else {
                                    codBarra.setCodBarraInicial(cursor.getString(2));
                                    codBarra.setCodBarraFinal(cursor.getString(1));
                                }
                            }
                        } else {
                            codBarra.setCodBarraInicial(cursor.getString(1));
                            codBarra.setCodBarraFinal(cursor.getString(2));
                        }
                        list.add(codBarra);
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

    public boolean auditagemConsignacaoAberta(int idCliente)
    {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("select Count(*) as Qtd, (DataEmissao >= Date()) as Maior from Consignado");
        sb.appendLine(" where Status = 0 and TipoConsignado = 'CON' and IdCLiente = " + idCliente);
        sb.appendLine(" order by DataEmissao desc");
        Cursor cursor = null;
        try {
            cursor = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(sb.toString(), null);

            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                if (cursor.getInt(0) > 0 && cursor.getInt(1) == 0)
                    return true;
                else
                    return false;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }

        return false;
    }

    public Consignado getByIdConsignadoRefer(String pId) {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("AND [IdServer] = ?");
        return Utilidades.firstOrDefault(getConsignado(sb.toString(), new String[]{pId}));
    }
}