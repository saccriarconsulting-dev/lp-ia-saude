package com.axys.redeflexmobile.shared.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import androidx.annotation.Nullable;

import com.axys.redeflexmobile.shared.bd.SimpleDbHelper;
import com.axys.redeflexmobile.shared.models.Cliente;
import com.axys.redeflexmobile.shared.models.Pendencia;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;

/**
 * @author Lucas Marciano on 28/02/2020
 */
public class PendenciaDaoImpl implements PendenciaDao {
    private final Context context;

    public PendenciaDaoImpl(Context context) {
        this.context = context;
    }

    @Nullable
    @Override
    public Pendencia obterPorId(String id) {
        String sql = "SELECT * " +
                "FROM [Pendencia]" +
                "WHERE pendenciaId = ?";

        SQLiteDatabase db = SimpleDbHelper.INSTANCE.open(context);
        Cursor cursor = db.rawQuery(sql, new String[]{id});
        List<Pendencia> items = moveCursor(cursor);

        return items.size() > 0 ? items.get(0) : null;
    }

    @Override
    public Observable<List<Pendencia>> obterTodos() {
        return Observable.create(emitter -> {
            if (emitter.isDisposed()) {
                return;
            }

            String sql = "SELECT * FROM [Pendencia]";

            SQLiteDatabase db = SimpleDbHelper.INSTANCE.open(context);
            Cursor cursor = db.rawQuery(sql, null);
            List<Pendencia> items = moveCursor(cursor);
            cursor.close();

            emitter.onNext(items);
        });
    }

    @Override
    public Observable<List<Cliente>> obterTodosComCliente(List<Cliente> clientes) {
        return Observable.create(emitter -> {
            List<Cliente> resposta = new ArrayList<>();
            for (Cliente cliente : clientes) {
                String sql = "SELECT P.* " +
                        "FROM PendenciaCliente AS PC " +
                        "JOIN Pendencia AS P ON (PC.pendenciaId = P.pendenciaId) " +
                        "WHERE PC.clienteId = ?";
                SQLiteDatabase db = SimpleDbHelper.INSTANCE.open(context);
                Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(cliente.getId())});
                List<Pendencia> pendencias = moveCursor(cursor);
                cliente.setPendencias(pendencias);
                resposta.add(cliente);
                cursor.close();
            }

            emitter.onNext(resposta);
        });
    }

    private List<Pendencia> moveCursor(Cursor cursor) {
        List<Pendencia> pendencias = new ArrayList<>();
        while (cursor.moveToNext()) {
            Pendencia pendencia = new Pendencia();
            pendencia.setId(cursor.getString(0));
            pendencia.setPendenciaId(cursor.getString(1));
            pendencia.setDescricao(cursor.getString(2));
            pendencia.setAtivo(cursor.getInt(3) == 1);

            pendencias.add(pendencia);
        }
        return pendencias;
    }
}
