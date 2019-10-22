package com.comp90018.H1Calendar.EventView;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.comp90018.H1Calendar.DBHelper.sqliteHelper;
import com.comp90018.H1Calendar.R;
import com.comp90018.H1Calendar.utils.CalenderEvent;

import java.util.Collections;
import java.util.List;

public class DayEventListViewAdapter extends BaseAdapter {

    private Context myContext;
    private LayoutInflater myLayoutInflater;
    private String mDate;
    private sqliteHelper dbhelper;
    private List<CalenderEvent> dayEvents;
    private CalenderEvent mEvent;
    private String timeStr;

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

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;
        mEvent = dayEvents.get(i);
        if (view == null) {
            view = myLayoutInflater.inflate(R.layout.day_event_list_layout, null);
            holder = new ViewHolder();
            holder.tvDayEventTitle = view.findViewById(R.id.day_event_title);
            holder.llEventBackground = view.findViewById(R.id.day_event_background);
            holder.tvDayEventLocation = view.findViewById(R.id.day_event_location);
            holder.tvDayEventTimeDuration = view.findViewById(R.id.day_event_start_End_Time);
            holder.tvDayEventDescription = view.findViewById(R.id.day_event_description);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        setTimeStr();
        holder.tvDayEventTitle.setText(mEvent.getTitle());
        holder.tvDayEventLocation.setText(mEvent.getLocal());
        holder.tvDayEventTimeDuration.setText(timeStr);
        holder.tvDayEventDescription.setText(mEvent.getDescription());
        holder.llEventBackground.setBackgroundColor(myContext.getResources().getColor(getColor(mEvent)));
        return view;
    }

    public void setDate(String date){
        mDate = date;
        setEventList();
    }
    public void setEventList(){
        dbhelper = new sqliteHelper(myContext.getApplicationContext());
        dayEvents = dbhelper.getEventsByDay(mDate);
        Collections.sort(dayEvents);
        notifyDataSetChanged();
        //System.out.println(dayEvents.size());
    }

    public CalenderEvent getEvent(int position){
        return dayEvents.get(position);
    }

    public List<CalenderEvent> getEvantList(){
        return dayEvents;
    }

    private int getColor(CalenderEvent mEvent){
        String color;
        if (mEvent == null) return R.color.Default;
        if(mEvent.getEventColor() == null) color = "default";
        else color = mEvent.getEventColor();

        switch (color){
            case "Green":
                return R.color.Green;
            case "Yellow":
                return R.color.Yellow;
            case "Red":
                return R.color.Red;
            case "Blue":
                return R.color.Blue;
            default:
                return R.color.Default;
        }
    }

    static class ViewHolder {
        public TextView tvDayEventTitle;
        public LinearLayout llEventBackground;
        public TextView tvDayEventLocation;
        public TextView tvDayEventTimeDuration;
        public TextView tvDayEventDescription;

    }
    public void setTimeStr() {
        if (mEvent.getIsAllday()) {
            timeStr = "Full Day";
        } else {
            timeStr = mEvent.getStartTimeHour() + " : " + mEvent.getStartTimeMinute() + " - " +
                    mEvent.getEndTimeHour() + " : " + mEvent.getEndTimeMinute();
        }
    }

}
