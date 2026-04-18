package com.axys.redeflexmobile.shared.di.module;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.schedulers.TestScheduler;

/**
 * @author Vitor Otero on 06/01/18.
 */

public class AppSchedulerProvider implements SchedulerProvider {

    private TestScheduler testScheduler;

    public AppSchedulerProvider() {
    }

    public AppSchedulerProvider(TestScheduler testScheduler) {
        this.testScheduler = testScheduler;
    }

    @Override
    public Scheduler ui() {
        if (testScheduler != null) {
            return testScheduler;
        }
        return AndroidSchedulers.mainThread();
    }

    @Override
    public Scheduler io() {
        if (testScheduler != null) {
            return testScheduler;
        }
        return Schedulers.io();
    }

    @Override
    public Scheduler computation() {
        if (testScheduler != null) {
            return testScheduler;
        }
        return Schedulers.computation();
    }

}