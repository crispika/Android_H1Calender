package com.comp90018.H1Calendar.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.comp90018.H1Calendar.R;

import java.util.List;

public class LocationListAdapter extends BaseAdapter {
    private List<EventLocation> locationList;
    private Context context;
    public LocationListAdapter(Context context, List<EventLocation> locationList){
        this.locationList = locationList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return locationList == null ? 0 : locationList.size();
    }

    @Override
    public Object getItem(int i) {
        return locationList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(context).inflate(R.layout.location_cell_view,null);

        TextView tv =  view.findViewById(R.id.location_name);
        tv.setText(locationList.get(i).getName());
        view.setTag(tv);

        return view;
    }


}
