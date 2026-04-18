package com.axys.redeflexmobile.shared.dao.clientinfo;

import com.axys.redeflexmobile.shared.bd.clientinfo.DBClientTaxMdr;
import com.axys.redeflexmobile.shared.models.clientinfo.ClientTaxMdr;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import io.reactivex.Single;

/**
 * @author lucasmarciano on 30/06/20
 */
public class ClientTaxMdrDaoImpl implements ClientTaxMdrDao {

    private final DBClientTaxMdr db;

    public ClientTaxMdrDaoImpl(DBClientTaxMdr db) {
        this.db = db;
    }

    @Override
    public Single<Boolean> insert(@NotNull ClientTaxMdr clientTaxMdr) {
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
    public Single<List<ClientTaxMdr>> getByClientId(int id) {
        return Single.create(emitter -> {
            try {
                if (emitter.isDisposed()) {
                    return;
                }
                List<ClientTaxMdr> response = db.getAll("AND idCliente = ? ",
                        new String[]{String.valueOf(id)});
                if (response.size() > 0)
                    emitter.onSuccess(response);
                else
                    emitter.onError(new Throwable("Lista vazia"));

            } catch (Exception e) {
                emitter.onError(e);
            }
        });
    }

    @Override
    public Single<List<ClientTaxMdr>> getAll() {
        return Single.create(emitter -> {
            try {
                if (emitter.isDisposed()) {
                    return;
                }
                List<ClientTaxMdr> list = db.getAll(null, null);
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
