package com.axys.redeflexmobile.shared.dao;

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
public interface RegisterMigrationSubTaxDao {

    @NotNull Single<Boolean> createAll(RegisterMigrationSub register);

    @NotNull Single<RegisterMigrationSubTax> getById(int id);

    @NotNull Single<List<RegisterMigrationSubTax>> getAll();

    Single<List<TaxaMdr>> getTaxList(RegisterMigrationSub registerMigrationSub, Cliente client);

    @NotNull Single<List<RegisterMigrationSubTax>> getByMigrationId(int idCadastroMigracaoSub);

    @NotNull Single<List<RegisterMigrationSubTax>> getByClientId(int idClient);

    @NotNull Single<Boolean> update(RegisterMigrationSubTax registerMigrationSubTax);
}
