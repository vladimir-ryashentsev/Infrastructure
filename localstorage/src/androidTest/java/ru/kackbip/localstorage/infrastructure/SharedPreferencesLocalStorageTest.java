package ru.kackbip.localstorage.infrastructure;

import org.junit.Test;

import ru.kackbip.infrastructure.localStorage.ILocalStorage;
import ru.kackbip.infrastructure.localStorage.InMemoryLocalStorage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class SharedPreferencesLocalStorageTest {

    private static String storedObjectKey = "key";
    private ILocalStorage localStorage = new InMemoryLocalStorage();

    @Test
    public void storeAndRestoreEqualObjects() throws Exception {

        SomeStoredClass innerStoredObject = new SomeStoredClass(54, null);
        SomeStoredClass storedObject = new SomeStoredClass(23, innerStoredObject);

        localStorage.store(storedObjectKey, storedObject).subscribe(aVoid -> {
                testRestore();
        });

    }

    private void testRestore(){
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