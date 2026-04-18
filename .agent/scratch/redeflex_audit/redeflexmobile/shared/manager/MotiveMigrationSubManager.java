package com.axys.redeflexmobile.shared.manager;

import com.axys.redeflexmobile.shared.models.migracao.MotiveMigrationSub;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import io.reactivex.Single;

/**
 * @author Lucas Marciano on 30/03/2020
 */
public interface MotiveMigrationSubManager {

    @NotNull Single<MotiveMigrationSub> getById(String id);

    @NotNull Single<List<MotiveMigrationSub>> getAll();
}
