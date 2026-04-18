package com.axys.redeflexmobile.shared.dao;

import com.axys.redeflexmobile.shared.bd.DBTipoConta;
import com.axys.redeflexmobile.shared.models.TipoConta;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import io.reactivex.Single;

public class TipoContaDaoImpl implements TipoContaDao {

    private final DBTipoConta db;

    public TipoContaDaoImpl(DBTipoConta db) {
        this.db = db;
    }

    @Override
    public Single<Integer> add(@NotNull TipoConta tipoConta) {
        return Single.create(emitter -> {
            try {
                if (emitter.isDisposed()) {
                    return;
                }
                int response = db.add(tipoConta);
                emitter.onSuccess(response);
            } catch (Exception e) {
                emitter.onError(e);
            }
        });
    }

    @Override
    public Single<List<TipoConta>> getAllActive() {
        return Single.create(emitter -> {
            try {
                if (emitter.isDisposed()) {
                    return;
                }
                List<TipoConta> response = db.getAllActive();
                emitter.onSuccess(response);
            } catch (Exception e) {
                emitter.onError(e);
            }
        });
    }

    @Override
    public Single<List<TipoConta>> getAll(@Nullable String where, @Nullable String[] fields) {
        return Single.create(emitter -> {
            try {
                if (emitter.isDisposed()) {
                    return;
                }
                List<TipoConta> response = db.getAll(where, fields);
                emitter.onSuccess(response);
            } catch (Exception e) {
                emitter.onError(e);
            }
        });
    }

    @Override
    public Single<Boolean> deleteAll() {
        return null;
    }
}
