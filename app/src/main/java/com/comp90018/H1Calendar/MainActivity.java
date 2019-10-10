package com.comp90018.H1Calendar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.comp90018.H1Calendar.EventView.DayEventView;
import com.comp90018.H1Calendar.EventView.WeekEventView;
import com.comp90018.H1Calendar.calendar.CalendarView;
import com.comp90018.H1Calendar.utils.CalendarManager;
import com.comp90018.H1Calendar.utils.EventBus;
import com.comp90018.H1Calendar.utils.Events;
import com.google.android.material.navigation.NavigationView;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionButton;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionHelper;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionLayout;
import com.wangjie.rapidfloatingactionbutton.contentimpl.labellist.RFACLabelItem;
import com.wangjie.rapidfloatingactionbutton.contentimpl.labellist.RapidFloatingActionContentLabelList;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

//import android.app.Activity;


public class MainActivity extends AppCompatActivity implements RapidFloatingActionContentLabelList.OnRapidFloatingActionContentLabelListListener {

    private DayEventView dayEventView;
    private WeekEventView weekEventView;
    private Button btnDWswitch;
    private ListView leftList;
    private NavigationView myNavigationView;


    //Region for basic UI
    //侧栏开关
    @BindView(R.id.drawer_layout)
    DrawerLayout drawer_layout;

    @OnClick(R.id.open_drawer_btn)
    public void open_drawer() {
        drawer_layout.openDrawer(GravityCompat.START);
    }

    //日历视图开关
    private boolean isOpen = true;

    @BindView(R.id.calendar_view)
    CalendarView calendar_view;

    @OnClick(R.id.hide_calendar)
    public void hide_calendar_view() {
        if (isOpen) {
            calendar_view.setVisibility(View.GONE);
            isOpen = false;
        } else {
            calendar_view.setVisibility(View.VISIBLE);
            isOpen = true;
        }
    }

    //Title_bar上的月份标签
    @BindView(R.id.title_month_label)
    TextView title_month_label;

    //Btn for back to today
    @OnClick(R.id.back_to_today)
    public void back_to_today(){
        EventBus.getInstance().send(new Events.BackToToday());
        calendar_view.scrollToDate(CalendarManager.getInstance().getToday(), CalendarManager.getInstance().getWeekList());
    }

    //CalendarView自定义控件的属性
    private int calendar_CurrentDayTextColor, calendar_PastDayTextColor, calendar_HeaderTextColor;

    //FAB 按钮
    private RapidFloatingActionHelper rfabHelper; //Helper for Fab

    @BindView(R.id.main_fab_layout)
    RapidFloatingActionLayout main_fab_layout;

    @BindView(R.id.main_fab_button)
    RapidFloatingActionButton main_fab_button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_drawerlayout);
        ButterKnife.bind(this);

        setCalendarInfo();
        initCalendarView();
        setMonthLabel();
        init_FAB();


        dayEventView = new DayEventView();
        weekEventView = new WeekEventView();
        myNavigationView = this.findViewById(R.id.navigation);
        if (myNavigationView != null) {
            myNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    switch(menuItem.getItemId()){
                        case R.id.dayview:
                            getSupportFragmentManager().beginTransaction().replace(R.id.Event_container, dayEventView).commitAllowingStateLoss();
                            drawer_layout.closeDrawers();
                            break;
                        case R.id.weeklyview:
                            getSupportFragmentManager().beginTransaction().replace(R.id.Event_container, weekEventView).commitAllowingStateLoss();
                            drawer_layout.closeDrawers();
                            break;
                        default:
                            break;
                    }
                    return false;
                }
            });
        }
        getSupportFragmentManager().beginTransaction().add(R.id.Event_container, dayEventView).commitAllowingStateLoss();

        //可以在这里创建数据库

    }

    //region CalendarView Settings
    /**
     * set the data in the calendar
     */
    private void setCalendarInfo() {
        Calendar min = Calendar.getInstance();
        Calendar max = Calendar.getInstance();

        //前推10个月
        min.add(Calendar.MONTH, -10);
        min.add(Calendar.DAY_OF_MONTH, 1);

        //后推10个月
        max.add(Calendar.MONTH, 11);
        max.set(Calendar.DAY_OF_MONTH, 1);
        max.add(Calendar.DAY_OF_MONTH, -1);

        Locale locale = Locale.getDefault();

        CalendarManager.getInstance().initCalendar(min, max, locale);
    }

    /**
     * init the style, color, theme of the calendar
     */
    private void initCalendarView(){
        calendar_HeaderTextColor = getColor(R.color.calendar_text_header);
        calendar_CurrentDayTextColor = getColor(R.color.calendar_text_current_day);
        calendar_PastDayTextColor = getColor(R.color.calendar_text_past_day);

        calendar_view.init(calendar_HeaderTextColor,calendar_CurrentDayTextColor,calendar_PastDayTextColor);
    }
    //endregion

    //region Fab Setting
    private void init_FAB() {
        RapidFloatingActionContentLabelList rfaContent = new RapidFloatingActionContentLabelList(this);
        rfaContent.setOnRapidFloatingActionContentLabelListListener(this);
        List<RFACLabelItem> items = new ArrayList<>();
        items.add(new RFACLabelItem<Integer>()
                .setLabel("Add Event by Form")
                .setResId(R.drawable.ic_form)
                .setIconNormalColor(0xff283593)
                .setIconPressedColor(0xff283593)
                .setLabelColor(0xff283593)
                .setWrapper(0)
        );
        items.add(new RFACLabelItem<Integer>()
                .setLabel("Add Event by QR Code")
                .setResId(R.drawable.ic_qrcode)
                .setIconNormalColor(0xff056f00)
                .setIconPressedColor(0xff0d5302)
                .setLabelColor(0xff056f00)
                .setWrapper(1)
        );
        rfaContent
                .setItems(items)
                .setIconShadowRadius(5)
                .setIconShadowColor(0xff888888)
                .setIconShadowDy(5)
        ;
        rfabHelper = new RapidFloatingActionHelper(
                this,
                main_fab_layout,
                main_fab_button,
                rfaContent
        ).build();
    }


    @Override
    public void onRFACItemLabelClick(int position, RFACLabelItem item) {
    }

    /**
     * 控制点击FAB后，展开列表中按钮的click事件
     *
     * @param position button的序号
     * @param item     列表中button的item
     */
    @Override
    public void onRFACItemIconClick(int position, RFACLabelItem item) {
        switch (position) {
            case 0:
                Intent intent_to_form = new Intent();
                intent_to_form.setClass(this, AddFormScheduleActivity.class);
                intent_to_form.putExtra("type","addEvent");
                startActivity(intent_to_form);
                break;
            case 1:
                Intent intent_to_qrcode = new Intent();
                intent_to_qrcode.setClass(this, AddQRCodeScheduleActivity.class);
                startActivity(intent_to_qrcode);
                break;
            default:
                break;
        }


    }
    //endregion

    private void setMonthLabel(){
        EventBus.getInstance().getSubject().subscribe(event -> {
           if(event instanceof Events.MonthChangeEvent){
               title_month_label.setText(((Events.MonthChangeEvent)event).getMonthFullName());
           }
        });
    }
}

