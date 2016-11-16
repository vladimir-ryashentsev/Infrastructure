package ru.kackbip.infrastructure.storage.string.local;

import android.content.SharedPreferences;

import org.junit.Before;
import org.junit.Test;

import java.util.NoSuchElementException;

import rx.observers.TestSubscriber;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class SharedPreferencesStringStorageTest{
    private static final String ACTUAL_KEY = "actualKey";
    private static final String EMPTY_KEY = "emptyKey";
    private static final String STORED_STRING = "storedString";
    private static final String STORED_STRING2 = "storedString2";

    private SharedPreferencesStringStorage storage;
    private SharedPreferences.Editor editor;
    private SharedPreferences sharedPreferences;

    @Before
    public void init(){
        sharedPreferences = mock(SharedPreferences.class);
        when(sharedPreferences.getString(ACTUAL_KEY, null)).thenReturn(STORED_STRING);
        when(sharedPreferences.contains(ACTUAL_KEY)).thenReturn(true);
        editor = mock(SharedPreferences.Editor.class);
        when(sharedPreferences.edit()).thenReturn(editor);
        storage = new SharedPreferencesStringStorage(sharedPreferences);
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
    public void throw_WhenGetNonExistentString() {
        TestSubscriber<String> subscriber = new TestSubscriber<>();
        storage.get(EMPTY_KEY).subscribe(subscriber);

        subscriber.assertNoValues();
        subscriber.assertError(NoSuchElementException.class);
    }

    @Test
    public void completes_AfterGettingString(){
        TestSubscriber<String> subscriber = new TestSubscriber<>();
        storage.get(ACTUAL_KEY).subscribe(subscriber);

        subscriber.assertCompleted();
    }

    @Test
    public void getCorrectString() {
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
    public void emitOnlyElementOnSubscribe_WhenObserve(){
        store(ACTUAL_KEY, STORED_STRING);
        store(ACTUAL_KEY, STORED_STRING2);

        TestSubscriber<String> observeSubscriber = new TestSubscriber<>();
        storage.observe(ACTUAL_KEY).subscribe(observeSubscriber);
        assertTrue(observeSubscriber.getOnNextEvents().size()==1);
        observeSubscriber.assertNoErrors();
    }

    @Test
    public void emitOnValueChanged_WhenObserve(){
        TestSubscriber<String> observeSubscriber = new TestSubscriber<>();
        storage.observe(ACTUAL_KEY).subscribe(observeSubscriber);
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
