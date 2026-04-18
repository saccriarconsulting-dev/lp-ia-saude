package com.axys.redeflexmobile.shared.dao;

import com.axys.redeflexmobile.shared.bd.DBCallReason;
import com.axys.redeflexmobile.shared.models.CallReason;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import io.reactivex.Single;

/**
 * @author lucasmarciano on 16/07/20
 */
public class CallReasonDaoImpl implements CallReasonDao {

    private final DBCallReason db;

    public CallReasonDaoImpl(DBCallReason db) {
        this.db = db;
    }

    @Override
    public Single<Integer> add(@NotNull CallReason callReason) {
        return Single.create(emitter -> {
            try {
                if (emitter.isDisposed()) {
                    return;
                }
                int response = db.add(callReason);
                emitter.onSuccess(response);
            } catch (Exception e) {
                emitter.onError(e);
            }
        });
    }

    @Override
    public Single<List<CallReason>> getAllActive() {
        return Single.create(emitter -> {
            try {
                if (emitter.isDisposed()) {
                    return;
                }
                List<CallReason> response = db.getAllActive();
                emitter.onSuccess(response);
            } catch (Exception e) {
                emitter.onError(e);
            }
        });
    }

    @Override
    public Single<List<CallReason>> getAll(@Nullable String where, @Nullable String[] fields) {
        return Single.create(emitter -> {
            try {
                if (emitter.isDisposed()) {
                    return;
                }
                List<CallReason> response = db.getAll(where, fields);
                emitter.onSuccess(response);
            } catch (Exception e) {
                emitter.onError(e);
            }
        });
    }

    @Override
    public Single<Boolean> deleteAll() {
        return Single.create(emitter -> {
            try {
                if (emitter.isDisposed()) {
                    return;
                }
                db.deleteAll();
                emitter.onSuccess(true);
            } catch (Exception e) {
                emitter.onError(e);
            }
        });
    }
}
