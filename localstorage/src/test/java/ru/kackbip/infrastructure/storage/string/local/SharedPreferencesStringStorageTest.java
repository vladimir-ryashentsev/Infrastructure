package ru.kackbip.infrastructure.storage.string.local;

import android.content.SharedPreferences;

import org.junit.Before;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class SharedPreferencesStringStorageTest extends StringStorageTest{
    @Before
    public void init(){
        SharedPreferences sharedPreferences = mock(SharedPreferences.class);
        when(sharedPreferences.getString(ACTUAL_KEY, null)).thenReturn(STORED_STRING);
        SharedPreferences.Editor editor = mock(SharedPreferences.Editor.class);
        when(sharedPreferences.edit()).thenReturn(editor);
        storage = new SharedPreferencesStringStorage(sharedPreferences);
    }
}
