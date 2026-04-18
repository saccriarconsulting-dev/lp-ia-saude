package com.axys.redeflexmobile.shared.dao;

import com.axys.redeflexmobile.shared.bd.DBMotiveMigrationSub;
import com.axys.redeflexmobile.shared.models.migracao.MotiveMigrationSub;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import io.reactivex.Single;

/**
 * @author Lucas Marciano on 30/03/2020
 */
public class MotiveMigrationSubDaoImpl implements MotiveMigrationSubDao {

    private DBMotiveMigrationSub dbMotiveMigrationSub;

    public MotiveMigrationSubDaoImpl(DBMotiveMigrationSub dbMotiveMigrationSub) {
        this.dbMotiveMigrationSub = dbMotiveMigrationSub;
    }

    @Override
    public @NotNull Single<MotiveMigrationSub> getById(String id) {
        return Single.create(emitter -> {
            try {
                if (emitter.isDisposed()) {
                    return;
                }

                MotiveMigrationSub motive = dbMotiveMigrationSub.getById(id);

                emitter.onSuccess(motive);
            } catch (Exception e) {
                emitter.onError(e);
            }
        });
    }

    @Override
    public @NotNull Single<List<MotiveMigrationSub>> getAll() {
        return Single.create(emitter -> {
            try {
                if (emitter.isDisposed()) {
                    return;
                }

                List<MotiveMigrationSub> motives = dbMotiveMigrationSub
                        .getAll("AND ativo = ?", new String[]{"1"});

                emitter.onSuccess(motives);
            } catch (Exception e) {
                emitter.onError(e);
            }
        });
    }
}
