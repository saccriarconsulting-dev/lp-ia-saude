package com.axys.redeflexmobile.ui.adquirencia.visit;

import com.axys.redeflexmobile.BuildConfig;
import com.axys.redeflexmobile.shared.di.module.SchedulerProvider;
import com.axys.redeflexmobile.shared.manager.VisitProspectManager;
import com.axys.redeflexmobile.shared.models.Colaborador;
import com.axys.redeflexmobile.shared.models.adquirencia.RouteClientProspect;
import com.axys.redeflexmobile.shared.models.adquirencia.VisitProspect;
import com.axys.redeflexmobile.shared.models.adquirencia.VisitProspectAttachment;
import com.axys.redeflexmobile.shared.models.adquirencia.VisitProspectCancelReason;
import com.axys.redeflexmobile.shared.models.adquirencia.VisitProspectQualityQuestion;
import com.axys.redeflexmobile.shared.mvp.BasePresenterImpl;
import com.axys.redeflexmobile.shared.util.IGPSTracker;
import com.axys.redeflexmobile.shared.util.exception.ExceptionUtils;

import java.util.Date;
import java.util.List;

import static com.axys.redeflexmobile.shared.models.adquirencia.RouteClientProspect.TYPE_PROSPECT;
import static com.axys.redeflexmobile.ui.adquirencia.visit.VisitProspectActivity.FLAG_VISIT_CANCEL;
import static com.axys.redeflexmobile.ui.adquirencia.visit.VisitProspectActivity.FLAG_VISIT_QUALITY;

/**
 * @author Rogério Massa on 29/11/18.
 */

public class VisitProspectPresenterImpl extends BasePresenterImpl<VisitProspectView> implements
        VisitProspectPresenter {

    private VisitProspectManager visitProspectManager;

    private int routeId;
    private Colaborador salesman;
    private VisitProspect visitProspect;
    private RouteClientProspect visitProspectCustomer;
    private VisitProspectAttachment visitProspectAttachment;

    public VisitProspectPresenterImpl(VisitProspectView view,
                                      SchedulerProvider schedulerProvider,
                                      ExceptionUtils exceptionUtils,
                                      VisitProspectManager visitProspectManager) {
        super(view, schedulerProvider, exceptionUtils);
        this.visitProspectManager = visitProspectManager;
    }

    @Override
    public Colaborador getSalesman() {
        return salesman;
    }

    @Override
    public VisitProspect getVisitProspect() {
        return visitProspect;
    }

    @Override
    public RouteClientProspect getVisitProspectCustomer() {
        return visitProspectCustomer;
    }

    @Override
    public VisitProspectAttachment getVisitProspectAttachment() {
        return visitProspectAttachment;
    }

    @Override
    public String getObservation() {
        return visitProspect.getObservation();
    }

    @Override
    public void setObservation(String observation) {
        visitProspect.setObservation(observation);
    }

    @Override
    public void setRouteId(int routeId) {
        this.routeId = routeId;
    }

    @Override
    public void getDataByCustomer(int clientId) {
        compositeDisposable.add(visitProspectManager.getClient(clientId)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .doOnSubscribe(disposable -> getView().showLoading())
                .doAfterTerminate(() -> getView().hideLoading())
                .subscribe(this::getSalesmanData, this::showError));
    }

    @Override
    public void getDataByProspect(int clientId) {
        compositeDisposable.add(visitProspectManager.getClientProspect(clientId)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .doOnSubscribe(disposable -> getView().showLoading())
                .doAfterTerminate(() -> getView().hideLoading())
                .subscribe(this::getSalesmanData, this::showError));
    }

    private void getSalesmanData(RouteClientProspect customer) {
        this.visitProspectCustomer = customer;
        compositeDisposable.add(visitProspectManager.getSalesman()
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .doOnSubscribe(disposable -> getView().showLoading())
                .doAfterTerminate(() -> getView().hideLoading())
                .subscribe(salesman -> {
                    this.salesman = salesman;
                    getView().initializeFlow();
                }, this::showError));
    }

    @Override
    public void initializeVisit(VisitProspectAttachment visitProspectAttachment) {
        this.visitProspect = new VisitProspect();
        boolean isProspect = visitProspectCustomer.getType() == TYPE_PROSPECT;
        this.visitProspect.setIdCustomer(!isProspect ? visitProspectCustomer.getId() : null);
        this.visitProspect.setIdProspect(isProspect ? visitProspectCustomer.getId() : null);
        this.visitProspect.setIdSalesman(salesman.getId());
        this.visitProspect.setIdCustomerSgv(visitProspectCustomer.getSgvCode());
        this.visitProspect.setDateStart(new Date());
        this.visitProspect.setVersionApp(BuildConfig.VERSION_NAME);
        this.visitProspect.setIdRoute(routeId);

        IGPSTracker gpsTracker = getView().getGpsTracker();
        this.visitProspect.setLatitude(gpsTracker.getLatitude());
        this.visitProspect.setLongitude(gpsTracker.getLongitude());
        this.visitProspect.setPrecision(gpsTracker.getPrecisao());

        visitProspectAttachment.setIdSalesman(salesman.getId());
        visitProspectAttachment.setIdCustomer(visitProspect.getIdCustomer());
        visitProspectAttachment.setIdProspect(visitProspect.getIdProspect());
        visitProspectAttachment.setLatitude(gpsTracker.getLatitude());
        visitProspectAttachment.setLongitude(gpsTracker.getLongitude());
        visitProspectAttachment.setPrecision(gpsTracker.getPrecisao());
        visitProspectAttachment.setDate(gpsTracker.getDataGps() != null
                ? gpsTracker.getDataGps()
                : new Date());
        this.visitProspectAttachment = visitProspectAttachment;
    }

    @Override
    public void cancelVisit(VisitProspectCancelReason reason, String observation) {
        this.visitProspect.setCancelIdReason(reason.getId());
        this.visitProspect.setCancelObservation(observation);
        this.visitProspect.setDateFinish(new Date());

        compositeDisposable.add(visitProspectManager.cancelVisit(visitProspect, visitProspectAttachment, reason)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .doOnSubscribe(disposable -> getView().showLoading())
                .doAfterTerminate(() -> getView().hideLoading())
                .subscribe(() -> getView().finishVisit(FLAG_VISIT_CANCEL), this::showError));
    }

    @Override
    public void saveQualityVisit(List<VisitProspectQualityQuestion> answers,
                                 List<VisitProspectQualityQuestion> questions) {
        this.visitProspect.setDateFinish(new Date());

        compositeDisposable.add(visitProspectManager.saveVisitQuality(visitProspect, answers,
                questions, visitProspectAttachment)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .doOnSubscribe(disposable -> getView().showLoading())
                .doAfterTerminate(() -> getView().hideLoading())
                .subscribe(() -> getView().finishVisit(FLAG_VISIT_QUALITY), this::showError));
    }

    @Override
    public void saveVisit() {
        this.visitProspect.setDateFinish(new Date());

        compositeDisposable.add(visitProspectManager.saveVisit(visitProspect, visitProspectAttachment)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .doOnSubscribe(disposable -> getView().showLoading())
                .doAfterTerminate(() -> getView().hideLoading())
                .subscribe(() -> getView().finishVisit(null), this::showError));
    }
}
