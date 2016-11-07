package ru.kackbip.infrastructure.storage.string.local;

import org.junit.Before;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class InMemoryStringStorageTest extends StringStorageTest {
    @Before
    public void init() {
        storage = new InMemoryStringStorage();
    }
}
