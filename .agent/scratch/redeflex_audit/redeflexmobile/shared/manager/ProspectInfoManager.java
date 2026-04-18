package com.axys.redeflexmobile.shared.manager;

import com.axys.redeflexmobile.shared.models.adquirencia.RouteClientProspect;

import io.reactivex.Single;

/**
 * @author Denis Gasparoto on 30/04/2019.
 */
public interface ProspectInfoManager {

    Single<RouteClientProspect> getProspectInfo(int prospectId);
}
