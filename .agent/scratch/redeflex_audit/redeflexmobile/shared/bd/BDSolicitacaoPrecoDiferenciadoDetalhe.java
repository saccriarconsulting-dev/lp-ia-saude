package com.axys.redeflexmobile.shared.bd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.axys.redeflexmobile.shared.models.SolicitacaoPrecoDiferenciado;
import com.axys.redeflexmobile.shared.models.SolicitacaoPrecoDiferenciadoDetalhe;
import com.axys.redeflexmobile.shared.util.Util_DB;
import com.axys.redeflexmobile.shared.util.Util_IO;
import com.axys.redeflexmobile.ui.redeflex.Config;

import java.util.ArrayList;

public class BDSolicitacaoPrecoDiferenciadoDetalhe {
    private final Context mContext;
    private final String mTabela = "SolicitacaoPrecoDiferenciadoDetalhe";

    public BDSolicitacaoPrecoDiferenciadoDetalhe(Context pContext) {
        mContext = pContext;
    }

    public long addSolicitacaoDetalhes(SolicitacaoPrecoDiferenciadoDetalhe pDetalhe) throws Exception {
        ContentValues values = new ContentValues();
        values.put("IdSolicitacao", pDetalhe.getIdSolicitacao());
        values.put("IdServer", pDetalhe.getIdServer());
        values.put("DDD", pDetalhe.getDDD());
        values.put("IdCliente", pDetalhe.getIdCliente());
        values.put("CodigoClienteSGV", pDetalhe.getCodigoClienteSGV());
        values.put("ItemCode", pDetalhe.getItemCode());
        values.put("Quantidade", pDetalhe.getQuantidade());
        values.put("PrecoVenda", pDetalhe.getPrecoVenda());
        values.put("PrecoDiferenciado", pDetalhe.getPrecoDiferenciado());
        values.put("TipoPagamentoId", pDetalhe.getTipoPagamentoId());
        values.put("StatusId", pDetalhe.getStatusId());
        values.put("IdVendedor", pDetalhe.getIdVendedor());
        values.put("MotivoRecusada", pDetalhe.getMotivoRecusada());
        values.put("Aplicada", pDetalhe.getAplicada());
        values.put("Justificativa", pDetalhe.getJustificativa());
        long codigoDetalhe = 0;

        if (pDetalhe.getIdServer() > 0) {
            if (!Util_DB.isCadastrado(mContext, mTabela, "IdServer", String.valueOf(pDetalhe.getIdServer())))
                codigoDetalhe = SimpleDbHelper.INSTANCE.open(mContext).insert(mTabela, null, values);
            else
                codigoDetalhe = SimpleDbHelper.INSTANCE.open(mContext).update(mTabela, values, "[IdServer]=?", new String[]{String.valueOf(pDetalhe.getIdServer())});
        } else {
            if (pDetalhe.getId() <= 0)
                codigoDetalhe = SimpleDbHelper.INSTANCE.open(mContext).insert(mTabela, null, values);
            else if (!Util_DB.isCadastrado(mContext, mTabela, "Id", String.valueOf(pDetalhe.getId())))
                codigoDetalhe = SimpleDbHelper.INSTANCE.open(mContext).insert(mTabela, null, values);
            else
                codigoDetalhe = SimpleDbHelper.INSTANCE.open(mContext).update(mTabela, values, "[Id]=?", new String[]{String.valueOf(pDetalhe.getId())});
        }

        return codigoDetalhe;
    }

    public void deleteById(int pId) {
        SimpleDbHelper.INSTANCE.open(mContext).delete(mTabela, "[Id]=?", new String[]{String.valueOf(pId)});
    }

    public ArrayList<SolicitacaoPrecoDiferenciadoDetalhe> getSolicitacaoDetalhes(String pCondicao, String[] pCampos) {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();

        sb.appendLine("Select Id, IdSolicitacao, IdServer, DDD, IdCliente, CodigoClienteSGV, ItemCode, Quantidade,");
        sb.appendLine(" PrecoVenda, PrecoDiferenciado, TipoPagamentoId, StatusId, IdVendedor, MotivoRecusada, Aplicada, Justificativa");
        sb.appendLine("from SolicitacaoPrecoDiferenciadoDetalhe");
        sb.appendLine("WHERE 1=1");
        if (pCondicao != null)
            sb.append(pCondicao);
        sb.appendLine(" Order by Id");
        return moverCursorDb(sb, pCampos);
    }

    private ArrayList<SolicitacaoPrecoDiferenciadoDetalhe> moverCursorDb(Util_IO.StringBuilder sb, String[] args) {
        Cursor cursor = null;
        ArrayList<SolicitacaoPrecoDiferenciadoDetalhe> lista = new ArrayList<>();
        try {
            cursor = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(sb.toString(), args);
            SolicitacaoPrecoDiferenciadoDetalhe detalhe;
            if (cursor.moveToFirst()) {
                do {
                    detalhe = new SolicitacaoPrecoDiferenciadoDetalhe();
                    detalhe.setId(cursor.getInt(cursor.getColumnIndexOrThrow("Id")));
                    detalhe.setIdSolicitacao(cursor.getInt(cursor.getColumnIndexOrThrow("IdSolicitacao")));
                    detalhe.setIdServer(cursor.getInt(cursor.getColumnIndexOrThrow("IdServer")));
                    detalhe.setDDD(cursor.getInt(cursor.getColumnIndexOrThrow("DDD")));
                    detalhe.setIdCliente(cursor.getInt(cursor.getColumnIndexOrThrow("IdCliente")));
                    detalhe.setCodigoClienteSGV(cursor.getString(cursor.getColumnIndexOrThrow("CodigoClienteSGV")));
                    detalhe.setItemCode(cursor.getString(cursor.getColumnIndexOrThrow("ItemCode")));
                    detalhe.setQuantidade(cursor.getInt(cursor.getColumnIndexOrThrow("Quantidade")));
                    detalhe.setPrecoVenda(cursor.getDouble(cursor.getColumnIndexOrThrow("PrecoVenda")));
                    detalhe.setPrecoDiferenciado(cursor.getDouble(cursor.getColumnIndexOrThrow("PrecoDiferenciado")));
                    detalhe.setTipoPagamentoId(cursor.getInt(cursor.getColumnIndexOrThrow("TipoPagamentoId")));
                    detalhe.setStatusId(cursor.getInt(cursor.getColumnIndexOrThrow("StatusId")));
                    detalhe.setIdVendedor(cursor.getInt(cursor.getColumnIndexOrThrow("IdVendedor")));
                    detalhe.setMotivoRecusada(cursor.getString(cursor.getColumnIndexOrThrow("MotivoRecusada")));
                    detalhe.setAplicada(cursor.getString(cursor.getColumnIndexOrThrow("Aplicada")));
                    detalhe.setJustificativa(cursor.getString(cursor.getColumnIndexOrThrow("Justificativa")));
                    lista.add(detalhe);
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
