package com.comp90018.H1Calendar.EventView;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.comp90018.H1Calendar.R;

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

}
