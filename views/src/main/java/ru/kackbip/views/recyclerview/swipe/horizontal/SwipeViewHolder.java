package ru.kackbip.views.recyclerview.swipe.horizontal;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by ryashentsev on 26.11.2016.
 */

public class SwipeViewHolder extends RecyclerView.ViewHolder {

    private View foreground;
    private View background;

    public SwipeViewHolder(View view, int backgroundId, int foregroundId) {
        super(view);
        background = view.findViewById(backgroundId);
        foreground = view.findViewById(foregroundId);
    }

    public View getForeground() {
        return foreground;
    }

    public View getBackground() {
        return background;
    }
}
