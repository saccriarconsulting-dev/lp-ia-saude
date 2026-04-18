package com.axys.redeflexmobile.shared.bd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.axys.redeflexmobile.shared.models.SenhaMasters;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Vitor Herrmann - Capptan on 07/08/2018.
 */

public class DBSenhaMasters {
    private Context mContext;
    private String mTabela = "SenhaMasters";

    public DBSenhaMasters(Context mContext) {
        this.mContext = mContext;
    }

    public void addSenhaMasters(String senha) {
        ContentValues values = new ContentValues();
        values.put("senha", senha);
        values.put("dataAtualizacao", new SimpleDateFormat("dd/MM/yyyy").format(new Date()));

        SimpleDbHelper.INSTANCE.open(mContext).delete(mTabela, "[senha] is not null", null);
        SimpleDbHelper.INSTANCE.open(mContext).insert(mTabela, null, values);
    }

    public SenhaMasters getSenhaMasters() {
        String query = "select * from SenhaMasters";
        Cursor cursor = null;
        SenhaMasters senhaMasters = null;

        try {
            cursor = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(query, null);

            if (cursor.moveToNext()) {
                senhaMasters = new SenhaMasters();
                senhaMasters.setSenha(cursor.getString(cursor.getColumnIndex("senha")));
                senhaMasters.setDataUltimaAtualizacao(cursor.getString(cursor.getColumnIndex("dataAtualizacao")));
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return senhaMasters;
    }

    public void deleteAll() {
        SimpleDbHelper.INSTANCE
                .open(mContext)
                .delete(mTabela, null, null);
    }
}
