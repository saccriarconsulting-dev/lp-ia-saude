package com.axys.redeflexmobile.shared.bd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.axys.redeflexmobile.shared.models.PrecoDiferenciado;
import com.axys.redeflexmobile.shared.models.RetornoVenda;
import com.axys.redeflexmobile.shared.util.Util_DB;
import com.axys.redeflexmobile.shared.util.Util_IO;
import com.axys.redeflexmobile.ui.redeflex.Config;

import java.util.ArrayList;

/**
 * Created by joao.viana on 18/08/2016.
 */
public class DBPreco {
    private String mTabelaPreco = "PrecoDiferenciado";
    private String mTabelaLog = "LogPreco";
    private Context mContext;

    public DBPreco(Context pContext) {
        mContext = pContext;
    }

    public void addPreco(PrecoDiferenciado pPrecoDiferenciado) throws Exception {
        ContentValues values = new ContentValues();
        values.put("idProduto", pPrecoDiferenciado.getIdProduto());
        values.put("data", Util_IO.dateTimeToString(pPrecoDiferenciado.getDataCadastro(), Config.FormatDateTimeStringBanco));
        values.put("situacao", pPrecoDiferenciado.getSituacao());
        values.put("preco", pPrecoDiferenciado.getValor());
        values.put("datainicio", Util_IO.dateTimeToString(pPrecoDiferenciado.getDataInicial(), Config.FormatDateStringBanco));
        values.put("datafim", Util_IO.dateTimeToString(pPrecoDiferenciado.getDataFinal(), Config.FormatDateStringBanco));
        values.put("idCliente", (Util_IO.isNullOrEmpty(pPrecoDiferenciado.getIdCliente()) ? "" : pPrecoDiferenciado.getIdCliente()));
        values.put("qtd", pPrecoDiferenciado.getQtdPreco());
        values.put("qtdVendida", pPrecoDiferenciado.getQtdVendida());
        if (!Util_DB.isCadastrado(mContext, mTabelaPreco, "id", String.valueOf(pPrecoDiferenciado.getId()))) {
            values.put("id", pPrecoDiferenciado.getId());
            SimpleDbHelper.INSTANCE.open(mContext).insert(mTabelaPreco, null, values);
        } else
            SimpleDbHelper.INSTANCE.open(mContext).update(mTabelaPreco, values, "[id]=?", new String[]{String.valueOf(pPrecoDiferenciado.getId())});
    }

    public void deleteAll() {
        SimpleDbHelper.INSTANCE.open(mContext).delete(mTabelaPreco, null, null);
        SimpleDbHelper.INSTANCE.open(mContext).delete(mTabelaLog, null, null);
    }

    public void deletePrecoUtilizado() {
        SimpleDbHelper.INSTANCE.open(mContext).delete(mTabelaPreco, "[qtd] > 0 AND [qtdVendida] >= [qtd]", null);
        SimpleDbHelper.INSTANCE.open(mContext).delete(mTabelaPreco, "date([datafim]) < date('now', '-2 day')", null);
        SimpleDbHelper.INSTANCE.open(mContext).delete(mTabelaLog, "[sync]=?", new String[]{"1"});
    }

    public ArrayList<PrecoDiferenciado> getPrecoDiferenciado(String pIdProduto, String pIdCliente) {

        if (!Util_IO.isNullOrEmpty(pIdProduto)) {

            ArrayList<PrecoDiferenciado> lista = new ArrayList<>();

            try {
                if (!Util_IO.isNullOrEmpty(pIdCliente))
                    lista.addAll(getPrecoDiferenciadoCliente(pIdProduto, pIdCliente));

                lista.addAll(getPrecoDiferenciado(pIdProduto));
                lista.addAll(getPrecoDiferenciadoByPeriodo());
                lista.addAll(getPrecoDiferenciadoByQtd());

                return lista;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return null;
    }

    public ArrayList<PrecoDiferenciado> getPrecoDiferenciado(String pIdProduto) {
        Util_IO.StringBuilder sbSql = retornaQueryPreco(true);
        sbSql.appendLine("AND [situacao] = 'A'");
        sbSql.appendLine("AND [idProduto] = '" + pIdProduto + "'");
        sbSql.appendLine("AND IFNULL([idCliente],'') = ''");
        sbSql.appendLine("AND ((date([datainicio]) <= date('now') AND date(datafim) >= date('now')) OR (IFNULL([qtd],0) > 0))");
        sbSql.appendLine("ORDER BY p.[id]");

        ArrayList<PrecoDiferenciado> lista = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(sbSql.toString(), null);
            PrecoDiferenciado precoDiferenciado;
            if (cursor.moveToFirst()) {
                do {
                    precoDiferenciado = retornaPrecoToCursor(cursor);
                    if (precoDiferenciado != null)
                        lista.add(precoDiferenciado);
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

    public ArrayList<PrecoDiferenciado> getPrecoDiferenciadoCliente(String pIdProduto, String pIdCliente) {
        Util_IO.StringBuilder sbSql = retornaQueryPreco(true);
        sbSql.appendLine("AND [situacao] = 'A'");
        sbSql.appendLine("AND CAST([idProduto] AS INT) = '" + pIdProduto + "'");
        sbSql.appendLine("AND [idCliente] = '" + pIdCliente + "'");
        sbSql.appendLine("AND ((date([datainicio]) <= date('now') AND date([datafim]) >= date('now')) OR (IFNULL([qtd],0) > 0))");
        sbSql.appendLine("ORDER BY p.[id]");

        ArrayList<PrecoDiferenciado> lista = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(sbSql.toString(), null);
            PrecoDiferenciado precoDiferenciado;
            if (cursor.moveToFirst()) {
                do {
                    precoDiferenciado = retornaPrecoToCursor(cursor);
                    if (precoDiferenciado != null)
                        lista.add(precoDiferenciado);
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

    public ArrayList<PrecoDiferenciado> getPrecoDiferenciadoPorCliente(String pIdCliente) {
        Util_IO.StringBuilder sbSql = retornaQueryPreco(false);
        sbSql.appendLine("AND [idCliente] = '" + pIdCliente + "' or [idCliente] IS NULL OR [idCliente] = ''");
        sbSql.appendLine("AND [situacao] = 'A' ");
        sbSql.appendLine("AND ((date([datainicio]) <= date('now') AND date([datafim]) >= date('now')) OR (IFNULL([qtd],0) > 0)) ");
        sbSql.appendLine("ORDER BY p.[id]");

        ArrayList<PrecoDiferenciado> lista = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(sbSql.toString(), null);
            PrecoDiferenciado precoDiferenciado;
            if (cursor.moveToFirst()) {
                do {
                    precoDiferenciado = retornaPrecoToCursor(cursor);
                    if (precoDiferenciado != null)
                        lista.add(precoDiferenciado);
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

    public void atualizaIdVenda(String pIdPreco, String pIdVenda, String pIdVendaItem, int pQuantidade) {
        ContentValues values = new ContentValues();
        values.put("idPreco", pIdPreco);
        values.put("idVenda", pIdVenda);
        values.put("idVendaItem", pIdVendaItem);
        values.put("quantidade", pQuantidade);
        SimpleDbHelper.INSTANCE.open(mContext).insert(mTabelaLog, null, values);
    }

    public void atualizaQtdPreco(String pIdPreco, int pQuantidade) {
        if (!Util_IO.isNullOrEmpty(pIdPreco)) {
            String query = "UPDATE [PrecoDiferenciado] SET [qtdVendida] = [qtdVendida] + " + String.valueOf(pQuantidade) + " WHERE [id] = " + pIdPreco;
            SimpleDbHelper.INSTANCE.open(mContext).execSQL(query);
        }
    }

    public void removeIdVenda(String pIdVendaItem) {
        Util_IO.StringBuilder sbSql = new Util_IO.StringBuilder();
        sbSql.appendLine("SELECT t0.[id], t0.[idVenda], t0.[idVendaItem], t0.[quantidade], t0.[idPreco]");
        sbSql.appendLine("FROM [LogPreco] t0");
        sbSql.appendLine("WHERE t0.[idVendaItem] = ?");
        Cursor cursor = null;
        try {
            cursor = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(sbSql.toString(), new String[]{pIdVendaItem});
            if (cursor != null && cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    do {
                        sbSql = new Util_IO.StringBuilder();
                        sbSql.appendLine("UPDATE [PrecoCliente] SET [qtdVendida] = [qtdVendida] - " + String.valueOf(cursor.getInt(3)));
                        sbSql.appendLine(" WHERE [id] = " + String.valueOf(cursor.getInt(4)));
                        SimpleDbHelper.INSTANCE.open(mContext).execSQL(sbSql.toString());
                    } while (cursor.moveToNext());
                }
            }
            SimpleDbHelper.INSTANCE.open(mContext).delete(mTabelaLog, "[idVendaItem]=?", new String[]{pIdVendaItem});
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
    }

    public void removeIdVendaGeral(String pIdVenda) {
        Util_IO.StringBuilder sbSql = new Util_IO.StringBuilder();
        sbSql.appendLine("SELECT t0.[id], t0.[idVenda], t0.[idVendaItem], t0.[quantidade], t0.[idPreco]");
        sbSql.appendLine("FROM [LogPreco] t0");
        sbSql.appendLine("WHERE t0.[idVenda] = ?");
        Cursor cursor = null;
        try {
            cursor = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(sbSql.toString(), new String[]{pIdVenda});
            if (cursor != null && cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    do {
                        sbSql = new Util_IO.StringBuilder();
                        sbSql.appendLine("UPDATE [PrecoCliente] SET [qtdVendida] = [qtdVendida] - " + String.valueOf(cursor.getInt(3)));
                        sbSql.appendLine("WHERE [id] = " + String.valueOf(cursor.getInt(4)));
                        SimpleDbHelper.INSTANCE.open(mContext).execSQL(sbSql.toString());
                    } while (cursor.moveToNext());
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        SimpleDbHelper.INSTANCE.open(mContext).delete(mTabelaLog, "[idVenda]=?", new String[]{pIdVenda});
    }

    public PrecoDiferenciado getPrecoById(String pId) {
        Util_IO.StringBuilder sbSql = retornaQueryPreco(false);
        sbSql.appendLine("AND p.[id] = ?");
        Cursor cursor = null;
        try {
            cursor = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(sbSql.toString(), new String[]{pId});
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                return retornaPrecoToCursor(cursor);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    public ArrayList<RetornoVenda> getPrecoPendenteSync() {
        Util_IO.StringBuilder sbSql = new Util_IO.StringBuilder();
        sbSql.appendLine("SELECT t0.[id], t0.[idVenda], t0.[idVendaItem], t0.[quantidade], t0.[idPreco]");
        sbSql.appendLine("FROM [LogPreco] t0");
        sbSql.appendLine("JOIN [Venda] t1 ON t1.[id] = t0.[idVenda]");
        sbSql.appendLine("JOIN [Visita] t2 ON t2.[id] = t1.[idVisita]");
        sbSql.appendLine("WHERE 1=1");
        sbSql.appendLine("AND t0.[sync] = 0");
        sbSql.appendLine("AND t2.[dataFim] IS NOT NULL");
        sbSql.appendLine("ORDER BY t0.[id]");

        ArrayList<RetornoVenda> lista = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(sbSql.toString(), null);
            RetornoVenda retornoVenda;
            if (cursor.moveToFirst()) {
                do {
                    retornoVenda = new RetornoVenda();
                    retornoVenda.setId(cursor.getInt(0));
                    retornoVenda.setIdVenda(cursor.getInt(1));
                    retornoVenda.setIdVendaItem(cursor.getInt(2));
                    retornoVenda.setQuantidade(cursor.getInt(3));
                    retornoVenda.setIdPreco(cursor.getInt(4));
                    lista.add(retornoVenda);
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

    public void atualizaSync(String pId) {
        ContentValues values = new ContentValues();
        values.put("sync", 1);
        SimpleDbHelper.INSTANCE.open(mContext).update(mTabelaLog, values, "[id]=?", new String[]{pId});
    }

    private Util_IO.StringBuilder retornaQueryPreco(boolean validaQuantidadeVendida) {
        Util_IO.StringBuilder sbSQL = new Util_IO.StringBuilder();
        sbSQL.appendLine("SELECT p.[id]");
        sbSQL.appendLine(",p.[idCliente]");
        sbSQL.appendLine(",p.[idProduto]");
        sbSQL.appendLine(",p.[data]");
        sbSQL.appendLine(",p.[situacao]");
        sbSQL.appendLine(",p.[preco]");
        sbSQL.appendLine(",p.[datainicio]");
        sbSQL.appendLine(",p.[datafim]");
        sbSQL.appendLine(",p.[qtd]");
        sbSQL.appendLine(",p.[qtdVendida]");
        sbSQL.appendLine(",prod.[nome]");
        sbSQL.appendLine("FROM [PrecoDiferenciado] p");
        sbSQL.appendLine("LEFT JOIN [Produto] prod");
        sbSQL.appendLine(" ON p.[idProduto] = prod.[id]");
        sbSQL.appendLine("WHERE 1 = 1");

        if (validaQuantidadeVendida) {
            sbSQL.appendLine("AND p.qtd > (SELECT IFNULL(SUM(i.qtde),0) FROM itemvenda i WHERE i.idPreco = p.id)");
            sbSQL.appendLine("AND NOT EXISTS (select 1 from PistolagemComboTemp pct where p.id = pct.idPreco and pct.quantidade >= p.qtd)");
        }

        return sbSQL;
    }

    private PrecoDiferenciado retornaPrecoToCursor(Cursor cursor) {
        if (cursor != null) {
            int quantidade = cursor.getInt(8);
            if (cursor.getInt(8) > 0 && cursor.getInt(9) > 0) {
                quantidade = cursor.getInt(8) - cursor.getInt(9);
                if (cursor.getInt(8) <= cursor.getInt(9))
                    return null;
            }

            PrecoDiferenciado preco = new PrecoDiferenciado();
            preco.setId(cursor.getInt(0));
            preco.setIdCliente(cursor.getString(1));
            preco.setIdProduto(cursor.getString(2));
            preco.setDataCadastro(Util_IO.stringToDate(cursor.getString(3), Config.FormatDateTimeStringBanco));
            preco.setSituacao(cursor.getString(4));
            preco.setValor(cursor.getDouble(5));
            preco.setDataInicial(Util_IO.stringToDate(cursor.getString(6), Config.FormatDateStringBanco));
            preco.setDataFinal(Util_IO.stringToDate(cursor.getString(7), Config.FormatDateStringBanco));
            preco.setQtdPreco(quantidade);
            preco.setQtdVendida(cursor.getInt(9));
            preco.setProdutoNome(cursor.getString(10));
            return preco;
        } else
            return null;
    }

    public void deletarInativos() {
        SimpleDbHelper.INSTANCE.open(mContext).delete(mTabelaPreco, "[situacao]=?", new String[]{"I"});
    }

    private ArrayList<PrecoDiferenciado> getPrecoDiferenciadoByIdVendedor() {
        return null;
    }

    private ArrayList<PrecoDiferenciado> getPrecoDiferenciadoByDDD() {
        return null;
    }

    private ArrayList<PrecoDiferenciado> getPrecoDiferenciadoByPeriodo() {
        return null;
    }

    private ArrayList<PrecoDiferenciado> getPrecoDiferenciadoByQtd() {
        return null;
    }
}