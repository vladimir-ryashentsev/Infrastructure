package ru.kackbip.infrastructure.storage.string;

import rx.Observable;

/**
 * Created by ryashentsev on 02.10.2016.
 */

public interface IStringStorage {
    Observable<Void> store(String key, String storedString);
    Observable<String> restore(String key);
}