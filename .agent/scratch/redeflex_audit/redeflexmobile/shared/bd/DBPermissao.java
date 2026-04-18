package com.axys.redeflexmobile.shared.bd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.axys.redeflexmobile.shared.models.Permissao;
import com.axys.redeflexmobile.shared.util.Util_DB;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * Created by Desenvolvimento on 23/06/2016.
 */
public class DBPermissao {
    private Context mContext;
    private String mTabela = "Permissao";

    public DBPermissao(Context pContext) {
        this.mContext = pContext;
    }

    public void addPermissao(Permissao permissao) throws Exception {
        ContentValues values = new ContentValues();
        values.put("id", permissao.getId());
        values.put("idMenuApp", permissao.getIdApp());
        values.put("ativo", permissao.getAtivo());
        if (!Util_DB.isCadastrado(mContext, mTabela, "id", permissao.getId()))
            SimpleDbHelper.INSTANCE.open(mContext).insert(mTabela, null, values);
        else
            SimpleDbHelper.INSTANCE.open(mContext).update(mTabela, values, "[id]=?", new String[]{permissao.getId()});
    }

    public void deletePermissaoById(String pCodigo) {
        SimpleDbHelper.INSTANCE.open(mContext).delete(mTabela, "[id]=?", new String[]{pCodigo});
    }

    public boolean isPermissaoLiberada(String pCodigo) {
        String selectQuery = "SELECT id FROM [Permissao] WHERE ativo = 'S' AND idMenuApp = '" + pCodigo + "'";
        try (Cursor cursor = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(selectQuery, null)) {
            return (cursor != null && cursor.getCount() > 0);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public ArrayList<Permissao> getPermissoes() {
        String selectQuery = "SELECT id,idMenuApp,ativo FROM [Permissao] WHERE ativo = 'S'";
        ArrayList<Permissao> lista = new ArrayList<>();
        try (Cursor cursor = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(selectQuery, null)) {
            Permissao permissao;
            if (cursor.moveToFirst()) {
                do {
                    permissao = new Permissao();
                    permissao.setId(cursor.getString(0));
                    permissao.setIdApp(cursor.getString(1));
                    permissao.setAtivo(cursor.getString(2));
                    lista.add(permissao);
                } while (cursor.moveToNext());
            }
        }
        return lista;
    }

    public void deleteAll() {
        SimpleDbHelper.INSTANCE.open(mContext).delete(mTabela, null, null);
    }
}
