package com.axys.redeflexmobile.shared.manager.registerrate;

import com.axys.redeflexmobile.shared.dao.registerrate.RegistrationSimulationFeeDao;
import com.axys.redeflexmobile.shared.models.registerrate.RegistrationSimulationFee;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import io.reactivex.Single;

/**
 * @author Lucas Marciano on 23/04/2020
 */
public class RegistrationSimulationFeeManagerImpl implements RegistrationSimulationFeeManager {

    private RegistrationSimulationFeeDao dao;

    public RegistrationSimulationFeeManagerImpl(RegistrationSimulationFeeDao dao) {
        this.dao = dao;
    }

    @Override
    public Single<Boolean> add(@NotNull RegistrationSimulationFee tax) {
        return dao.add(tax);
    }

    @Override
    public Single<Boolean> addAll(@NotNull List<RegistrationSimulationFee> listTaxes, int prospectId) {
        return dao.addAll(listTaxes, prospectId);
    }

    @Override
    public Single<RegistrationSimulationFee> getById(int id) {
        return dao.getById(id);
    }

    @Override
    public Single<List<RegistrationSimulationFee>> getByProspectId(int id) {
        return dao.getByProspectId(id);
    }

    @Override
    public Single<List<RegistrationSimulationFee>> getAll() {
        return dao.getAll();
    }

    @Override
    public Single<Boolean> deleteById(int id) {
        return dao.deleteById(id);
    }

    @Override
    public Single<List<RegistrationSimulationFee>> loadTaxes(
            int personType,
            double estimatedAverageBilling,
            int idTradingTerm,
            int idMcc
    ) {
        return dao.loadTaxes(personType, estimatedAverageBilling, idTradingTerm, idMcc);
    }
}
