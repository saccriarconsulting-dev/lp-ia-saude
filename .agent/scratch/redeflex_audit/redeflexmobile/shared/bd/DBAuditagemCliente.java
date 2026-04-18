package com.axys.redeflexmobile.shared.bd;

import android.content.Context;
import android.database.Cursor;

public class DBAuditagemCliente {

    public static final String TABELA = "AuditagemCliente";
    private Context context;

    public DBAuditagemCliente(Context context) {
        this.context = context;
    }

    public boolean temAuditagemCliente(String idCliente) {
        String query = "SELECT * FROM [AuditagemCliente] WHERE idCliente = ?";

        Cursor cursor = null;
        try {
            cursor = SimpleDbHelper.INSTANCE.open(context).rawQuery(query, new String[]{ idCliente });
            return (cursor != null && cursor.getCount() > 0);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }

        return false;
    }

    public void deleteAll() {
        SimpleDbHelper.INSTANCE.open(context).delete(TABELA, null, null);
    }
}
