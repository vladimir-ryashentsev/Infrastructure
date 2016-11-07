package ru.kackbip.infrastructure.storage.string.local;

import org.junit.Test;

import ru.kackbip.infrastructure.storage.NotFoundException;
import ru.kackbip.infrastructure.storage.string.IStringStorage;
import rx.observers.TestSubscriber;

import static org.junit.Assert.assertTrue;

/**
 * Created by ryashentsev on 07.11.2016.
 */

public abstract class StringStorageTest {
    public static final String ACTUAL_KEY = "actualKey";
    public static final String EMPTY_KEY = "emptyKey";
    public static final String STORED_STRING = "StoredString";

    protected IStringStorage storage;

    @Test
    public void throwWhenTryToRestoreNullString() {
        TestSubscriber<String> subscriber = new TestSubscriber<>();
        storage.restore(EMPTY_KEY).subscribe(subscriber);

        assertTrue(subscriber.getOnNextEvents().isEmpty());
        subscriber.assertError(NotFoundException.class);
    }

    @Test
    public void storeString(){
        TestSubscriber<Void> subscriber = new TestSubscriber<>();
        storage.store(ACTUAL_KEY, STORED_STRING).subscribe(subscriber);

        assertTrue(subscriber.getOnNextEvents().isEmpty());
        subscriber.assertNoErrors();
        subscriber.assertCompleted();
    }

    @Test
    public void storeAndRestoreEqualString() {
        storeString();

        TestSubscriber<String> subscriber = new TestSubscriber<>();
        storage.restore(ACTUAL_KEY).subscribe(subscriber);

        subscriber.assertNoErrors();
        subscriber.assertValue(STORED_STRING);
        subscriber.assertCompleted();
    }
}
