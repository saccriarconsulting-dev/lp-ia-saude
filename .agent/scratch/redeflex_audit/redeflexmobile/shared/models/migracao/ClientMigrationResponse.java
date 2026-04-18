package com.axys.redeflexmobile.shared.models.migracao;

import com.axys.redeflexmobile.shared.models.Cliente;

/**
 * @author Lucas Marciano on 30/03/2020
 */
public class ClientMigrationResponse {
    private Cliente client;
    private RegisterMigrationSub registerMigrationSub;

    public Cliente getClient() {
        return client;
    }

    public void setClient(Cliente client) {
        this.client = client;
    }

    public RegisterMigrationSub getRegisterMigrationSub() {
        return registerMigrationSub;
    }

    public void setRegisterMigrationSub(RegisterMigrationSub registerMigrationSub) {
        this.registerMigrationSub = registerMigrationSub;
    }
}
