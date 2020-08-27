package com.example.nan.tbook.Calender.component;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;


import com.example.nan.tbook.Calender.model.CalendarDate;
import com.example.nan.tbook.Calender.interf.IDayRenderer;
import com.example.nan.tbook.Calender.view.MonthPager;
import com.example.nan.tbook.Calender.interf.OnAdapterSelectListener;
import com.example.nan.tbook.Calender.interf.OnSelectDateListener;
import com.example.nan.tbook.Calender.Utils;
import com.example.nan.tbook.Calender.view.Calendar;

import java.util.ArrayList;
import java.util.HashMap;

public class CalendarViewAdapter extends PagerAdapter {                                             //为DayView生成的每个视图配置数据显示
    private static CalendarDate date = new CalendarDate();                                          //当天的日期
    private ArrayList<Calendar> calendars = new ArrayList<>();
    private int currentPosition = MonthPager.CURRENT_DAY_INDEX;
    private CalendarAttr.CalendarType calendarType = CalendarAttr.CalendarType.MONTH;
    private int rowCount = 0;
    private CalendarDate seedDate;
    private OnCalendarTypeChanged onCalendarTypeChangedListener;
    CalendarDate SelectData = new CalendarDate();


    public void setSelctData(CalendarDate data){
        this.SelectData = data;
    }

    public CalendarDate getSelectData() {
        return SelectData;
    }

    public void setSelectData(CalendarDate selectData) {
        SelectData = selectData;
    }

    //周排列方式 1：代表周日显示为本周的第一天
    //           0:代表周一显示为本周的第一天
    private CalendarAttr.WeekArrayType weekArrayType = CalendarAttr.WeekArrayType.Monday;

    public CalendarViewAdapter(Context context,
                               OnSelectDateListener onSelectDateListener,
                               CalendarAttr.WeekArrayType weekArrayType,
                               IDayRenderer dayView) {
        super();
        this.weekArrayType = weekArrayType;
        init(context, onSelectDateListener);        //初始化
        setCustomDayRenderer(dayView);              //使用CustomRenderer画日历
    }

    //保存静态的当日日期
    public static void saveSelectedDate(CalendarDate calendarDate) {
        date = calendarDate;
    }

    //载入静态的当日日期
    public static CalendarDate loadSelectedDate() {
        return date;
    }

    //calendar对象初始化
    private void init(Context context, OnSelectDateListener onSelectDateListener) {
        saveSelectedDate(new CalendarDate());

        //初始化的种子日期为今天
        seedDate = new CalendarDate();
        for (int i = 0; i < 3; i++) {
            CalendarAttr calendarAttr = new CalendarAttr();
            calendarAttr.setCalendarType(CalendarAttr.CalendarType.WEEK);                      //设置为周显示模式
            calendarAttr.setWeekArrayType(weekArrayType);                                   //设置第一天为周一的模式
            Calendar calendar = new Calendar(context, onSelectDateListener, calendarAttr);      //载入ATTR生成calendar对象
            calendar.setOnAdapterSelectListener(new OnAdapterSelectListener() {                 //设置对象的状态监听器
                @Override
                public void cancelSelectState() {
                    cancelOtherSelectState();
                }       //初始化取消全部选中

                @Override
                public void updateSelectState() {
//                    invalidateCurrentCalendar();                                      //bug
                }
            });
            calendars.add(calendar);                                                //添加3个calendar对象
        }
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        Log.e("ldf", "setPrimaryItem");
        super.setPrimaryItem(container, position, object);
        this.currentPosition = position;
    }

    //每次左右滑动执行,匹配对应的calendarView
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Log.e("ldf", "instantiateItem");
        if (position < 2) {
            return null;
        }
        Calendar calendar = calendars.get(position % calendars.size());
        if (calendarType == CalendarAttr.CalendarType.MONTH) {
            CalendarDate current = seedDate.modifyMonth(position - MonthPager.CURRENT_DAY_INDEX);
            current.setDay(1);//每月的种子日期都是1号
            calendar.showDate(current);
        } else {
            CalendarDate current = seedDate.modifyWeek(position - MonthPager.CURRENT_DAY_INDEX);
            if (weekArrayType == CalendarAttr.WeekArrayType.Sunday) {
                calendar.showDate(Utils.getSaturday(current));
            } else {
                calendar.showDate(Utils.getSunday(current));
            }//每周的种子日期为这一周的最后一天
            calendar.updateWeek(rowCount);
        }
        if (container.getChildCount() == calendars.size()) {
            container.removeView(calendars.get(position % 3));
        }
        if (container.getChildCount() < calendars.size()) {
            container.addView(calendar, 0);
        } else {
            container.addView(calendar, position % 3);
        }
        return calendar;
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((View) object);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(container);
    }

    public ArrayList<Calendar> getPagers() {
        return calendars;
    }

    public CalendarDate getFirstVisibleDate() {
        return calendars.get(currentPosition % 3).getFirstDate();
    }

    public CalendarDate getLastVisibleDate() {
        return calendars.get(currentPosition % 3).getLastDate();
    }


    //取消选择
    public void cancelOtherSelectState() {
        for (int i = 0; i < calendars.size(); i++) {
            Calendar calendar = calendars.get(i);
            calendar.cancelSelectState();
        }
    }


//    public void invalidateCurrentCalendar() {
//        for (int i = 0; i < calendars.size(); i++) {
//            Calendar calendar = calendars.get(i);
//            calendar.update();
//            if (calendar.getCalendarType() == CalendarAttr.CalendarType.WEEK) {
//                calendar.updateWeek(rowCount);
//            }
//        }
//    }

    //设置标记日期
    public void setMarkData(HashMap<String, String> markData) {
        Utils.setMarkData(markData);
        notifyDataChanged();
    }



    //使用月显示模式
    public void switchToMonth() {
        if (calendars != null && calendars.size() > 0 && calendarType != CalendarAttr.CalendarType.MONTH) { //有calender对象而不为月模式
            if (onCalendarTypeChangedListener != null) {
                onCalendarTypeChangedListener.onCalendarTypeChanged(CalendarAttr.CalendarType.MONTH);
            }
            calendarType = CalendarAttr.CalendarType.MONTH;
            MonthPager.CURRENT_DAY_INDEX = currentPosition;
            Calendar v = calendars.get(currentPosition % 3);//0
            seedDate = v.getSeedDate();

            Calendar v1 = calendars.get(currentPosition % 3);//0
            v1.switchCalendarType(CalendarAttr.CalendarType.MONTH);
            v1.showDate(seedDate);

            Calendar v2 = calendars.get((currentPosition - 1) % 3);//2
            v2.switchCalendarType(CalendarAttr.CalendarType.MONTH);
            CalendarDate last = seedDate.modifyMonth(-1);
            last.setDay(1);
            v2.showDate(last);

            Calendar v3 = calendars.get((currentPosition + 1) % 3);//1
            v3.switchCalendarType(CalendarAttr.CalendarType.MONTH);
            CalendarDate next = seedDate.modifyMonth(1);
            next.setDay(1);
            v3.showDate(next);
        }
    }


    //启用周显示模式
    public void switchToWeek(int rowIndex) {
        rowCount = rowIndex;
        if (calendars != null && calendars.size() > 0&& calendarType != CalendarAttr.CalendarType.WEEK ) {//
            if (onCalendarTypeChangedListener != null) {
                onCalendarTypeChangedListener.onCalendarTypeChanged(CalendarAttr.CalendarType.WEEK);
            }
            calendarType = CalendarAttr.CalendarType.WEEK;
            MonthPager.CURRENT_DAY_INDEX = currentPosition;
            Calendar v = calendars.get(currentPosition % 3);
            seedDate = v.getSeedDate();

            rowCount = v.getSelectedRowIndex();
            Calendar v1 = calendars.get(currentPosition % 3);
            v1.switchCalendarType(CalendarAttr.CalendarType.WEEK);
            v1.showDate(seedDate);
            v1.updateWeek(rowIndex);

            Calendar v2 = calendars.get((currentPosition - 1) % 3);
            v2.switchCalendarType(CalendarAttr.CalendarType.WEEK);
            CalendarDate last = seedDate.modifyWeek(-1);
            if (weekArrayType == CalendarAttr.WeekArrayType.Sunday) {
                v2.showDate(Utils.getSaturday(last));
            } else {
                v2.showDate(Utils.getSunday(last));
            }//每周的种子日期为这一周的最后一天
            v2.updateWeek(rowIndex);

            Calendar v3 = calendars.get((currentPosition + 1) % 3);
            v3.switchCalendarType(CalendarAttr.CalendarType.WEEK);
            CalendarDate next = seedDate.modifyWeek(1);
            if (weekArrayType == CalendarAttr.WeekArrayType.Sunday) {
                v3.showDate(Utils.getSaturday(next));
            } else {
                v3.showDate(Utils.getSunday(next));
            }//每周的种子日期为这一周的最后一天
            v3.updateWeek(rowIndex);
        }
    }

    //更新月
    public void notifyMonthDataChanged(CalendarDate date) {
        seedDate = date;
        refreshCalendar();
    }

    //更新日期,更新新的种子对象
    public void notifyDataChanged(CalendarDate date) {
        seedDate = date;
        saveSelectedDate(date);
        refreshCalendar();
    }

    //更新日期
    public void notifyDataChanged() {
        refreshCalendar();
    }

    //更新日历
    private void refreshCalendar() {
        if (calendarType == CalendarAttr.CalendarType.WEEK) {       //周模式
            MonthPager.CURRENT_DAY_INDEX = currentPosition;
            Calendar v1 = calendars.get(currentPosition % 3);
            v1.showDate(seedDate);
            v1.updateWeek(rowCount);

            Calendar v2 = calendars.get((currentPosition - 1) % 3);
            CalendarDate last = seedDate.modifyWeek(-1);
            if (weekArrayType == CalendarAttr.WeekArrayType.Sunday) {
                v2.showDate(Utils.getSaturday(last));
            } else {
                v2.showDate(Utils.getSunday(last));
            }
            v2.updateWeek(rowCount);

            Calendar v3 = calendars.get((currentPosition + 1) % 3);
            CalendarDate next = seedDate.modifyWeek(1);
            if (weekArrayType == CalendarAttr.WeekArrayType.Sunday) {
                v3.showDate(Utils.getSaturday(next));
            } else {
                v3.showDate(Utils.getSunday(next));
            }//每周的种子日期为这一周的最后一天
            v3.updateWeek(rowCount);
        } else {                                                        //月模式
            MonthPager.CURRENT_DAY_INDEX = currentPosition;

            Calendar v1 = calendars.get(currentPosition % 3);//0  1000%3
            v1.showDate(seedDate);
            Calendar v2 = calendars.get((currentPosition - 1) % 3);//2 999%3
            CalendarDate last = seedDate.modifyMonth(-1);         //上个月
            last.setDay(1);
            v2.showDate(last);                                      //以下个月一号为种子
            Calendar v3 = calendars.get((currentPosition + 1) % 3);//1  1001%3
            CalendarDate next = seedDate.modifyMonth(1);            //下个月
            next.setDay(1);
            v3.showDate(next);                                          //以下个月的一号为种子
        }
    }



    /**
     * 为每一个Calendar实例设置renderer对象
     *
     * @return void
     */
    public void setCustomDayRenderer(IDayRenderer dayRenderer) {
        Calendar c0 = calendars.get(0);
        c0.setDayRenderer(dayRenderer);

        Calendar c1 = calendars.get(1);
        c1.setDayRenderer(dayRenderer.copy());

        Calendar c2 = calendars.get(2);
        c2.setDayRenderer(dayRenderer.copy());
    }


    //获得当前周显示模式(周一/日在前)
    public CalendarAttr.WeekArrayType getWeekArrayType() {
        return weekArrayType;
    }

    public void setOnCalendarTypeChangedListener(OnCalendarTypeChanged onCalendarTypeChangedListener) {
        this.onCalendarTypeChangedListener = onCalendarTypeChangedListener;
    }

    //内部接口
    public interface OnCalendarTypeChanged {
        void onCalendarTypeChanged(CalendarAttr.CalendarType type);
    }


    public CalendarAttr.CalendarType getCalendarType() {
        return calendarType;
    }

    public void setCalendarTypeToMonth(){
         calendarType=CalendarAttr.CalendarType.MONTH;
    }

}

