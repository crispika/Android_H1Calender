package com.comp90018.H1Calendar.EventView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.comp90018.H1Calendar.R;

public class DayEventListViewAdapter extends BaseAdapter {

    private Context myContext;
    private LayoutInflater myLayoutInflater;

    public DayEventListViewAdapter(Context context) {
        myContext = context;
        myLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return 10;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    static class ViewHolder {
        public TextView tvDayEvent;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if (view == null) {
            view = myLayoutInflater.inflate(R.layout.day_event_list_layout, null);
            holder = new ViewHolder();
            holder.tvDayEvent = view.findViewById(R.id.tv_day_event);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        return view;
    }
}
