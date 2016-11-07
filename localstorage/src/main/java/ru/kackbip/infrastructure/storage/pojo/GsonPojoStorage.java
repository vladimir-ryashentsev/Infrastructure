package ru.kackbip.infrastructure.storage.pojo;

import ru.kackbip.infrastructure.storage.string.IStringStorage;
import rx.AsyncEmitter;
import rx.Observable;

/**
 * Created by ryashentsev on 06.11.2016.
 */

public class GsonPojoStorage implements IPojoStorage {

    private IStringStorage storage;
    private IStringifier stringifier;

    public GsonPojoStorage(IStringStorage storage, IStringifier stringifier) {
        this.storage = storage;
        this.stringifier = stringifier;
    }

    @Override
    public Observable<Void> store(String key, Object object) {
        return objectToString(object)
                .flatMap(s -> storage.store(key, s));
    }

    @Override
    public <ModelType> Observable<ModelType> restore(String key, Class<ModelType> clazz) {
        return storage.restore(key)
                .flatMap(string -> stringToObject(string, clazz));
    }

    private Observable<String> objectToString(Object object) {
        return Observable.fromEmitter(
                emitter -> {
                    String str = stringifier.fromObject(object);
                    emitter.onNext(str);
                    emitter.onCompleted();
                },
                AsyncEmitter.BackpressureMode.ERROR
        );
    }

    private <T> Observable<T> stringToObject(String string, Class<T> clazz) {
        return Observable.fromEmitter(
                emitter -> {
                    emitter.onNext(stringifier.toObject(string, clazz));
                    emitter.onCompleted();
                },
                AsyncEmitter.BackpressureMode.ERROR
        );
    }

}
