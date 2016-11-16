package ru.kackbip.infrastructure.storage.pojo;

import com.google.gson.Gson;

/**
 * Created by ryashentsev on 06.11.2016.
 */

public class GsonStringifier implements IStringifier {

    private Gson gson;

    public GsonStringifier(Gson gson) {
        this.gson = gson;
    }

    @Override
    public String fromObject(Object object) {
        if(object==null) throw new NullPointerException();
        return gson.toJson(object);
    }

    @Override
    public <T> T toObject(String string, Class<T> clazz) {
        if (string == null || clazz == null) throw new NullPointerException();
        return gson.fromJson(string, clazz);
    }
}
