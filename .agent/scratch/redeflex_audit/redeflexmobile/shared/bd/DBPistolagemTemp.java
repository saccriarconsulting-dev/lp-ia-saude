package com.axys.redeflexmobile.shared.bd;

import android.content.Context;
import android.database.Cursor;

import com.axys.redeflexmobile.shared.models.CodBarra;
import com.axys.redeflexmobile.shared.util.Util_IO;

import java.util.ArrayList;

/**
 * Created by diego.lobo on 12/02/2018.
 */

public class DBPistolagemTemp {

    public static final String TABELA = "PistolagemTemp";
    private Context mContext;

    public DBPistolagemTemp(Context _context) {
        this.mContext = _context;
    }

    public ArrayList<CodBarra> GetPistolagemTemp() {
        ArrayList<CodBarra> lista = new ArrayList<CodBarra>();

        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("SELECT p.codigoBarra, p.codigoBarraFinal");
        sb.appendLine("FROM [PistolagemTemp] p");
        sb.appendLine("WHERE 1 = 1 ");
        sb.appendLine("AND p.finalizado = 0");
        sb.appendLine("UNION");
        sb.appendLine("SELECT ac.codigoBarra, ac.codigoBarraFinal FROM AuditagemEstoque a");
        sb.appendLine("INNER JOIN AuditagemEstoqueCodigoBarra ac ON a.id = ac.idAuditagem AND DATE(a.data) = DATE('now', 'localtime')");

        Cursor cursor = null;
        try {
            cursor = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(sb.toString(), null);
            CodBarra codBarra;
            if (cursor.moveToFirst()) {
                do {
                    codBarra = new CodBarra();
                    codBarra.setCodBarraInicial(cursor.getString(0));
                    codBarra.setCodBarraFinal(cursor.getString(1));
                    codBarra.setIndividual(Util_IO.isNullOrEmpty(codBarra.getCodBarraFinal()));
                    lista.add(codBarra);
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

    public void deleteAll() {
        SimpleDbHelper.INSTANCE.open(mContext).delete(TABELA, null, null);
    }
}
