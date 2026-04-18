package com.axys.redeflexmobile.shared.bd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.axys.redeflexmobile.shared.models.CadastroVendedorPOS;
import com.axys.redeflexmobile.shared.models.Vendedor;
import com.axys.redeflexmobile.shared.util.Util_DB;
import com.axys.redeflexmobile.shared.util.Util_IO;

import java.sql.Date;
import java.util.ArrayList;

public class BDVendedor {
    private Context mContext;
    private String mTabela = "Vendedor";

    public BDVendedor(Context pContext) {
        mContext = pContext;
    }

    public void addVendedor(Vendedor pVendedor) throws Exception {
        ContentValues values = new ContentValues();
        values.put("IdVendedor", pVendedor.getIdVendedor());
        values.put("Vendedor", pVendedor.getVendedor());
        values.put("SemanaRota", pVendedor.getSemanaRota());
        SimpleDbHelper.INSTANCE.open(mContext).insert(mTabela, null, values);
    }

    public void deleteAll() {
        SimpleDbHelper.INSTANCE.open(mContext).delete(mTabela, null, null);
    }

    public ArrayList<Vendedor> getVendedor(String pCondicao, String[] pCampos) {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("SELECT [IdVendedor]");
        sb.appendLine(",[Vendedor]");
        sb.appendLine(",[SemanaRota]");
        sb.appendLine("FROM [Vendedor]");
        sb.appendLine("WHERE 1=1");
        if (pCondicao != null)
            sb.append(pCondicao);

        Cursor cursor = null;
        ArrayList<Vendedor> lista = new ArrayList<>();
        try {
            cursor = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(sb.toString(), pCampos);
            Vendedor vendedor = new Vendedor();
            vendedor.setIdVendedor(0);
            vendedor.setVendedor("ESCOLHER VENDEDOR");
            vendedor.setSemanaRota(0);
            lista.add(vendedor);

            if (cursor.moveToFirst()) {
                do {
                    vendedor = new Vendedor();
                    vendedor.setIdVendedor(Integer.parseInt(cursor.getString(0)));
                    vendedor.setVendedor(cursor.getString(1));
                    vendedor.setSemanaRota(Integer.parseInt(cursor.getString(2)));
                    lista.add(vendedor);
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
