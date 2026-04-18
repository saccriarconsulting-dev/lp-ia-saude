package com.axys.redeflexmobile.ui.adquirencia.visit.catalog;

import com.annimon.stream.Stream;
import com.axys.redeflexmobile.shared.di.module.SchedulerProvider;
import com.axys.redeflexmobile.shared.manager.VisitProspectManager;
import com.axys.redeflexmobile.shared.models.adquirencia.VisitProspectCatalog;
import com.axys.redeflexmobile.shared.mvp.BasePresenterImpl;
import com.axys.redeflexmobile.shared.util.exception.ExceptionUtils;

import java.util.List;

/**
 * @author Rogério Massa on 04/01/19.
 */

public class VisitProspectCatalogPresenterImpl extends BasePresenterImpl<VisitProspectCatalogView>
        implements VisitProspectCatalogPresenter {

    private VisitProspectManager visitProspectManager;

    public VisitProspectCatalogPresenterImpl(VisitProspectCatalogView view,
                                      SchedulerProvider schedulerProvider,
                                      ExceptionUtils exceptionUtils,
                                      VisitProspectManager visitProspectManager) {
        super(view, schedulerProvider, exceptionUtils);
        this.visitProspectManager = visitProspectManager;
    }

    @Override
    public void getImages() {
        compositeDisposable.add(visitProspectManager.getCatalogImages()
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .doOnSubscribe(disposable -> getView().showLoading())
                .doAfterTerminate(() -> getView().hideLoading())
                .subscribe(this::parseImages, this::showError));
    }

    private void parseImages(List<VisitProspectCatalog> items) {
        List<String> images = Stream.ofNullable(items)
                .map(VisitProspectCatalog::getImage)
                .toList();
        getView().fillImages(images);
    }
}
