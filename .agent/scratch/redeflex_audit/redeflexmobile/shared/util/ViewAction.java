package com.axys.redeflexmobile.shared.util;

import com.axys.redeflexmobile.shared.mvp.BaseView;

@FunctionalInterface
public interface ViewAction<V extends BaseView> {

    void execute(V view);
}
