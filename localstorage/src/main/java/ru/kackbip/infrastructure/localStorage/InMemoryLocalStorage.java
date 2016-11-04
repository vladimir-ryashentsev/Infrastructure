package ru.kackbip.infrastructure.localStorage;

import java.util.HashMap;
import java.util.Map;

import ru.kackbip.infrastructure.localStorage.ILocalStorage;
import rx.AsyncEmitter;
import rx.Observable;

/**
 * Created by ryashentsev on 03.11.2016.
 */

public class InMemoryLocalStorage implements ILocalStorage {

    private Map<String, Object> repository = new HashMap<>();

    @Override
    public Observable<Void> store(String key, Object object) {
        return Observable.fromEmitter(
                emitter -> {
                    repository.put(key, object);
                    emitter.onNext(null);
                    emitter.onCompleted();
                },
                AsyncEmitter.BackpressureMode.ERROR);
    }

    @Override
    public <T> Observable<T> restore(String key, Class<T> clazz) {
        return Observable.fromEmitter(
                emitter -> {
                    @SuppressWarnings("unchecked") T object = (T) repository.get(key);
                    emitter.onNext(object);
                    emitter.onCompleted();
                },
                AsyncEmitter.BackpressureMode.ERROR);
    }
}
