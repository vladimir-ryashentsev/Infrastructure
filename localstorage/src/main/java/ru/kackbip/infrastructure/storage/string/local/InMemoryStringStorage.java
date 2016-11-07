package ru.kackbip.infrastructure.storage.string.local;

import java.util.HashMap;
import java.util.Map;

import ru.kackbip.infrastructure.storage.NotFoundException;
import ru.kackbip.infrastructure.storage.string.IStringStorage;
import rx.AsyncEmitter;
import rx.Observable;

/**
 * Created by ryashentsev on 03.11.2016.
 */

public class InMemoryStringStorage implements IStringStorage {

    private Map<String, String> repository = new HashMap<>();

    @Override
    public Observable<Void> store(String key, String storedString) {
        return Observable.fromEmitter(
                emitter -> {
                    repository.put(key, storedString);
                    emitter.onCompleted();
                },
                AsyncEmitter.BackpressureMode.ERROR);
    }

    @Override
    public Observable<String> restore(String key) {
        return Observable.fromEmitter(
                emitter -> {
                    try {
                        String str = repository.get(key);
                        if (str == null)
                            throw new NotFoundException(String.format("String under key \"%s\" not found", key));
                        emitter.onNext(str);
                        emitter.onCompleted();
                    }catch (Exception e){
                        emitter.onError(e);
                    }
                },
                AsyncEmitter.BackpressureMode.ERROR);
    }
}
