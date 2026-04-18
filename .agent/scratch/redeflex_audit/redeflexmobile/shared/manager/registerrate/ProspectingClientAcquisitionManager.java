package com.axys.redeflexmobile.shared.manager.registerrate;

import com.axys.redeflexmobile.shared.models.Colaborador;
import com.axys.redeflexmobile.shared.models.registerrate.ProspectingClientAcquisition;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import io.reactivex.Single;

/**
 * @author Lucas Marciano on 23/04/2020
 */
public interface ProspectingClientAcquisitionManager {

    Single<Integer> add(@NotNull ProspectingClientAcquisition prospect);

    Single<ProspectingClientAcquisition> getById(int id);

    Single<List<ProspectingClientAcquisition>> getAll();

    Single<List<ProspectingClientAcquisition>> searchBy(String text);

    Single<Boolean> deleteById(int id);

    Single<Colaborador> getCurrentSeller();
}
