package ru.kackbip.infrastructure.storage.string.local;

import android.content.SharedPreferences;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import ru.kackbip.infrastructure.storage.string.IStringStorage;
import rx.AsyncEmitter;
import rx.Observable;
import rx.subjects.PublishSubject;

/**
 * Created by ryashentsev on 09.10.2016.
 */

public class SharedPreferencesStringStorage implements IStringStorage {

    private Map<String, PublishSubject<String>> subjects = new HashMap<>();
    private SharedPreferences repository;

    public SharedPreferencesStringStorage(SharedPreferences repository) {
        this.repository = repository;
    }

    @Override
    public synchronized Observable<Void> store(String key, String storedString) {
        return Observable.fromEmitter(
                emitter -> {
                    SharedPreferences.Editor editor = repository.edit();
                    editor.putString(key, storedString);
                    editor.apply();
                    if (subjects.containsKey(key)) {
                        subjects.get(key).onNext(storedString);
                    }
                    emitter.onNext(null);
                    emitter.onCompleted();
                },
                AsyncEmitter.BackpressureMode.ERROR);
    }

    @Override
    public synchronized Observable<String> observe(String key) {
        if (!subjects.containsKey(key)) {
            PublishSubject<String> subject = PublishSubject.create();
            subjects.put(key, subject);
        }
        if(repository.contains(key)) return Observable.merge(
                subjects.get(key),
                Observable.just(repository.getString(key, null))
        );
        return subjects.get(key);
    }

    @Override
    public Observable<String> get(String key) throws NoSuchElementException {
        if(repository.contains(key)){
            return Observable.just(repository.getString(key, null));
        }
        return Observable.error(new NoSuchElementException());
    }
}
