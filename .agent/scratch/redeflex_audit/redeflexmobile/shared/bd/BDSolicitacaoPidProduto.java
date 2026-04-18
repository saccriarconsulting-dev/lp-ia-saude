package com.axys.redeflexmobile.shared.bd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.axys.redeflexmobile.shared.models.SolicitacaoPidAnexo;
import com.axys.redeflexmobile.shared.models.SolicitacaoPidProduto;
import com.axys.redeflexmobile.shared.models.SolicitacaoPidRede;
import com.axys.redeflexmobile.shared.util.Util_DB;
import com.axys.redeflexmobile.shared.util.Util_IO;

import java.util.ArrayList;

public class BDSolicitacaoPidProduto {
    private final Context mContext;
    private final String mTabela = "SolicitacaoPidProduto";

    public BDSolicitacaoPidProduto(Context pContext) {
        mContext = pContext;
    }

    public long addSolicitacaoPidPRoduto(SolicitacaoPidProduto pSolicitacaoPidProduto) throws Exception {
        ContentValues values = new ContentValues();
        values.put("IdSolicitacaoPid", pSolicitacaoPidProduto.getIdSolicitacaoPid());
        values.put("BandeiraTipoId", pSolicitacaoPidProduto.getBandeiraTipoId());
        values.put("TaxaDebito", pSolicitacaoPidProduto.getTaxaDebito());
        values.put("TaxaCredito", pSolicitacaoPidProduto.getTaxaCredito());
        values.put("TaxaCredito6x", pSolicitacaoPidProduto.getTaxaCredito6x());
        values.put("TaxaCredito12x", pSolicitacaoPidProduto.getTaxaCredito12x());
        values.put("TaxaRavAutomatica", pSolicitacaoPidProduto.getTaxaRavAutomatica());
        values.put("TaxaRavEventual", pSolicitacaoPidProduto.getTaxaRavEventual());
        values.put("Aluguel", pSolicitacaoPidProduto.getAluguel());

        long codigo = 0;
        if (!Util_DB.isCadastrado(mContext, mTabela, "id", String.valueOf(pSolicitacaoPidProduto.getId())))
            codigo = SimpleDbHelper.INSTANCE.open(mContext).insert(mTabela, null, values);
        else
            codigo = SimpleDbHelper.INSTANCE.open(mContext).update(mTabela, values, "[id]=?", new String[]{String.valueOf(pSolicitacaoPidProduto.getId())});
        return codigo;
    }

    public ArrayList<SolicitacaoPidProduto> getSolicitacaoPidProduto(String pCondicao, String[] pCampos) {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();

        sb.appendLine("Select Id, IdSolicitacaoPid, BandeiraTipoId, TaxaDebito, TaxaCredito, TaxaCredito6x, TaxaCredito12x, TaxaRavAutomatica, TaxaRavEventual, Aluguel");
        sb.appendLine("from SolicitacaoPidProduto");
        sb.appendLine("WHERE 1=1");
        if (pCondicao != null)
            sb.append(pCondicao);
        sb.appendLine(" Order by Id");
        return moverCursorDb(sb, pCampos);
    }

    private ArrayList<SolicitacaoPidProduto> moverCursorDb(Util_IO.StringBuilder sb, String[] args) {
        Cursor cursor = null;
        ArrayList<SolicitacaoPidProduto> lista = new ArrayList<>();
        try {
            cursor = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(sb.toString(), args);
            SolicitacaoPidProduto detalhe;
            if (cursor.moveToFirst()) {
                do {
                    detalhe = new SolicitacaoPidProduto();
                    detalhe.setId(cursor.getInt(cursor.getColumnIndexOrThrow("Id")));
                    detalhe.setIdSolicitacaoPid(cursor.getInt(cursor.getColumnIndexOrThrow("IdSolicitacaoPid")));
                    detalhe.setBandeiraTipoId(cursor.getInt(cursor.getColumnIndexOrThrow("BandeiraTipoId")));
                    detalhe.setTaxaDebito(cursor.getInt(cursor.getColumnIndexOrThrow("TaxaDebito")));
                    detalhe.setTaxaCredito(cursor.getInt(cursor.getColumnIndexOrThrow("TaxaCredito")));
                    detalhe.setTaxaCredito6x(cursor.getInt(cursor.getColumnIndexOrThrow("TaxaCredito6x")));
                    detalhe.setTaxaCredito12x(cursor.getInt(cursor.getColumnIndexOrThrow("TaxaCredito12x")));
                    detalhe.setTaxaRavAutomatica(cursor.getInt(cursor.getColumnIndexOrThrow("TaxaRavAutomatica")));
                    detalhe.setTaxaRavEventual(cursor.getInt(cursor.getColumnIndexOrThrow("TaxaRavEventual")));
                    detalhe.setAluguel(cursor.getInt(cursor.getColumnIndexOrThrow("Aluguel")));
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
