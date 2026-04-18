package com.axys.redeflexmobile.shared.bd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.axys.redeflexmobile.shared.models.ConsignadoItem;
import com.axys.redeflexmobile.shared.models.ConsignadoItemCodBarra;
import com.axys.redeflexmobile.shared.util.Util_DB;
import com.axys.redeflexmobile.shared.util.Util_IO;
import com.axys.redeflexmobile.shared.util.Utilidades;

import java.util.ArrayList;

public class BDConsignadoItem {
    private final Context mContext;
    private final String mTabela = "ConsignadoItem";
    private BDConsignadoItemCodBarra bdConsignadoItemCodBarra;

    public BDConsignadoItem(Context pContext) {
        mContext = pContext;
        bdConsignadoItemCodBarra = new BDConsignadoItemCodBarra(mContext);
    }

    public long addConsignadoItem(ConsignadoItem pConsignadoItem) throws Exception {
        ContentValues values = new ContentValues();
        values.put("IdConsignado", pConsignadoItem.getIdConsignado());
        values.put("IdServer", pConsignadoItem.getIdServer());
        values.put("IdProduto", pConsignadoItem.getIdProduto());
        values.put("Qtd", pConsignadoItem.getQtd());
        values.put("ValorUnit", pConsignadoItem.getValorUnit());
        values.put("ValorTotalItem", pConsignadoItem.getValorTotalItem());
        long codigoItemConsignado = 0;
        if (!Util_DB.isCadastrado(mContext, mTabela, "id", String.valueOf(pConsignadoItem.getId())))
            codigoItemConsignado = SimpleDbHelper.INSTANCE.open(mContext).insert(mTabela, null, values);
        else
            codigoItemConsignado = SimpleDbHelper.INSTANCE.open(mContext).update(mTabela, values, "[id]=?", new String[]{String.valueOf(pConsignadoItem.getId())});
        return codigoItemConsignado;
    }

    public ConsignadoItem getById(String pId) {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("AND t0.[id] = ?");
        return Utilidades.firstOrDefault(getConsignadoItem(sb.toString(), new String[]{pId}));
    }

    public ArrayList<ConsignadoItem> getByIdCConsignado(String pIdConsignado) {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("AND t0.[IdConsignado] = ?");
        return getConsignadoItem(sb.toString(), new String[]{pIdConsignado});
    }

    public void deleteById(String pId) {
        SimpleDbHelper.INSTANCE.open(mContext).delete(mTabela, "[id]=?", new String[]{pId});
    }

    public void deleteByIdConsignado(String pIdConsignado) {
        SimpleDbHelper.INSTANCE.open(mContext).delete(mTabela, "[IdConsignado]=?", new String[]{pIdConsignado});
    }

    public void deleteAll() {
        SimpleDbHelper.INSTANCE.open(mContext).delete(mTabela, null, null);
    }

    public ArrayList<ConsignadoItem> getAll() {
        return getConsignadoItem(null, null);
    }

    private ArrayList<ConsignadoItem> getConsignadoItem(String pCondicao, String[] pCampos) {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("select t0.Id, t0.IdConsignado, t0.IdServer, t0.IdProduto");
        sb.appendLine("      ,t0.Qtd, t0.ValorUnit, t0.ValorTotalItem, t1.Nome");
        sb.appendLine("FROM [ConsignadoItem] t0");
        sb.appendLine("Left Join [Produto] t1 on t1.Id = t0.IdProduto");
        sb.appendLine("WHERE 1=1");
        if (pCondicao != null)
            sb.append(pCondicao);

        return moverCursorDb(sb, pCampos);
    }

    private ArrayList<ConsignadoItem> moverCursorDb(Util_IO.StringBuilder sb, String[] args) {
        Cursor cursor = null;
        ArrayList<ConsignadoItem> lista = new ArrayList<>();
        try {
            cursor = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(sb.toString(), args);
            ConsignadoItem consignadoItem;
            if (cursor.moveToFirst()) {
                do {
                    consignadoItem = new ConsignadoItem();
                    consignadoItem.setId(cursor.getInt(0));
                    consignadoItem.setIdConsignado(cursor.getInt(1));
                    consignadoItem.setIdServer(cursor.getInt(2));
                    consignadoItem.setIdProduto(cursor.getString(3));
                    consignadoItem.setQtd(cursor.getInt(4));
                    consignadoItem.setValorUnit(cursor.getDouble(5));
                    consignadoItem.setValorTotalItem(cursor.getDouble(6));
                    consignadoItem.setNomeProduto(cursor.getString(7));
                    consignadoItem.setListaCodigoBarra(new ArrayList<ConsignadoItemCodBarra>(bdConsignadoItemCodBarra.getByIdConsignadoItens(cursor.getString(0))));
                    lista.add(consignadoItem);
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

    public ArrayList<ConsignadoItem> getByIdCConsignadoAuditagem(String pIdConsignado, int pFinalizado) {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("Select t0.Id, t0.IdConsignado, t0.IdServer, t0.IdProduto, t0.Qtd, t1.PrecoVenda as ValorUnit, (t1.PrecoVenda * t2.qtdCombo) as ValorTotalItem, t1.Nome, ");
        sb.appendLine("t2.qtdCombo as QtdVendido, t2.Quantidade as QtdAuditado, 0 as QtdDevolvido");
        sb.appendLine("from ConsignadoItem t0");
        sb.appendLine("left join [Produto] t1 on t1.Id = t0.IdProduto");
        sb.appendLine("left join [PistolagemComboTemp] t2 on t2.IdConsignado = t0.IdConsignado and t2.IdProduto = t0.IdProduto and t2.Finalizado = " + pFinalizado);
        sb.appendLine("where t0.IdConsignado = " + pIdConsignado);
        Cursor cursor = null;
        ArrayList<ConsignadoItem> lista = new ArrayList<>();
        try {
            cursor = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(sb.toString(), null);
            ConsignadoItem consignadoItem;
            if (cursor.moveToFirst()) {
                do {
                    consignadoItem = new ConsignadoItem();
                    consignadoItem.setId(cursor.getInt(0));
                    consignadoItem.setIdConsignado(cursor.getInt(1));
                    consignadoItem.setIdServer(cursor.getInt(2));
                    consignadoItem.setIdProduto(cursor.getString(3));
                    consignadoItem.setQtd(cursor.getInt(4));
                    consignadoItem.setValorUnit(cursor.getDouble(5));
                    consignadoItem.setValorTotalItem(cursor.getDouble(6));
                    consignadoItem.setNomeProduto(cursor.getString(7));
                    consignadoItem.setQtdVendido(cursor.getInt(8));
                    consignadoItem.setQtdAuditado(cursor.getInt(9));
                    consignadoItem.setQtdDevolvido(cursor.getInt(10));
                    consignadoItem.setListaCodigoBarra(new ArrayList<ConsignadoItemCodBarra>(bdConsignadoItemCodBarra.getByIdConsignadoItens(cursor.getString(0))));
                    lista.add(consignadoItem);
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
