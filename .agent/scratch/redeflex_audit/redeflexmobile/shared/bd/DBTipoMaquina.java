package com.axys.redeflexmobile.shared.bd;

import android.content.Context;
import android.database.Cursor;

import com.axys.redeflexmobile.shared.models.TipoMaquina;
import com.axys.redeflexmobile.shared.util.Util_IO;
import com.axys.redeflexmobile.shared.util.Utilidades;

import java.util.ArrayList;

/**
 * Created by diego.lobo on 28/02/2018.
 */

public class DBTipoMaquina {

    private Context mContext;
    private String mTabela = "TipoMaquina";

    public DBTipoMaquina(Context pContext) {
        mContext = pContext;
    }

    public ArrayList<TipoMaquina> getAll(String pCondicao, String[] pCampos) {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("SELECT ");
        sb.appendLine("[TipoMaquinaId]");
        sb.appendLine(",[Modelo]");
        sb.appendLine(",[ValorAluguelPadrao]");
        sb.appendLine("FROM [TipoMaquina]");
        sb.appendLine("WHERE 1=1");
        if (pCondicao != null)
            sb.append(pCondicao);

        Cursor cursor = null;
        ArrayList<TipoMaquina> lista = new ArrayList<>();
        try {
            cursor = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(sb.toString(), pCampos);
            TipoMaquina tipoMaquina;
            if (cursor.moveToFirst()) {
                do {
                    tipoMaquina = new TipoMaquina();
                    tipoMaquina.setTipoMaquinaId(Integer.parseInt(cursor.getString(0)));
                    tipoMaquina.setModelo(cursor.getString(1));
                    tipoMaquina.setValorAluguelPadrao(Double.parseDouble(cursor.getString(2)));
                    lista.add(tipoMaquina);
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

    public TipoMaquina get(Integer pId) {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("AND [id] = ?");

        return Utilidades.firstOrDefault(getAll(sb.toString(), new String[]{String.valueOf(pId)}));
    }

    public void deleteAll() {
        SimpleDbHelper.INSTANCE.open(mContext).delete(mTabela, null, null);
    }
}
