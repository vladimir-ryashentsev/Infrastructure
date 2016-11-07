package ru.kackbip.infrastructure.storage.string.local;

import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import ru.kackbip.infrastructure.storage.NotFoundException;
import ru.kackbip.infrastructure.storage.string.IStringStorage;
import rx.AsyncEmitter;
import rx.Observable;

/**
 * Created by ryashentsev on 09.10.2016.
 */

public class SharedPreferencesStringStorage implements IStringStorage {

    private SharedPreferences sharedPreferences;
    private Gson gson;

    public SharedPreferencesStringStorage(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
        gson = new GsonBuilder().create();
    }

    @Override
    public Observable<Void> store(String key, String storedString) {
        return Observable.fromEmitter(emitter -> {
            try {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(key, storedString);
                editor.apply();
                emitter.onCompleted();
            } catch (Exception e) {
                emitter.onError(e);
            }
        }, AsyncEmitter.BackpressureMode.ERROR);
    }

    @Override
    public Observable<String> restore(String key) {
        return Observable.fromEmitter(emitter -> {
            try {
                String str = sharedPreferences.getString(key, null);
                if (str == null) throw new NotFoundException(String.format("String under key \"%s\" not found!", key));
                emitter.onNext(str);
                emitter.onCompleted();
            } catch (Exception e) {
                emitter.onError(e);
            }
        }, AsyncEmitter.BackpressureMode.ERROR);
    }
}
