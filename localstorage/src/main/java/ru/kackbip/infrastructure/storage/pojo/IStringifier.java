package ru.kackbip.infrastructure.storage.pojo;

/**
 * Created by ryashentsev on 06.11.2016.
 */

public interface IStringifier {
    String fromObject(Object object);
    <T> T toObject(String string, Class<T> clazz);
}
