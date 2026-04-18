package com.axys.redeflexmobile.shared.manager;

import com.axys.redeflexmobile.shared.bd.DBTaxaMdr;
import com.axys.redeflexmobile.shared.models.adquirencia.TaxaMdr;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;

/**
 * @author Lucas Marciano on 07/04/2020
 */
public class TaxaMdrManagerImpl implements TaxaMdrManager {

    @Inject DBTaxaMdr dao;

    @Override
    public Single<TaxaMdr> getById(int id) {
        return Single.create(emitter -> {
            try {
                if (emitter.isDisposed()) {
                    return;
                }
                TaxaMdr taxMdr = dao.pegarPorId(id);

                emitter.onSuccess(taxMdr);
            } catch (Exception e){
                emitter.onError(e);
            }
        });
    }

    @Override
    public Single<List<TaxaMdr>> getAll() {
        return Single.create(emitter -> {
            try {
                if (emitter.isDisposed()) {
                    return;
                }
                List<TaxaMdr> taxMdrList = dao.pegarTodas();

                emitter.onSuccess(taxMdrList);
            } catch (Exception e){
                emitter.onError(e);
            }
        });
    }
}
