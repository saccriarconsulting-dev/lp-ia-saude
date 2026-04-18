package com.axys.redeflexmobile.shared.manager;

import com.axys.redeflexmobile.shared.models.adquirencia.TaxaMdr;

import java.util.List;

import io.reactivex.Single;

/**
 * @author Lucas Marciano on 07/04/2020
 */
public interface TaxaMdrManager {

    Single<TaxaMdr> getById(int id);

    Single<List<TaxaMdr>> getAll();
}
