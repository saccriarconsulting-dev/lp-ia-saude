package com.axys.redeflexmobile.shared.bd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.axys.redeflexmobile.shared.models.TaxasAdquirencia;

/**
 * Created by joao.viana on 23/01/2017.
 */

public class DBTaxasAdquirencia {
    private Context mContext;
    private final String TABELA = "TaxasAdquirencia";

    public DBTaxasAdquirencia(Context _context) {
        mContext = _context;
    }

    public void addTaxas(TaxasAdquirencia item) {
        ContentValues values = new ContentValues();
        values.put("situacao", item.getSituacao());
        values.put("minDebito", item.getMinDebito());
        values.put("minCreditoAVista", item.getMinCreditoAVista());
        values.put("minAte6", item.getMinAte6());
        values.put("minMaior6", item.getMaxMaior6());
        values.put("tabDebito", item.getTabDebito());
        values.put("tabCreditoAVista", item.getTabCreditoAVista());
        values.put("tabAte6", item.getTabAte6());
        values.put("tabMaior6", item.getTabMaior6());
        values.put("maxDebito", item.getMaxDebito());
        values.put("maxCreditoAVista", item.getMaxCreditoAVista());
        values.put("maxAte6", item.getMaxAte6());
        values.put("maxMaior6", item.getMaxMaior6());
        if (!isCadastrado(item.getMcc())) {
            values.put("id", item.getMcc());
            SimpleDbHelper.INSTANCE.open(mContext).insert(TABELA, null, values);
        } else
            SimpleDbHelper.INSTANCE.open(mContext).update(TABELA, values, "id=?", new String[]{String.valueOf(item.getMcc())});
    }

    private boolean isCadastrado(String codigo) {
        Cursor cursor = SimpleDbHelper.INSTANCE.open(mContext).query(TABELA, new String[]{"id"}
                , "id=?", new String[]{codigo}, null, null, null, null);
        try {
            return (cursor != null && cursor.getCount() > 0);
        } finally {
            if (cursor != null)
                cursor.close();
        }
    }

    public TaxasAdquirencia getTaxaById(String id) {
        StringBuilder sbSQL = new StringBuilder();
        sbSQL.append("SELECT id ");
        sbSQL.append(",situacao ");
        sbSQL.append(",minDebito ");
        sbSQL.append(",minCreditoAVista ");
        sbSQL.append(",minAte6 ");
        sbSQL.append(",minMaior6 ");
        sbSQL.append(",tabDebito ");
        sbSQL.append(",tabCreditoAVista ");
        sbSQL.append(",tabAte6 ");
        sbSQL.append(",tabMaior6 ");
        sbSQL.append(",maxDebito ");
        sbSQL.append(",maxCreditoAVista ");
        sbSQL.append(",maxAte6 ");
        sbSQL.append(",maxMaior6 ");
        sbSQL.append("FROM [TaxasAdquirencia] ");
        sbSQL.append("WHERE [id] = " + id);
        sbSQL.append(" AND [situacao] = 'A' ");

        Cursor cursor = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(sbSQL.toString(), null);
        try {
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                TaxasAdquirencia item = new TaxasAdquirencia();
                item.setId(cursor.getString(0));
                item.setSituacao(cursor.getString(1));
                item.setMinDebito(cursor.getDouble(2));
                item.setMinCreditoAVista(cursor.getDouble(3));
                item.setMinAte6(cursor.getDouble(4));
                item.setMinMaior6(cursor.getDouble(5));
                item.setTabDebito(cursor.getDouble(6));
                item.setTabCreditoAVista(cursor.getDouble(7));
                item.setTabAte6(cursor.getDouble(8));
                item.setTabMaior6(cursor.getDouble(9));
                item.setMaxDebito(cursor.getDouble(10));
                item.setMaxCreditoAVista(cursor.getDouble(11));
                item.setMaxAte6(cursor.getDouble(12));
                item.setMaxMaior6(cursor.getDouble(13));
                return item;
            } else
                return null;
        } finally {
            if (cursor != null)
                cursor.close();
        }
    }

    public TaxasAdquirencia getTaxaByIdType(int mcc, int tipo) {

        String sbSQL = "SELECT id" +
                ", situacao" +
                ", minDebito" +
                ", minCreditoAVista" +
                ", minAte6" +
                ", minMaior6" +
                ", tabDebito" +
                ", tabCreditoAVista" +
                ", tabAte6" +
                ", tabMaior6" +
                ", maxDebito" +
                ", maxCreditoAVista" +
                ", maxAte6" +
                ", maxMaior6" +
                ", tipo " +
                "FROM [TaxasAdquirencia]" +
                String.format(" WHERE [id] = %s", mcc) +
                String.format(" AND [tipo] = %s", tipo) +
                " AND [situacao] = 'A' ";

        Cursor cursor = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(sbSQL, null);

        try {
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                TaxasAdquirencia item = new TaxasAdquirencia();
                item.setId(cursor.getString(0));
                item.setSituacao(cursor.getString(1));
                item.setMinDebito(cursor.getDouble(2));
                item.setMinCreditoAVista(cursor.getDouble(3));
                item.setMinAte6(cursor.getDouble(4));
                item.setMinMaior6(cursor.getDouble(5));
                item.setTabDebito(cursor.getDouble(6));
                item.setTabCreditoAVista(cursor.getDouble(7));
                item.setTabAte6(cursor.getDouble(8));
                item.setTabMaior6(cursor.getDouble(9));
                item.setMaxDebito(cursor.getDouble(10));
                item.setMaxCreditoAVista(cursor.getDouble(11));
                item.setMaxAte6(cursor.getDouble(12));
                item.setMaxMaior6(cursor.getDouble(13));
                item.setTipo(cursor.getInt(14));
                return item;
            }
            return null;
        } finally {
            if (cursor != null) cursor.close();
        }
    }

    public void deleteAll() {
        SimpleDbHelper.INSTANCE.open(mContext).delete(TABELA, null, null);
    }
}