package ru.kackbip.infrastructure.storage;

/**
 * Created by ryashentsev on 07.11.2016.
 */

public class NotFoundException extends Exception {
    public NotFoundException(String message){
        super(message);
    }
}
