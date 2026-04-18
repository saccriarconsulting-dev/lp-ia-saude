package com.axys.redeflexmobile.shared.manager;

import com.axys.redeflexmobile.shared.bd.DBProspect;
import com.axys.redeflexmobile.shared.models.adquirencia.RouteClientProspect;

import io.reactivex.Single;

/**
 * @author Denis Gasparoto on 30/04/2019.
 */
public class ProspectInfoManagerImpl implements ProspectInfoManager {

    private DBProspect dbProspect;

    public ProspectInfoManagerImpl(DBProspect dbProspect) {
        this.dbProspect = dbProspect;
    }

    @Override
    public Single<RouteClientProspect> getProspectInfo(int prospectId) {
        return Single.just(dbProspect.pegarPorId(prospectId));
    }
}
