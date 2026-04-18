package com.axys.redeflexmobile.shared.dao.clientinfo;

import com.axys.redeflexmobile.shared.bd.clientinfo.DBClientHomeBanking;
import com.axys.redeflexmobile.shared.models.clientinfo.ClientHomeBanking;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import io.reactivex.Single;

/**
 * @author lucasmarciano on 30/06/20
 */
public class ClientHomeBankingDaoImpl implements ClientHomeBankingDao {

    private final DBClientHomeBanking db;

    public ClientHomeBankingDaoImpl(DBClientHomeBanking db) {
        this.db = db;
    }

    @Override
    public Single<Boolean> insert(@NotNull ClientHomeBanking clientTaxMdr) {
        return Single.create(emitter -> {
            try {
                if (emitter.isDisposed()) {
                    return;
                }
                db.insert(clientTaxMdr);
                emitter.onSuccess(true);
            } catch (Exception e) {
                emitter.onError(e);
            }
        });
    }

    @Override
    public Single<List<ClientHomeBanking>> getByClientId(int id) {
        return Single.create(emitter -> {
            try {
                if (emitter.isDisposed()) {
                    return;
                }
                List<ClientHomeBanking> response = db.getAllByClientId(id);
                emitter.onSuccess(response);
            } catch (Exception e) {
                emitter.onError(e);
            }
        });
    }

    @Override
    public Single<List<ClientHomeBanking>> getAll() {
        return Single.create(emitter -> {
            try {
                if (emitter.isDisposed()) {
                    return;
                }
                List<ClientHomeBanking> list = db.getAll(null, null);
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

    @Override
    public Single<List<ClientHomeBanking>> getAllByClientId(int clientId) {
        return Single.create(emitter -> {
            try {
                if (emitter.isDisposed()) {
                    return;
                }
                List<ClientHomeBanking> response = db.getAllByClientId(clientId);
                emitter.onSuccess(response);
            } catch (Exception e) {
                emitter.onError(e);
            }
        });
    }
}
