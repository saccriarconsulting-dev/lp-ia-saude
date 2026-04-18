package com.axys.redeflexmobile.shared.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import androidx.annotation.Nullable;

import com.axys.redeflexmobile.shared.bd.SimpleDbHelper;
import com.axys.redeflexmobile.shared.models.Cliente;
import com.axys.redeflexmobile.shared.models.Pendencia;
import com.axys.redeflexmobile.shared.models.PendenciaMotivo;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;

/**
 * @author Lucas Marciano on 28/02/2020
 */
public class PendenciaMotivoDaoImpl implements PendenciaMotivoDao {

    private final Context context;

    public PendenciaMotivoDaoImpl(final Context context) {
        this.context = context;
    }

    @Nullable
    @Override
    public PendenciaMotivo obterPorId(String id) {
        String sql = "SELECT * FROM [PendenciaMotivo] WHERE pendenciaMotivoId = ?";
        SQLiteDatabase db = SimpleDbHelper.INSTANCE.open(context);
        Cursor cursor = db.rawQuery(sql, new String[]{id});
        List<PendenciaMotivo> items = moveCursor(cursor);
        cursor.close();
        return items.size() > 0 ? items.get(0) : null;
    }

    @Override
    public Observable<List<PendenciaMotivo>> obterTodos() {
        return Observable.create(emitter -> {
            if (emitter.isDisposed()) {
                return;
            }
            String sql = "SELECT * FROM [PendenciaMotivo]";
            SQLiteDatabase db = SimpleDbHelper.INSTANCE.open(context);
            Cursor cursor = db.rawQuery(sql, null);
            List<PendenciaMotivo> items = moveCursor(cursor);
            cursor.close();
            emitter.onNext(items);
        });
    }

    @Override
    public Observable<List<Cliente>> obterTodosComCliente(List<Cliente> clientes) {
        return Observable.create(emitter -> {
            List<Cliente> resposta = new ArrayList<>();
            for (Cliente cliente : clientes) {
                List<Pendencia> pendencias = new ArrayList<>();
                for (Pendencia pendencia : cliente.getPendencias()) {
                    String sql = "SELECT * FROM [PendenciaMotivo] WHERE pendenciaId = ? ORDER BY descricao ASC";
                    SQLiteDatabase db = SimpleDbHelper.INSTANCE.open(context);
                    Cursor cursor = db.rawQuery(sql,
                            new String[]{String.valueOf(pendencia.getPendenciaId())});

                    pendencia.setMotivos(moveCursor(cursor));
                    pendencias.add(pendencia);
                    cursor.close();
                }
                cliente.setPendencias(pendencias);
                resposta.add(cliente);
            }

            emitter.onNext(resposta);
            emitter.onComplete();
        });
    }

    private List<PendenciaMotivo> moveCursor(Cursor cursor) {
        List<PendenciaMotivo> items = new ArrayList<>();
        while (cursor.moveToNext()) {
            PendenciaMotivo item = new PendenciaMotivo();
            item.setId(cursor.getString(0));
            item.setPendenciaId(cursor.getInt(1));
            item.setPendenciaMotivoId(cursor.getInt(2));
            item.setDescricao(cursor.getString(3));
            item.setAtivo(cursor.getInt(4) == 1);

            items.add(item);
        }
        return items;
    }
}
