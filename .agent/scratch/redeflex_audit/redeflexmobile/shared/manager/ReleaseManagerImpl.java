package com.axys.redeflexmobile.shared.manager;

import com.annimon.stream.Stream;
import com.axys.redeflexmobile.shared.bd.DBClienteCadastro;
import com.axys.redeflexmobile.shared.bd.DBClienteCadastroPOS;
import com.axys.redeflexmobile.shared.models.adquirencia.Release;

import java.util.List;

import io.reactivex.Single;

/**
 * @author Rogério Massa on 04/12/18.
 */

public class ReleaseManagerImpl implements ReleaseManager {

    private DBClienteCadastroPOS dbClienteCadastroPOS;

    public ReleaseManagerImpl(DBClienteCadastroPOS dbClienteCadastroPOS) {
        this.dbClienteCadastroPOS = dbClienteCadastroPOS;
    }

    @Override
    public Single<List<Release>> getReleases(int clientId) {
        return Single.just(Stream.ofNullable(dbClienteCadastroPOS.obterPOS(clientId, true))
                .map(pos -> new Release(pos.getId(),
                        pos.getPosDescricao(),
                        pos.getIdTerminal(),
                        String.valueOf(clientId)))
                .toList());
    }
}
