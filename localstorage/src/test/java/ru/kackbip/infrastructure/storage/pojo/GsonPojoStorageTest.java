package ru.kackbip.infrastructure.storage.pojo;

import org.junit.Before;
import org.junit.Test;

import ru.kackbip.infrastructure.storage.NotFoundException;
import ru.kackbip.infrastructure.storage.string.IStringStorage;
import rx.Observable;
import rx.observers.TestSubscriber;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by ryashentsev on 06.11.2016.
 */
public class GsonPojoStorageTest {

    private static final String ACTUAL_KEY = "actualKey";
    private static final String EMPTY_KEY = "emptyKey";
    private static final String STRINGIFIED_OBJECT = "String representation of the stored object";
    private static final SomeStoredClass STORED_OBJECT = new SomeStoredClass(
            23,
            new SomeStoredClass(54, null)
    );

    private GsonPojoStorage storage;

    @Before
    public void init() {
        IStringStorage stringStorage = mock(IStringStorage.class);
        when(stringStorage.store(ACTUAL_KEY, STRINGIFIED_OBJECT)).thenReturn(Observable.empty());
        when(stringStorage.restore(ACTUAL_KEY)).thenReturn(Observable.just(STRINGIFIED_OBJECT));
        when(stringStorage.restore(EMPTY_KEY)).thenReturn(Observable.error(new NotFoundException("Not found!")));

        IStringifier stringifier = mock(IStringifier.class);
        when(stringifier.fromObject(STORED_OBJECT)).thenReturn(STRINGIFIED_OBJECT);
        when(stringifier.toObject(STRINGIFIED_OBJECT, SomeStoredClass.class)).thenReturn(STORED_OBJECT);

        storage = new GsonPojoStorage(stringStorage, stringifier);
    }

    @Test
    public void storeObject(){
        TestSubscriber<Void> subscriber = new TestSubscriber<>();
        storage.store(ACTUAL_KEY, STORED_OBJECT).subscribe(subscriber);

        subscriber.assertNoErrors();
        assertTrue(subscriber.getOnNextEvents().isEmpty());
        subscriber.assertCompleted();
    }

    @Test
    public void restoreRightObject() {
        TestSubscriber<SomeStoredClass> subscriber = new TestSubscriber<>();
        storage.restore(ACTUAL_KEY, SomeStoredClass.class).subscribe(subscriber);

        subscriber.assertNoErrors();
        subscriber.assertValue(STORED_OBJECT);
        subscriber.assertCompleted();
    }

    @Test
    public void throwWhenTryToRestoreNullObject() {
        TestSubscriber<SomeStoredClass> subscriber = new TestSubscriber<>();
        storage.restore(EMPTY_KEY, SomeStoredClass.class).subscribe(subscriber);

        subscriber.assertError(NotFoundException.class);
        assertTrue(subscriber.getOnNextEvents().isEmpty());
    }
}