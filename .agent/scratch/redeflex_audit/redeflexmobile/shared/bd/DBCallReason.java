package com.axys.redeflexmobile.shared.bd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.axys.redeflexmobile.shared.models.CallReason;
import com.axys.redeflexmobile.shared.util.Util_DB;
import com.axys.redeflexmobile.shared.util.Util_IO;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lucasmarciano on 16/07/20
 */
public class DBCallReason {
    private final Context context;
    private final String TABLE_NAME = "ChamadoMotivo";
    private final String ID_COLUMN = "id";
    private final String DESCRIPTION_COLUMN = "descricao";
    private final String ACTIVE_COLUMN = "ativo";
    private final String WHERE_ID = ID_COLUMN + " = ? ";

    public DBCallReason(Context context) {
        this.context = context;
    }

    public int add(@NotNull CallReason callReason) {
        ContentValues values = new ContentValues();
        values.put(ID_COLUMN, callReason.getId());
        values.put(DESCRIPTION_COLUMN, callReason.getDescription());
        values.put(ACTIVE_COLUMN, callReason.isActive());
        try {
            if (!Util_DB.isCadastrado(context, TABLE_NAME, ID_COLUMN, String.valueOf(callReason.getId()))) {
                return (int) SimpleDbHelper.INSTANCE.open(context).insert(TABLE_NAME, null, values);
            } else {
                SimpleDbHelper.INSTANCE.open(context).update(
                        TABLE_NAME,
                        values,
                        WHERE_ID,
                        new String[]{String.valueOf(callReason.getId())});
                return callReason.getId();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public List<CallReason> getAllActive() {
        return getAll("AND " + ACTIVE_COLUMN + " = ?", new String[]{"1"});
    }

    public List<CallReason> getAll(@Nullable String where, @Nullable String[] fields) {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("SELECT " + ID_COLUMN);
        sb.appendLine(", " + DESCRIPTION_COLUMN);
        sb.appendLine(", " + ACTIVE_COLUMN);
        sb.appendLine("FROM [" + TABLE_NAME + "]");
        sb.appendLine("WHERE 1 = 1 ");
        if (where != null)
            sb.append(where);
        sb.append(" ORDER BY " + DESCRIPTION_COLUMN);

        return moverCursorDb(sb, fields);
    }

    public void deleteAll() {
        SimpleDbHelper.INSTANCE.open(context).delete(TABLE_NAME, null, null);
    }

    public void deleteById(int id) {
        SimpleDbHelper.INSTANCE.open(context).delete(TABLE_NAME, WHERE_ID, new String[]{String.valueOf(id)});
    }

    private List<CallReason> moverCursorDb(Util_IO.StringBuilder sb, String[] args) {
        Cursor cursor = null;
        List<CallReason> list = new ArrayList<>();
        try {
            cursor = SimpleDbHelper.INSTANCE.open(context).rawQuery(sb.toString(), args);
            if (cursor.moveToFirst()) {
                do {
                    CallReason callReason = new CallReason();
                    callReason.setId(cursor.getInt(0));
                    callReason.setDescription(cursor.getString(1));
                    callReason.setActive(cursor.getInt(2) == 1);
                    list.add(callReason);
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
