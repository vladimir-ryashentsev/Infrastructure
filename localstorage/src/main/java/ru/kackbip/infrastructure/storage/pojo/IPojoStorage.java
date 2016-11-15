package ru.kackbip.infrastructure.storage.pojo;

import rx.Observable;

/**
 * Created by ryashentsev on 02.10.2016.
 */

public interface IPojoStorage {
    Observable<Void> store(String key, Object object);
    <ModelType> Observable<ModelType> get(String key, Class<ModelType> clazz);
    <ModelType> Observable<ModelType> observe(String key, Class<ModelType> clazz);
}
