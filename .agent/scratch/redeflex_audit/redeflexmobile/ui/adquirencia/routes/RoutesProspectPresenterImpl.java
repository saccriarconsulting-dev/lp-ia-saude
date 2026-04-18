package com.axys.redeflexmobile.ui.adquirencia.routes;

import com.annimon.stream.Stream;
import com.axys.redeflexmobile.shared.di.module.SchedulerProvider;
import com.axys.redeflexmobile.shared.enums.adquirencia.EnumRotasDiasDaSemana;
import com.axys.redeflexmobile.shared.manager.RoutesProspectManager;
import com.axys.redeflexmobile.shared.models.Colaborador;
import com.axys.redeflexmobile.shared.models.adquirencia.RoutesProspect;
import com.axys.redeflexmobile.shared.mvp.BasePresenterImpl;
import com.axys.redeflexmobile.shared.util.StringUtils;
import com.axys.redeflexmobile.shared.util.exception.ExceptionUtils;

import java.util.List;

/**
 * @author Rogério Massa on 22/01/19.
 */

public class RoutesProspectPresenterImpl extends BasePresenterImpl<RoutesProspectView> implements
        RoutesProspectPresenter {

    private RoutesProspectManager routesProspectManager;
    private List<RoutesProspect> routesProspects;
    private Colaborador salesman;

    public RoutesProspectPresenterImpl(RoutesProspectView view,
                                       SchedulerProvider schedulerProvider,
                                       ExceptionUtils exceptionUtils,
                                       RoutesProspectManager routesProspectManager) {
        super(view, schedulerProvider, exceptionUtils);
        this.routesProspectManager = routesProspectManager;
    }

    @Override
    public Colaborador getSalesman() {
        return salesman;
    }

    @Override
    public List<RoutesProspect> getRoutesProspects(EnumRotasDiasDaSemana dayOfWeek) {
        return Stream.ofNullable(this.routesProspects)
                .filter(ranking -> ranking.getDayOfWeek() == dayOfWeek.getValue())
                .sortBy(RoutesProspect::getOrder)
                .toList();
    }

    @Override
    public List<RoutesProspect> getRoutesProspects(String filter) {
        return Stream.ofNullable(this.routesProspects)
                .filter(ranking -> StringUtils.isNotEmpty(ranking.getCustomerName())
                        && StringUtils.isNotEmpty(filter)
                        && ranking.getCustomerName().toLowerCase().trim().contains(filter.trim().toLowerCase()))
                .sortBy(RoutesProspect::getOrder)
                .toList();
    }

    @Override
    public void getListItems() {
        compositeDisposable.add(routesProspectManager.getSalesman()
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .flatMap(salesman -> {
                    this.salesman = salesman;
                    return routesProspectManager.getListItems();
                })
                .doOnSubscribe(disposable -> getView().showLoading())
                .doAfterTerminate(() -> getView().hideLoading())
                .subscribe(routes -> {
                    this.routesProspects = routes;
                    getView().initializePager();
                }, this::showError));
    }
}
