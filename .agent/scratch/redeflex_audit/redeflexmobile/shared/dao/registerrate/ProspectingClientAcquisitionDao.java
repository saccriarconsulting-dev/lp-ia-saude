package com.axys.redeflexmobile.shared.dao.registerrate;

import com.axys.redeflexmobile.shared.models.Colaborador;
import com.axys.redeflexmobile.shared.models.registerrate.ProspectingClientAcquisition;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import io.reactivex.Single;

/**
 * @author Lucas Marciano on 23/04/2020
 */
public interface ProspectingClientAcquisitionDao {

    Single<Integer> add(@NotNull ProspectingClientAcquisition prospect);

    Single<ProspectingClientAcquisition> getById(int id);

    Single<List<ProspectingClientAcquisition>> getAll();

    Single<Boolean> deleteById(int id);

    Single<Colaborador> getCurrentSeller();

    Single<List<ProspectingClientAcquisition>> searchBy(String text);
}
