package com.axys.redeflexmobile.shared.bd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.axys.redeflexmobile.shared.models.ConsignadoLimiteCliente;
import com.axys.redeflexmobile.shared.util.Util_DB;
import com.axys.redeflexmobile.shared.util.Util_IO;
import com.axys.redeflexmobile.shared.util.Utilidades;

import java.util.ArrayList;

public class BDConsignadoLimiteCliente {
    private final Context mContext;
    private final String mTabela = "ConsignadoLimiteCliente";

    public BDConsignadoLimiteCliente(Context pContext) {
        mContext = pContext;
    }

    public void addConsignadoLimiteCliente(ConsignadoLimiteCliente pConsignadoLimiteCliente) throws Exception {
        if (pConsignadoLimiteCliente.getAtivo().equals("A")) {
            ContentValues values = new ContentValues();
            values.put("Id", pConsignadoLimiteCliente.getId());
            values.put("IdCliente", pConsignadoLimiteCliente.getIdCLiente());
            values.put("CodigoClienteSGV", pConsignadoLimiteCliente.getCodigoClienteSGV());
            values.put("Ativo", pConsignadoLimiteCliente.getAtivo());

            if (!Util_DB.isCadastrado(mContext, mTabela, "id", String.valueOf(pConsignadoLimiteCliente.getId())))
                SimpleDbHelper.INSTANCE.open(mContext).insert(mTabela, null, values);
            else
                SimpleDbHelper.INSTANCE.open(mContext).update(mTabela, values, "[id]=?", new String[]{String.valueOf(pConsignadoLimiteCliente.getId())});
        } else {
            SimpleDbHelper.INSTANCE.open(mContext).delete(mTabela, "[id]=? ", new String[]{String.valueOf(pConsignadoLimiteCliente.getId())});
            new BDConsignadoLimiteProduto(mContext).deleteByIdLimite(String.valueOf(pConsignadoLimiteCliente.getId()));
        }
    }

    public ConsignadoLimiteCliente getById(String pId) {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("AND [id] = ?");
        return Utilidades.firstOrDefault(getConsignadoLimiteCliente(sb.toString(), new String[]{pId}));
    }

    public ConsignadoLimiteCliente getByIdCLiente(String pIdCliente) {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("AND [IdCliente] = ?");
        return Utilidades.firstOrDefault(getConsignadoLimiteCliente(sb.toString(), new String[]{pIdCliente}));
    }

    public void deleteById(String pId) {
        SimpleDbHelper.INSTANCE.open(mContext).delete(mTabela, "[id]=?", new String[]{pId});
    }

    public void deleteAll() {
        SimpleDbHelper.INSTANCE.open(mContext).delete(mTabela, null, null);
    }

    public ArrayList<ConsignadoLimiteCliente> getAll() {
        return getConsignadoLimiteCliente(null, null);
    }

    private ArrayList<ConsignadoLimiteCliente> getConsignadoLimiteCliente(String pCondicao, String[] pCampos) {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("SELECT Id");
        sb.appendLine(",IdCliente");
        sb.appendLine(",CodigoClienteSGV");
        sb.appendLine(",Ativo");
        sb.appendLine("FROM [ConsignadoLimiteCliente]");
        sb.appendLine("WHERE 1=1");
        if (pCondicao != null)
            sb.append(pCondicao);

        return moverCursorDb(sb, pCampos);
    }

    private ArrayList<ConsignadoLimiteCliente> moverCursorDb(Util_IO.StringBuilder sb, String[] args) {
        Cursor cursor = null;
        ArrayList<ConsignadoLimiteCliente> lista = new ArrayList<>();
        try {
            cursor = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(sb.toString(), args);
            ConsignadoLimiteCliente consignadoLimiteCliente;
            if (cursor.moveToFirst()) {
                do {
                    consignadoLimiteCliente = new ConsignadoLimiteCliente();
                    consignadoLimiteCliente.setId(cursor.getInt(0));
                    consignadoLimiteCliente.setIdCLiente(cursor.getInt(1));
                    consignadoLimiteCliente.setCodigoClienteSGV(cursor.getString(2));
                    consignadoLimiteCliente.setAtivo(cursor.getString(3));
                    lista.add(consignadoLimiteCliente);
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
