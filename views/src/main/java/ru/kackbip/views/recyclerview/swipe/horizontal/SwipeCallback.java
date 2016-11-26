package ru.kackbip.views.recyclerview.swipe.horizontal;

import android.graphics.Canvas;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import rx.AsyncEmitter;
import rx.Observable;

/**
 * Created by ryashentsev on 26.11.2016.
 */

public class SwipeCallback extends ItemTouchHelper.SimpleCallback {

    private AsyncEmitter<Void> emitter;
    private Observable<Void> observable;

    public SwipeCallback() {
        super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
    }

    public Observable<Void> observeSwipe(){
        if(emitter==null) return observable = Observable.fromEmitter(emitter -> this.emitter = emitter, AsyncEmitter.BackpressureMode.ERROR);
        return observable;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {

    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        if (viewHolder != null) {
            final View foregroundView = ((SwipeViewHolder) viewHolder).getForeground();

            getDefaultUIUtil().onSelected(foregroundView);
        }
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        final View foregroundView = ((SwipeViewHolder) viewHolder).getForeground();

        drawBackground(viewHolder, dX, actionState);

        getDefaultUIUtil().onDraw(c, recyclerView, foregroundView, dX, dY,
                actionState, isCurrentlyActive);
    }

    @Override
    public void onChildDrawOver(Canvas c, RecyclerView recyclerView,
                                RecyclerView.ViewHolder viewHolder, float dX, float dY,
                                int actionState, boolean isCurrentlyActive) {
        final View foregroundView = ((SwipeViewHolder) viewHolder).getForeground();

        drawBackground(viewHolder, dX, actionState);

        getDefaultUIUtil().onDrawOver(c, recyclerView, foregroundView, dX, dY,
                actionState, isCurrentlyActive);
    }

    private void drawBackground(RecyclerView.ViewHolder viewHolder, float dX, int actionState) {
        final View backgroundView = ((SwipeViewHolder) viewHolder).getBackground();
        final View foregroundView = ((SwipeViewHolder) viewHolder).getForeground();

        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            if (dX < 0) {
                backgroundView.setRight(foregroundView.getWidth());
            } else {
                backgroundView.setRight((int) dX);
                backgroundView.setLeft(0);
            }
        }
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        final View backgroundView = ((SwipeViewHolder) viewHolder).getBackground();
        final View foregroundView = ((SwipeViewHolder) viewHolder).getForeground();
        backgroundView.setRight(0);
        backgroundView.setLeft(0);
        getDefaultUIUtil().clearView(foregroundView);
    }
}