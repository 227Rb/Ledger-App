package com.example.nan.tbook;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nan.tbook.CustomView.MyAppTitle;
import com.example.nan.tbook.Data.AccountResBean;
import com.example.nan.tbook.Data.GlobalUtil;
import com.example.nan.tbook.Data.TData;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class ThirdAcitivity extends AppCompatActivity implements View.OnClickListener {
    ImageView showtype;
    TextView showName;
    EditText inputName;
    EditText inputAmout;
    EditText inputRemark;
    LinearLayout thirdPart;
    LinearLayout secPart;
    RecyclerView accoutList;
    ImageButton remarkBack;


    private Context context;
    private List<AccountResBean>  listDate= new ArrayList<>();
    private PayWayAdapet adapet;
    int selected;
    int accountType=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_third);
        context =this;
        GlobalUtil.getInstance().setContext(context);
        thirdPart=findViewById(R.id.third_part);
        accoutList=findViewById(R.id.account_list);
        showtype = findViewById(R.id.third_show_type);
        showName = findViewById(R.id.third_show_name);
        inputName = findViewById(R.id.third_input_name);
        inputAmout = findViewById(R.id.third_input_amout);
        inputRemark = findViewById(R.id.third_input_remark);
        secPart=findViewById(R.id.sec_part);



        setMyAppTitle();
        initRecyclerView();
        initOnClick();


    }

    private void initRecyclerView(){
        LinearLayoutManager layout = new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false);
        accoutList.setLayoutManager(layout);
        adapet = new PayWayAdapet(listDate);
        accoutList.setAdapter(adapet);
        accoutList.setItemAnimator(new DefaultItemAnimator());
        adapet.setIermOnClickListener(new PayWayAdapet.ItemOnClickListener() {
            @Override
            public void onClick(int position) {
                selected=position;
                accoutList.setVisibility(View.GONE);
                thirdPart.setVisibility(View.VISIBLE);
                showtype.setImageResource(listDate.get(position).accountIcon);
                showName.setText(listDate.get(position).title);
                inputName.setText(listDate.get(position).title);

                if(listDate.size()==GlobalUtil.getInstance().onlineRes.size()){
                    accountType=position+1;                }

            }
        });

    }

    private  void initOnClick(){
        findViewById(R.id.account_icon1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                thirdPart.setVisibility(View.GONE);
                accoutList.setVisibility(View.VISIBLE);
                accountType= 4;
                listDate = GlobalUtil.getInstance().yinhangRes;
                adapet.setList(listDate);
                adapet.notifyDataSetChanged();

            }
        });

        findViewById(R.id.account_icon2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                thirdPart.setVisibility(View.GONE);
                accoutList.setVisibility(View.VISIBLE);
                listDate=GlobalUtil.getInstance().onlineRes;
                adapet.setList(listDate);
                adapet.notifyDataSetChanged();
            }
        });

        findViewById(R.id.third_input_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                secPart.setVisibility(View.VISIBLE);
                thirdPart.setVisibility(View.GONE);
            }
        });

        findViewById(R.id.account_icon3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accoutList.setVisibility(View.GONE);
                thirdPart.setVisibility(View.VISIBLE);
                accountType = 0;
            }
        });

        findViewById(R.id.third_input_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!inputAmout.getText().toString().equals("0")){

                    if(isInteger(inputAmout.getText().toString())){
                        TData budget = new TData();
                        budget.setAmout(Double.valueOf(inputAmout.getText().toString()));
                        budget.setRecordType(5);
                        budget.setPayWay(accountType);
                        budget.setRemark(inputName.getText().toString()+"-"+inputRemark.getText().toString());

                        GlobalUtil.getInstance().tDatabaseHeiper.addRecord(budget);
                        finish();
                    }else {
                        Toast.makeText(ThirdAcitivity.this,"你的输入有误",Toast.LENGTH_SHORT).show();
                    }

                }else{
                    Toast.makeText(ThirdAcitivity.this,"你的输入为空",Toast.LENGTH_SHORT).show();
                }


            }
        });

        inputName.setOnClickListener(this);
        inputAmout.setOnClickListener(this);
        inputRemark.setOnClickListener(this);
    }


    private void setMyAppTitle()
    {
        ActionBar actionBar= getSupportActionBar();
        if(actionBar!=null){
            actionBar.hide();
        }

        MyAppTitle mNewAppTitle = (MyAppTitle) this.findViewById(R.id.myNewAppTitle);

        mNewAppTitle.initViewsVisible(true, false, false, false);

        mNewAppTitle.setAppTitle("请选择账户类型");
        mNewAppTitle.setOnLeftButtonClickListener(new MyAppTitle.OnLeftButtonClickListener()
        {
            @Override
            public void onLeftButtonClick(View v)
            {
                onBackPressed();
            }
        });
    }

    @Override
    public void onClick(View v) {
        secPart.setVisibility(View.GONE);
        Log.d("Third","1");
    }

    public static boolean isInteger(String str) {
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }
}
