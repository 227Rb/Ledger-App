package com.example.nan.tbook.Calender.behavior;



import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.example.nan.tbook.Calender.model.CalendarDate;
import com.example.nan.tbook.Calender.view.MonthPager;
import com.example.nan.tbook.Calender.Utils;
import com.example.nan.tbook.Calender.component.CalendarViewAdapter;

public class MonthPagerBehavior extends CoordinatorLayout.Behavior<MonthPager> {
    private int top = 0;
    private int touchSlop = 1;
    private int offsetY = 0;
    CalendarDate today = new CalendarDate();

    private  static int rowY ;
    private  static int cY ;
    private boolean isdown;




    public MonthPagerBehavior(){

    }

    public static int getRowY() {
        return rowY;
    }

    public static void setRowY(int rowY) {
        MonthPagerBehavior.rowY = rowY;
    }

    public static int getcY() {
        return cY;
    }

    public static void setcY(int cY) {
        MonthPagerBehavior.cY = cY;
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, MonthPager child, View dependency) {
        return dependency instanceof RecyclerView;
    }

    @Override
    public boolean onLayoutChild(CoordinatorLayout parent, MonthPager child, int layoutDirection) {
        parent.onLayoutChild(child, layoutDirection);
        child.offsetTopAndBottom(top);
        return true;
    }

    private float downX, downY, lastY, lastTop;
    private boolean isVerticalScroll;
    private boolean directionUpa;
    int row;

    /*
    * 触控响应-ViewPager块
    * 作用于整个过程
    */
    @Override
    public boolean onTouchEvent(CoordinatorLayout parent, MonthPager child, MotionEvent ev) {
        if (downY > lastTop) {
            return false;
        }

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                if (isVerticalScroll) {
                    if (ev.getY() > lastY) {
                        Utils.setScrollToBottom(true);
                        directionUpa = false;
                    } else {
                        Utils.setScrollToBottom(false);
                        directionUpa = true;
                    }

                    if (lastTop < cY / 2 + (cY/rowY) / 2) {
                        //这里表示本来是收缩状态
                        if (ev.getY() - downY <= 0 || Utils.loadTop() >= cY) {
                            //向上滑或者已展开了
                            Log.d("text9","c");
                            lastY = ev.getY();
                            return true;
                        }
                        if (ev.getY() - downY + child.getCellHeight() >= child.getViewHeight()) {
                            //将要滑过头了
                            Log.d("text9","b");
                            saveTop(child.getViewHeight());
                            Utils.scrollTo(parent, (RecyclerView) parent.getChildAt(1),cY, 10);
                            isVerticalScroll = false;
                        } else {
                            //正常下滑Utils.dpi2px(context,240)
                            Log.d("text9","a");
                            saveTop((int) (child.getCellHeight() + ((ev.getY() - downY))));
                                Utils.scroll(parent.getChildAt(1), (int) (lastY - ev.getY()), cY/rowY,cY);
                                isdown = true;

                        }
                    } else {
                        if (ev.getY() - downY >= 0 || Utils.loadTop() <= cY/rowY) {
                            Log.d("text9","3");
                            lastY = ev.getY();
                            return true;
                        }

                        if (ev.getY() - downY + cY <= cY/rowY) {
                            //将要滑过头了
                             Log.d("text9","2");
                            saveTop(cY/rowY);
                            Utils.scrollTo(parent, (RecyclerView) parent.getChildAt(1),cY/rowY, 10);
                            isVerticalScroll = false;
                        } else {
                            //正常上滑
                            Log.d("text9","1");
                            saveTop((int) (cY + ((ev.getY() - downY))));
                            Utils.scroll(parent.getChildAt(1), (int) (lastY - ev.getY()),
                                    cY/rowY, cY);
                            Log.d("text9","1"+" "+cY+" "+rowY);
                            isdown = false;

                        }
                    }

                    lastY = ev.getY();
                    return true;
                }
                break;

            /*
             *锁定滑动最后停留的位置
             */
            case MotionEvent.ACTION_UP:
                if (isVerticalScroll) {

                    child.setScrollable(true);

                    CalendarViewAdapter calendarViewAdapter =
                            (CalendarViewAdapter) child.getAdapter();
                    if (calendarViewAdapter != null) {
                        if (directionUpa) {
                            Utils.setScrollToBottom(true);
                            Log.d("textSc1","a1 "+child.getRowIndex());


                            calendarViewAdapter.switchToWeek(child.getRowIndex());
                            Utils.scrollTo(parent, (RecyclerView) parent.getChildAt(1), cY/rowY, 300);
                        } else {
                            Utils.setScrollToBottom(false);
                            calendarViewAdapter.switchToMonth();
                            Utils.scrollTo(parent, (RecyclerView) parent.getChildAt(1), cY, 300);

                        }
                    }

                    isVerticalScroll = false;
                    return true;
                }
                break;
        }
        return false;
    }

    private void saveTop(int top) {
        Utils.saveTop(top);
    }



    @Override
    public boolean onInterceptTouchEvent(CoordinatorLayout parent, MonthPager child, MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = ev.getX();
                downY = ev.getY();
                lastTop = Utils.loadTop();
                lastY = downY;
                break;
            case MotionEvent.ACTION_MOVE:
                if (downY > lastTop) {
                    return false;
                }
                if (Math.abs(ev.getY() - downY) > 25 && Math.abs(ev.getX() - downX) <= 25
                        && !isVerticalScroll) {
                    isVerticalScroll = true;
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                if (isVerticalScroll) {
                    isVerticalScroll = false;
                    return true;
                }
                break;
        }
        return isVerticalScroll;
    }

    private int dependentViewTop = -1;



    /*触摸监控-Coord块
    * 会与touch冲突
    * */
    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, MonthPager child, View dependency) {
        CalendarViewAdapter calendarViewAdapter = (CalendarViewAdapter) child.getAdapter();
        //dependency对其依赖的view(本例依赖的view是RecycleView)
        if (dependentViewTop != -1) {
            //dy view将要减少的量 = 隐藏得高度 Y 轴 为负时上移
            int dy = dependency.getTop() - dependentViewTop;
            int top = child.getTop();

            if (dy > touchSlop&&dependentViewTop<525) {
                calendarViewAdapter.switchToMonth();
                isdown = true;
            } else if (dy < -touchSlop&&dependentViewTop<=525) {
                    calendarViewAdapter.notifyDataChanged(calendarViewAdapter.getSelectData());
                    calendarViewAdapter.switchToWeek(child.getRowIndex());
                isdown = false;
            }


            if (dy > -top) {
                dy = -top;

            }

            if (dy < -top - child.getTopMovableDistance()) {
                dy = -top - child.getTopMovableDistance();

            }

            child.offsetTopAndBottom(dy);


        }

        dependentViewTop = dependency.getTop();


        if(!isdown&&rowY==6){
            if(child.getRowIndex()==5&&child.getTop()<-400){
                child.setTop(-525);
            }
        }


        top = child.getTop();

        if (offsetY > cY/rowY) {

            calendarViewAdapter.switchToMonth();
        }
        if (offsetY < -child.getCellHeight()) {
            calendarViewAdapter.switchToWeek(child.getRowIndex());
        }

        if (!isdown&&dependentViewTop > cY/rowY - 24
                && dependentViewTop < cY/rowY + 24
                && top > -touchSlop - child.getTopMovableDistance()
                && top < touchSlop - child.getTopMovableDistance()
                ) {
            Utils.setScrollToBottom(true);
            calendarViewAdapter.switchToWeek(child.getRowIndex());
            Log.d("textSc1","A "+top);
            offsetY = 0;
        }
        if (isdown&&dependentViewTop > cY- 24
                && dependentViewTop < cY + 24
                && top < touchSlop
                && top > -touchSlop) {
            Utils.setScrollToBottom(false);
            calendarViewAdapter.switchToMonth();
            offsetY = 0;
        }


        return true;
        // TODO: 16/12/8 dy为负时表示向上滑动，dy为正时表示向下滑动，dy为零时表示滑动停止
    }
}
