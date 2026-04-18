package com.axys.redeflexmobile.shared.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import androidx.annotation.Nullable;

import com.axys.redeflexmobile.shared.bd.SimpleDbHelper;
import com.axys.redeflexmobile.shared.models.PendenciaCliente;
import com.axys.redeflexmobile.shared.util.Util_IO;
import com.axys.redeflexmobile.ui.redeflex.Config;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.reactivex.Observable;

/**
 * @author Lucas Marciano on 28/02/2020
 */
public class PendenciaClienteDaoImpl implements PendenciaClienteDao {
    private final Context context;

    public PendenciaClienteDaoImpl(Context context) {
        this.context = context;
    }

    @Nullable
    @Override
    public PendenciaCliente obterPorId(String id) {
        String sql = "SELECT * FROM [PendenciaCliente] WHERE pendenciaClienteId = ?";
        SQLiteDatabase db = SimpleDbHelper.INSTANCE.open(context);
        Cursor cursor = db.rawQuery(sql, new String[]{id});
        List<PendenciaCliente> items = moveCursor(cursor);
        cursor.close();

        return items.size() > 0 ? items.get(0) : null;
    }

    @Override
    public Observable<List<PendenciaCliente>> obterTodos() {
        return Observable.create(emitter -> {
            if (emitter.isDisposed()) {
                return;
            }
            String sql = "SELECT * FROM [PendenciaCliente]";
            SQLiteDatabase db = SimpleDbHelper.INSTANCE.open(context);
            Cursor cursor = db.rawQuery(sql, null);
            List<PendenciaCliente> items = moveCursor(cursor);
            cursor.close();
            emitter.onNext(items);
            emitter.onComplete();
        });
    }

    @Override
    public Observable<Integer> updateMotivo(PendenciaCliente pendenciaCliente) {
        return Observable.create(emitter -> {
            if (emitter.isDisposed()) {
                return;
            }
            ContentValues values = prepareDataToUpdate(pendenciaCliente);

            int row = SimpleDbHelper.INSTANCE.open(context).update(
                    "PendenciaCliente",
                    values, "[pendenciaClienteId]=?",
                    new String[]{String.valueOf(pendenciaCliente.getId())}
            );
            emitter.onNext(row);
            emitter.onComplete();
        });
    }

    private List<PendenciaCliente> moveCursor(Cursor cursor) {
        PendenciaCliente item;
        List<PendenciaCliente> items = new ArrayList<>();
        while (cursor.moveToNext()) {
            item = new PendenciaCliente();
            item.setId(cursor.getString(0));
            item.setClienteId(cursor.getInt(1));
            item.setPendenciaId(cursor.getInt(2));
            item.setPendenciaMotivoId(cursor.getInt(3));
            item.setObservacao(cursor.getString(4));
            item.setLatitude(cursor.getDouble(5));
            item.setLongitude(cursor.getDouble(6));
            item.setPrecision(cursor.getDouble(7));
            item.setDataVisualizacao(cursor.getString(8));
            item.setDataResposta(cursor.getString(9));
            item.setAtivo(cursor.getInt(10) == 1);
            item.setOrdem(cursor.getInt(11));
            item.setExibeExplicacao(cursor.getInt(12) == 1);
            item.setExplicacao(cursor.getString(13));

            items.add(item);
        }
        return items;
    }

    private ContentValues prepareDataToUpdate(PendenciaCliente pendenciaCliente) {
        ContentValues values = new ContentValues();
        values.put("pendenciaClienteId", pendenciaCliente.getId());
        values.put("clienteId", pendenciaCliente.getClienteId());
        values.put("pendenciaId", pendenciaCliente.getPendenciaId());
        values.put("pendenciaMotivoId", pendenciaCliente.getPendenciaMotivoId());
        values.put("observacao", pendenciaCliente.getObservacao());
        values.put("latitude", pendenciaCliente.getLatitude());
        values.put("longitude", pendenciaCliente.getLongitude());
        values.put("precisao", pendenciaCliente.getPrecision());
        values.put("dataVisualizacao", getCurrentDateToString());
        values.put("dataResposta", getCurrentDateToString());
        values.put("ativo", pendenciaCliente.isAtivo() ? 1 : 0);
        values.put("ordem", pendenciaCliente.getOrdem());
        values.put("exibeExplicacao", pendenciaCliente.isExibeExplicacao());
        values.put("explicacao", pendenciaCliente.getExplicacao());

        return values;
    }

    private String getCurrentDateToString() {
        return Util_IO.dateTimeToString(new Date(), Config.FormatDateTimeStringBanco);
    }
}
