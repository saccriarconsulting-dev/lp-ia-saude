package com.axys.redeflexmobile.shared.bd.registerrate;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.axys.redeflexmobile.shared.bd.SimpleDbHelper;
import com.axys.redeflexmobile.shared.models.registerrate.ProspectingClientAcquisition;
import com.axys.redeflexmobile.shared.util.Util_DB;
import com.axys.redeflexmobile.shared.util.Util_IO;
import com.axys.redeflexmobile.shared.util.Utilidades;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Lucas Marciano on 23/04/2020
 */
public class DBProspectingClientAcquisition {
    private final Context context;
    private final String TABLE_NAME = "ProspeccaoClienteAdquirencia";
    private final String ID_COLUMN = "id";
    private final String WHERE_ID_PROSPECT = "[id] = ?";

    public DBProspectingClientAcquisition(Context context) {
        this.context = context;
    }

    public int add(@NotNull ProspectingClientAcquisition prospect) throws Exception {
        ContentValues values = new ContentValues();
        values.put("idVendedor", prospect.getIdSeller());
        values.put("tipoPessoa", prospect.getPersonType());
        values.put("cpfCnpj", prospect.getCpfCnpjNumber());
        values.put("mcc", prospect.getIdMcc());
        values.put("faturamentoMedioPrevisto", prospect.getEstimatedAverageBilling());
        values.put("idPrazoNegociacao", prospect.getIdTradingTerm());
        values.put("antecipacao", prospect.isAnticipation() ? 1 : 0);
        values.put("nomeCompleto", prospect.getCompleteName());
        values.put("nomeFantasia", prospect.getFantasyName());
        values.put("telefone", prospect.getPhone());
        values.put("email", prospect.getEmail());
        values.put("latitude", prospect.getLatitude());
        values.put("longitude", prospect.getLongitude());
        values.put("precisao", prospect.getPrecision());
        values.put("TaxaRav", prospect.getTaxaRav());

        if (!Util_DB.isCadastrado(context, TABLE_NAME, ID_COLUMN, String.valueOf(prospect.getId()))) {
            return (int) SimpleDbHelper.INSTANCE.open(context).insert(TABLE_NAME, null, values);
        } else {
            SimpleDbHelper.INSTANCE.open(context).update(TABLE_NAME, values, WHERE_ID_PROSPECT,
                    new String[]{String.valueOf(prospect.getId())});
            return prospect.getId();
        }
    }

    public ProspectingClientAcquisition getById(int id) {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("AND [" + ID_COLUMN + "] = ? ");
        return Utilidades.firstOrDefault(getAll(sb.toString(), new String[]{String.valueOf(id)}));
    }

    public ArrayList<ProspectingClientAcquisition> getAll(@Nullable String where, @Nullable String[] fields) {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("SELECT " + ID_COLUMN);
        sb.appendLine(",idVendedor ");
        sb.appendLine(",tipoPessoa ");
        sb.appendLine(",cpfCnpj ");
        sb.appendLine(",mcc ");
        sb.appendLine(",faturamentoMedioPrevisto ");
        sb.appendLine(",idPrazoNegociacao ");
        sb.appendLine(",antecipacao ");
        sb.appendLine(",nomeCompleto ");
        sb.appendLine(",nomeFantasia ");
        sb.appendLine(",telefone ");
        sb.appendLine(",email ");
        sb.appendLine(",latitude ");
        sb.appendLine(",longitude ");
        sb.appendLine(",precisao ");
        sb.appendLine(",dataCadastro ");
        sb.appendLine(",sync ");
        sb.appendLine(",TaxaRav ");
        sb.appendLine("FROM [" + TABLE_NAME + "]");
        sb.appendLine("WHERE 1 = 1");
        if (where != null)
            sb.append(where);
        sb.appendLine("ORDER BY nomeFantasia ASC");

        return moverCursorDb(sb, fields);
    }

    public List<ProspectingClientAcquisition> getNoSync() {
        return getAll("AND sync = 0 ", null);
    }

    public void deleteById(int id) {
        SimpleDbHelper.INSTANCE.open(context).delete(TABLE_NAME, WHERE_ID_PROSPECT, new String[]{String.valueOf(id)});
    }

    public void deleteAll() {
        SimpleDbHelper.INSTANCE.open(context).delete(TABLE_NAME, null, null);
    }

    public void updateSync(int id) {
        ContentValues values = new ContentValues();
        values.put("sync", 1);
        SimpleDbHelper.INSTANCE.open(context).update(
                TABLE_NAME,
                values,
                WHERE_ID_PROSPECT,
                new String[]{String.valueOf(id)});
    }

    public void delete60DaysOld() {
        try {
            Cursor cursor = SimpleDbHelper.INSTANCE.open(context).rawQuery("SELECT " + ID_COLUMN + " FROM " +
                    "[" + TABLE_NAME + "] WHERE [dataCadastro] < datetime('now', '-60 day')", null);
            if (cursor != null && cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    do {
                        SimpleDbHelper.INSTANCE.open(context).delete(TABLE_NAME, WHERE_ID_PROSPECT,
                                new String[]{cursor.getString(0)});
                    } while (cursor.moveToNext());
                }
            }
            if (cursor != null)
                cursor.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private ArrayList<ProspectingClientAcquisition> moverCursorDb(Util_IO.StringBuilder sb, String[] args) {
        Cursor cursor = null;
        ArrayList<ProspectingClientAcquisition> list = new ArrayList<>();
        try {
            cursor = SimpleDbHelper.INSTANCE.open(context).rawQuery(sb.toString(), args);
            if (cursor.moveToFirst()) {
                do {
                    ProspectingClientAcquisition prospect = new ProspectingClientAcquisition();
                    prospect.setId(cursor.getInt(0));
                    prospect.setIdSeller(cursor.getInt(1));
                    prospect.setPersonType(cursor.getString(2));
                    prospect.setCpfCnpjNumber(cursor.getString(3));
                    prospect.setIdMcc(cursor.getInt(4));
                    prospect.setEstimatedAverageBilling(cursor.getDouble(5));
                    prospect.setIdTradingTerm(cursor.getInt(6));
                    prospect.setAnticipation(cursor.getInt(7) == 1);
                    prospect.setCompleteName(cursor.getString(8));
                    prospect.setFantasyName(cursor.getString(9));
                    prospect.setPhone(cursor.getString(10));
                    prospect.setEmail(cursor.getString(11));
                    prospect.setLatitude(cursor.getDouble(12));
                    prospect.setLongitude(cursor.getDouble(13));
                    prospect.setPrecision(cursor.getDouble(14));
                    prospect.setRegisterDate(Util_IO.stringToDateSql(cursor.getString(15)));
                    prospect.setSync(cursor.getInt(16) == 1);
                    prospect.setTaxaRav(cursor.getDouble(17));
                    list.add(prospect);
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
