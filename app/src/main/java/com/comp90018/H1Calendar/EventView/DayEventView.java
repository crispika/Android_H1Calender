package com.comp90018.H1Calendar.EventView;


import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.comp90018.H1Calendar.R;
import com.comp90018.H1Calendar.utils.CalendarManager;
import com.comp90018.H1Calendar.utils.DateManager;
import com.comp90018.H1Calendar.utils.EventBus;
import com.comp90018.H1Calendar.utils.Events;

import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class DayEventView extends Fragment {

    private final String LOG_TAG = this.getClass().getSimpleName();

    private TextView tv_day;
    private ListView lv_day;
    private DayEventListViewAdapter dayEventListViewAdapter;

    public DayEventView() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_day_event_view, container, false);
        return view;
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tv_day = view.findViewById(R.id.tv_day_event);
        lv_day = view.findViewById(R.id.lv_day_event);
        dayEventListViewAdapter = new DayEventListViewAdapter(DayEventView.this.getActivity());
        lv_day.setAdapter(dayEventListViewAdapter);

    }

    public void setView(String date){
        dayEventListViewAdapter.setDate(date);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        EventBus.getInstance().getSubject().subscribe(event ->{
            if (event instanceof Events.DayClickedEvent){
                Date date = ((Events.DayClickedEvent) event).getDayItem().getDate();
                String dateStr = DateManager.dateToStr(date);
                dayEventListViewAdapter.setDate(dateStr);
                //((DayEventListViewAdapter)lv_day.getAdapter()).setDate(dateStr);
                //Log.d(LOG_TAG, "Date Setted!");
            }
            else if (event instanceof Events.BackToToday){
                String dateStr = DateManager.dateToStr(CalendarManager.getInstance().getToday());
                dayEventListViewAdapter.setDate(dateStr);
            }
        });
    }

}
