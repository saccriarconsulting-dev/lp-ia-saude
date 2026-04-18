package com.axys.redeflexmobile.shared.dao.registerrate;

import com.axys.redeflexmobile.shared.bd.DBColaborador;
import com.axys.redeflexmobile.shared.bd.registerrate.DBProspectingClientAcquisition;
import com.axys.redeflexmobile.shared.models.Colaborador;
import com.axys.redeflexmobile.shared.models.registerrate.ProspectingClientAcquisition;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import io.reactivex.Single;

/**
 * @author Lucas Marciano on 23/04/2020
 */
public class ProspectingClientAcquisitionDaoImpl implements ProspectingClientAcquisitionDao {

    private final DBProspectingClientAcquisition db;
    private final DBColaborador dbColaborador;

    public ProspectingClientAcquisitionDaoImpl(DBProspectingClientAcquisition db, DBColaborador dbColaborador) {
        this.db = db;
        this.dbColaborador = dbColaborador;
    }

    @Override
    public Single<Integer> add(@NotNull ProspectingClientAcquisition prospect) {
        return Single.create(emitter -> {
            try {
                if (emitter.isDisposed()) {
                    return;
                }

                int id = db.add(prospect);

                emitter.onSuccess(id);
            } catch (Exception e) {
                emitter.onError(e);
            }
        });
    }

    @Override
    public Single<ProspectingClientAcquisition> getById(int id) {
        return Single.create(emitter -> {
            try {
                if (emitter.isDisposed()) {
                    return;
                }

                ProspectingClientAcquisition item = db.getById(id);

                emitter.onSuccess(item);
            } catch (Exception e) {
                emitter.onError(e);
            }
        });
    }

    @Override
    public Single<List<ProspectingClientAcquisition>> getAll() {
        return Single.create(emitter -> {
            try {
                if (emitter.isDisposed()) {
                    return;
                }

                List<ProspectingClientAcquisition> list = db.getAll(
                        null,
                        null);

                emitter.onSuccess(list);
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

    @Override
    public Single<Colaborador> getCurrentSeller() {
        return Single.create(emitter -> {
            try {
                if (emitter.isDisposed()) {
                    return;
                }

                Colaborador colaborador = dbColaborador.get();

                emitter.onSuccess(colaborador);
            } catch (Exception e) {
                emitter.onError(e);
            }
        });
    }

    @Override
    public Single<List<ProspectingClientAcquisition>> searchBy(String text) {
        return Single.create(emitter -> {
            try {
                if (emitter.isDisposed()) {
                    return;
                }

                List<ProspectingClientAcquisition> list = db.getAll(
                        "AND (cpfCnpj LIKE '%" + text + "%' " +
                                "OR nomeCompleto LIKE '%" + text + "%' " +
                                "OR nomeFantasia LIKE '%" + text + "%') ",
                        null);

                emitter.onSuccess(list);
            } catch (Exception e) {
                emitter.onError(e);
            }
        });
    }
}
