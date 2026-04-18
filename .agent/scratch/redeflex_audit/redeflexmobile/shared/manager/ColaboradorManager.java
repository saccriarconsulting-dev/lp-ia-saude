package com.axys.redeflexmobile.shared.manager;

import com.axys.redeflexmobile.shared.models.Colaborador;

import io.reactivex.Single;

/**
 * @author lucasmarciano on 26/06/20
 */
public interface ColaboradorManager {
    Single<Colaborador> getCurrent();
}
