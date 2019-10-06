package com.comp90018.H1Calendar.EventView;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.comp90018.H1Calendar.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class DayEventView extends Fragment {

    private TextView tv_day;
    private ListView lv_day;

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
        lv_day.setAdapter(new DayEventListViewAdapter(DayEventView.this.getActivity()));
    }

}
