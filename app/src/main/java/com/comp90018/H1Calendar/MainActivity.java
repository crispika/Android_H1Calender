package com.comp90018.H1Calendar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionButton;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionHelper;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionLayout;
import com.wangjie.rapidfloatingactionbutton.contentimpl.labellist.RFACLabelItem;
import com.wangjie.rapidfloatingactionbutton.contentimpl.labellist.RapidFloatingActionContentLabelList;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class MainActivity extends Activity implements RapidFloatingActionContentLabelList.OnRapidFloatingActionContentLabelListListener {

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
    LinearLayout calendar_view;

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
    //End Region

    //Region for Fab Layout
    private RapidFloatingActionHelper rfabHelper; //Helper for Fab

    @BindView(R.id.main_fab_layout)
    RapidFloatingActionLayout main_fab_layout;

    @BindView(R.id.main_fab_button)
    RapidFloatingActionButton main_fab_button;

    //End Region

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_drawerlayout);
        ButterKnife.bind(this);
        init_FAB();
    }

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
}

