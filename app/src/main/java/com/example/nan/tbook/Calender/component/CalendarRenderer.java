package com.example.nan.tbook.Calender.component;

import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;

import com.example.nan.tbook.Calender.model.CalendarDate;
import com.example.nan.tbook.Calender.Const;
import com.example.nan.tbook.Calender.interf.IDayRenderer;
import com.example.nan.tbook.Calender.interf.OnSelectDateListener;
import com.example.nan.tbook.Calender.Utils;
import com.example.nan.tbook.Calender.view.Calendar;
import com.example.nan.tbook.Calender.view.Day;
import com.example.nan.tbook.Calender.view.Week;


/**
 * Created by ldf on 17/6/26.
 */

public class CalendarRenderer {
    private Week weeks[] = new Week[Const.TOTAL_ROW];    // 行数组，每个元素代表一行
    private Calendar calendar;
    private CalendarAttr attr;
    private IDayRenderer dayRenderer;
    private Context context;
    private OnSelectDateListener onSelectDateListener;    // 单元格点击回调事件
    private CalendarDate seedDate; //种子日期
    private CalendarDate selectedDate; //被选中的日期
    private int selectedRowIndex=5 ;
    private static int drawRow;

    public static void setDrawRow(int drawRow) {
        CalendarRenderer.drawRow = drawRow;
    }

    public CalendarRenderer(Calendar calendar, CalendarAttr attr, Context context) {
        this.calendar = calendar;
        this.attr = attr;
        this.context = context;
    }

    /**
     * 使用dayRenderer绘制每一天
     *
     * @return void
     */
    int count = 0;
    public void draw(Canvas canvas) {
        drawRow = 0;

        for (int row = 0; row < Const.TOTAL_ROW; row++) {
            if (weeks[row] != null) {
                count=0;
                for (int col = 0; col < Const.TOTAL_COL; col++) {
                    if (weeks[row].days[col] != null && weeks[row].days[col].getState() !=State.NOT) {
                        count++;
                        dayRenderer.drawDay(canvas, weeks[row].days[col]);
                        if(count==1){
                            drawRow++;
                        }
                    }
                }
            }
        }
    }

    /**
     * 点击某一天时刷新这一天的状态
     *监听事件
     * @return void
     */
        public void onClickDate(int col, int row) {
            if (col >= Const.TOTAL_COL || row >= Const.TOTAL_ROW || weeks[row].days[col].getState() ==State.NOT)                       //越界终止
                return;
            if (weeks[row] != null) {
                if (attr.getCalendarType() == CalendarAttr.CalendarType.MONTH) {        //检查是否为月模式
                    if (weeks[row].days[col].getState() == State.CURRENT_MONTH) {       //检查是不是当前月
                        weeks[row].days[col].setState(State.SELECT);                    //修改day对象的状态
                        selectedDate = weeks[row].days[col].getDate();                  //获得选中的日期
                        CalendarViewAdapter.saveSelectedDate(selectedDate);             //给适配器作为种子日期
                        onSelectDateListener.onSelectDate(selectedDate);            //单元格点击回调
                        seedDate = selectedDate;                                      //种子日期赋值  (本类的种子)
                    }
                    //点击上月 返回数据 与点击回调
                    else if (weeks[row].days[col].getState() == State.PAST_MONTH) {   //检查是不是上个月
                        selectedDate = weeks[row].days[col].getDate();
                        CalendarViewAdapter.saveSelectedDate(selectedDate);                 //
                        onSelectDateListener.onSelectOtherMonth(-1);
                        onSelectDateListener.onSelectDate(selectedDate);
                    }
                    //点击下月 返回数据 与点击回调
                    else if (weeks[row].days[col].getState() == State.NEXT_MONTH) {   //检查是不是当下个月
                        selectedDate = weeks[row].days[col].getDate();
                        CalendarViewAdapter.saveSelectedDate(selectedDate);
                        onSelectDateListener.onSelectOtherMonth(1);
                        onSelectDateListener.onSelectDate(selectedDate);
                    }
                }
                //周模式的响应
                else {
                    weeks[row].days[col].setState(State.SELECT);                        //直接设置为选中
                    selectedDate = weeks[row].days[col].getDate();
                    CalendarViewAdapter.saveSelectedDate(selectedDate);
                    onSelectDateListener.onSelectDate(selectedDate);
                    seedDate = selectedDate;
                }
            }
        }

    /**
     * 刷新指定行的周数据
     *
     * @param rowIndex  参数月所在年
     * @return void
     */
    public void updateWeek(int rowIndex) {
        CalendarDate currentWeekLastDay;                                                                //当前周的最后一天
        if (attr.getWeekArrayType() == CalendarAttr.WeekArrayType.Sunday) {                            //周日模式
            currentWeekLastDay = Utils.getSaturday(seedDate);                                           //获得星期六的日期
        } else {
            currentWeekLastDay = Utils.getSunday(seedDate);                                             //周一模式获得星期天的日期
        }

        int day = currentWeekLastDay.day;                                                                 //最后一天的day对象
        for (int i = Const.TOTAL_COL - 1; i >= 0; i--) {                                                //根据最后一个单元格倒推出一行
            CalendarDate date = currentWeekLastDay.modifyDay(day);
            if (weeks[rowIndex] == null) {
                weeks[rowIndex] = new Week(rowIndex);

            }
            if (weeks[rowIndex].days[i] != null) {
                if (date.equals(CalendarViewAdapter.loadSelectedDate())) {
                    weeks[rowIndex].days[i].setState(State.SELECT);
                    weeks[rowIndex].days[i].setDate(date);
                } else {
                    weeks[rowIndex].days[i].setState(State.CURRENT_MONTH);
                    //判断是否为跨月的信息
//                    if((weeks[rowIndex].days[i].getDate().getMonth()-CalendarViewAdapter.loadSelectedDate().getMonth())==-1){   //与选中日期对比
//                        weeks[rowIndex].days[i].setState(State.PAST_MONTH);
//                    } else if( (weeks[rowIndex].days[i].getDate().getMonth()-CalendarViewAdapter.loadSelectedDate().getMonth())==1){
//                        weeks[rowIndex].days[i].setState(State.NEXT_MONTH);
//                        Log.d("text8"," "+weeks[rowIndex].days[i].getDate().getDay()+" "+weeks[rowIndex].days[i].getState());
//                    }

                        weeks[rowIndex].days[i].setDate(date);
                }
            } else {
                if (date.equals(CalendarViewAdapter.loadSelectedDate())) {
                    weeks[rowIndex].days[i] = new Day(State.SELECT, date, rowIndex, i);
                } else {
                    weeks[rowIndex].days[i] = new Day(State.CURRENT_MONTH, date, rowIndex, i);
                }
            }
            day--;      //从周日减出一周的数据
        }
    }

    /**
     * 填充月数据
     *
     * @return void
     */
    private void instantiateMonth() {
        int lastMonthDays = Utils.getMonthDays(seedDate.year, seedDate.month - 1);    // 上个月的天数
        int currentMonthDays = Utils.getMonthDays(seedDate.year, seedDate.month);    // 当前月的天数
        int firstDayPosition = Utils.getFirstDayWeekPosition(                            //获得一号是周几
                seedDate.year,
                seedDate.month,
                attr.getWeekArrayType());
        int day = 0;

        for (int row = 0; row < Const.TOTAL_ROW; row++) {                               //循环产生行
            day = fillWeek(lastMonthDays, currentMonthDays, firstDayPosition, day, row);
        }
    }


    /**
     * 填充月中周数据
     *
     * @return void
     */
    private int fillWeek(int lastMonthDays,int currentMonthDays, int firstDayWeek, int day, int row) {
        for (int col = 0; col < Const.TOTAL_COL; col++) {                                          //循环产生列
            int position = col + row * Const.TOTAL_COL;// 单元格位置                               //日历表的单元格,0对应第一行第一列 41对应第6行第7咧
            if (position >= firstDayWeek && position < firstDayWeek + currentMonthDays) {
                day++;
                fillCurrentMonthDate(day, row, col);
            } else if (position < firstDayWeek) {
                    instantiateLastMonth(lastMonthDays, firstDayWeek, row, col, position);           //补上第一周的上月日期
            } else if (position >= firstDayWeek + currentMonthDays) {
                    instantiateNextMonth(currentMonthDays, firstDayWeek, row, col, position);       //补上最后一周的下月日期
            }
        }
        return day;
    }


    //填充日期数据的方法 对应输入当月的日期数据
    private void fillCurrentMonthDate(int day, int row, int col) {
        CalendarDate date = seedDate.modifyDay(day);                      //修改为对应单元格的日期,按调用关系第一个调用的day=FfistDayPsition
        if (weeks[row] == null) {
            weeks[row] = new Week(row);
        }
        if (weeks[row].days[col] != null) {
            if (date.equals(CalendarViewAdapter.loadSelectedDate())) {
                weeks[row].days[col].setDate(date);
                weeks[row].days[col].setState(State.SELECT);
            } else {
                weeks[row].days[col].setDate(date);
                weeks[row].days[col].setState(State.CURRENT_MONTH);

            }
        } else {
            if (date.equals(CalendarViewAdapter.loadSelectedDate())) {
                weeks[row].days[col] = new Day(State.SELECT, date, row, col);           //当日期为系统时间是,设置状态为选中                 //打开时调用,此时周日数组为空,切循环至当天时
            } else {
                weeks[row].days[col] = new Day(State.CURRENT_MONTH, date, row, col);   //打开时调用,此时周日数组为空
            }
        }

        if (date.equals(seedDate)) {
            selectedRowIndex = row;                                                 //得到选中日期的行数 以服务周视图
            Log.d("textSc2","A"+row);
        }
    }


    //画下个月的补足日期的方法
    private void instantiateNextMonth(int currentMonthDays, int firstDayWeek, int row, int col, int position) {
        CalendarDate date = new CalendarDate(
                seedDate.year,
                seedDate.month + 1,
                position - firstDayWeek - currentMonthDays + 1);
        if (weeks[row] == null) {
            weeks[row] = new Week(row);
        }
        if (weeks[row].days[col] != null) {
            weeks[row].days[col].setDate(date);
            weeks[row].days[col].setState(State.NOT);
        } else {
            weeks[row].days[col] = new Day(State.NOT, date, row, col);
        }
        // TODO: 17/6/27  当下一个月的天数大于七时，说明该月有六周
//        if(position - firstDayWeek - currentMonthDays + 1 >= 7) { //当下一个月的天数大于七时，说明该月有六周
//        }
    }


    //画上个月的补足日期的方法
    private void instantiateLastMonth(int lastMonthDays, int firstDayWeek, int row, int col, int position) {
        CalendarDate date = new CalendarDate(
                seedDate.year,
                seedDate.month - 1,
                lastMonthDays - (firstDayWeek - position - 1));                                         //计算对应的单月格的上个月的日期
        if (weeks[row] == null) {
            weeks[row] = new Week(row);
        }
        if (weeks[row].days[col] != null) {
            weeks[row].days[col].setDate(date);
            weeks[row].days[col].setState(State.NOT);
        } else {
            weeks[row].days[col] = new Day(State.NOT, date, row, col);
        }
    }

    /**
     * 根据种子日期孵化出本日历牌的数据
     *  主要设置种子日期
     * @return void
     */
    public void showDate(CalendarDate seedDate) {
        if (seedDate != null) {
            this.seedDate = seedDate;
        } else {
            this.seedDate = new CalendarDate(); //为空以当前日期为种子
        }
        update();
    }

    //真正的孵化方法 调用了填充月的方法 和重绘方法
    public void update() {
        instantiateMonth();
        calendar.invalidate();
    }


    //取消选择
    public void cancelSelectState() {
        for (int i = 0; i < Const.TOTAL_ROW; i++) {
            if (weeks[i] != null) {
                for (int j = 0; j < Const.TOTAL_COL; j++) {
                    if (weeks[i].days[j].getState() == State.SELECT) {          //给每一个人Day[]检查是否选中
                        weeks[i].days[j].setState(State.CURRENT_MONTH);        //选中取消
                        resetSelectedRowIndex();
                        break;
                    }
                }
            }
        }
    }

    //设置选择监听器
    public void setOnSelectDateListener(OnSelectDateListener onSelectDateListener) {
        this.onSelectDateListener = onSelectDateListener;
    }


    public void setDayRenderer(IDayRenderer dayRenderer) {
        this.dayRenderer = dayRenderer;
    }

    public CalendarDate getSeedDate() {
        return this.seedDate;
    }

    //返回表格第一个日期的日期数据
    public CalendarDate getFirstDate() {
        Week week = weeks[0];
        Day day = week.days[0];

        return day.getDate();

    }

    //返回表格最后一个日期
    public CalendarDate getLastDate() {
        Week week = weeks[weeks.length - 1];
        Day day = week.days[week.days.length - 1];
        return day.getDate();
    }

    //重置被选择的行数
    public void resetSelectedRowIndex() {
        selectedRowIndex = 0;
    }

    //获得被选择的行数
    public int getSelectedRowIndex() {
        return selectedRowIndex;
    }

    //设置被选择的行数
    public void setSelectedRowIndex(int selectedRowIndex) {
        this.selectedRowIndex = selectedRowIndex;
    }

    public Calendar getCalendar() {
        return calendar;
    }

    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }

    public CalendarAttr getAttr() {
        return attr;
    }

    public void setAttr(CalendarAttr attr) {
        this.attr = attr;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public int getDrawRow(){
        return drawRow;
    }
}
