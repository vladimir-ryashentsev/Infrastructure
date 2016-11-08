package ru.kackbip.infrastructure.storage.string.local;

import org.junit.Before;
import org.junit.Test;

import ru.kackbip.infrastructure.storage.NotFoundException;
import ru.kackbip.infrastructure.storage.string.IStringStorage;
import rx.observers.TestSubscriber;

import static org.junit.Assert.assertTrue;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class InMemoryStringStorageTest {
    private static final String ACTUAL_KEY = "actualKey";
    private static final String EMPTY_KEY = "emptyKey";
    private static final String STORED_STRING = "StoredString";

    private IStringStorage storage;

    @Before
    public void init() {
        storage = new InMemoryStringStorage();
    }

    @Test
    public void restoreNonExistentString() {
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
