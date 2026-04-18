package com.axys.redeflexmobile.ui.adquirencia.map;

import com.axys.redeflexmobile.shared.di.module.SchedulerProvider;
import com.axys.redeflexmobile.shared.manager.MapItemManager;
import com.axys.redeflexmobile.shared.util.exception.ExceptionUtils;

import dagger.Module;
import dagger.Provides;

/**
 * @author Rogério Massa on 02/01/19.
 */

@Module
public class MapProspectActivityModule {

    @Provides
    MapProspectPresenter providePresenter(MapProspectActivity view,
                                          SchedulerProvider schedulerProvider,
                                          ExceptionUtils exceptionUtils,
                                          MapItemManager mapItemManager) {
        return new MapProspectPresenterImpl(view, schedulerProvider, exceptionUtils, mapItemManager);
    }
}
