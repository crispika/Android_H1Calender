package com.comp90018.H1Calendar.EventView;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.comp90018.H1Calendar.EventDetailActivity;
import com.comp90018.H1Calendar.R;
import com.comp90018.H1Calendar.utils.CalendarManager;
import com.comp90018.H1Calendar.utils.DateManager;
import com.comp90018.H1Calendar.utils.EventBus;
import com.comp90018.H1Calendar.utils.Events;

import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * this is the fragment to contain day event list
 */
public class DayEventView extends Fragment {

    private final String LOG_TAG = this.getClass().getSimpleName();

    private TextView tv_day;
    private ListView lv_day;
    public TextView tvDayEventTextHeader;
    private DayEventListViewAdapter dayEventListViewAdapter;
    private String daySelected;

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
        tv_day = view.findViewById(R.id.day_event_title);
        lv_day = view.findViewById(R.id.lv_day_event);
        tvDayEventTextHeader = view.findViewById(R.id.day_event_textHeader);
        dayEventListViewAdapter = new DayEventListViewAdapter(DayEventView.this.getActivity());

        //set day selected into today or last selected date to refresh the layout
        if (CalendarManager.getInstance().getSelectedItem() == null) {
            daySelected = DateManager.dateToStr(CalendarManager.getInstance().getToday());
        } else {
            daySelected = DateManager.dateToStr(CalendarManager.getInstance().getSelectedDate());
        }
        dayEventListViewAdapter.setDate(daySelected);
        //set Day Event View Header Text
        setTitle();
        lv_day.setAdapter(dayEventListViewAdapter);

        //if item is clicked, jump to the event detail activity
        lv_day.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(DayEventView.this.getActivity(), EventDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("id", dayEventListViewAdapter.getEvent(i).getEventId());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

    }


    public void setView(String date) {
        dayEventListViewAdapter.setDate(date);
    }

    //receive date from event bus
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        EventBus.getInstance().getSubject().subscribe(event -> {
            if (event instanceof Events.DayClickedEvent) {
                Date date = ((Events.DayClickedEvent) event).getDayItem().getDate();
                String dateStr = DateManager.dateToStr(date);
                dayEventListViewAdapter.setDate(dateStr);
                Log.d("single_day", dateStr);
                setTitle();
                daySelected = dateStr;
            } else if (event instanceof Events.BackToToday) {
                String dateStr = DateManager.dateToStr(CalendarManager.getInstance().getToday());
                dayEventListViewAdapter.setDate(dateStr);
                setTitle();
            }
        });
    }

    private void setTitle() {
        if (dayEventListViewAdapter.getEvantList() == null) {
            tvDayEventTextHeader.setText("No event for today, please add one");
        } else if (dayEventListViewAdapter.getEvantList().isEmpty()) {
            tvDayEventTextHeader.setText("No event for today, please add one");
        } else {
            tvDayEventTextHeader.setText("Today's Event");
        }
    }

}
