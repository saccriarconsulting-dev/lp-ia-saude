package com.axys.redeflexmobile.shared.bd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import androidx.annotation.NonNull;

import com.axys.redeflexmobile.shared.models.AudOpe;
import com.axys.redeflexmobile.shared.util.Util_DB;
import com.axys.redeflexmobile.shared.util.Util_IO;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by joao.viana on 10/01/2018.
 */
public class DBAudOpe {

    @NonNull
    private final Context mContext;

    @NonNull
    private final String mTabela = "AuditoriaOperadora";

    public DBAudOpe(@NonNull Context pContext) {
        mContext = pContext.getApplicationContext();
    }

    public void addAudOpe(@NonNull AudOpe pAudOpe) throws Exception {
        if (pAudOpe.isAtivo()) {
            if (!Util_DB.isCadastrado(
                    mContext,
                    mTabela,
                    new String[]{"idCliente", "operadora"},
                    new String[]{pAudOpe.getCliente(), pAudOpe.getOperadora()}
            )) {
                ContentValues values = new ContentValues();
                values.put("idCliente", pAudOpe.getCliente());
                values.put("operadora", pAudOpe.getOperadora());
                SimpleDbHelper.INSTANCE.open(mContext).insert(mTabela, null, values);
            }
        } else {
            SimpleDbHelper.INSTANCE.open(mContext).delete(
                    mTabela,
                    "[idCliente]=? AND [operadora]=?",
                    new String[]{pAudOpe.getCliente(), pAudOpe.getOperadora()}
            );
        }
    }

    public void deleteByIdCliente(@NonNull String pIdCliente) {
        SimpleDbHelper.INSTANCE.open(mContext).delete(mTabela, "[idCliente]=?", new String[]{pIdCliente});
    }

    /**
     * Verifica se existe parâmetro de auditagem por (cliente, operadora).
     * Usado como sinalizador para obrigar bipagem/pistolagem no fluxo de auditagem.
     */
    public boolean obrigarPistolagemAuditoria(@NonNull String pIdCliente, int pOperadora) {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("SELECT 1");
        sb.appendLine("FROM [AuditoriaOperadora]");
        sb.appendLine("WHERE idCliente = ?");
        sb.appendLine("AND operadora = ?");
        sb.appendLine("LIMIT 1");

        Cursor cursor = null;
        try {
            cursor = SimpleDbHelper.INSTANCE.open(mContext)
                    .rawQuery(sb.toString(), new String[]{pIdCliente, String.valueOf(pOperadora)});
            return cursor != null && cursor.moveToFirst();
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        } finally {
            if (cursor != null) cursor.close();
        }
    }

    /**
     * Retorna o conjunto de operadoras obrigatórias para auditagem de um cliente.
     */
    @NonNull
    public Set<Integer> getOperadorasObrigatorias(@NonNull String pIdCliente) {
        Set<Integer> out = new HashSet<>();
        Cursor cursor = null;

        try {
            cursor = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(
                    "SELECT operadora FROM " + mTabela + " WHERE idCliente = ?",
                    new String[]{pIdCliente}
            );
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    out.add(cursor.getInt(0));
                } while (cursor.moveToNext());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (cursor != null) cursor.close();
        }

        return out;
    }

    public void deleteAll() {
        SimpleDbHelper.INSTANCE.open(mContext).delete(mTabela, null, null);
    }
}