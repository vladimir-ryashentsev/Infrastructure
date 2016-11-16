package ru.kackbip.infrastructure.storage.string.local;

import org.junit.Before;
import org.junit.Test;

import java.util.NoSuchElementException;

import rx.observers.TestSubscriber;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class InMemoryStringStorageTest {
    private static final String ACTUAL_KEY = "actualKey";
    private static final String EMPTY_KEY = "emptyKey";
    private static final String STORED_STRING = "StoredString";
    private static final String STORED_STRING2 = "StoredString2";

    private InMemoryStringStorage storage;

    @Before
    public void init() {
        storage = new InMemoryStringStorage();
    }

    @Test
    public void doesNotEmitAfterValueChanged_WhenGettingString(){
        store(ACTUAL_KEY, STORED_STRING);
        TestSubscriber<String> observeSubscriber = new TestSubscriber<>();
        storage.get(ACTUAL_KEY).subscribe(observeSubscriber);
        observeSubscriber.assertValue(STORED_STRING);
        observeSubscriber.assertNoErrors();

        store(ACTUAL_KEY, STORED_STRING2);
        observeSubscriber.assertValues(STORED_STRING);
        observeSubscriber.assertNoErrors();
    }

    @Test
    public void throw_WhenStoreNullString(){
        TestSubscriber<Void> storeSubscriber = new TestSubscriber<>();
        storage.store(ACTUAL_KEY, null).subscribe(storeSubscriber);
        storeSubscriber.assertNoValues();
        storeSubscriber.assertError(NullPointerException.class);
    }

    @Test
    public void throw_WhenGetNonExistentString() {
        TestSubscriber<String> subscriber = new TestSubscriber<>();
        storage.get(EMPTY_KEY).subscribe(subscriber);

        subscriber.assertNoValues();
        subscriber.assertError(NoSuchElementException.class);
    }

    @Test
    public void completes_AfterGettingString(){
        store(ACTUAL_KEY, STORED_STRING);

        TestSubscriber<String> subscriber = new TestSubscriber<>();
        storage.get(ACTUAL_KEY).subscribe(subscriber);

        subscriber.assertCompleted();
    }

    @Test
    public void getCorrectString() {
        store(ACTUAL_KEY, STORED_STRING);

        TestSubscriber<String> subscriber = new TestSubscriber<>();
        storage.get(ACTUAL_KEY).subscribe(subscriber);

        subscriber.assertValue(STORED_STRING);
    }

    @Test
    public void doesNotComplete_WhenObserve(){
        TestSubscriber<String> subscriber = new TestSubscriber<>();
        storage.observe(EMPTY_KEY).subscribe(subscriber);
        subscriber.assertNotCompleted();
    }

    @Test
    public void emitNoElements_WhenObserveNonExistentKey() {
        TestSubscriber<String> subscriber = new TestSubscriber<>();
        storage.observe(EMPTY_KEY).subscribe(subscriber);

        subscriber.assertNoValues();
        subscriber.assertNoErrors();
    }

    @Test
    public void emitOnlyLastElementOnSubscribe_WhenObserve(){
        store(ACTUAL_KEY, STORED_STRING);
        store(ACTUAL_KEY, STORED_STRING2);

        TestSubscriber<String> observeSubscriber = new TestSubscriber<>();
        storage.observe(ACTUAL_KEY).subscribe(observeSubscriber);
        observeSubscriber.assertValue(STORED_STRING2);
        observeSubscriber.assertNoErrors();
    }

    @Test
    public void emitOnValueChanged_WhenObserve(){
        TestSubscriber<String> observeSubscriber = new TestSubscriber<>();
        storage.observe(ACTUAL_KEY).subscribe(observeSubscriber);
        observeSubscriber.assertNoValues();
        observeSubscriber.assertNoErrors();

        store(ACTUAL_KEY, STORED_STRING);
        observeSubscriber.assertValue(STORED_STRING);
        observeSubscriber.assertNoErrors();

        store(ACTUAL_KEY, STORED_STRING2);
        observeSubscriber.assertValues(STORED_STRING, STORED_STRING2);
        observeSubscriber.assertNoErrors();
    }

    @Test
    public void emitNullAndCompletes_WhenStore(){
        TestSubscriber<Void> subscriber = new TestSubscriber<>();
        storage.store(ACTUAL_KEY, STORED_STRING).subscribe(subscriber);
        subscriber.assertValue(null);
        subscriber.assertCompleted();
        subscriber.assertNoErrors();
    }

    private void store(String key, String string){
        storage.store(key, string).subscribe();
    }
}
