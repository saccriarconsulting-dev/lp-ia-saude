package com.axys.redeflexmobile.shared.util;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

public final class RxBus {

    private static final PublishSubject<Object> bus = PublishSubject.create();
    private static RxBus instance;

    private RxBus() {
    }

    public static RxBus getInstance() {
        if (instance == null) {
            instance = new RxBus();
        }
        return instance;
    }

    public void send(final Object event) {
        bus.onNext(event);
    }

    @SuppressWarnings("unchecked")
    public <T> Observable<T> observer(final Class<T> eventClass) {
        return bus.filter(eventClass::isInstance).map(o -> (T) o);
    }

    public Observable<Object> toObservable() {
        return bus;
    }

    public boolean hasObservers() {
        return bus.hasObservers();
    }

}
