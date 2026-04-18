package com.axys.redeflexmobile.shared.bd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.axys.redeflexmobile.shared.models.ConsignadoItemCodBarra;
import com.axys.redeflexmobile.shared.util.Util_DB;
import com.axys.redeflexmobile.shared.util.Util_IO;
import com.axys.redeflexmobile.shared.util.Utilidades;

import java.util.ArrayList;

public class BDConsignadoItemCodBarra {
    private final Context mContext;
    private final String mTabela = "ConsignadoItemCodBarra";

    public BDConsignadoItemCodBarra(Context pContext) {
        mContext = pContext;
    }

    public void addConsignadoItemCodBarra(ConsignadoItemCodBarra pConsignadoItemCodBarra) throws Exception {
        ContentValues values = new ContentValues();
        values.put("IdConsignado", pConsignadoItemCodBarra.getIdConsignado());
        values.put("IdConsignadoItem", pConsignadoItemCodBarra.getIdConsignadoItem());
        values.put("IdServer", pConsignadoItemCodBarra.getIdServer());
        values.put("CodigoBarraIni", pConsignadoItemCodBarra.getCodigoBarraIni());
        values.put("CodigoBarraFim", pConsignadoItemCodBarra.getCodigoBarraFim());
        values.put("Qtd", pConsignadoItemCodBarra.getQtd());

        if (!Util_DB.isCadastrado(mContext, mTabela, "id", String.valueOf(pConsignadoItemCodBarra.getId())))
            SimpleDbHelper.INSTANCE.open(mContext).insert(mTabela, null, values);
        else
            SimpleDbHelper.INSTANCE.open(mContext).update(mTabela, values, "[id]=?", new String[]{String.valueOf(pConsignadoItemCodBarra.getId())});
    }

    public ConsignadoItemCodBarra getById(String pId) {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("AND [id] = ?");
        return Utilidades.firstOrDefault(getConsignadoItemCodBarra(sb.toString(), new String[]{pId}));
    }

    public ConsignadoItemCodBarra getByIdConsignadoItem(String pIdConsignadoItem) {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("AND [IdConsignadoItem] = ?");
        return Utilidades.firstOrDefault(getConsignadoItemCodBarra(sb.toString(), new String[]{pIdConsignadoItem}));
    }

    public ArrayList<ConsignadoItemCodBarra> getByIdConsignadoItens(String pIdConsignadoItem) {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("AND [IdConsignadoItem] = ?");
        return getConsignadoItemCodBarra(sb.toString(), new String[]{pIdConsignadoItem});
    }

    public ConsignadoItemCodBarra getByIdConsignado(String pIdConsignado) {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("AND [IdConsignado] = ?");
        return Utilidades.firstOrDefault(getConsignadoItemCodBarra(sb.toString(), new String[]{pIdConsignado}));
    }

    public void deleteById(String pId) {
        SimpleDbHelper.INSTANCE.open(mContext).delete(mTabela, "[id]=?", new String[]{pId});
    }

    public void deleteByIdConsignado(String pIdConsignado) {
        SimpleDbHelper.INSTANCE.open(mContext).delete(mTabela, "[IdConsignado]=?", new String[]{pIdConsignado});
    }

    public void deleteByIdConsignadoItem(String pIdConsignadoItem) {
        SimpleDbHelper.INSTANCE.open(mContext).delete(mTabela, "[IdConsignadoItem]=?", new String[]{pIdConsignadoItem});
    }

    public void deleteAll() {
        SimpleDbHelper.INSTANCE.open(mContext).delete(mTabela, null, null);
    }

    public ArrayList<ConsignadoItemCodBarra> getAll() {
        return getConsignadoItemCodBarra(null, null);
    }

    private ArrayList<ConsignadoItemCodBarra> getConsignadoItemCodBarra(String pCondicao, String[] pCampos) {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("select Id, IdConsignado, IdConsignadoItem, IdServer");
        sb.appendLine(",CodigoBarraIni, CodigoBarraFim, Qtd");
        sb.appendLine("FROM [ConsignadoItemCodBarra]");
        sb.appendLine("WHERE 1=1");
        if (pCondicao != null)
            sb.append(pCondicao);

        return moverCursorDb(sb, pCampos);
    }

    public ArrayList<ConsignadoItemCodBarra> getByIdConsignadoProduto(String pIdConsignado, String pIdProduto) {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("select T0.Id, T0.IdConsignado, T0.IdConsignadoItem, T0.IdServer");
        sb.appendLine(",T0.CodigoBarraIni, T0.CodigoBarraFim, T0.Qtd");
        sb.appendLine("FROM [ConsignadoItemCodBarra] T0");
        sb.appendLine("Left join [ConsignadoItem] t1 on t1.idConsignado = " + pIdConsignado + " and t1.IdProduto = '"+ pIdProduto +"'");
        sb.appendLine("WHERE 1=1 AND T0.IdConsignadoItem = t1.Id");
        return moverCursorDb(sb, null);
    }

    private ArrayList<ConsignadoItemCodBarra> moverCursorDb(Util_IO.StringBuilder sb, String[] args) {
        Cursor cursor = null;
        ArrayList<ConsignadoItemCodBarra> lista = new ArrayList<>();
        try {
            cursor = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(sb.toString(), args);
            ConsignadoItemCodBarra consignadoItemCodBarra;
            if (cursor.moveToFirst()) {
                do {
                    consignadoItemCodBarra = new ConsignadoItemCodBarra();
                    consignadoItemCodBarra.setId(cursor.getInt(0));
                    consignadoItemCodBarra.setIdConsignado(cursor.getInt(1));
                    consignadoItemCodBarra.setIdConsignadoItem(cursor.getInt(2));
                    consignadoItemCodBarra.setIdServer(cursor.getInt(3));
                    consignadoItemCodBarra.setCodigoBarraIni(cursor.getString(4));
                    consignadoItemCodBarra.setCodigoBarraFim(cursor.getString(5));
                    consignadoItemCodBarra.setQtd(cursor.getInt(6));
                    lista.add(consignadoItemCodBarra);
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
