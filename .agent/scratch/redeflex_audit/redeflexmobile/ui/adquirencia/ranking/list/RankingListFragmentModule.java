package com.axys.redeflexmobile.ui.adquirencia.ranking.list;

import dagger.Module;
import dagger.Provides;

/**
 * @author Rogério Massa on 02/01/19.
 */

@Module
public class RankingListFragmentModule {

    @Provides
    RankingListAdapter provideAdapter(RankingListFragment view) {
        return new RankingListAdapter(view);
    }
}
