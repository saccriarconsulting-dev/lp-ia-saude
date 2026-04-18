package com.axys.redeflexmobile.shared.bd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.axys.redeflexmobile.shared.models.SolicitacaoPid;
import com.axys.redeflexmobile.shared.models.SolicitacaoPidAnexo;
import com.axys.redeflexmobile.shared.models.SolicitacaoPidRede;
import com.axys.redeflexmobile.shared.util.Util_IO;

import java.util.ArrayList;

public class BDSolicitacaoPidAnexo {
    private final Context mContext;
    private final String mTabela = "SolicitacaoPidAnexo";

    public BDSolicitacaoPidAnexo(Context pContext) {
        mContext = pContext;
    }
    public void addSolicitacaoPidAnexo(SolicitacaoPidAnexo pSolicitacao) throws Exception {
        ContentValues valueAttachment = new ContentValues();
        valueAttachment.put("IdSolicitacaoPid", pSolicitacao.getIdSolicitacaoPid());
        valueAttachment.put("tipo", pSolicitacao.getTipo());
        valueAttachment.put("anexo", pSolicitacao.getAnexo());
        valueAttachment.put("NomeArquivo", pSolicitacao.getNomeArquivo());
        valueAttachment.put("TipoArquivo", pSolicitacao.getTipoArquivo());
        valueAttachment.put("latitude", pSolicitacao.getLatitude());
        valueAttachment.put("longitude", pSolicitacao.getLongitude());
        valueAttachment.put("precisao", pSolicitacao.getPrecisao());
        valueAttachment.put("tamanhoArquivo", pSolicitacao.getTamanhoarquivo());
        valueAttachment.put("sync", 0);
        SimpleDbHelper.INSTANCE.open(mContext).insert(mTabela, null, valueAttachment);
    }

    public ArrayList<SolicitacaoPidAnexo> getSolicitacaoPidAnexo(String pCondicao, String[] pCampos) {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();

        sb.appendLine("Select Id, IdSolicitacaoPid, Tipo, Anexo, NomeArquivo, TipoArquivo, Latitude, Longitude, Precisao, Sync, Tamanhoarquivo");
        sb.appendLine("from SolicitacaoPidAnexo");
        sb.appendLine("WHERE 1=1");
        if (pCondicao != null)
            sb.append(pCondicao);
        sb.appendLine(" Order by Id");
        return moverCursorDb(sb, pCampos);
    }

    private ArrayList<SolicitacaoPidAnexo> moverCursorDb(Util_IO.StringBuilder sb, String[] args) {
        Cursor cursor = null;
        ArrayList<SolicitacaoPidAnexo> lista = new ArrayList<>();
        try {
            cursor = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(sb.toString(), args);
            SolicitacaoPidAnexo detalhe;
            if (cursor.moveToFirst()) {
                do {
                    detalhe = new SolicitacaoPidAnexo();
                    detalhe.setId(cursor.getInt(cursor.getColumnIndexOrThrow("Id")));
                    detalhe.setIdSolicitacaoPid(cursor.getInt(cursor.getColumnIndexOrThrow("IdSolicitacaoPid")));
                    detalhe.setTipo(cursor.getString(cursor.getColumnIndexOrThrow("Tipo")));
                    detalhe.setAnexo(cursor.getString(cursor.getColumnIndexOrThrow("Anexo")));
                    detalhe.setNomeArquivo(cursor.getString(cursor.getColumnIndexOrThrow("NomeArquivo")));
                    detalhe.setTipoArquivo(cursor.getString(cursor.getColumnIndexOrThrow("TipoArquivo")));
                    detalhe.setLatitude(cursor.getDouble(cursor.getColumnIndexOrThrow("Latitude")));
                    detalhe.setLongitude(cursor.getDouble(cursor.getColumnIndexOrThrow("Longitude")));
                    detalhe.setPrecisao(cursor.getDouble(cursor.getColumnIndexOrThrow("Precisao")));
                    detalhe.setSync(cursor.getInt(cursor.getColumnIndexOrThrow("Sync")));
                    detalhe.setTamanhoarquivo(cursor.getString(cursor.getColumnIndexOrThrow("Tamanhoarquivo")));
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
