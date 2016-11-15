package ru.kackbip.infrastructure.storage.pojo;

/**
 * Created by ryashentsev on 06.11.2016.
 */

public class SomeStoredClass{
    private int num;
    private SomeStoredClass inner;
    public SomeStoredClass(int num, SomeStoredClass inner){
        this.num = num;
        this.inner = inner;
    }

    public int getNum() {
        return num;
    }

    public SomeStoredClass getInner() {
        return inner;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj==null ||
                !(obj instanceof SomeStoredClass)) return false;
        return areEquals(this, (SomeStoredClass) obj);
    }

    private boolean areEquals(SomeStoredClass stored1, SomeStoredClass stored2){
        if(stored1==null && stored2==null) return true;
        if(stored1==null || stored2==null) return false;
        return stored1.getNum()==stored2.getNum() &&
                areEquals(stored1.getInner(), stored2.getInner());
    }

    @Override
    public String toString() {
        return String.format("%d {%s}", num, inner);
    }
}

