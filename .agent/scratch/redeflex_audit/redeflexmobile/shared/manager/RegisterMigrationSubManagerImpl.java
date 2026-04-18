package com.axys.redeflexmobile.shared.manager;

import com.axys.redeflexmobile.shared.dao.RegisterMigrationSubDao;
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
public class RegisterMigrationSubManagerImpl implements RegisterMigrationSubManager {

    private final RegisterMigrationSubDao dao;

    public RegisterMigrationSubManagerImpl(@NotNull RegisterMigrationSubDao dao) {
        this.dao = dao;
    }

    @Override
    public @NotNull Single<RegisterMigrationSub> create(
            @NotNull RegisterMigrationSub registerMigrationSub) {
        return dao.create(registerMigrationSub);
    }

    @Override
    public @NotNull Observable<RegisterMigrationSub> obterPorId(String id) {
        return dao.obterPorId(id);
    }

    @Override
    public @NotNull Observable<List<RegisterMigrationSub>> obterTodos() {
        return dao.obterTodos();
    }

    @Override
    public @NotNull Single<RegisterMigrationSub> obterCadastroMigracaoPorClienteId(int clientId, String tipoMigracao) {
        return dao.obterCadastroMigracaoPorClienteId(clientId, tipoMigracao);
    }

    @Override
    public @NotNull Observable<List<ClientMigrationResponse>> mountListClientMigrationResponse(
            @NotNull List<Cliente> clients) {
        return dao.mountListClientMigrationResponse(clients);
    }

    @Override
    public @NotNull Observable<Boolean> validateToken(int clientId, String token) {
        return dao.validateToken(clientId, token);
    }

    @Override
    public @NotNull Single<RegisterMigrationSub> createToken(@NotNull RegisterMigrationSub registerMigrationSub) {
        return dao.createToken(registerMigrationSub);
    }

    @Override
    public Single<Boolean> updateTokenConfirmation(int clientId) {
        return dao.updateTokenConfirmation(clientId);
    }
}
