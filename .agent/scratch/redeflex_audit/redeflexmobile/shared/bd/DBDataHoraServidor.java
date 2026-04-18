package com.axys.redeflexmobile.shared.bd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.axys.redeflexmobile.shared.models.DataHoraServidor;
import com.axys.redeflexmobile.shared.util.Util_IO;
import com.axys.redeflexmobile.ui.redeflex.Config;

/**
 * @author Vitor Otero on 08/02/2016.
 */
public class DBDataHoraServidor {
    private String mTabela = "DataHoraServidor";
    private Context mContext;

    public DBDataHoraServidor(Context pContext) {
        mContext = pContext;
    }

    public void add(DataHoraServidor pDataServidor) {
        delete();
        ContentValues values = alimentaDados(pDataServidor);
        SimpleDbHelper.INSTANCE.open(mContext).insert(mTabela, null, values);
    }

    private ContentValues alimentaDados(DataHoraServidor pDataServidor) {
        ContentValues values = new ContentValues();
        values.put("dataServidor", Util_IO.dateTimeToString(pDataServidor.getDate(), Config.FormatDateTimeStringBanco));
        return values;
    }

    public void atualiza(DataHoraServidor pDataServidor) {
        ContentValues values = alimentaDados(pDataServidor);
        SimpleDbHelper.INSTANCE.open(mContext).update(mTabela, values, null, null);
    }

    public DataHoraServidor get() {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("SELECT dataServidor FROM DataHoraServidor");
        DataHoraServidor dataHoraServidor = new DataHoraServidor();

        Cursor cursor = null;
        try {
            cursor = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(sb.toString(), new String[]{});
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                dataHoraServidor.setDate(Util_IO.stringToDate(cursor.getString(0), Config.FormatDateTimeStringBanco));
                return dataHoraServidor;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }

        return dataHoraServidor;
    }

    public void delete() {
        SimpleDbHelper.INSTANCE.open(mContext).delete(mTabela, "", new String[]{});
    }
}