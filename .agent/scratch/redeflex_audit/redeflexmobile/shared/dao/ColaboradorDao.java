package com.axys.redeflexmobile.shared.dao;

import com.axys.redeflexmobile.shared.models.Colaborador;

import io.reactivex.Single;

public interface ColaboradorDao {

    Colaborador get();
    Single<Colaborador> getCurrent();
}
