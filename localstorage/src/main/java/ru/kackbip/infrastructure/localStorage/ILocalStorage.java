package ru.kackbip.infrastructure.localStorage;

import rx.Observable;

/**
 * Created by ryashentsev on 02.10.2016.
 */

public interface ILocalStorage {
    Observable<Void> store(String key, Object object);
    <ModelType> Observable<ModelType> restore(String key, Class<ModelType> clazz);
}
