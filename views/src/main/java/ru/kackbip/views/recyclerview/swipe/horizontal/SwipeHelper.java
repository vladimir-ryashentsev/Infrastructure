package ru.kackbip.views.recyclerview.swipe.horizontal;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import rx.Observable;

/**
 * Created by ryashentsev on 26.11.2016.
 */

public class SwipeHelper {

    private SwipeCallback mCallback;
    private ItemTouchHelper itemTouchHelper;

    public SwipeHelper(RecyclerView recyclerView) {
        mCallback = new SwipeCallback();
        itemTouchHelper = new ItemTouchHelper(mCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    public Observable<Void> observeSwipe(){
        return mCallback.observeSwipe();
    }
}
