package com.axys.redeflexmobile.ui.adquirencia.routes;

import com.axys.redeflexmobile.shared.enums.adquirencia.EnumRotasDiasDaSemana;
import com.axys.redeflexmobile.shared.models.Colaborador;
import com.axys.redeflexmobile.shared.models.adquirencia.RoutesProspect;
import com.axys.redeflexmobile.shared.mvp.BasePresenter;

import java.util.List;

/**
 * @author Rogério Massa on 22/01/19.
 */

public interface RoutesProspectPresenter extends BasePresenter<RoutesProspectView> {

    Colaborador getSalesman();

    List<RoutesProspect> getRoutesProspects(EnumRotasDiasDaSemana dayOfWeek);

    List<RoutesProspect> getRoutesProspects(String filter);

    void getListItems();
}
