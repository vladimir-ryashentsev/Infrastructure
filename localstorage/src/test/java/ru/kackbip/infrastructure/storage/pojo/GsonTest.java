package ru.kackbip.infrastructure.storage.pojo;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by ryashentsev on 06.11.2016.
 */
public class GsonTest {

    private Gson gson;
    private SomeStoredClass storedObject;

    @Before
    public void init(){
        gson = new GsonBuilder().create();
        storedObject = new SomeStoredClass(32, new SomeStoredClass(55, null));
    }

    @Test
    public void restoreEqualObject() throws Exception {
        String json = gson.toJson(storedObject);
        SomeStoredClass restoredObject = gson.fromJson(json, SomeStoredClass.class);
        assertEquals(storedObject, restoredObject);
    }
}