package com.axys.redeflexmobile.shared.dao;

import android.content.Context;
import android.database.Cursor;

import com.axys.redeflexmobile.shared.bd.SimpleDbHelper;
import com.axys.redeflexmobile.shared.models.Iccid;
import com.axys.redeflexmobile.shared.util.Util_IO;

public class IccidDaoImpl implements IccidDao {

    private Context context;

    public IccidDaoImpl(Context context) {
        this.context = context;
    }

    public Iccid getByCodigo(String pCodigo) {
        if(!Util_IO.isNullOrEmpty(pCodigo)) {
            Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
            sb.appendLine("SELECT idServer");
            sb.appendLine(",codigoBarra");
            sb.appendLine(",codigoSemVerificador");
            sb.appendLine(",itemCode");
            sb.appendLine("FROM [Iccid]");
            sb.appendLine("WHERE codigoBarra = '" + pCodigo +"' ");
            Cursor cursor = null;
            try {
                cursor = SimpleDbHelper.INSTANCE.open(context).rawQuery(sb.toString(), null);
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    Iccid iccid = new Iccid();
                    iccid.setId(String.valueOf(cursor.getInt(0)));
                    iccid.setCodigo(cursor.getString(1));
                    iccid.setCodigoSemVerificador(cursor.getString(2));
                    iccid.setItemCode(cursor.getString(3));
                    iccid.setAtivo(true);
                    return iccid;
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                if (cursor != null)
                    cursor.close();
            }
        }
        return null;
    }
}
