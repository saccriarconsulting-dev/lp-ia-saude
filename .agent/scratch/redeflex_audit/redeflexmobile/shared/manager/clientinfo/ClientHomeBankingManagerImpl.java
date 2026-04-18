package com.axys.redeflexmobile.shared.manager.clientinfo;

import com.axys.redeflexmobile.shared.dao.clientinfo.ClientHomeBankingDao;
import com.axys.redeflexmobile.shared.models.clientinfo.ClientHomeBanking;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import io.reactivex.Single;

/**
 * @author lucasmarciano on 30/06/20
 */
public class ClientHomeBankingManagerImpl implements ClientHomeBankingManager {

    private final ClientHomeBankingDao dao;

    public ClientHomeBankingManagerImpl(ClientHomeBankingDao dao) {
        this.dao = dao;
    }

    @Override
    public Single<Boolean> insert(@NotNull ClientHomeBanking clientTaxMdr) {
        return dao.insert(clientTaxMdr);
    }

    @Override
    public Single<List<ClientHomeBanking>> getByClientId(int id) {
        return dao.getByClientId(id);
    }

    @Override
    public Single<List<ClientHomeBanking>> getAllByClientId(int clientId) {
        return dao.getAllByClientId(clientId);
    }

    @Override
    public Single<List<ClientHomeBanking>> getAll() {
        return dao.getAll();
    }

    @Override
    public Single<Boolean> deleteAll() {
        return dao.deleteAll();
    }

    @Override
    public Single<Boolean> deleteById(int id) {
        return dao.deleteById(id);
    }
}
