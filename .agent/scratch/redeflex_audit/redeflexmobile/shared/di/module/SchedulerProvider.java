package com.axys.redeflexmobile.shared.di.module;

import io.reactivex.Scheduler;

public interface SchedulerProvider {
    Scheduler ui();

    Scheduler io();

    Scheduler computation();
}
