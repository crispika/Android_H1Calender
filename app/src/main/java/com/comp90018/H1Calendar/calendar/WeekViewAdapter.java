package com.comp90018.H1Calendar.calendar;

import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.comp90018.H1Calendar.R;
import com.comp90018.H1Calendar.model.DayItem;
import com.comp90018.H1Calendar.model.WeekItem;
import com.comp90018.H1Calendar.utils.CalendarManager;
import com.comp90018.H1Calendar.utils.EventBus;
import com.comp90018.H1Calendar.utils.Events;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Adapter for WeekView
 */
public class WeekViewAdapter extends RecyclerView.Adapter<WeekViewAdapter.WeekViewHolder> {

    final String LOG_TAG = WeekViewAdapter.class.getSimpleName();

    int[] test = {1, 2, 3, 4, 5, 6, 7};

    private int hearderTextColor, currentDayTextColor, pastDayTextColor;

    private ArrayList<WeekItem> week_list = CalendarManager.getInstance().getWeekList();

    public WeekViewAdapter(int hearderTextColor, int currentDayTextColor, int pastDayTextColor) {
        this.hearderTextColor = hearderTextColor;
        this.currentDayTextColor = currentDayTextColor;
        this.pastDayTextColor = pastDayTextColor;
    }

    @Override
    public WeekViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.week_view, parent, false);
        return new WeekViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WeekViewHolder holder, int position) {
        WeekItem weekItem = week_list.get(position);
        holder.onBind(weekItem);
    }

    @Override
    public int getItemCount() {
        return week_list.size();
    }


    /**
     * ViewHolder for the adapter
     */
    class WeekViewHolder extends RecyclerView.ViewHolder {

        private ArrayList<LinearLayout> dayview_list; //List contains layout for each day in the week

        @BindView(R.id.week_view)
        LinearLayout week_view;

        @BindView(R.id.dayview_month_label)
        TextView month_label;

        @BindView(R.id.dayview_content)
        FrameLayout dayView_content;

        public WeekViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            setDayViewList(week_view);
        }

        private void setDayViewList(LinearLayout week_view) {
            dayview_list = new ArrayList<>();
            for (int i = 0; i < week_view.getChildCount(); i++) {
                dayview_list.add((LinearLayout) week_view.getChildAt(i));
            }
        }


        void onBind(WeekItem weekItem) {

            ArrayList<DayItem> day_list = weekItem.getDayItems();

            for (int i = 0; i < dayview_list.size(); i++) {

                //get day item
                final DayItem dayItem = day_list.get(i);

                //get layout for each day view
                LinearLayout dayview = dayview_list.get(i);
                //Bind the day_view layout subcomponent of each itemview
                TextView day_label = dayview.findViewById(R.id.dayview_day_label);
                TextView month_label = dayview.findViewById(R.id.dayview_month_label);
                View circle = dayview.findViewById(R.id.dayview_circle_selected);

                //clear used settings to avoid error
                month_label.setVisibility(View.GONE);
                circle.setVisibility(View.GONE);
                day_label.setTypeface(null, Typeface.NORMAL);
                month_label.setTypeface(null, Typeface.NORMAL);

                //Send Click event
                dayview.setOnClickListener(view -> {
                    EventBus.getInstance().send(new Events.DayClickedEvent(dayItem));
                    CalendarManager.getInstance().setSelectedItem(dayItem);
                });

                day_label.setText(dayItem.getDayOfTheMonth() + "");
                //set text color
                if (dayItem.getDate().before(CalendarManager.getInstance().getToday())) {
                    day_label.setTextColor(pastDayTextColor);
                    month_label.setTextColor(pastDayTextColor);
                } else {
                    day_label.setTextColor(currentDayTextColor);
                    month_label.setTextColor(currentDayTextColor);
                }

                //if selected, show the Selected circle
                if (dayItem.isSelected()) {
                    circle.setVisibility(View.VISIBLE);
                    circle.setBackgroundResource(R.drawable.dayview_circle_selected);
                    //if it is today, show blue circle
                } else if (dayItem.isToday()) {
                    circle.setVisibility(View.VISIBLE);
                    circle.setBackgroundResource(R.drawable.dayview_circle_current);
                }
                //show month label for 1st day every month
                else if (dayItem.isFirstDayOfTheMonth()) {
                    month_label.setVisibility(View.VISIBLE);
                    month_label.setText(dayItem.getMonth());
                    //bold text
                    month_label.setTypeface(null, Typeface.BOLD);
                    day_label.setTypeface(null, Typeface.BOLD);
                    //System.err.println("Month setted for" + dayItem.toString());
                }

                //When the 15th of the month is displayed on the screen, it indicates that this month's dayitem occupies most of the CalendarView.
                //Set the month information of the Title column to this month of the 15th
                if (dayItem.getDayOfTheMonth() == 15) {
                    EventBus.getInstance().send(new Events.MonthChangeEvent(dayItem.getmMonthFullName()));
                }
            }
        }
    }
}
