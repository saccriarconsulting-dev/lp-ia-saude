package com.axys.redeflexmobile.shared.dao;

import com.axys.redeflexmobile.shared.models.Cliente;
import com.axys.redeflexmobile.shared.models.migracao.ClientMigrationResponse;
import com.axys.redeflexmobile.shared.models.migracao.RegisterMigrationSub;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;

/**
 * @author Lucas Marciano on 30/03/2020
 */
public interface RegisterMigrationSubDao {
    @NotNull Single<RegisterMigrationSub> create(@NotNull RegisterMigrationSub registerMigrationSub);

    @NotNull Observable<RegisterMigrationSub> obterPorId(String id);

    @NotNull Observable<List<RegisterMigrationSub>> obterTodos();

    @NotNull Single<RegisterMigrationSub> obterCadastroMigracaoPorClienteId(int clienteId, String tipoMigracao);

    @NotNull Observable<List<ClientMigrationResponse>> mountListClientMigrationResponse(
            @NotNull List<Cliente> clients);

    @NotNull Single<RegisterMigrationSub> createToken(
            @NotNull RegisterMigrationSub registerMigrationSub);

    @NotNull Observable<Boolean> validateToken(int clientId, String token);

    @NotNull Single<Boolean> updateTokenConfirmation(int clientId);
}
