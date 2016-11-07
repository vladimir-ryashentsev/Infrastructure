package ru.kackbip.infrastructure.storage.pojo;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertNull;
import static org.junit.Assert.assertEquals;

/**
 * Created by ryashentsev on 06.11.2016.
 */
public class GsonStringifierTest {

    private IStringifier converter;
    private static final SomeStoredClass storedObject = new SomeStoredClass(
            123,
            new SomeStoredClass(321, null)
    );

    @Before
    public void init(){
        Gson gson = new GsonBuilder().create();
        converter = new GsonStringifier(gson);
    }

    @Test
    public void convertObjectToStringToEqualObject() throws Exception {
        String str = converter.fromObject(storedObject);
        SomeStoredClass restoredObject = converter.toObject(str, SomeStoredClass.class);
        assertEquals(storedObject, restoredObject);
    }

    @Test(expected=NullPointerException.class)
    public void throwExceptionWhenConvertNullStringToNullObject() throws Exception {
        SomeStoredClass restoredObject = converter.toObject(null, SomeStoredClass.class);
        assertNull(restoredObject);
    }
}