package com.comp90018.H1Calendar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class LeftListAdapter extends BaseAdapter {

    private Context myContext;
    private LayoutInflater myLayoutInflater;
    private String[] Command = {"Day", "Week"};

    public LeftListAdapter(Context context) {
        myContext = context;
        myLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return 2;
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
        private TextView eventSwitch;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if (view == null) {
            view = myLayoutInflater.inflate(R.layout.left_list_layout_item, null);
            holder = new ViewHolder();
            holder.eventSwitch = view.findViewById(R.id.event_switch);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.eventSwitch.setText(Command[i]);
        return view;
    }
}
