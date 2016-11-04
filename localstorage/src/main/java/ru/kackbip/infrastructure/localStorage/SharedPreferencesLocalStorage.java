package ru.kackbip.infrastructure.localStorage;

import android.content.SharedPreferences;

import com.google.gson.Gson;

import ru.kackbip.infrastructure.localStorage.ILocalStorage;
import rx.AsyncEmitter;
import rx.Observable;

/**
 * Created by ryashentsev on 09.10.2016.
 */

public class SharedPreferencesLocalStorage implements ILocalStorage {

    private SharedPreferences sharedPreferences;
    private Gson gson;

    public SharedPreferencesLocalStorage(SharedPreferences sharedPreferences, Gson gson){
        this.sharedPreferences = sharedPreferences;
        this.gson = gson;
    }

    @Override
    public Observable<Void> store(String key, Object object) {
        return Observable.fromEmitter(emitter -> {
            try {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                String json = gson.toJson(object);
                editor.putString(key, json);
                editor.commit();
                emitter.onCompleted();
            }catch (Exception e){
                emitter.onError(e);
            }
        }, AsyncEmitter.BackpressureMode.ERROR);
    }

    @Override
    public <ModelType> Observable<ModelType> restore(String key, Class<ModelType> clazz) {
        return Observable.fromEmitter(emitter -> {
            try {
                String json = sharedPreferences.getString(key, null);
                if(json!=null){
                    ModelType result = gson.fromJson(json, clazz);
                    emitter.onNext(result);
                }
                emitter.onCompleted();
            }catch (Exception e){
                emitter.onError(e);
            }
        }, AsyncEmitter.BackpressureMode.ERROR);
    }
}
