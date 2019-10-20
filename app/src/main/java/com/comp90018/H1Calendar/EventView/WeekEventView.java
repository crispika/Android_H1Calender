package com.comp90018.H1Calendar.EventView;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.comp90018.H1Calendar.EventDetailActivity;
import com.comp90018.H1Calendar.R;
import com.comp90018.H1Calendar.utils.CalendarManager;
import com.comp90018.H1Calendar.utils.CalenderEvent;
import com.comp90018.H1Calendar.utils.DateManager;
import com.comp90018.H1Calendar.utils.EventBus;
import com.comp90018.H1Calendar.utils.Events;

import java.util.Calendar;
import java.util.Date;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class WeekEventView extends Fragment {

    private TextView tv_week;
    private ListView lv_week;
    public TextView tvWeekEventTextHeader;

    private WeekEventListViewAdapter weekEventListViewAdapter;
    private String defautStart = DateManager.headTailOfWeek(CalendarManager.getInstance().getTodayCalendar().get(Calendar.WEEK_OF_YEAR))[0];
    private String defautEnd = DateManager.headTailOfWeek(CalendarManager.getInstance().getTodayCalendar().get(Calendar.WEEK_OF_YEAR))[1];

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
        lv_week = view.findViewById(R.id.lv_week_event);
        tvWeekEventTextHeader = view.findViewById(R.id.week_event_textHeader);
        weekEventListViewAdapter = new WeekEventListViewAdapter(WeekEventView.this.getActivity());
        lv_week.setAdapter(weekEventListViewAdapter);
        weekEventListViewAdapter.setDate(defautStart,defautEnd);
        setTitle();
        lv_week.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(WeekEventView.this.getActivity(), EventDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("id",weekEventListViewAdapter.getEvent(i).getEventId());
                //System.out.println(dayEventListViewAdapter.getEvent(i).getEventId());
                intent.putExtras(bundle);
                startActivity(intent);
                System.out.println(weekEventListViewAdapter.getEvent(i).getTitle());
            }
        });
    }

    private void setTitle(){
        if(weekEventListViewAdapter.getEvantList() == null){
            tvWeekEventTextHeader.setText("No event for this week, please add one");
        }else if(weekEventListViewAdapter.getEvantList() .isEmpty()){
            tvWeekEventTextHeader.setText("No event for this week, please add one");
        }else{
            tvWeekEventTextHeader.setText("Weekly Event");
        }
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        EventBus.getInstance().getSubject().subscribe(event ->{
            if (event instanceof Events.DayClickedEvent){
                int week_of_year = ((Events.DayClickedEvent) event).getDayItem().getmWeekOfTheYear();
                String start = DateManager.headTailOfWeek(week_of_year)[0];
                String end = DateManager.headTailOfWeek(week_of_year)[1];
                defautStart = start;
                defautEnd = end;
                weekEventListViewAdapter.setDate(start,end);

                //((DayEventListViewAdapter)lv_day.getAdapter()).setDate(dateStr);
                //Log.d(LOG_TAG, "Date Setted!");
                setTitle();
            }
            else if (event instanceof Events.BackToToday){
                String dateStr = DateManager.dateToStr(CalendarManager.getInstance().getToday());

            }
        });
    }


}
