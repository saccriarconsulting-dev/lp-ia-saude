package com.axys.redeflexmobile.shared.manager.registerrate;

import com.axys.redeflexmobile.shared.models.registerrate.RegistrationSimulationFee;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import io.reactivex.Single;

/**
 * @author Lucas Marciano on 23/04/2020
 */
public interface RegistrationSimulationFeeManager {

    Single<Boolean> add(@NotNull RegistrationSimulationFee tax);

    Single<Boolean> addAll(@NotNull List<RegistrationSimulationFee> listTaxes, int prospectId);

    Single<RegistrationSimulationFee> getById(int id);

    Single<List<RegistrationSimulationFee>> getByProspectId(int id);

    Single<List<RegistrationSimulationFee>> getAll();

    Single<Boolean> deleteById(int id);

    Single<List<RegistrationSimulationFee>> loadTaxes(
            int personType,
            double estimatedAverageBilling,
            int idTradingTerm,
            int idMcc
    );

}
