package com.axys.redeflexmobile.shared.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import androidx.annotation.Nullable;

import com.axys.redeflexmobile.shared.bd.SimpleDbHelper;
import com.axys.redeflexmobile.shared.models.PrecoDiferenciado;
import com.axys.redeflexmobile.shared.util.Util_IO;
import com.axys.redeflexmobile.ui.redeflex.Config;

import java.util.ArrayList;

public class PrecoDaoImpl implements PrecoDao {

    private Context context;
    private String mTabelaPreco = "PrecoDiferenciado";
    private String mTabelaLog = "LogPreco";

    public PrecoDaoImpl(Context context) {
        this.context = context;
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
        sbSQL.appendLine("FROM [PrecoDiferenciado] p");
        sbSQL.appendLine("WHERE 1 = 1");

        if (validaQuantidadeVendida) {
            sbSQL.appendLine("AND p.qtd > (SELECT IFNULL(SUM(i.qtde),0) FROM itemvenda i WHERE i.idPreco = p.id AND cancelado = 0)");
            sbSQL.appendLine("AND NOT EXISTS (select 1 from PistolagemComboTemp pct where p.id = pct.idPreco and pct.quantidade >= p.qtd)");
        }

        return sbSQL;
    }

    public ArrayList<PrecoDiferenciado> getPrecoDiferenciadoCliente(String pIdProduto, String pIdCliente) {
        return (ArrayList<PrecoDiferenciado>) Tabela.TabelaPreco.obterPrecoDiferenciadoPorCliente(context, pIdProduto, pIdCliente);
    }

    public ArrayList<PrecoDiferenciado> getPrecoDiferenciado(String pIdProduto, String pIdCliente) {

        if (!Util_IO.isNullOrEmpty(pIdProduto)) {

            ArrayList<PrecoDiferenciado> lista = new ArrayList<>();

            try {
                if (!Util_IO.isNullOrEmpty(pIdCliente))
                    lista.addAll(getPrecoDiferenciadoCliente(pIdProduto, pIdCliente));

                lista.addAll(getPrecoDiferenciado(pIdProduto));

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
        sbSql.appendLine("ORDER BY [id]");

        ArrayList<PrecoDiferenciado> lista = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = SimpleDbHelper.INSTANCE.open(context).rawQuery(sbSql.toString(), null);
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

    @Nullable
    public PrecoDiferenciado getPrecoById(String pId) {
        Util_IO.StringBuilder sbSql = retornaQueryPreco(false);
        sbSql.appendLine("AND [id] = ?");
        Cursor cursor = null;
        try {
            cursor = SimpleDbHelper.INSTANCE.open(context).rawQuery(sbSql.toString(), new String[]{pId});
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

    public void removeIdVenda(String pIdVendaItem) {
        Util_IO.StringBuilder sbSql = new Util_IO.StringBuilder();
        sbSql.appendLine("SELECT t0.[id], t0.[idVenda], t0.[idVendaItem], t0.[quantidade], t0.[idPreco]");
        sbSql.appendLine("FROM [LogPreco] t0");
        sbSql.appendLine("WHERE t0.[idVendaItem] = ?");
        Cursor cursor = null;
        try {
            cursor = SimpleDbHelper.INSTANCE.open(context).rawQuery(sbSql.toString(), new String[]{pIdVendaItem});
            if (cursor != null && cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    do {
                        sbSql = new Util_IO.StringBuilder();
                        sbSql.appendLine("UPDATE [PrecoCliente] SET [qtdVendida] = [qtdVendida] - " + cursor.getInt(3));
                        sbSql.appendLine(" WHERE [id] = " + cursor.getInt(4));
                        SimpleDbHelper.INSTANCE.open(context).execSQL(sbSql.toString());
                    } while (cursor.moveToNext());
                }
            }
            SimpleDbHelper.INSTANCE.open(context).delete(mTabelaLog, "[idVendaItem]=?", new String[]{pIdVendaItem});
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
    }

    public void atualizaIdVenda(String pIdPreco, String pIdVenda, String pIdVendaItem, int pQuantidade) {
        ContentValues values = new ContentValues();
        values.put("idPreco", pIdPreco);
        values.put("idVenda", pIdVenda);
        values.put("idVendaItem", pIdVendaItem);
        values.put("quantidade", pQuantidade);
        SimpleDbHelper.INSTANCE.open(context).insert(mTabelaLog, null, values);
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
            return preco;
        } else
            return null;
    }
}