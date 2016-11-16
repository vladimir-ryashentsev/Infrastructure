package ru.kackbip.infrastructure.storage.pojo;

import org.junit.Before;
import org.junit.Test;

import java.util.NoSuchElementException;

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
    private static final String STRINGIFIED_OBJECT2 = "String representation of the stored object 2";
    private static final SomeStoredClass STORED_OBJECT = new SomeStoredClass(
            23,
            new SomeStoredClass(54, null)
    );
    private static final SomeStoredClass STORED_OBJECT2 = new SomeStoredClass(
            233,
            new SomeStoredClass(544, null)
    );

    private GsonPojoStorage storage;
    private IStringStorage stringStorage;
    private IStringifier stringifier;

    @Before
    public void init() {
        stringStorage = mock(IStringStorage.class);
        when(stringStorage.store(ACTUAL_KEY, STRINGIFIED_OBJECT)).thenReturn(Observable.just(null));
        when(stringStorage.store(ACTUAL_KEY, null)).thenThrow(new NullPointerException());
        when(stringStorage.observe(ACTUAL_KEY)).thenReturn(Observable.create(subscriber -> {subscriber.onNext(STRINGIFIED_OBJECT);}));
        when(stringStorage.get(ACTUAL_KEY)).thenReturn(Observable.just(STRINGIFIED_OBJECT));
        when(stringStorage.observe(EMPTY_KEY)).thenReturn(Observable.never());
        when(stringStorage.get(EMPTY_KEY)).thenReturn(Observable.error(new NoSuchElementException()));

        stringifier = mock(IStringifier.class);
        when(stringifier.fromObject(STORED_OBJECT)).thenReturn(STRINGIFIED_OBJECT);
        when(stringifier.fromObject(STORED_OBJECT2)).thenReturn(STRINGIFIED_OBJECT2);
        when(stringifier.toObject(STRINGIFIED_OBJECT, SomeStoredClass.class)).thenReturn(STORED_OBJECT);

        storage = new GsonPojoStorage(stringStorage, stringifier);
    }

    @Test
    public void throw_WhenStoreNullObject(){
        TestSubscriber<Void> storeSubscriber = new TestSubscriber<>();
        storage.store(ACTUAL_KEY, null).subscribe(storeSubscriber);
        storeSubscriber.assertNoValues();
        storeSubscriber.assertError(NullPointerException.class);
    }

    @Test
    public void doesNotEmitAfterValueChanged_WhenGettingObject(){
        store(ACTUAL_KEY, STORED_OBJECT);
        TestSubscriber<SomeStoredClass> observeSubscriber = new TestSubscriber<>();
        storage.get(ACTUAL_KEY, SomeStoredClass.class).subscribe(observeSubscriber);
        observeSubscriber.assertValue(STORED_OBJECT);
        observeSubscriber.assertNoErrors();

        store(ACTUAL_KEY, STORED_OBJECT2);
        observeSubscriber.assertValues(STORED_OBJECT);
        observeSubscriber.assertNoErrors();
    }

    @Test
    public void throw_WhenGetNonExistentObject() {
        TestSubscriber<SomeStoredClass> subscriber = new TestSubscriber<>();
        storage.get(EMPTY_KEY, SomeStoredClass.class).subscribe(subscriber);

        subscriber.assertNoValues();
        subscriber.assertError(NoSuchElementException.class);
    }

    @Test
    public void completes_AfterGettingObject(){
        store(ACTUAL_KEY, STORED_OBJECT);

        TestSubscriber<SomeStoredClass> subscriber = new TestSubscriber<>();
        storage.get(ACTUAL_KEY, SomeStoredClass.class).subscribe(subscriber);

        subscriber.assertCompleted();
    }

    @Test
    public void getCorrectObject() {
        store(ACTUAL_KEY, STORED_OBJECT);

        TestSubscriber<SomeStoredClass> subscriber = new TestSubscriber<>();
        storage.get(ACTUAL_KEY, SomeStoredClass.class).subscribe(subscriber);

        subscriber.assertValue(STORED_OBJECT);
    }

    @Test
    public void doesNotComplete_WhenObserve(){
        TestSubscriber<SomeStoredClass> subscriber = new TestSubscriber<>();
        storage.observe(ACTUAL_KEY, SomeStoredClass.class).subscribe(subscriber);
        subscriber.assertNotCompleted();
    }

    @Test
    public void emitNoElements_WhenObserveNonExistentKey() {
        TestSubscriber<SomeStoredClass> subscriber = new TestSubscriber<>();
        storage.observe(EMPTY_KEY, SomeStoredClass.class).subscribe(subscriber);

        subscriber.assertNoValues();
        subscriber.assertNoErrors();
    }

    @Test
    public void emitOnlyLastElementOnSubscribe_WhenObserve(){
        store(ACTUAL_KEY, STORED_OBJECT);
        store(ACTUAL_KEY, STORED_OBJECT2);

        TestSubscriber<SomeStoredClass> observeSubscriber = new TestSubscriber<>();
        storage.observe(ACTUAL_KEY, SomeStoredClass.class).subscribe(observeSubscriber);
        assertTrue(observeSubscriber.getOnNextEvents().size()==1);
        observeSubscriber.assertNoErrors();
    }

    @Test
    public void emitNullAndCompletes_WhenStore(){
        TestSubscriber<Void> subscriber = new TestSubscriber<>();
        storage.store(ACTUAL_KEY, STORED_OBJECT).subscribe(subscriber);
        subscriber.assertValue(null);
        subscriber.assertCompleted();
        subscriber.assertNoErrors();
    }

    private void store(String key, Object object){
        storage.store(key, object).subscribe();
    }
}