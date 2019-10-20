package com.comp90018.H1Calendar.EventView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.comp90018.H1Calendar.R;
import com.comp90018.H1Calendar.DBHelper.sqliteHelper;
import com.comp90018.H1Calendar.utils.CalenderEvent;

import java.util.List;

public class WeekEventListViewAdapter extends BaseAdapter {

    private Context myContext;
    private LayoutInflater myLayoutInflater;
    private String mDate;
    private sqliteHelper dbhelper;
    private List<CalenderEvent> weekEvents;
    private CalenderEvent mEvent;

    public WeekEventListViewAdapter(Context context) {
        myContext = context;
        myLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        if(weekEvents == null){
            return 0;
        }else if(weekEvents.isEmpty()){
            return 0;
        }else{
            return weekEvents.size();
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
        weekEvents = dbhelper.getEventsByDay(mDate);
        notifyDataSetChanged();
        //System.out.println(dayEvents.size());
    }

    public CalenderEvent getEvent(int position){
        return weekEvents.get(position);
    }



    static class ViewHolder {
        public TextView tvWeekEvent;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if(weekEvents!=null){
            mEvent = weekEvents.get(i);
        }

        if (view == null) {
            view = myLayoutInflater.inflate(R.layout.week_event_list_layout, null);
            holder = new ViewHolder();
            holder.tvWeekEvent = view.findViewById(R.id.tv_week_event);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.tvWeekEvent.setText(mEvent.getTitle());
        return view;
    }
}
