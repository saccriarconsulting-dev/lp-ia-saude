package com.axys.redeflexmobile.shared.bd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.axys.redeflexmobile.shared.models.ConsignadoLimiteProduto;
import com.axys.redeflexmobile.shared.util.Util_DB;
import com.axys.redeflexmobile.shared.util.Util_IO;
import com.axys.redeflexmobile.shared.util.Utilidades;

import java.util.ArrayList;

public class BDConsignadoLimiteProduto {
    private final Context mContext;
    private final String mTabela = "ConsignadoLimiteProduto";

    public BDConsignadoLimiteProduto(Context pContext) {
        mContext = pContext;
    }

    public void addConsignadoLimiteProduto(ConsignadoLimiteProduto pConsignadoLimiteProduto) throws Exception {
        ContentValues values = new ContentValues();
        values.put("Id", pConsignadoLimiteProduto.getId());
        values.put("IdConsignadoLimiteCliente", pConsignadoLimiteProduto.getIdConsignadoLimiteCliente());
        values.put("IdProduto", pConsignadoLimiteProduto.getIdProduto());
        values.put("Quantidade", pConsignadoLimiteProduto.getQuantidade());

        if (!Util_DB.isCadastrado(mContext, mTabela, "id", String.valueOf(pConsignadoLimiteProduto.getId())))
            SimpleDbHelper.INSTANCE.open(mContext).insert(mTabela, null, values);
        else
            SimpleDbHelper.INSTANCE.open(mContext).update(mTabela, values, "[id]=?", new String[]{String.valueOf(pConsignadoLimiteProduto.getId())});
    }

    public ConsignadoLimiteProduto getById(String pId) {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("AND [id] = ?");
        return Utilidades.firstOrDefault(getConsignadoLimiteProduto(sb.toString(), new String[]{pId}));
    }

    public ArrayList<ConsignadoLimiteProduto> getByIdConsignadoLimiteCliente(String pIdLimiteCliente) {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("AND [IdConsignadoLimiteCliente] = ?");
        return getConsignadoLimiteProduto(sb.toString(), new String[]{pIdLimiteCliente});
    }

    public void deleteById(String pId) {
        SimpleDbHelper.INSTANCE.open(mContext).delete(mTabela, "[id]=?", new String[]{pId});
    }

    public void deleteByIdLimite(String pIdLimiteCliente) {
        SimpleDbHelper.INSTANCE.open(mContext).delete(mTabela, "[IdConsignadoLimiteCliente]=?", new String[]{pIdLimiteCliente});
    }

    public void deleteAll() {
        SimpleDbHelper.INSTANCE.open(mContext).delete(mTabela, null, null);
    }

    public ArrayList<ConsignadoLimiteProduto> getAll() {
        return getConsignadoLimiteProduto(null, null);
    }

    public ConsignadoLimiteProduto getByIdConsignadoLimiteProduto(String pIdCliente, String pIdProduto) {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        return Utilidades.firstOrDefault(getConsignadoLimiteProdutoCliente(Integer.parseInt(pIdCliente), pIdProduto));
    }

    private ArrayList<ConsignadoLimiteProduto> getConsignadoLimiteProduto(String pCondicao, String[] pCampos) {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("SELECT Id");
        sb.appendLine(",IdConsignadoLimiteCliente");
        sb.appendLine(",IdProduto");
        sb.appendLine(",Quantidade");
        sb.appendLine("FROM [ConsignadoLimiteProduto]");
        sb.appendLine("WHERE 1=1");
        if (pCondicao != null)
            sb.append(pCondicao);

        return moverCursorDb(sb, null);
    }

    public ArrayList<ConsignadoLimiteProduto> getLimiteProdutoPorCliente(String pCliente) {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("SELECT t1.Id");
        sb.appendLine(",t1.IdConsignadoLimiteCliente");
        sb.appendLine(",t1.IdProduto");
        sb.appendLine(",t1.Quantidade");
        sb.appendLine("FROM [ConsignadoLimiteCliente] t0");
        sb.appendLine("LEFT JOIN [ConsignadoLimiteProduto] t1 on t1.IdConsignadoLimiteCliente = t0.id");
        sb.appendLine("WHERE 1=1 AND t0.IdCliente = " + pCliente);
        return moverCursorDb(sb, null);
    }

    private ArrayList<ConsignadoLimiteProduto> getConsignadoLimiteProdutoCliente(int idCliente, String idProduto) {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("SELECT t0.Id, t0.IdConsignadoLimiteCliente, t0.IdProduto, t0.Quantidade");
        sb.appendLine("FROM [ConsignadoLimiteProduto] t0");
        sb.appendLine("LEFT JOIN [ConsignadoLimiteCliente] t1 on t1.Id = t0.IdConsignadoLimiteCliente");
        sb.appendLine("WHERE 1=1 and t1.idCliente = " + idCliente + " and t0.IdProduto = '" + idProduto + "'");
        return moverCursorDb(sb, null);
    }

    private ArrayList<ConsignadoLimiteProduto> moverCursorDb(Util_IO.StringBuilder sb, String[] args) {
        Cursor cursor = null;
        ArrayList<ConsignadoLimiteProduto> lista = new ArrayList<>();
        try {
            cursor = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(sb.toString(), args);
            ConsignadoLimiteProduto consignadoLimiteProduto;
            if (cursor.moveToFirst()) {
                do {
                    consignadoLimiteProduto = new ConsignadoLimiteProduto();
                    consignadoLimiteProduto.setId(cursor.getInt(0));
                    consignadoLimiteProduto.setIdConsignadoLimiteCliente(cursor.getInt(1));
                    consignadoLimiteProduto.setIdProduto(cursor.getString(2));
                    consignadoLimiteProduto.setQuantidade(cursor.getInt(3));
                    lista.add(consignadoLimiteProduto);
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
