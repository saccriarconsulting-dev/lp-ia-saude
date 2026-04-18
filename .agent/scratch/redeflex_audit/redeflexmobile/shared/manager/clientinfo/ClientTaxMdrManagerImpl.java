package com.axys.redeflexmobile.shared.manager.clientinfo;

import com.axys.redeflexmobile.shared.dao.clientinfo.ClientTaxMdrDao;
import com.axys.redeflexmobile.shared.models.clientinfo.ClientTaxMdr;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import io.reactivex.Single;

/**
 * @author lucasmarciano on 30/06/20
 */
public class ClientTaxMdrManagerImpl implements ClientTaxMdrManager {

    private final ClientTaxMdrDao dao;

    public ClientTaxMdrManagerImpl(ClientTaxMdrDao dao) {
        this.dao = dao;
    }

    @Override
    public Single<Boolean> insert(@NotNull ClientTaxMdr clientTaxMdr) {
        return dao.insert(clientTaxMdr);
    }

    @Override
    public Single<List<ClientTaxMdr>> getByClientId(int id) {
        return dao.getByClientId(id);
    }

    @Override
    public Single<List<ClientTaxMdr>> getAll() {
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
