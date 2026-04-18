package com.axys.redeflexmobile.ui.adquirencia.release;

import com.axys.redeflexmobile.shared.models.adquirencia.Release;
import com.axys.redeflexmobile.shared.mvp.BaseActivityView;

import java.util.List;

/**
 * @author Rogério Massa on 04/12/18.
 */

public interface ReleaseView extends BaseActivityView {

    void showSwipeLoading();

    void hideSwipeLoading();

    void fillReleases(List<Release> list);
}
