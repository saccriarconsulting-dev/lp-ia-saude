package com.axys.redeflexmobile.shared.bd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.axys.redeflexmobile.shared.models.Produto;
import com.axys.redeflexmobile.shared.models.ProjetoTrade;
import com.axys.redeflexmobile.shared.models.ProjetoTradeItens;
import com.axys.redeflexmobile.shared.util.Util_DB;
import com.axys.redeflexmobile.shared.util.Util_IO;
import com.axys.redeflexmobile.ui.redeflex.Config;

import java.util.ArrayList;

/**
 * Created by joao.viana on 05/10/2016.
 */

public class DBProjetoTrade {
    private Context mContext;
    private String mTabela = "ProjetoTrade";
    private String mTabelaItens = "ProjetoTradeItens";

    public DBProjetoTrade(Context pContext) {
        mContext = pContext;
    }

    public void addProjeto(ProjetoTrade pProjetoTrade) throws Exception {
        ContentValues values = new ContentValues();
        values.put("id", pProjetoTrade.getId());
        values.put("descricao", pProjetoTrade.getDescricao());
        values.put("ddd", pProjetoTrade.getDdd());
        values.put("dataInicio", Util_IO.dateTimeToString(pProjetoTrade.getDataInicio(), Config.FormatDateStringBanco));
        values.put("dataFinal", Util_IO.dateTimeToString(pProjetoTrade.getDataFinal(), Config.FormatDateStringBanco));
        values.put("dataLimite", Util_IO.dateTimeToString(pProjetoTrade.getDataLimite(), Config.FormatDateStringBanco));
        values.put("situacao", pProjetoTrade.getSituacao());
        if (!Util_DB.isCadastrado(mContext, mTabela, "id", pProjetoTrade.getId())) {
            values.put("id", pProjetoTrade.getId());
            SimpleDbHelper.INSTANCE.open(mContext).insert(mTabela, null, values);
        } else
            SimpleDbHelper.INSTANCE.open(mContext).update(mTabela, values, "[id]=?", new String[]{pProjetoTrade.getId()});
    }

    public void deleteAll() {
        SimpleDbHelper.INSTANCE.open(mContext).delete(mTabelaItens, null, null);
        SimpleDbHelper.INSTANCE.open(mContext).delete(mTabela, null, null);
    }

    public ArrayList<ProjetoTrade> getProjetos() {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT t0.id ");
        sb.append(",t0.descricao ");
        sb.append(",t0.ddd ");
        sb.append("FROM [ProjetoTrade] t0 ");
        sb.append("WHERE t0.situacao = 'A' ");
        sb.append("AND date(t0.dataLimite) >= date('now')");
        sb.append("ORDER BY t0.descricao");
        Cursor cursor = null;
        ArrayList<ProjetoTrade> lista = new ArrayList<>();
        try {
            cursor = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(sb.toString(), null);
            ProjetoTrade projetoTrade;
            if (cursor.moveToFirst()) {
                do {
                    projetoTrade = new ProjetoTrade();
                    projetoTrade.setId(cursor.getString(0));
                    projetoTrade.setDescricao(cursor.getString(1));
                    projetoTrade.setDdd(cursor.getString(2));
                    lista.add(projetoTrade);
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

    public ArrayList<Produto> getProdutosProjeto(String pIdProjeto) {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("SELECT idProduto");
        sb.appendLine(",descProduto");
        sb.appendLine(",qtd");
        sb.appendLine(",preco");
        sb.appendLine("FROM [ProjetoTradeItens]");
        sb.appendLine("WHERE idProjeto = ?");
        sb.appendLine("AND situacao = 'A'");
        ArrayList<Produto> lista = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(sb.toString(), new String[]{pIdProjeto});
            Produto produto;
            if (cursor.moveToFirst()) {
                do {
                    produto = new Produto();
                    produto.setId(cursor.getString(0));
                    produto.setNome(cursor.getString(1));
                    produto.setQtde(cursor.getInt(2));
                    produto.setPrecovenda(cursor.getDouble(3));
                    produto.setAtivo("S");
                    produto.setBipagem("N");
                    lista.add(produto);
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

    public void addItens(ProjetoTradeItens pProjetoTradeItens) throws Exception {
        ContentValues values = new ContentValues();
        values.put("id", pProjetoTradeItens.getId());
        values.put("idProjeto", pProjetoTradeItens.getIdProjeto());
        values.put("idProduto", pProjetoTradeItens.getIdProduto());
        values.put("descProduto", pProjetoTradeItens.getDescProduto());
        values.put("qtd", pProjetoTradeItens.getQtd());
        values.put("preco", pProjetoTradeItens.getPreco());
        values.put("situacao", pProjetoTradeItens.getSituacao());
        if (!Util_DB.isCadastrado(mContext, mTabelaItens, "id", pProjetoTradeItens.getId())) {
            values.put("id", pProjetoTradeItens.getId());
            SimpleDbHelper.INSTANCE.open(mContext).insert(mTabelaItens, null, values);
        } else
            SimpleDbHelper.INSTANCE.open(mContext).update(mTabelaItens, values, "[id]=?", new String[]{pProjetoTradeItens.getId()});
    }

    public void deletePrazo() {
        StringBuilder sb = new StringBuilder();
        sb.append("DELETE FROM [ProjetoTradeItens] ");
        sb.append("WHERE idProjeto IN (SELECT id ");
        sb.append("FROM [ProjetoTrade] ");
        sb.append("WHERE date([dataLimite]) < date('now','localtime')) ");
        SimpleDbHelper.INSTANCE.open(mContext).execSQL(sb.toString());

        sb = new StringBuilder();
        sb.append("DELETE FROM [ProjetoTrade] WHERE date([dataLimite]) < date('now','localtime')");
        SimpleDbHelper.INSTANCE.open(mContext).execSQL(sb.toString());
    }
}