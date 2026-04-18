package com.axys.redeflexmobile.ui.adquirencia.routes;

import com.axys.redeflexmobile.shared.enums.adquirencia.EnumRotasDiasDaSemana;
import com.axys.redeflexmobile.shared.models.Colaborador;
import com.axys.redeflexmobile.shared.models.adquirencia.RoutesProspect;
import com.axys.redeflexmobile.shared.mvp.BaseActivityView;

import java.util.List;

/**
 * @author Rogério Massa on 02/01/19.
 */

public interface RoutesProspectView extends BaseActivityView {

    void initializePager();

    void showError(String message);

    Colaborador getSalesman();

    List<RoutesProspect> getRoutesProspect(EnumRotasDiasDaSemana dayOfWeek);

    List<RoutesProspect> getRoutesProspect(String filter);
}
