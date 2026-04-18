package com.axys.redeflexmobile.shared.manager;

import com.axys.redeflexmobile.shared.models.customerregister.PrazoNegociacao;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import io.reactivex.Single;

/**
 * @author lucasmarciano on 06/04/20
 */
public interface PeriodNegotiationManager {

    @NotNull Single<PrazoNegociacao> getById(Integer id);

    @NotNull Single<List<PrazoNegociacao>> getAll();
}
