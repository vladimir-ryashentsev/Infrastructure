package ru.kackbip.infrastructure.storage.pojo;

import rx.Observable;

/**
 * Created by ryashentsev on 02.10.2016.
 */

public interface IPojoStorage {
    Observable<Void> store(String key, Object object);
    <ModelType> Observable<ModelType> restore(String key, Class<ModelType> clazz);
}
