package com.axys.redeflexmobile.shared.dao.clientinfo;

import com.axys.redeflexmobile.shared.bd.clientinfo.DBFlagsBank;
import com.axys.redeflexmobile.shared.models.clientinfo.FlagsBank;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import io.reactivex.Single;

/**
 * @author lucasmarciano on 01/07/20
 */
public class FlagsBankDaoImpl implements FlagsBankDao {

    private final DBFlagsBank db;

    public FlagsBankDaoImpl(DBFlagsBank db) {
        this.db = db;
    }

    @Override
    public Single<Boolean> insert(@NotNull FlagsBank flagsBank) {
        return Single.create(emitter -> {
            try {
                if (emitter.isDisposed()) {
                    return;
                }
                db.insert(flagsBank);
                emitter.onSuccess(true);
            } catch (Exception e) {
                emitter.onError(e);
            }
        });
    }

    @Override
    public Single<FlagsBank> getById(int id) {
        return Single.create(emitter -> {
            try {
                if (emitter.isDisposed()) {
                    return;
                }
                FlagsBank response = db.getById(id);
                emitter.onSuccess(response);
            } catch (Exception e) {
                emitter.onError(e);
            }
        });
    }

    @Override
    public Single<List<FlagsBank>> getAll() {
        return Single.create(emitter -> {
            try {
                if (emitter.isDisposed()) {
                    return;
                }
                List<FlagsBank> list = db.getAll(null, null);
                emitter.onSuccess(list);
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

    @Override
    public Single<Boolean> deleteById(int id) {
        return Single.create(emitter -> {
            try {
                if (emitter.isDisposed()) {
                    return;
                }
                db.deleteById(id);
                emitter.onSuccess(true);
            } catch (Exception e) {
                emitter.onError(e);
            }
        });
    }
}
