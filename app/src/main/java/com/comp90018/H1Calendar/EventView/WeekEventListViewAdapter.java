package com.comp90018.H1Calendar.EventView;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.comp90018.H1Calendar.R;
import com.comp90018.H1Calendar.DBHelper.sqliteHelper;
import com.comp90018.H1Calendar.utils.CalenderEvent;

import java.util.List;

public class WeekEventListViewAdapter extends BaseAdapter {

    private Context myContext;
    private LayoutInflater myLayoutInflater;
    private String mDateStart;
    private String mDateEnd;
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

    public void setDate(String start, String end){
        mDateStart = start;
        mDateEnd = end;
        setEventList();
    }
    public void setEventList(){
        dbhelper = new sqliteHelper(myContext.getApplicationContext());
        weekEvents = dbhelper.getEventsByWeek(mDateStart,mDateEnd);
        System.out.println("week:" + weekEvents.size());
        notifyDataSetChanged();
        //System.out.println(dayEvents.size());
    }

    public CalenderEvent getEvent(int position){
        return weekEvents.get(position);
    }



    static class ViewHolder {
        public TextView tvWeekEventTitle;
        public LinearLayout llWeekEventBackground;
        public TextView tvWeekEventLocation;
        public TextView tvWeekEventDate;
        public TextView tvWeekEventTimeDuration;
        public TextView tvWeekEventDescription;
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
            holder.tvWeekEventTitle = view.findViewById(R.id.week_event_title);
            holder.llWeekEventBackground = view.findViewById(R.id.week_event_background);
            holder.tvWeekEventLocation = view.findViewById(R.id.week_event_location);
            holder.tvWeekEventDate = view.findViewById(R.id.week_event_date);
            holder.tvWeekEventTimeDuration = view.findViewById(R.id.week_event_start_End_Time);
            holder.tvWeekEventDescription = view.findViewById(R.id.week_event_description);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        String dateStr = mEvent.getDay() + "/"+mEvent.getMonth() + "/" + mEvent.getYear();
        holder.tvWeekEventTitle.setText(mEvent.getTitle());
        holder.tvWeekEventLocation.setText(mEvent.getLocal());
        holder.tvWeekEventDate.setText(dateStr);
        holder.tvWeekEventTimeDuration.setText(mEvent.getEventTime());
        holder.tvWeekEventDescription.setText(mEvent.getDescription());
        holder.llWeekEventBackground.setBackgroundColor(Color.BLUE);
        return view;
    }

    public List<CalenderEvent> getEvantList(){
        return weekEvents;
    }
}
