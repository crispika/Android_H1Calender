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

                //得到这一天的数据
                final DayItem dayItem = day_list.get(i);

                //得到每个itemView的day_view布局
                LinearLayout dayview = dayview_list.get(i);
                //绑定每个itemview 的day_view 布局子组件
                TextView day_label = dayview.findViewById(R.id.dayview_day_label);
                TextView month_label = dayview.findViewById(R.id.dayview_month_label);
                View circle = dayview.findViewById(R.id.dayview_circle_selected);

                //clear下方曾经用过的设置，下面重新设置，否则会因为view recycle而出现错误设置
                month_label.setVisibility(View.GONE);
                circle.setVisibility(View.GONE);
                day_label.setTypeface(null,Typeface.NORMAL);
                month_label.setTypeface(null,Typeface.NORMAL);

                //TODO to set click circle color for selected day
                //dayview.setOnClickListener(view ->);

                day_label.setText(dayItem.getDayOfTheMonth() + "");
                //设置字体颜色
                if (dayItem.getDate().before(CalendarManager.getInstance().getToday())) {
                    day_label.setTextColor(pastDayTextColor);
                    month_label.setTextColor(pastDayTextColor);
                } else {
                    day_label.setTextColor(currentDayTextColor);
                    month_label.setTextColor(currentDayTextColor);
                }
                //控制每月第一天显示month_label
                if (dayItem.isFirstDayOfTheMonth()) {
                    month_label.setVisibility(View.VISIBLE);
                    month_label.setText(dayItem.getMonth());
                    //字体加粗
                    month_label.setTypeface(null, Typeface.BOLD);
                    day_label.setTypeface(null,Typeface.BOLD);
                    //System.err.println("Month setted for" + dayItem.toString());
                }

                if (dayItem.isToday()){
                    circle.setVisibility(View.VISIBLE);
                    circle.setBackgroundResource(R.drawable.dayview_circle_current);
                }

            }
        }
    }
}
