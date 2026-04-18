package com.axys.redeflexmobile.shared.manager;

import com.axys.redeflexmobile.shared.models.Colaborador;
import com.axys.redeflexmobile.shared.models.adquirencia.RoutesProspect;

import java.util.List;

import io.reactivex.Single;

/**
 * @author Rogério Massa on 26/11/18.
 */

public interface RoutesProspectManager {

    Single<List<RoutesProspect>> getListItems();

    Single<Colaborador> getSalesman();
}
