package com.axys.redeflexmobile.shared.dao.clientinfo;

import com.axys.redeflexmobile.shared.models.clientinfo.ClientTaxMdr;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import io.reactivex.Single;

/**
 * @author lucasmarciano on 30/06/20
 */
public interface ClientTaxMdrDao {

    Single<Boolean> insert(@NotNull ClientTaxMdr clientTaxMdr);

    Single<List<ClientTaxMdr>> getByClientId(int id);

    Single<List<ClientTaxMdr>> getAll();

    Single<Boolean> deleteAll();

    Single<Boolean> deleteById(int id);

}
