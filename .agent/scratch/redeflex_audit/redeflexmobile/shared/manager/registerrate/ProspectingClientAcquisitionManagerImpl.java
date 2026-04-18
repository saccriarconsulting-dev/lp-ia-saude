package com.axys.redeflexmobile.shared.manager.registerrate;

import com.axys.redeflexmobile.shared.dao.registerrate.ProspectingClientAcquisitionDao;
import com.axys.redeflexmobile.shared.models.Colaborador;
import com.axys.redeflexmobile.shared.models.registerrate.ProspectingClientAcquisition;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import io.reactivex.Single;

/**
 * @author Lucas Marciano on 23/04/2020
 */
public class ProspectingClientAcquisitionManagerImpl implements ProspectingClientAcquisitionManager {

    private final ProspectingClientAcquisitionDao dao;

    public ProspectingClientAcquisitionManagerImpl(ProspectingClientAcquisitionDao dao) {
        this.dao = dao;
    }

    @Override
    public Single<Integer> add(@NotNull ProspectingClientAcquisition prospect) {
        return dao.add(prospect);
    }

    @Override
    public Single<ProspectingClientAcquisition> getById(int id) {
        return dao.getById(id);
    }

    @Override
    public Single<List<ProspectingClientAcquisition>> getAll() {
        return dao.getAll();
    }

    @Override
    public Single<List<ProspectingClientAcquisition>> searchBy(String text) {
        return dao.searchBy(text);
    }

    @Override
    public Single<Boolean> deleteById(int id) {
        return dao.deleteById(id);
    }

    @Override
    public Single<Colaborador> getCurrentSeller() {
        return dao.getCurrentSeller();
    }
}
