package com.axys.redeflexmobile.shared.manager;

import com.axys.redeflexmobile.shared.bd.DBColaborador;
import com.axys.redeflexmobile.shared.bd.DBRotaAdquirencia;
import com.axys.redeflexmobile.shared.models.Colaborador;
import com.axys.redeflexmobile.shared.models.adquirencia.RoutesProspect;

import java.util.List;

import io.reactivex.Single;

/**
 * @author Rogério Massa on 26/11/18.
 */

public class RoutesProspectManagerImpl implements RoutesProspectManager {

    private DBRotaAdquirencia dbRotaAdquirencia;
    private DBColaborador dbColaborador;

    public RoutesProspectManagerImpl(DBRotaAdquirencia dbRotaAdquirencia,
                                     DBColaborador dbColaborador) {
        this.dbRotaAdquirencia = dbRotaAdquirencia;
        this.dbColaborador = dbColaborador;
    }

    @Override
    public Single<List<RoutesProspect>> getListItems() {
        return Single.just(dbRotaAdquirencia.pegarTodas());
    }

    @Override
    public Single<Colaborador> getSalesman() {
        return Single.just(dbColaborador.get());
    }
}
