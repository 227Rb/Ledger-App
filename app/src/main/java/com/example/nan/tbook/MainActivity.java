package com.example.nan.tbook;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.example.nan.tbook.Calender.behavior.MonthPagerBehavior;
import com.example.nan.tbook.Calender.view.Calendar;
import com.example.nan.tbook.Calender.component.CalendarAttr;
import com.example.nan.tbook.Calender.model.CalendarDate;
import com.example.nan.tbook.Calender.component.CalendarViewAdapter;
import com.example.nan.tbook.Calender.view.MonthPager;
import com.example.nan.tbook.Calender.interf.OnSelectDateListener;
import com.example.nan.tbook.Calender.Utils;
import com.example.nan.tbook.CustomView.CustomDayView;
import com.example.nan.tbook.Data.ExampleAdapet;
import com.example.nan.tbook.Data.GlobalUtil;
import com.example.nan.tbook.Data.TData;
import com.example.nan.tbook.CustomView.MyAppTitle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Context context;
    TextView tvYear;
    TextView tvMonth;
    TextView backToday;
    CoordinatorLayout content;
    MonthPager monthPager;
    RecyclerView recordView;
    MonthPagerBehavior monthPagerBehavior = new MonthPagerBehavior();
    FloatingActionButton floatingActionButton;
    BottomSheetBehavior mBottomSheetBehavior;
    ExampleAdapet adapet;
    LinearLayout bottomList;
    EditText amoutText;
    ImageView bottomAccont;
    RecyclerView categoryView;
    CategoyAdapet categoyAdapet;
    LinearLayout keyboard;
    LinearLayout categoryList;

    private ArrayList<Calendar> currentCalendars = new ArrayList<>();
    private CalendarViewAdapter calendarAdapter;
    private OnSelectDateListener onSelectDateListener;
    private int mCurrentPage = MonthPager.CURRENT_DAY_INDEX;
    private boolean initiated = false;
    private CalendarDate currentDate;
    private List<TData> recordList = new ArrayList<TData>();
    private FragmentManager manager = getSupportFragmentManager();
    private String userinput ="";
    private int recordType = 1;
    private int payWay = 1;
    private int updataPos;
    private boolean isOnUpdata=false;
    private long firstTime;// 记录点击返回时第一次的时间毫秒值
    private double dayAmout=0;
    private  TData selectTData= new TData();
    private int category=0;
    private String remark="";
    private int saveAmout=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;
        content = (CoordinatorLayout) findViewById(R.id.content);
        monthPager = (MonthPager) findViewById(R.id.calendar_view);
        tvYear = (TextView) findViewById(R.id.show_year_view);
        tvMonth = (TextView) findViewById(R.id.show_month_view);
        backToday = (TextView) findViewById(R.id.back_today_button);
        recordView = (RecyclerView) findViewById(R.id.list);
        floatingActionButton = (FloatingActionButton) findViewById(R.id.add_record);
        bottomList =(LinearLayout) findViewById(R.id.bottom_list);
        bottomAccont =(ImageView) findViewById(R.id.botton_accont);
        categoryView = (RecyclerView) findViewById(R.id.categoryList);
        keyboard=(LinearLayout) findViewById(R.id.keyboard);
        categoryList =(LinearLayout) findViewById(R.id.category);
        amoutText = (EditText)findViewById(R.id.input_amout);

        //生成数据库对象与表
        GlobalUtil.getInstance().setContext(this);
        GlobalUtil.getInstance().tDatabaseHeiper.getWritableDatabase();

        //初始化数据
        initTData();
        initRecyclerView();

        //初始化视图类
        setMyAppTitle();
        initcellHell();
        initCurrentDate();
        initCalendarView();
        initToolbarClickListener();
        initBottomSteed();
        initCategoryView();

        CustomDayView.setAmouts(GlobalUtil.getInstance().tDatabaseHeiper.getAvaliableDate(currentDate.toString()));

    }

    public void initCategoryView(){
      categoyAdapet = new CategoyAdapet(getApplicationContext());

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(),4);
        categoryView.setLayoutManager(gridLayoutManager);
        categoryView.setAdapter(categoyAdapet);

        categoyAdapet.setmOnCategoryOnClickListen(new CategoyAdapet.OnCategoryOnClickListen() {
            @Override
            public void OnClick(int position) {
                category=categoyAdapet.selectedID;
            }
        });

    }


    //初始化记录列表
    public void initRecyclerView(){
        recordView.setHasFixedSize(true);
        LinearLayoutManager layout = new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false);
        recordView.setLayoutManager(layout);
        adapet = new ExampleAdapet(recordList);
        recordView.setAdapter(adapet);
        recordView.setItemAnimator(new DefaultItemAnimator());

        recordView.addOnItemTouchListener(new RecyclerViewClickListener(this, recordView, new RecyclerViewClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }

            //长按处理
            @Override
            public void onItemLongClick(View view, int position) {
               adapet.initBeforeDo();
                if(position!=0){
                    adapet.setLongChilk(true);
                    adapet.notifyDataSetChanged();

                    calendarAdapter.switchToWeek(monthPager.getRowIndex());
                    Utils.scrollTo(content, recordView, Utils.dpi2px(context,40), 200);


                    isOnUpdata=true;
                }

            }
        }));

        adapet.setUpDataOnClickListener(new ExampleAdapet.upDataOnClickListener() {
            @Override
            public void onClick(int position) {
                updataKey(recordList.get(position));
                isOnUpdata=true;
                updataPos = position;
            }

            @Override
            public void onCanelClick(int position) {
                canelKey();
            }

            @Override
            public void onDel(int position) {
                CustomDayView.setAmouts(GlobalUtil.getInstance().tDatabaseHeiper.getAvaliableDate(currentDate.toString()));
                calendarAdapter.notifyDataChanged();
            }
        });

    }

    //关闭,清空键盘
    public void canelKey(){

            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            floatingActionButton.show();
            bottomList.setVisibility(View.VISIBLE);
        userinput="";
        amoutText.setText(userinput);
    }

    //打开点击记录的键盘
    public void updataKey(TData upRecord){
        bottomList.setVisibility(View.GONE);
        if (mBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_HIDDEN) {
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            floatingActionButton.hide(); }

       if((upRecord.getAmout()+"").contains(".0")){
           userinput=(upRecord.getAmout()+"").substring(0,(upRecord.getAmout()+"").length()-2);
       }else {
           userinput=upRecord.getAmout()+"";
       }
        amoutText.setText(userinput);

    }


    //底部滑动初始化
    public void  initBottomSteed(){
        View bottimView = findViewById(R.id.bottom_sheet);
        mBottomSheetBehavior = BottomSheetBehavior.from(bottimView);

        int peekHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 250, getResources().getDisplayMetrics());
        mBottomSheetBehavior.setPeekHeight(peekHeight);//设置最小高度
        mBottomSheetBehavior.setHideable(true);//设置是否可隐藏
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);//设置当前为隐藏状态


        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendarAdapter.notifyDataChanged(calendarAdapter.getSelectData());
                calendarAdapter.switchToWeek(monthPager.getRowIndex());
                Utils.scrollTo(content, recordView, Utils.dpi2px(context,40), 200);

                //根据状态不同显示隐藏
                if (mBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_HIDDEN) {
                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    //隐藏按钮
                    floatingActionButton.hide();
                    bottomList.setVisibility(View.GONE);
                }
//                else if (mBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
//                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
//                }
            }
        });

        bottomAccont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,SecAtivity.class);
                startActivity(intent);

            }
        });


        initSpinner();
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && !initiated) {
//            refreshMonthPager();
            initiated = true;
        }
    }

    /*
    * 如果你想以周模式启动你的日历，请在onResume是调用
    * Utils.scrollTo(content, recordView, monthPager.getCellHeight(), 200);
    * calendarAdapter.switchToWeek(monthPager.getRowIndex());
    * */
    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * 初始化对应功能的listener--按键响应
     * @return void
     */
    private void initToolbarClickListener() {
        backToday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickBackToDayBtn();
            }
        });

        final ImageView outType=findViewById(R.id.keyboard_out);
        final ImageView inType=findViewById(R.id.keyboard_in);
        findViewById(R.id.keyboard_one).setOnClickListener(this);
        findViewById(R.id.keyboard_two).setOnClickListener(this);
        findViewById(R.id.keyboard_three).setOnClickListener(this);
        findViewById(R.id.keyboard_four).setOnClickListener(this);
        findViewById(R.id.keyboard_five).setOnClickListener(this);
        findViewById(R.id.keyboard_six).setOnClickListener(this);
        findViewById(R.id.keyboard_seven).setOnClickListener(this);
        findViewById(R.id.keyboard_eight).setOnClickListener(this);
        findViewById(R.id.keyboard_nine).setOnClickListener(this);
        findViewById(R.id.keyboard_zero).setOnClickListener(this);

        findViewById(R.id.keyboard_dian).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userinput==""){
                    userinput="0.";
                }
                if(!userinput.contains(".")){
                    userinput+=".";
                    updataAmoutText();
                }

            }
        });

        findViewById(R.id.keyboard_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(userinput.length()>1){
                    userinput = userinput.substring(0,userinput.length()-1);
                    if(userinput.charAt(userinput.length()-1)=='.'){
                        userinput = userinput.substring(0,userinput.length()-1);
                    }
                }else{
                   userinput="";
                }
                updataAmoutText();
            }
        });

        findViewById(R.id.keyboard_ok).setOnClickListener(new View.OnClickListener() {
            TData data;
            @Override
            public void onClick(View v) {
                if(userinput.equals("")||userinput.equals("0")||userinput.equals("0.")){
                    Toast.makeText(MainActivity.this,"请输入数据",Toast.LENGTH_SHORT).show();
                }else{

                    if(isOnUpdata){
                        if(recordList.size()==adapet.getbeforeDo()&&recordList.size()!=1){
                            data=recordList.get(updataPos);
                            data.setAmout(Double.valueOf(amoutText.getText().toString()));
                            data.setRecordType(recordType);
                            data.setPayWay(payWay);
                            data.setCategoy(category);
                            //修改
                             GlobalUtil.getInstance().tDatabaseHeiper.upRecord(adapet.getmRecordData().get(updataPos).getUuid(),data);

//                             recordList.set(updataPos,data);
//                             recordList.get(0).setAmout(adapet.getmRecordData().get(0).getAmout());
                             adapet.updata(updataPos,data);

                        }else{
                            userinput="";
                            updataAmoutText();
                            Toast.makeText(MainActivity.this,"您已经删除这条记录了喔",Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        data= greateTData(amoutText.getText().toString(),recordType,payWay);
                        GlobalUtil.getInstance().tDatabaseHeiper.addRecord(data);
                        adapet.addData(1,data);
                        recordView.getLayoutManager().scrollToPosition(1);

                        //添加到数据库

                    }
                    isOnUpdata=false;
                    userinput="";
                    category=0;
                    updataAmoutText();
                    CustomDayView.setAmouts(GlobalUtil.getInstance().tDatabaseHeiper.getAvaliableDate(currentDate.toString()));
                    calendarAdapter.notifyDataChanged();
                }

            }
        });

        findViewById(R.id.keyboard_tag).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               keyboard.setVisibility(View.GONE);
               categoryList.setVisibility(View.VISIBLE);
               amoutText.setFocusable(true);
                amoutText.setFocusableInTouchMode(true);

               saveAmout=Integer.valueOf(amoutText.getText().toString());
               if(remark.equals("")){
                   amoutText.setText("可输入明细");
                   amoutText.setTextColor(Color.parseColor("#F3F1F1"));
               }else{
                   amoutText.setText(remark);

               }
//               amoutText.requestFocus();
//                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                imm.showSoftInput(amoutText,0);

               if(recordType==1){
                   categoyAdapet.setCellList(GlobalUtil.getInstance().costRes);
                   categoyAdapet.notifyDataSetChanged();
                }else{
                   categoyAdapet.setCellList(GlobalUtil.getInstance().earnRes);
                   categoyAdapet.notifyDataSetChanged();
               }
            }
        });


        amoutText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(amoutText.getText().toString().equals("可输入明细")){
                   amoutText.setText("");
                   amoutText.setTextColor(Color.parseColor("#000000"));
               }

            }
        });

        findViewById(R.id.keyboard_cate_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                categoryList.setVisibility(View.GONE);
                keyboard.setVisibility(View.VISIBLE);
                amoutText.setFocusable(false);
                if(!amoutText.getText().toString().equals("可输入明细")){
                    remark=amoutText.getText().toString();
                }
                amoutText.setText(saveAmout+"");
                amoutText.setTextColor(Color.parseColor("#000000"));
            }
        });

        outType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recordType=1;
                outType.setBackgroundColor(Color.parseColor("#FFFFFF"));
                inType.setBackgroundColor(Color.parseColor("#F3F1F1"));
            }
        });

        inType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recordType=2;
                inType.setBackgroundColor(Color.parseColor("#FFFFFF"));
                outType.setBackgroundColor(Color.parseColor("#F3F1F1"));
            }
        });
    }

    /**
     * 初始化currentDate
     *
     * @return void
     */
    private void initCurrentDate() {
        currentDate = new CalendarDate();
        tvYear.setText(currentDate.getYear() + "年");
        tvMonth.setText(currentDate.getMonth() + "");
    }

    /**
     * 初始化CustomDayView，并作为CalendarViewAdapter的参数传入
     */
    private void initCalendarView() {
        initListener();
        CustomDayView customDayView = new CustomDayView(context, R.layout.custom_day);

        calendarAdapter = new CalendarViewAdapter(
                context,
                onSelectDateListener,
                CalendarAttr.WeekArrayType.Monday,
                customDayView);
        calendarAdapter.setOnCalendarTypeChangedListener(new CalendarViewAdapter.OnCalendarTypeChanged() {
            @Override
            public void onCalendarTypeChanged(CalendarAttr.CalendarType type) {
                recordView.scrollToPosition(0);
            }
        });
        initMarkData();
        initMonthPager();
    }

    /**
     * 初始化标记数据，HashMap的形式，可自定义
     * 如果存在异步的话，在使用setMarkData之后调用 calendarAdapter.notifyDataChanged();
     */
    private void initMarkData() {
        HashMap<String, String> markData = new HashMap<>();
        markData.put("2019-5-10", "1");
        markData.put("2019-5-15", "0");
        markData.put("2019-5-20", "1");
        markData.put("2017-6-11", "0");
        calendarAdapter.setMarkData(markData);
    }

    //初始化日历点击的监听
    private void initListener() {
        onSelectDateListener = new OnSelectDateListener() {
            @Override
            public void onSelectDate(CalendarDate date) {
                dayAmout=0;
                calendarAdapter.setSelctData(date);
                refreshClickDate(date);

                selectTData.setDate(date.toString());
                recordList.clear();


                for(int i=0;i<GlobalUtil.getInstance().tDatabaseHeiper.readRecord(date.toString()).size();i++){
                    recordList.add(GlobalUtil.getInstance().tDatabaseHeiper.readRecord(date.toString()).get(i));
                    if (recordList.get(i).getRecordType()==1){
                        dayAmout =dayAmout-recordList.get(i).getAmout();
                    }else{
                        dayAmout =dayAmout+recordList.get(i).getAmout();
                    }
                }
                selectTData.setAmout(dayAmout);
                recordList.add(0,selectTData);
                adapet.notifyDataSetChanged();
            }

            @Override
            public void onSelectOtherMonth(int offset) {
                //偏移量 -1表示刷新成上一个月数据 ， 1表示刷新成下一个月数据
                monthPager.selectOtherMonth(offset);
            }
        };
    }

    //更新日历的标题日期
    private void refreshClickDate(CalendarDate date) {
        currentDate = date;
        tvYear.setText(date.getYear() + "年");
        tvMonth.setText(date.getMonth() + "");
    }

    /**
     * 初始化monthPager，MonthPager继承自ViewPager
     * @return void
     */
    private void initMonthPager() {
        monthPager.setAdapter(calendarAdapter);
        monthPager.setCurrentItem(MonthPager.CURRENT_DAY_INDEX);
        monthPager.setPageTransformer(false, new ViewPager.PageTransformer() {
            @Override
            public void transformPage(View page, float position) {
                position = (float) Math.sqrt(1 - Math.abs(position));
                page.setAlpha(position);
            }
        });
        monthPager.addOnPageChangeListener(new MonthPager.OnPageChangeListener() {

            //从触摸开始一直执行到滑动结束
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }


            //内容(view)发生改变时调用
            @Override
            public void onPageSelected(int position) {
                mCurrentPage = position;
                currentCalendars = calendarAdapter.getPagers();
                if (currentCalendars.get(position % currentCalendars.size()) != null) {
                    CalendarDate date = currentCalendars.get(position % currentCalendars.size()).getSeedDate();
                    currentDate = date;
                    tvYear.setText(date.getYear() + "年");
                    tvMonth.setText(date.getMonth() + "");}
                CustomDayView.setAmouts(GlobalUtil.getInstance().tDatabaseHeiper.getAvaliableDate(currentDate.toString()));

            }

            //滑动结束后执行,更新日历牌的高度
            @Override
            public void onPageScrollStateChanged(int state) {

                if(calendarAdapter.getCalendarType()== CalendarAttr.CalendarType.MONTH){
                        Log.d("ldf1", "伸缩判断");
                        if ((Utils.getFirstDayWeekPosition(currentDate.getYear(), currentDate.getMonth(),null) +
                                Utils.getMonthDays(currentDate.getYear(), currentDate.getMonth())) >= 36) {

                            Utils.scrollTo(content, recordView, Utils.dpi2px(context,240), 200);
                            monthPagerBehavior.setcY(Utils.dpi2px(context,240));
                            monthPagerBehavior.setRowY(6);
                            Log.d("ldf1", "伸长");
                        }
                          else {
                             Utils.scrollTo(content, recordView, Utils.dpi2px(context, 200), 200);
                            monthPagerBehavior.setcY(Utils.dpi2px(context,200));
                            monthPagerBehavior.setRowY(5);
                        Log.d("ldf1", "收缩");

                    }
                }
            }
        });
    }


    //点击重置为今天的日历界面
    public void onClickBackToDayBtn() {
        CalendarDate today = new CalendarDate();
        calendarAdapter.setSelctData(today);
        calendarAdapter.notifyDataChanged(today);
        tvYear.setText(today.getYear() + "年");
        tvMonth.setText(today.getMonth() + "");
    }

    //初始化日历高度
    public void initcellHell(){
        CalendarDate today = new CalendarDate();
            if ((Utils.getFirstDayWeekPosition(today.getYear(), today.getMonth(),null) +
                    Utils.getMonthDays(today.getYear(), today.getMonth())) >= 36) {
                monthPager.setViewHeight(Utils.dpi2px(context, 240));
                monthPager.setCalendarRow(6);
                monthPagerBehavior.setcY(Utils.dpi2px(context,240));
                monthPagerBehavior.setRowY(6);
            }else {
                monthPager.setViewHeight(Utils.dpi2px(context, 200));
                monthPager.setCalendarRow(5);
                monthPagerBehavior.setcY(Utils.dpi2px(context,200));
                monthPagerBehavior.setRowY(5);
        }
    }


    //初始化记录数据
    private void initTData(){
        TData init = new TData();


        recordList = GlobalUtil.getInstance().tDatabaseHeiper.readRecord(init.getDate());

        for(int i=0;i<recordList.size();i++){
            if (recordList.get(i).getRecordType()==1){
                dayAmout =dayAmout-recordList.get(i).getAmout();
            }else{
                dayAmout =dayAmout+recordList.get(i).getAmout();
            }
        }

        init.setAmout(dayAmout);
        recordList.add(0,init);


    }


    //点击系统返回键
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){// 点击了返回按键
//            calendarAdapter.switchToMonth();
//                if ((Utils.getFirstDayWeekPosition(currentDate.getYear(), currentDate.getMonth(),null) +
//                        Utils.getMonthDays(currentDate.getYear(), currentDate.getMonth())) >= 36) {
//
//                    Utils.scrollTo(content, recordView, Utils.dpi2px(context,240), 200);
//                    monthPagerBehavior.setcY(Utils.dpi2px(context,240));
//                    monthPagerBehavior.setRowY(6);
//                    Log.d("ldf1", "伸长");
//            }
//            else {
//                Utils.scrollTo(content, recordView, Utils.dpi2px(context, 200), 200);
//                monthPagerBehavior.setcY(Utils.dpi2px(context,200));
//                monthPagerBehavior.setRowY(5);
//                Log.d("ldf1", "收缩");}
            adapet.setLongChilk(false);
            adapet.notifyDataSetChanged();
            categoryList.setVisibility(View.GONE);
            keyboard.setVisibility(View.VISIBLE);
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            floatingActionButton.show();
            bottomList.setVisibility(View.VISIBLE);
            if(manager.getBackStackEntryCount() != 0){
                manager.popBackStack();
            }else {
                exitApp(500);// 退出应用
            }
            return true;// 返回true，防止该事件继续向下传播
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 退出应用
     * 设置第二次点击退出的时间间隔
     */
    private void exitApp(long timeInterval) {
        // 第一次肯定会进入到if判断里面，然后把firstTime重新赋值当前的系统时间
        // 然后点击第二次的时候，当点击间隔时间小于2s，那么退出应用；反之不退出应用
        if (System.currentTimeMillis() - firstTime >= timeInterval) {
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            firstTime = System.currentTimeMillis();
        } else {
            finish();// 销毁当前activity
            System.exit(0);// 完全退出应用
        }
    }


    //点击键盘数字的响应
    @Override
    public void onClick(View v) {
        Button button = (Button)v;
        String input = button.getText().toString();

            if (userinput.contains(".") && userinput != null) {
                if (userinput.split("\\.").length == 1 || userinput.split("\\.")[1].length() < 2) {
                    userinput += input;
                }
            } else {
                if(userinput!=""&&userinput.charAt(0)=='0'){
                    userinput = input;
                }else{
                    userinput += input;
                }

            }

        updataAmoutText();
    }

    private void updataAmoutText(){
        if(userinput.equals("")){
            amoutText.setText("0");
        }else{
            amoutText.setText(userinput);
        }
    }

    //初始化支付方式的spinner
    private void initSpinner(){
        final Spinner pay_way =(Spinner) findViewById(R.id.in_pay_way);
        final SimpleAdapter spinnerAdapter = new SimpleAdapter(context,GlobalUtil.spinnerListData(),R.layout.spinner_item,new String[]{"pay_image"},new int[]{R.id.pay_image});
        pay_way.setAdapter(spinnerAdapter);
        pay_way.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                payWay=position+1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    //根据输入生成Tdata对象
    public TData greateTData(String amoutText,int recordType,int payWay){
        TData greatedData = new TData(currentDate);
        double input_amout =Double.valueOf(amoutText) ;
        greatedData.setCategoy(category);
        greatedData.setRemark(remark);
        greatedData.setPayWay(payWay);
        greatedData.setRecordType(recordType);
        greatedData.setAmout(input_amout);
        return greatedData;
    }

    //设置自定义标题
    private void setMyAppTitle() {
        ActionBar actionBar= getSupportActionBar();
        if(actionBar!=null){
            actionBar.hide();
        }

        MyAppTitle mNewAppTitle = (MyAppTitle) this.findViewById(R.id.myNewAppTitle);
        mNewAppTitle.initViewsVisible(false, true, false, false);

        findViewById(R.id.title_book).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent = new Intent(MainActivity.this,FourActivity.class);
               startActivity(intent);
            }
        });
    }


}

