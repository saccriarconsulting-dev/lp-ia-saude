package com.axys.redeflexmobile.shared.manager;

import com.axys.redeflexmobile.shared.dao.ColaboradorDao;
import com.axys.redeflexmobile.shared.models.Colaborador;

import io.reactivex.Single;

/**
 * @author lucasmarciano on 26/06/20
 */
public class ColaboradorManagerImpl implements ColaboradorManager {

    private final ColaboradorDao colaboradorDao;

    public ColaboradorManagerImpl(ColaboradorDao colaboradorDao) {
        this.colaboradorDao = colaboradorDao;
    }

    @Override
    public Single<Colaborador> getCurrent() {
        return colaboradorDao.getCurrent();
    }
}
