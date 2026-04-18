package com.axys.redeflexmobile.shared.dao;

import com.axys.redeflexmobile.shared.bd.DBRegisterMigrationSubTax;
import com.axys.redeflexmobile.shared.bd.DBTaxaMdr;
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
public class RegisterMigrationSubTaxDaoImpl implements RegisterMigrationSubTaxDao {

    private final DBRegisterMigrationSubTax dbImpl;
    private final DBTaxaMdr dbTaxaMdr;

    public RegisterMigrationSubTaxDaoImpl(DBRegisterMigrationSubTax dbImpl, DBTaxaMdr dbTaxaMdr) {
        this.dbImpl = dbImpl;
        this.dbTaxaMdr = dbTaxaMdr;
    }

    @Override
    public @NotNull Single<Boolean> createAll(RegisterMigrationSub register) {
        return Single.create(emitter -> {
            try {
                if (emitter.isDisposed()) {
                    return;
                }

                for (RegisterMigrationSubTax item : register.getTaxesList()) {
                    item.setIdCadastroMigracaoSub(register.getId());
                    item.setIdCliente(register.getIdCliente());
                    item.setAtivo(true);
                    dbImpl.add(item);
                }

                emitter.onSuccess(true);
            } catch (Exception e) {
                emitter.onError(e);
            }
        });
    }

    @Override
    public @NotNull Single<RegisterMigrationSubTax> getById(int id) {
        return Single.create(emitter -> {
            try {
                if (emitter.isDisposed()) {
                    return;
                }
                emitter.onSuccess(dbImpl.get(id));
            } catch (Exception e) {
                emitter.onError(e);
            }
        });
    }

    @Override
    public @NotNull Single<List<RegisterMigrationSubTax>> getAll() {
        return Single.create(emitter -> {
            try {
                if (emitter.isDisposed()) {
                    return;
                }
                emitter.onSuccess(dbImpl.getAll());
            } catch (Exception e) {
                emitter.onError(e);
            }
        });
    }

    @Override
    public Single<List<TaxaMdr>> getTaxList(RegisterMigrationSub registerMigrationSub, Cliente client) {
        return Single.create(emitter -> {
            List<TaxaMdr> taxList = dbTaxaMdr.getAllTaxFlagTypes(
                    client.personType().getIdValue(),
                    registerMigrationSub.getFaturamentoMedioPrevisto(),
                    registerMigrationSub.getIdPrazoNegociacao(),
                    registerMigrationSub.getIdMcc()
            );

            if (taxList.isEmpty() || taxList.size() < 3) {
                emitter.onError(new IllegalArgumentException("Não foram encontradas taxas para os parâmetros informados."));
            }
            emitter.onSuccess(taxList);
        });
    }

    @Override
    public @NotNull Single<List<RegisterMigrationSubTax>> getByMigrationId(int idCadastroMigracaoSub) {
        return Single.create(emitter -> {
            try {
                if (emitter.isDisposed()) {
                    return;
                }
                emitter.onSuccess(dbImpl.getByMigrationId(idCadastroMigracaoSub));
            } catch (Exception e) {
                emitter.onError(e);
            }
        });
    }

    @Override
    public @NotNull Single<List<RegisterMigrationSubTax>> getByClientId(int idClient) {
        return Single.create(emitter -> {
            try {
                if (emitter.isDisposed()) {
                    return;
                }
                List<RegisterMigrationSubTax> taxList = dbImpl.getByClientId(idClient);
                if (taxList.isEmpty() || taxList.size() < 3) {
                    emitter.onError(new IllegalArgumentException("Não foram encontradas taxas para os parâmetros informados."));
                }
                emitter.onSuccess(dbImpl.getByClientId(idClient));
            } catch (Exception e) {
                emitter.onError(e);
            }
        });
    }

    @Override
    public @NotNull Single<Boolean> update(RegisterMigrationSubTax registerMigrationSubTax) {
        return Single.create(emitter -> {
            try {
                if (emitter.isDisposed()) {
                    return;
                }
                emitter.onSuccess(dbImpl.update(registerMigrationSubTax) > 0);
            } catch (Exception e) {
                emitter.onError(e);
            }
        });
    }
}
