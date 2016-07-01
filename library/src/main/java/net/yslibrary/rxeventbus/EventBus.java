package net.yslibrary.rxeventbus;

import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;

public final class EventBus {
    private final SerializedSubject<Event, Event> mBus = new SerializedSubject<>(PublishSubject.create());

    public void emit(Event event) {
        mBus.onNext(event);
    }

    public Observable<Event> asObservable() {
        return mBus.asObservable();
    }

    public <R extends Event> Observable<R> on(Class<R> eventType) {
        return mBus.ofType(eventType);
    }

    public <R extends Event> Observable<R> on(Class<R> eventType, R initialValue) {
        return mBus.ofType(eventType).startWith(initialValue);
    }

    public boolean hasObservers() {
        return mBus.hasObservers();
    }

    /**
     * empty interface to restrict EventBus' value
     */
    public interface Event {

    }
}
