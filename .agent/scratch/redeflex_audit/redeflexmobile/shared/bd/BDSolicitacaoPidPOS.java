package com.axys.redeflexmobile.shared.bd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.axys.redeflexmobile.shared.models.SolicitacaoPidPos;
import com.axys.redeflexmobile.shared.models.SolicitacaoPidRede;
import com.axys.redeflexmobile.shared.util.Util_DB;
import com.axys.redeflexmobile.shared.util.Util_IO;
import com.axys.redeflexmobile.shared.util.Utilidades;

import java.util.ArrayList;

public class BDSolicitacaoPidPOS {
    private final Context mContext;
    private final String mTabela = "SolicitacaoPidPos";

    public BDSolicitacaoPidPOS(Context pContext) {
        mContext = pContext;
    }

    public long addSolicitacaoPidPOS(SolicitacaoPidPos pSolicitacaoPidPos) throws Exception {
        ContentValues values = new ContentValues();
        values.put("IdSolicitacaoPid", pSolicitacaoPidPos.getIdSolicitacaoPid());
        values.put("TipoMaquinaId", pSolicitacaoPidPos.getTipoMaquinaId());
        values.put("ValorAluguel", pSolicitacaoPidPos.getValorAluguel());
        values.put("TipoConexaoId", pSolicitacaoPidPos.getTipoConexaoId());
        values.put("IdOperadora", pSolicitacaoPidPos.getIdOperadora());
        values.put("MetragemCabo", pSolicitacaoPidPos.getMetragemCabo());
        values.put("Quantidade", pSolicitacaoPidPos.getQuantidade());
        values.put("Situacao", pSolicitacaoPidPos.getSituacao());

        long codigo = 0;
        if (!Util_DB.isCadastrado(mContext, mTabela, "id", String.valueOf(pSolicitacaoPidPos.getId())))
            codigo = SimpleDbHelper.INSTANCE.open(mContext).insert(mTabela, null, values);
        else
            codigo = SimpleDbHelper.INSTANCE.open(mContext).update(mTabela, values, "[id]=?", new String[]{String.valueOf(pSolicitacaoPidPos.getId())});
        return codigo;
    }

    public SolicitacaoPidPos getById(String pId) {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("AND t0.[id] = ?");
        return Utilidades.firstOrDefault(getSolicitacaoPidPOS(sb.toString(), new String[]{pId}));
    }

    public ArrayList<SolicitacaoPidPos> getSolicitacaoPidPOS(String pCondicao, String[] pCampos) {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();

        sb.appendLine("Select Id, IdSolicitacaoPid, TipoMaquinaId, ValorAluguel, TipoConexaoId, IdOperadora, MetragemCabo, Quantidade, Situacao");
        sb.appendLine("from SolicitacaoPidPos");
        sb.appendLine("WHERE 1=1");
        if (pCondicao != null)
            sb.append(pCondicao);
        sb.appendLine(" Order by Id");
        return moverCursorDb(sb, pCampos);
    }

    private ArrayList<SolicitacaoPidPos> moverCursorDb(Util_IO.StringBuilder sb, String[] args) {
        Cursor cursor = null;
        ArrayList<SolicitacaoPidPos> lista = new ArrayList<>();
        try {
            cursor = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(sb.toString(), args);
            SolicitacaoPidPos detalhe;
            if (cursor.moveToFirst()) {
                do {
                    detalhe = new SolicitacaoPidPos();
                    detalhe.setId(cursor.getInt(cursor.getColumnIndexOrThrow("Id")));
                    detalhe.setIdSolicitacaoPid(cursor.getInt(cursor.getColumnIndexOrThrow("IdSolicitacaoPid")));
                    detalhe.setTipoMaquinaId(cursor.getInt(cursor.getColumnIndexOrThrow("TipoMaquinaId")));
                    detalhe.setValorAluguel(cursor.getDouble(cursor.getColumnIndexOrThrow("ValorAluguel")));
                    detalhe.setTipoConexaoId(cursor.getInt(cursor.getColumnIndexOrThrow("TipoConexaoId")));
                    detalhe.setIdOperadora(cursor.getInt(cursor.getColumnIndexOrThrow("IdOperadora")));
                    detalhe.setMetragemCabo(cursor.getInt(cursor.getColumnIndexOrThrow("MetragemCabo")));
                    detalhe.setQuantidade(cursor.getInt(cursor.getColumnIndexOrThrow("Quantidade")));
                    detalhe.setSituacao(cursor.getInt(cursor.getColumnIndexOrThrow("Situacao")));
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
