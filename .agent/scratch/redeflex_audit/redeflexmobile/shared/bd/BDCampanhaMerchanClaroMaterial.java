package com.axys.redeflexmobile.shared.bd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.axys.redeflexmobile.shared.models.CampanhaMerchanClaroMaterial;
import com.axys.redeflexmobile.shared.util.Util_DB;
import com.axys.redeflexmobile.shared.util.Util_IO;
import com.axys.redeflexmobile.shared.util.Utilidades;

import java.util.ArrayList;

public class BDCampanhaMerchanClaroMaterial {
    private Context mContext;

    public BDCampanhaMerchanClaroMaterial(Context mContext) {
        this.mContext = mContext;
    }

    public void addCampanhaMerchanClaroMaterial(CampanhaMerchanClaroMaterial pMaterial) throws Exception {
        ContentValues values = new ContentValues();
        values.put("descricao", pMaterial.getDescricao());
        values.put("idCampanha", pMaterial.getIdCampanha());
        values.put("ativo", pMaterial.getAtivo());

        if (!Util_DB.isCadastrado(mContext, "CampanhaMerchanClaroMaterial", "idMaterial", pMaterial.getIdMaterial())) {
            values.put("idMaterial", pMaterial.getIdMaterial());
            SimpleDbHelper.INSTANCE.open(mContext).insert("CampanhaMerchanClaroMaterial", null, values);
        } else
            SimpleDbHelper.INSTANCE.open(mContext).update("CampanhaMerchanClaroMaterial", values, "[idMaterial]=?", new String[]{pMaterial.getIdMaterial()});
    }

    private ArrayList<CampanhaMerchanClaroMaterial> getMateriais(String pCondicao, String[] pCampos) {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("SELECT [idMaterial]");
        sb.appendLine(",[descricao]");
        sb.appendLine(",[idCampanha]");
        sb.appendLine(",[ativo]");
        sb.appendLine("FROM [CampanhaMerchanClaroMaterial]");
        sb.appendLine("WHERE 1=1");

        if (pCondicao != null)
            sb.append(pCondicao);

        Cursor cursor = null;
        ArrayList<CampanhaMerchanClaroMaterial> lista = new ArrayList<>();
        try {
            cursor = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(sb.toString(), pCampos);
            CampanhaMerchanClaroMaterial material;
            if (cursor.moveToFirst()) {
                do {
                    material = new CampanhaMerchanClaroMaterial();
                    material.setIdMaterial(cursor.getString(0));
                    material.setDescricao(cursor.getString(1));
                    material.setIdCampanha(cursor.getInt(2));
                    material.setAtivo(cursor.getInt(3));
                    lista.add(material);
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

    public ArrayList<CampanhaMerchanClaroMaterial> getMaterialAtivo() {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("AND [Ativo] = 1");
        return getMateriais(sb.toString(), null);
    }

    public ArrayList<CampanhaMerchanClaroMaterial> getMaterialTodos() {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        return getMateriais(sb.toString(), null);
    }

    public CampanhaMerchanClaroMaterial getById(int pId) {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("AND [idMaterial] = ?");
        return Utilidades.firstOrDefault(getMateriais(sb.toString(), new String[]{String.valueOf(pId)}));
    }

    public void deleteById(int pId) {
        SimpleDbHelper.INSTANCE.open(mContext).delete("CampanhaMerchanClaroMaterial", "[idMaterial]=?", new String[]{String.valueOf(pId)});
    }

}
