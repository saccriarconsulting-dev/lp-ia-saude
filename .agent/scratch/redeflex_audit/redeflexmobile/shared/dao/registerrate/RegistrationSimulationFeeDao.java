package com.axys.redeflexmobile.shared.dao.registerrate;

import com.axys.redeflexmobile.shared.models.registerrate.RegistrationSimulationFee;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import io.reactivex.Single;

/**
 * @author Lucas Marciano on 23/04/2020
 */
public interface RegistrationSimulationFeeDao {

    Single<Boolean> add(@NotNull RegistrationSimulationFee tax);

    Single<RegistrationSimulationFee> getById(int id);

    Single<List<RegistrationSimulationFee>> getAll();

    Single<Boolean> deleteById(int id);

    Single<List<RegistrationSimulationFee>> loadTaxes(
            int personType,
            double estimatedAverageBilling,
            int idTradingTerm,
            int idMcc
    );

    Single<Boolean> addAll(List<RegistrationSimulationFee> listTaxes, int prospectId);

    Single<List<RegistrationSimulationFee>> getByProspectId(int id);
}
