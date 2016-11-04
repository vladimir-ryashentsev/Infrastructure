package ru.kackbip.localstorage.infrastructure;

import android.content.SharedPreferences;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.junit.Test;
import org.junit.runner.RunWith;

import ru.kackbip.infrastructure.localStorage.ILocalStorage;
import ru.kackbip.infrastructure.localStorage.InMemoryLocalStorage;
import ru.kackbip.infrastructure.localStorage.SharedPreferencesLocalStorage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    private static String storedObjectKey = "key";
    private ILocalStorage localStorage;

    @Test
    public void SharedPreferenceLocalStorage_storeAndRestoreEqualObjects() throws Exception {
        initSharedPreferencesLocalStorage();
        store();
    }

    @Test
    public void InMemoryLocalStorage_storeAndRestoreEqualObjects() throws Exception {
        initInMemoryLocalStorage();
        store();
    }

    private void initInMemoryLocalStorage(){
        localStorage = new InMemoryLocalStorage();
    }

    private void initSharedPreferencesLocalStorage(){
        SharedPreferences sp = InstrumentationRegistry.getTargetContext().getSharedPreferences("TestSP", 0);
        Gson gson = new GsonBuilder().create();
        localStorage = new SharedPreferencesLocalStorage(sp, gson);
    }

    private void store(){
        localStorage.store(storedObjectKey, generateTestData()).subscribe(aVoid -> restore());
    }

    private SomeStoredClass generateTestData(){
        SomeStoredClass innerStoredObject = new SomeStoredClass(54, null);
        return new SomeStoredClass(23, innerStoredObject);
    }

    private void restore(){
        localStorage.restore(storedObjectKey, SomeStoredClass.class).subscribe(this::checkRestoredObject);
    }

    private void checkRestoredObject(SomeStoredClass restoredObject){
        assertEquals(restoredObject.getNum(), 23);
        SomeStoredClass innerRestoredObject = restoredObject.getInner();
        assertNotNull(innerRestoredObject);
        assertNull(innerRestoredObject.getInner());
        assertEquals(innerRestoredObject.getNum(), 54);
    }

    private static class SomeStoredClass{
        private int num;
        private SomeStoredClass inner;
        public SomeStoredClass(int num, SomeStoredClass inner){
            this.num = num;
            this.inner = inner;
        }

        public int getNum() {
            return num;
        }

        public SomeStoredClass getInner() {
            return inner;
        }
    }
}
