package com.comp90018.H1Calendar.calendar;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.comp90018.H1Calendar.R;
import com.comp90018.H1Calendar.model.WeekItem;
import com.comp90018.H1Calendar.utils.CalendarManager;
import com.comp90018.H1Calendar.utils.DateManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * CalendarView is a customized widget for a section to display a scrollable calendar with click/scroll events.
 */
public class CalendarView extends LinearLayout {

    private final String LOG_TAG = "CalendarView";

    /**
     * Calendar 组件内部显示每个星期日期的recycler view.
     */
    private WeekView weekView;

    private int hearderTextColor, currentDayTextColor, pastDayTextColor;

    //region Constructors
    public CalendarView(Context context) {
        super(context);
    }

    public CalendarView(Context context, AttributeSet attrs) {
        super(context, attrs);

        //加载布局进这个空layout中
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        layoutInflater.inflate(R.layout.calendar_view, this, true);

        //设置布局垂直！！一定要设置，不然recyclerview显示不出来，因为默认horizontal的
        setOrientation(VERTICAL);

    }

    public CalendarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    //endregion


    //region Override View Methods
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        weekView = findViewById(R.id.calendarview_weekview);
        //set LayoutManager for RecyclerView WeekView
        weekView.setLayoutManager(new LinearLayoutManager(getContext()));
        //因为每个item的高度是固定的，设置这个提高性能
        weekView.setHasFixedSize(true);
        weekView.setCustomizedScrollListener();

    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        MarginLayoutParams layoutParams = (MarginLayoutParams) getLayoutParams();
        layoutParams.height  = (int) (getResources().getDimension(R.dimen.calendarview_header) + 5 * getResources().getDimension(R.dimen.calendarview_weekitem));
        setLayoutParams(layoutParams);
    }
    //endregion


    public void init(int hearderTextColor, int currentDayTextColor, int pastDayTextColor) {
        this.hearderTextColor = hearderTextColor;
        this.currentDayTextColor = currentDayTextColor;
        this.pastDayTextColor = pastDayTextColor;
        initWeekView();
        scrollToDate(CalendarManager.getInstance().getToday(), CalendarManager.getInstance().getWeekList());
    }

    void initWeekView() {
        weekView.setAdapter(new WeekViewAdapter(hearderTextColor, currentDayTextColor, pastDayTextColor));
        Log.d(LOG_TAG, "WeekView initiated");
    }

    /**
     * 将视图scroll到某个日期的那一周处.
     * e.g 在calendarView初始化的时候，从当天的日期开始显示.
     */
    void scrollToDate(Date date, ArrayList<WeekItem> week_list) {

        Integer position = null;
        for (int i = 0; i < week_list.size(); i++) {
            if (DateManager.isSameWeek(date, week_list.get(i).getDate())) {
                position = i;
                break;
            }
        }

        if (position != null) {
            //post()方法run
            //weekView.post(() -> scrollToPosition(position));
            scrollToPosition(position);
        }
    }

    void scrollToPosition(int position){
        LinearLayoutManager layoutManager = (LinearLayoutManager) weekView.getLayoutManager();
        layoutManager.scrollToPosition(position);
    }

}
