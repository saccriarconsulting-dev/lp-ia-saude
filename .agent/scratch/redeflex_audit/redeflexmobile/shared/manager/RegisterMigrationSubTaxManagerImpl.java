package com.axys.redeflexmobile.shared.manager;

import com.axys.redeflexmobile.shared.dao.RegisterMigrationSubTaxDao;
import com.axys.redeflexmobile.shared.models.Cliente;
import com.axys.redeflexmobile.shared.models.adquirencia.TaxaMdr;
import com.axys.redeflexmobile.shared.models.migracao.RegisterMigrationSub;
import com.axys.redeflexmobile.shared.models.migracao.RegisterMigrationSubTax;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import io.reactivex.Single;

/**
 * @author lucasmarciano on 03/04/20
 */
public class RegisterMigrationSubTaxManagerImpl implements RegisterMigrationSubTaxManager {

    private final RegisterMigrationSubTaxDao dao;

    public RegisterMigrationSubTaxManagerImpl(RegisterMigrationSubTaxDao dao) {
        this.dao = dao;
    }

    @Override
    public @NotNull Single<Boolean> createAll(RegisterMigrationSub register) {
        return dao.createAll(register);
    }

    @Override
    public @NotNull Single<RegisterMigrationSubTax> getById(int id) {
        return dao.getById(id);
    }

    @Override
    public @NotNull Single<List<RegisterMigrationSubTax>> getAll() {
        return dao.getAll();
    }

    @Override
    public @NotNull Single<List<RegisterMigrationSubTax>> getByMigrationId(int idCadastroMigracaoSub) {
        return dao.getByMigrationId(idCadastroMigracaoSub);
    }

    @Override
    public @NotNull Single<List<RegisterMigrationSubTax>> getByClientId(int idClient) {
        return dao.getByClientId(idClient);
    }

    @Override
    public @NotNull Single<Boolean> update(RegisterMigrationSubTax registerMigrationSubTax) {
        return dao.update(registerMigrationSubTax);
    }

    @Override
    public @NotNull Single<List<TaxaMdr>> getTaxList(RegisterMigrationSub registerMigrationSub, Cliente client) {
        return dao.getTaxList(registerMigrationSub, client);
    }
}
