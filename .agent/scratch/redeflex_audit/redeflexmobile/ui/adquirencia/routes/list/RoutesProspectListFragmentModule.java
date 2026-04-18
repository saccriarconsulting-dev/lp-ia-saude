package com.axys.redeflexmobile.ui.adquirencia.routes.list;

import dagger.Module;
import dagger.Provides;

/**
 * @author Rogério Massa on 02/01/19.
 */

@Module
public class RoutesProspectListFragmentModule {

    @Provides
    RoutesProspectListAdapter providerAdapter(RoutesProspectListFragment view) {
        return new RoutesProspectListAdapter(view);
    }
}
