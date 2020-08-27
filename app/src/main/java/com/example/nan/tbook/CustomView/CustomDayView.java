package com.example.nan.tbook.CustomView;

import com.example.nan.tbook.Calender.model.CalendarDate;
import com.example.nan.tbook.Calender.view.DayView;
import com.example.nan.tbook.Calender.interf.IDayRenderer;
import com.example.nan.tbook.Calender.component.State;
import com.example.nan.tbook.Calender.Utils;
import com.example.nan.tbook.R;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Nan on 2019/5/20.
 */

public class CustomDayView extends DayView {


    private TextView dateTv;
    private ImageView marker;
    private View selectedBackground;
    private View todayBackground;
    private TextView dayAmoutView;
    private final CalendarDate today = new CalendarDate();
    static  ArrayList<Integer>amouts;

    public static ArrayList<Integer> getAmouts() {
        return amouts;
    }

    public static void setAmouts(ArrayList<Integer> Iamouts) {
       amouts = Iamouts;
    }


    /**
     * 构造器
     *
     * @param context 上下文
     * @param layoutResource 自定义DayView的layout资源
     */
    public CustomDayView(Context context, int layoutResource) {
        super(context, layoutResource);
        dateTv = (TextView) findViewById(R.id.date);
        marker = (ImageView) findViewById(R.id.maker);
        selectedBackground = findViewById(R.id.selected_background);
        todayBackground = findViewById(R.id.today_background);
        dayAmoutView=findViewById(R.id.day_amout);

    }


    //更新日历
    @Override
    public void refreshContent() {
        renderToday(day.getDate());
        renderSelect(day.getState());
        renderMarker(day.getDate(), day.getState());
        super.refreshContent();
    }


    //更新标记日期画面
    private void renderMarker(CalendarDate date, State state) {
        if (Utils.loadMarkData().containsKey(date.toString())) {
            if (state == State.SELECT || date.toString().equals(today.toString())) {
                marker.setVisibility(GONE);
            } else {
                marker.setVisibility(VISIBLE);
                if (Utils.loadMarkData().get(date.toString()).equals("0")) {
                    marker.setEnabled(true);
                } else {
                    marker.setEnabled(false);
                }
            }
        } else {
            marker.setVisibility(GONE);
        }
    }

    //根据日期的状态设置效果
    private void renderSelect(State state) {
        if (state == State.SELECT) {
            selectedBackground.setVisibility(VISIBLE);
            dateTv.setTextColor(Color.WHITE);
        } else if (state == State.NEXT_MONTH || state == State.PAST_MONTH) {
            selectedBackground.setVisibility(GONE);
            dateTv.setTextColor(Color.parseColor("#d5d5d5"));
        } else {
            selectedBackground.setVisibility(GONE);
            dateTv.setTextColor(Color.parseColor("#111111"));
        }
    }

    //为View写入data数据
    public  void  renderToday(CalendarDate date) {
        if (date != null) {
            if (date.equals(today)) {
                dateTv.setText(date.day+"");
                todayBackground.setVisibility(VISIBLE);
            } else {
                dateTv.setText(date.day + "");
                todayBackground.setVisibility(GONE);
            }
        }

            findDayAmout();

        if(day.getDayAmout()==0){
            dayAmoutView.setVisibility(View.GONE);
        }else{
            dayAmoutView.setVisibility(VISIBLE);
            dayAmoutView.setText(day.getDayAmout()+"");

            if(day.getDayAmout()<0){
                dayAmoutView.setTextColor(Color.RED);
            }else{
                dayAmoutView.setTextColor(Color.GREEN);

            }

        }
    }



    public void findDayAmout(){

//        (int)GlobalUtil.getInstance().tDatabaseHeiper.readRecordsAmout(day.getDate().toString());

//            day.setDayAmout((int)GlobalUtil.getInstance().tDatabaseHeiper.readRecordsAmout(day.getDate().toString()));

        day.setDayAmout(amouts.get(day.getDate().day));

    }

    @Override
    public IDayRenderer copy() {
        return new CustomDayView(context, layoutResource);
    }
}
