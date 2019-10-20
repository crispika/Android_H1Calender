package com.comp90018.H1Calendar.EventView;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.comp90018.H1Calendar.R;
import com.comp90018.H1Calendar.utils.CalendarManager;
import com.comp90018.H1Calendar.utils.DateManager;
import com.comp90018.H1Calendar.utils.EventBus;
import com.comp90018.H1Calendar.utils.Events;

import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class WeekEventView extends Fragment {

    private TextView tv_week;
    private ListView lv_week;

    public WeekEventView() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_week_event_view, container, false);
        return view;
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tv_week = view.findViewById(R.id.tv_week_event);
        lv_week = view.findViewById(R.id.lv_week_event);
        lv_week.setAdapter(new WeekEventListViewAdapter(WeekEventView.this.getActivity()));
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        EventBus.getInstance().getSubject().subscribe(event ->{
            if (event instanceof Events.DayClickedEvent){
                int week_of_year = ((Events.DayClickedEvent) event).getDayItem().getmWeekOfTheYear();
                String start = DateManager.headTailOfWeek(week_of_year)[0];
                String end = DateManager.headTailOfWeek(week_of_year)[1];


                //((DayEventListViewAdapter)lv_day.getAdapter()).setDate(dateStr);
                //Log.d(LOG_TAG, "Date Setted!");

            }
            else if (event instanceof Events.BackToToday){
                String dateStr = DateManager.dateToStr(CalendarManager.getInstance().getToday());

            }
        });

    }
}
