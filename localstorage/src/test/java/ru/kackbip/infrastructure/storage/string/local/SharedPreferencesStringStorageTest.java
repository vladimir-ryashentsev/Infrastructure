package ru.kackbip.infrastructure.storage.string.local;

import android.content.SharedPreferences;

import org.junit.Before;
import org.junit.Test;

import ru.kackbip.infrastructure.storage.NotFoundException;
import ru.kackbip.infrastructure.storage.string.IStringStorage;
import rx.observers.TestSubscriber;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
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

    private IStringStorage storage;
    private SharedPreferences.Editor editor;
    private SharedPreferences sharedPreferences;

    @Before
    public void init(){
        sharedPreferences = mock(SharedPreferences.class);
        when(sharedPreferences.getString(ACTUAL_KEY, null)).thenReturn(STORED_STRING);
        editor = mock(SharedPreferences.Editor.class);
        when(sharedPreferences.edit()).thenReturn(editor);
        storage = new SharedPreferencesStringStorage(sharedPreferences);
    }

    @Test
    public void store(){
        TestSubscriber<Void> subscriber = new TestSubscriber<>();
        storage.store(ACTUAL_KEY, STORED_STRING).subscribe(subscriber);

        assertTrue(subscriber.getOnNextEvents().isEmpty());
        subscriber.assertNoErrors();
        subscriber.assertCompleted();

        verify(editor).putString(ACTUAL_KEY, STORED_STRING);
        verify(editor).apply();
    }

    @Test
    public void restoreUsingNonExistentKey() {
        TestSubscriber<String> subscriber = new TestSubscriber<>();
        storage.restore(EMPTY_KEY).subscribe(subscriber);

        assertTrue(subscriber.getOnNextEvents().isEmpty());
        subscriber.assertError(NotFoundException.class);

        verify(sharedPreferences).getString(EMPTY_KEY, null);
    }

    @Test
    public void restore() {
        TestSubscriber<String> subscriber = new TestSubscriber<>();
        storage.restore(ACTUAL_KEY).subscribe(subscriber);

        subscriber.assertNoErrors();
        subscriber.assertValue(STORED_STRING);
        subscriber.assertCompleted();

        verify(sharedPreferences).getString(ACTUAL_KEY, null);
    }

}
