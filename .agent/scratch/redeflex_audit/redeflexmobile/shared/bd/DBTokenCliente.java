package com.axys.redeflexmobile.shared.bd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.axys.redeflexmobile.shared.models.TokenCliente;

import java.util.ArrayList;

/**
 * Created by joao.viana on 21/09/2017.
 */

public class DBTokenCliente {
    private Context mContext;
    private String TABELA = "TokenCliente";

    public DBTokenCliente(Context _Context) {
        mContext = _Context;
    }

    public void addToken(TokenCliente pItem) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("idCliente", pItem.getIdCliente());
        contentValues.put("token", pItem.getToken());
        contentValues.put("tipoToken", pItem.getTipoToken());
        if (!isCadastrado(pItem.getId())) {
            contentValues.put("id", pItem.getId());
            SimpleDbHelper.INSTANCE.open(mContext).insert(TABELA, null, contentValues);
        } else
            SimpleDbHelper.INSTANCE.open(mContext).update(TABELA, contentValues, "[id]=?", new String[]{pItem.getId()});
    }

    private boolean isCadastrado(String pCodigo) {
        Cursor cursor = SimpleDbHelper.INSTANCE.open(mContext).query(TABELA, new String[]{"id"}
                , "id=?", new String[]{pCodigo}, null, null, null, null);
        try {
            return (cursor != null && cursor.getCount() > 0);
        } finally {
            if (cursor != null)
                cursor.close();
        }
    }

    public void deleteTokenByIdCliente(String pIdCliente) {
        SimpleDbHelper.INSTANCE.open(mContext).delete(TABELA, "[idCliente]=? ", new String[]{pIdCliente});
    }

    public void deleteAll() {
        SimpleDbHelper.INSTANCE.open(mContext).delete(TABELA, null, null);
    }

    public ArrayList<TokenCliente> getTokensByIdCliente(String pIdCliente) {
        ArrayList<TokenCliente> list = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT id ");
        sb.append(",idCliente ");
        sb.append(",token ");
        sb.append(",tipoToken ");
        sb.append("FROM [TokenCliente] ");
        sb.append("WHERE 1=1 ");
        sb.append("AND idCliente = ?");

        Cursor cursor = null;
        try {
            cursor = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(sb.toString(), new String[]{pIdCliente});
            TokenCliente tokenCliente;
            if (cursor.moveToFirst()) {
                do {
                    tokenCliente = new TokenCliente();
                    tokenCliente.setId(cursor.getString(0));
                    tokenCliente.setIdCliente(cursor.getString(1));
                    tokenCliente.setToken(cursor.getString(2));
                    tokenCliente.setTipoToken(cursor.getInt(3));
                    list.add(tokenCliente);
                } while (cursor.moveToNext());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return list;
    }
}