package com.axys.redeflexmobile.shared.bd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.axys.redeflexmobile.shared.models.ConsignadoItem;
import com.axys.redeflexmobile.shared.models.SolicitacaoPidRede;
import com.axys.redeflexmobile.shared.models.SolicitacaoPrecoDiferenciadoDetalhe;
import com.axys.redeflexmobile.shared.util.Util_DB;
import com.axys.redeflexmobile.shared.util.Util_IO;
import com.axys.redeflexmobile.shared.util.Utilidades;

import java.util.ArrayList;

public class BDSolicitacaoPidRede {
    private final Context mContext;
    private final String mTabela = "SolicitacaoPidRede";

    public BDSolicitacaoPidRede(Context pContext) {
        mContext = pContext;
    }

    public long addSolicitacaoPidRede(SolicitacaoPidRede pSolicitacaoPidRede) throws Exception {
        ContentValues values = new ContentValues();
        values.put("IdSolicitacaoPid", pSolicitacaoPidRede.getIdSolicitacaoPid());
        values.put("CpfCnpj", pSolicitacaoPidRede.getCpfCnpj());
        values.put("Mcc", pSolicitacaoPidRede.getMcc());
        values.put("TpvTotal", pSolicitacaoPidRede.getTpvTotal());
        values.put("TpvPorcentagem", pSolicitacaoPidRede.getTpvPorcentagem());

        long codigo = 0;
        if (!Util_DB.isCadastrado(mContext, mTabela, "id", String.valueOf(pSolicitacaoPidRede.getId())))
            codigo = SimpleDbHelper.INSTANCE.open(mContext).insert(mTabela, null, values);
        else
            codigo = SimpleDbHelper.INSTANCE.open(mContext).update(mTabela, values, "[id]=?", new String[]{String.valueOf(pSolicitacaoPidRede.getId())});
        return codigo;
    }

    public SolicitacaoPidRede getById(String pId) {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("AND t0.[id] = ?");
        return Utilidades.firstOrDefault(getSolicitacaoPidRede(sb.toString(), new String[]{pId}));
    }

    public ArrayList<SolicitacaoPidRede> getSolicitacaoPidRede(String pCondicao, String[] pCampos) {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();

        sb.appendLine("Select Id, IdSolicitacaoPid, CpfCnpj, Mcc, TpvTotal, TpvPorcentagem");
        sb.appendLine("from SolicitacaoPidRede");
        sb.appendLine("WHERE 1=1");
        if (pCondicao != null)
            sb.append(pCondicao);
        sb.appendLine(" Order by Id");
        return moverCursorDb(sb, pCampos);
    }

    private ArrayList<SolicitacaoPidRede> moverCursorDb(Util_IO.StringBuilder sb, String[] args) {
        Cursor cursor = null;
        ArrayList<SolicitacaoPidRede> lista = new ArrayList<>();
        try {
            cursor = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(sb.toString(), args);
            SolicitacaoPidRede detalhe;
            if (cursor.moveToFirst()) {
                do {
                    detalhe = new SolicitacaoPidRede();
                    detalhe.setId(cursor.getInt(cursor.getColumnIndexOrThrow("Id")));
                    detalhe.setIdSolicitacaoPid(cursor.getInt(cursor.getColumnIndexOrThrow("IdSolicitacaoPid")));
                    detalhe.setCpfCnpj(cursor.getString(cursor.getColumnIndexOrThrow("CpfCnpj")));
                    detalhe.setMcc(cursor.getString(cursor.getColumnIndexOrThrow("Mcc")));
                    detalhe.setTpvTotal(cursor.getDouble(cursor.getColumnIndexOrThrow("TpvTotal")));
                    detalhe.setTpvPorcentagem(cursor.getDouble(cursor.getColumnIndexOrThrow("TpvPorcentagem")));
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
