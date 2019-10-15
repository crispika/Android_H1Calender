package com.comp90018.H1Calendar.EventView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.comp90018.H1Calendar.DBHelper.sqliteHelper;
import com.comp90018.H1Calendar.R;
import com.comp90018.H1Calendar.utils.CalenderEvent;

import java.util.List;

public class DayEventListViewAdapter extends BaseAdapter {

    private Context myContext;
    private LayoutInflater myLayoutInflater;
    private String mDate;
    private sqliteHelper dbhelper;
    private List<CalenderEvent> dayEvents;
    private CalenderEvent mEvent;

    public DayEventListViewAdapter(Context context) {
        myContext = context;
        myLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        if(dayEvents == null){
            return 0;
        }else if(dayEvents.isEmpty()){
            return 0;
        }else{
            return dayEvents.size();
        }
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    public void setDate(String date){
        mDate = date;
        setEventList();
    }
    public void setEventList(){
        dbhelper = new sqliteHelper(myContext.getApplicationContext());
        dayEvents = dbhelper.getEventsByDay(mDate);
        notifyDataSetChanged();
        //System.out.println(dayEvents.size());
    }

    static class ViewHolder {
        public TextView tvDayEvent;

    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;
        mEvent = dayEvents.get(i);
        if (view == null) {
            view = myLayoutInflater.inflate(R.layout.day_event_list_layout, null);
            holder = new ViewHolder();
            holder.tvDayEvent = view.findViewById(R.id.tv_day_event);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.tvDayEvent.setText(mEvent.getTitle());
        return view;
    }
}
