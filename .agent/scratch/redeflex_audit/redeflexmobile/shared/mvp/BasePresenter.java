package com.axys.redeflexmobile.shared.mvp;

public interface BasePresenter<V extends BaseView> {

    void detach();

    V getView();

    void attachView(V view);

    void clearDispose();
}
