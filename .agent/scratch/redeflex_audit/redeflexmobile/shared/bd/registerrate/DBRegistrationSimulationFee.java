package com.axys.redeflexmobile.shared.bd.registerrate;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import androidx.annotation.Nullable;

import com.axys.redeflexmobile.shared.bd.SimpleDbHelper;
import com.axys.redeflexmobile.shared.models.registerrate.RegistrationSimulationFee;
import com.axys.redeflexmobile.shared.util.Util_DB;
import com.axys.redeflexmobile.shared.util.Util_IO;
import com.axys.redeflexmobile.shared.util.Utilidades;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import timber.log.Timber;

/**
 * @author Lucas Marciano on 23/04/2020
 */
public class DBRegistrationSimulationFee {

    private final Context context;
    private final String TABLE_NAME = "CadastroSimulacaoTaxa";
    private final String ID_COLUMN = "id";
    private final String WHERE_ID_PROSPECT = "[id] = ?";

    public DBRegistrationSimulationFee(Context context) {
        this.context = context;
    }

    public void add(@NotNull RegistrationSimulationFee fee) throws Exception {
        ContentValues values = new ContentValues();
        values.put("idSimulacaoTaxa", fee.getIdProspecting());
        values.put("bandeiraTipoId", fee.getIdFlag());
        values.put("debito", fee.getDebitValue());
        values.put("creditoAVista", fee.getCreditValue());
        values.put("creditoAte6", fee.getCreditSixValue());
        values.put("creditoMaior6", fee.getCreditMoreThanSixValue());
        values.put("antecipacaoAutomatica", fee.getAutomaticAnticipation());

        if (!Util_DB.isCadastrado(context, TABLE_NAME, new String[]{
                "idSimulacaoTaxa",
                "bandeiraTipoId"
        }, new String[]{
                String.valueOf(fee.getIdProspecting()),
                String.valueOf(fee.getIdFlag())
        })) {
          int id = (int) SimpleDbHelper.INSTANCE.open(context).insert(TABLE_NAME, null, values);
            Timber.d("Novo id CadastroSimulacaoTaxa: %s", id);
        } else {
            SimpleDbHelper.INSTANCE.open(context).update(TABLE_NAME, values, WHERE_ID_PROSPECT,
                    new String[]{String.valueOf(fee.getId())});
            Timber.d("Id editado CadastroSimulacaoTaxa: %s", fee.getId());
        }

    }

    public RegistrationSimulationFee getById(int id) {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("AND [" + ID_COLUMN + "] = ? ");
        return Utilidades.firstOrDefault(getAll(sb.toString(), new String[]{String.valueOf(id)}));
    }

    public ArrayList<RegistrationSimulationFee> getAll(@Nullable String where, @Nullable String[] fields) {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("SELECT " + ID_COLUMN);
        sb.appendLine(",idSimulacaoTaxa ");
        sb.appendLine(",bandeiraTipoId ");
        sb.appendLine(",debito ");
        sb.appendLine(",creditoAVista ");
        sb.appendLine(",creditoAte6 ");
        sb.appendLine(",creditoMaior6 ");
        sb.appendLine(",antecipacaoAutomatica ");
        sb.appendLine("FROM [" + TABLE_NAME + "] ");
        sb.appendLine("WHERE 1 = 1 ");
        if (where != null)
            sb.append(where);

        return moverCursorDb(sb, fields);
    }

    public ArrayList<RegistrationSimulationFee> getAllTaxesByProspectId(int idProspect) {
        return getAll("AND idSimulacaoTaxa = ? ", new String[]{String.valueOf(idProspect)});
    }

    public void deleteById(int id) {
        SimpleDbHelper.INSTANCE.open(context).delete(TABLE_NAME, WHERE_ID_PROSPECT, new String[]{String.valueOf(id)});
    }

    public void deleteAll() {
        SimpleDbHelper.INSTANCE.open(context).delete(TABLE_NAME, null, null);
    }

    private ArrayList<RegistrationSimulationFee> moverCursorDb(Util_IO.StringBuilder sb, String[] args) {
        Cursor cursor = null;
        ArrayList<RegistrationSimulationFee> list = new ArrayList<>();
        try {
            cursor = SimpleDbHelper.INSTANCE.open(context).rawQuery(sb.toString(), args);
            if (cursor.moveToFirst()) {
                do {
                    RegistrationSimulationFee fee = new RegistrationSimulationFee();
                    fee.setId(cursor.getInt(0));
                    fee.setIdProspecting(cursor.getInt(1));
                    fee.setIdFlag(cursor.getInt(2));
                    fee.setDebitValue(cursor.getDouble(3));
                    fee.setCreditValue(cursor.getDouble(4));
                    fee.setCreditSixValue(cursor.getDouble(5));
                    fee.setCreditMoreThanSixValue(cursor.getDouble(6));
                    fee.setAutomaticAnticipation(cursor.getDouble(7));
                    list.add(fee);
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
