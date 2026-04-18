package com.axys.redeflexmobile.shared.manager;

import com.axys.redeflexmobile.shared.dao.MotiveMigrationSubDao;
import com.axys.redeflexmobile.shared.models.migracao.MotiveMigrationSub;
import com.axys.redeflexmobile.shared.services.migracao.MotiveMigrationSubService;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import io.reactivex.Single;

/**
 * @author Lucas Marciano on 30/03/2020
 */
public class MotiveMigrationSubManagerImpl implements MotiveMigrationSubManager {

    private MotiveMigrationSubService service;
    private MotiveMigrationSubDao dao;

    public MotiveMigrationSubManagerImpl(MotiveMigrationSubService service, MotiveMigrationSubDao dao) {
        this.service = service;
        this.dao = dao;
    }

    @Override
    public @NotNull Single<MotiveMigrationSub> getById(String id) {
        return dao.getById(id);
    }

    @Override
    public @NotNull Single<List<MotiveMigrationSub>> getAll() {
        return dao.getAll();
    }
}
