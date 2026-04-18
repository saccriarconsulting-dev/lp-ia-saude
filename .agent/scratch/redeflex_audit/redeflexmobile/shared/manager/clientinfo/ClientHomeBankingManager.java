package com.axys.redeflexmobile.shared.manager.clientinfo;

import com.axys.redeflexmobile.shared.models.clientinfo.ClientHomeBanking;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import io.reactivex.Single;

/**
 * @author lucasmarciano on 30/06/20
 */
public interface ClientHomeBankingManager {
    Single<Boolean> insert(@NotNull ClientHomeBanking clientTaxMdr);

    Single<List<ClientHomeBanking>> getByClientId(int id);

    Single<List<ClientHomeBanking>> getAllByClientId(int clientId);

    Single<List<ClientHomeBanking>> getAll();

    Single<Boolean> deleteAll();

    Single<Boolean> deleteById(int id);
}
