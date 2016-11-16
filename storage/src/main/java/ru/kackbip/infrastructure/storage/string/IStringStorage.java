package ru.kackbip.infrastructure.storage.string;

import rx.Observable;

/**
 * Created by ryashentsev on 02.10.2016.
 */

public interface IStringStorage {

    /**
     * @param key
     * @param storedString
     * @return cold observable which completes immediately after storing. Does not emit any elements.
     */
    Observable<Void> store(String key, String storedString);

    /**
     * @param key   the key
     * @return  hot non completable observable which emits last stored value on subscribe
     * and other values on change
     */
    Observable<String> observe(String key);

    /**
     * @param key   the key
     * @return  cold observable which emits last stored value and completes.
     * Emits NoSuchElementException if not elements exist.
     */
    Observable<String> get(String key);
}