package com.example.nan.tbook.Calender.interf;


import com.example.nan.tbook.Calender.model.CalendarDate;

/**
 * Created by ldf on 17/6/2.
 */

public interface OnSelectDateListener {
    void onSelectDate(CalendarDate date); //点击了一个日期

    void onSelectOtherMonth(int offset);//点击其它月份日期
}
