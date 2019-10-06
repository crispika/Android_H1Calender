package com.comp90018.H1Calendar.calendar;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

public class WeekView extends RecyclerView {

    public WeekView(@NonNull Context context) {
        super(context);
    }

    public WeekView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public WeekView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setCustomizedScrollListener() {
        addOnScrollListener(scrollListener);
    }

    private OnScrollListener scrollListener;

    {
        scrollListener = new OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }

            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                switch (newState) {
                    case SCROLL_STATE_DRAGGING:
                        //getAdapter().notifyDataSetChanged();
                        break;

                    default:
                        break;
                }
            }
        };
    }

}
