package ru.kackbip.infrastructure.storage.string.local;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import ru.kackbip.infrastructure.storage.string.IStringStorage;
import rx.AsyncEmitter;
import rx.Observable;
import rx.subjects.PublishSubject;

/**
 * Created by ryashentsev on 03.11.2016.
 */

public class InMemoryStringStorage implements IStringStorage {

    private Map<String, String> repository = new HashMap<>();
    private Map<String, PublishSubject<String>> subjects = new HashMap<>();

    @Override
    public synchronized Observable<Void> store(String key, String storedString) {
        if(storedString==null) return Observable.error(new NullPointerException());
        return Observable.fromEmitter(
                emitter -> {
                    repository.put(key, storedString);
                    if (subjects.containsKey(key)) {
                        subjects.get(key).onNext(storedString);
                    }
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
        if(repository.containsKey(key)) return Observable.merge(
                subjects.get(key),
                Observable.just(repository.get(key))
        );
        return subjects.get(key);
    }

    @Override
    public synchronized Observable<String> get(String key) throws NoSuchElementException {
        if(repository.containsKey(key)){
            return Observable.just(repository.get(key));
        }
        return Observable.error(new NoSuchElementException());
    }
}
