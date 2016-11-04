package ru.kackbip.infrastructure.localStorage;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import rx.AsyncEmitter;
import rx.Observable;

/**
 * Created by ryashentsev on 03.11.2016.
 */

public class InMemoryLocalStorage implements ILocalStorage {

    private Map<String, String> repository = new HashMap<>();

    private Gson gson;

    public InMemoryLocalStorage(Gson gson){
        this.gson = gson;
    }

    @Override
    public Observable<Void> store(String key, Object object) {
        return Observable.fromEmitter(
                emitter -> {
                    repository.put(key, gson.toJson(object));
                    emitter.onNext(null);
                    emitter.onCompleted();
                },
                AsyncEmitter.BackpressureMode.ERROR);
    }

    @Override
    public <T> Observable<T> restore(String key, Class<T> clazz) {
        return Observable.fromEmitter(
                emitter -> {
                    String json = repository.get(key);
                    emitter.onNext(gson.fromJson(json, clazz));
                    emitter.onCompleted();
                },
                AsyncEmitter.BackpressureMode.ERROR);
    }
}
