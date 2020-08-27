package com.example.nan.tbook.Calender.model;

import android.util.Log;
import java.io.Serializable;
import java.util.Calendar;
import com.example.nan.tbook.Calender.Utils;

                                                         //本身为日历提供数据
public class CalendarDate implements Serializable {  //可序列化的类 用于传输 保存 实例的数据
    private static final long serialVersionUID = 1L;
    public int year;
    public int month;  //1~12
    public int day;





    public CalendarDate(int year, int month, int day) {  //构造法
        if (month > 12) {                                  //控制日历范围
            month = 1;
            year++;
        } else if (month < 1) {
            month = 12;
            year--;
        }
        this.year = year;
        this.month = month;
        this.day = day;
    }

    public CalendarDate() {
        this.year = Utils.getYear();
        this.month = Utils.getMonth();
        this.day = Utils.getDay();
    }

    /**
     * 通过修改当前Date对象的天数返回一个修改后的Date
     *
     * @return CalendarDate 修改后的日期
     */
    public CalendarDate modifyDay(int day) {
        int lastMonthDays = Utils.getMonthDays(this.year, this.month - 1);
        int currentMonthDays = Utils.getMonthDays(this.year, this.month);

        CalendarDate modifyDate;
        if (day > currentMonthDays) {                                               //控制在当前月内修改天数
            modifyDate = new CalendarDate(this.year, this.month, this.day);
            Log.e("ldf", "移动天数过大");
        } else if (day > 0) {
            modifyDate = new CalendarDate(this.year, this.month, day);
        } else if (day > 0 - lastMonthDays) {                                        //控制选中上月 与 当月连接的日期选择
            modifyDate = new CalendarDate(this.year, this.month - 1, lastMonthDays + day);
        } else {
            modifyDate = new CalendarDate(this.year, this.month, this.day);         //边界情况不改变--BUG?
            Log.e("ldf", "移动天数过大");
        }
        return modifyDate;
    }

    /**
     * 通过修改当前Date对象的所在周返回一个修改后的Date
     *
     * @return CalendarDate 修改后的日期
     */
    public CalendarDate modifyWeek(int offset) {
        CalendarDate result = new CalendarDate();
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month - 1);
        c.set(Calendar.DAY_OF_MONTH, day);
        c.add(Calendar.DATE, offset * 7);
        result.setYear(c.get(Calendar.YEAR));
        result.setMonth(c.get(Calendar.MONTH) + 1);
        result.setDay(c.get(Calendar.DATE));
        return result;
    }

    /**
     * 通过修改当前Date对象的所在月返回一个修改后的Date
     *改月
     * @return CalendarDate 修改后的日期
     */
    public CalendarDate modifyMonth(int offset) {
        CalendarDate result = new CalendarDate();
        int addToMonth = this.month + offset;
        if (offset > 0) {
            if (addToMonth > 12) {
                result.setYear(this.year + (addToMonth - 1) / 12);
                result.setMonth(addToMonth % 12 == 0 ? 12 : addToMonth % 12);
            } else {
                result.setYear(this.year);
                result.setMonth(addToMonth);
            }
        } else {
            if (addToMonth == 0) {
                result.setYear(this.year - 1);
                result.setMonth(12);
            } else if (addToMonth < 0) {
                result.setYear(this.year + addToMonth / 12 - 1);
                int month = 12 - Math.abs(addToMonth) % 12;
                result.setMonth(month == 0 ? 12 : month);
            } else {
                result.setYear(this.year);
                result.setMonth(addToMonth == 0 ? 12 : addToMonth);
            }
        }
        return result;
    }

    @Override
    public String toString() {
        if(month<10){
           if(day<10){
               return year + "-0" + month + "-0" + day;
           }else {
               return year + "-0" + month + "-" + day;
           }
        }else {
            if(day<10){
                return year + "-" + month + "-0" + day;
            }else {
                return year + "-" + month + "-" + day;
            }
        }
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public boolean equals(CalendarDate date) {
        if (date == null) {
            return false;
        }
        if (this.getYear() == date.getYear()
                && this.getMonth() == date.getMonth()
                && this.getDay() == date.getDay()) {
            return true;
        }
        return false;
    }

    public CalendarDate cloneSelf() {
        return new CalendarDate(year, month, day);
    }

}