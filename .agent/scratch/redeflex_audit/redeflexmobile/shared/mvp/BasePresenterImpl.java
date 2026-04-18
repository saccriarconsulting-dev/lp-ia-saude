package com.axys.redeflexmobile.shared.mvp;

import android.content.Context;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import android.view.View.OnClickListener;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.di.module.SchedulerProvider;
import com.axys.redeflexmobile.shared.util.ViewAction;
import com.axys.redeflexmobile.shared.util.exception.ExceptionUtils;
import com.axys.redeflexmobile.shared.util.exception.MessageException;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * @author diego.
 */

public abstract class BasePresenterImpl<T extends BaseView> implements BasePresenter<T> {

    protected final CompositeDisposable compositeDisposable;
    protected SchedulerProvider schedulerProvider;
    protected ExceptionUtils exceptionUtils;
    private T view;

    public BasePresenterImpl(T view) {
        this.view = view;
        this.compositeDisposable = new CompositeDisposable();
    }

    public BasePresenterImpl(T view,
                             SchedulerProvider schedulerProvider,
                             ExceptionUtils exceptionUtils) {
        this.view = view;
        this.schedulerProvider = schedulerProvider;
        this.exceptionUtils = exceptionUtils;
        this.compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void detach() {
        this.view = null;
        this.compositeDisposable.dispose();
    }

    @Override
    public void clearDispose() {
        this.compositeDisposable.clear();
    }

    @Override
    public T getView() {
        return this.view;
    }

    @Override
    public void attachView(T view) {
        this.view = view;
    }

    public Context getContext() {
        if (getView() instanceof Fragment) {
            return ((Fragment) getView()).getContext();
        }
        return (Context) getView();
    }

    protected void addDisposable(Disposable disposable) {
        compositeDisposable.add(disposable);
    }

    protected void showError(Throwable throwable) {
        MessageException messageException = exceptionUtils.getExceptionsMessage(throwable);
        String message = messageException.getMessage() != null
                ? messageException.getMessage()
                : this.view.getStringByResId(messageException.getMessageResId());

        showDialog(message, null);
    }

    protected void showError(Throwable throwable, OnClickListener callback) {
        MessageException messageException = exceptionUtils.getExceptionsMessage(throwable);
        String message = messageException.getMessage() != null
                ? messageException.getMessage()
                : this.view.getStringByResId(messageException.getMessageResId());

        showDialog(message, callback);
    }

    protected void showError(String message) {
        showDialog(message, null);
    }

    protected void showError(String message, OnClickListener callback) {
        showDialog(message, callback);
    }

    private void showDialog(String message, OnClickListener callback) {
        getView().showOneButtonDialog(getView().getStringByResId(R.string.app_name),
                message, callback);
    }

    protected String getString(@StringRes Integer resId) {
        return getView().getStringByResId(resId);
    }

    protected void safeExecute(final ViewAction<T> function) {
        if (view == null) {
            return;
        }

        function.execute(view);
    }
}