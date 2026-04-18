package com.axys.redeflexmobile.shared.manager;

import com.axys.redeflexmobile.shared.bd.DBPrazoNegociacao;
import com.axys.redeflexmobile.shared.models.customerregister.PrazoNegociacao;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import io.reactivex.Single;

/**
 * @author lucasmarciano on 06/04/20
 */
public class PeriodNegotiationManagerImpl implements PeriodNegotiationManager {

    private final DBPrazoNegociacao dbPrazoNegociacao;

    public PeriodNegotiationManagerImpl(DBPrazoNegociacao dbPrazoNegociacao) {
        this.dbPrazoNegociacao = dbPrazoNegociacao;
    }

    @Override
    public @NotNull Single<PrazoNegociacao> getById(Integer id) {
        return dbPrazoNegociacao.getById(id);
    }

    @Override
    public @NotNull Single<List<PrazoNegociacao>> getAll() {
        return dbPrazoNegociacao.getAll();
    }
}
