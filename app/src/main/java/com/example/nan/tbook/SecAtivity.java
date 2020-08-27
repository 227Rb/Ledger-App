package com.example.nan.tbook;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.nan.tbook.CustomView.MyAppTitle;
import com.example.nan.tbook.Data.GlobalUtil;
import com.example.nan.tbook.Data.TData;

import java.util.ArrayList;
import java.util.List;

public class SecAtivity extends AppCompatActivity {
    RecyclerView accountView;
    AccoutAdapet adapet;
    Button addAccount;
    TextView amouttAllView;
    TextView amoutBorrowView;
    TextView amoutBorrowBugetView;
    TextView amoutRealView;
    private double amoutAll=0;
    private double amoutBorrow=0;
    private double amoutReal=0;
    private Context context;
    private List<TData> accountList = new ArrayList<TData>();
    TData budget=new TData();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ativity_sec);
        context=this;
        amouttAllView = findViewById(R.id.sec_amout_all);
        amoutBorrowView = findViewById(R.id.sec_amout_burrow);
        amoutRealView=findViewById(R.id.sec_amout_real);
        amoutBorrowBugetView=findViewById(R.id.sec_buget_burrow);

        initData();
        setMyAppTitle();
        initRecyclerView();
        initButton();


        amouttAllView.setText(amoutAll+"");
        amoutBorrowView.setText("-"+amoutBorrow);
        amoutRealView.setText(""+(amoutAll+GlobalUtil.getInstance().tDatabaseHeiper.readRealRecord()+amoutBorrow));
        amoutBorrowBugetView.setText(""+GlobalUtil.getInstance().tDatabaseHeiper.readBorrowBuget());

    }

    private void initButton(){
        addAccount=findViewById(R.id.sec_add_account);


        addAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SecAtivity.this,ThirdAcitivity.class);
                startActivity(intent);

            }
        });

    }

    private void initData(){
        accountList= GlobalUtil.getInstance().tDatabaseHeiper.readRealBuget();
        for(int i=0;i<accountList.size();i++){
            if(accountList.get(i).getPayWay()!=3){
                amoutAll=amoutAll+accountList.get(i).getAmout();
            }

        }

        for(int i=0;i<GlobalUtil.getInstance().tDatabaseHeiper.readBorrow().size();i++){
            amoutBorrow=amoutBorrow+   GlobalUtil.getInstance().tDatabaseHeiper.readBorrow().get(i).getAmout();
        }

        for(int i=0;i<GlobalUtil.getInstance().tDatabaseHeiper.readBorrowBugetObject().size();i++){
            accountList.add(GlobalUtil.getInstance().tDatabaseHeiper.readBorrowBugetObject().get(i));
        }



    }


    private void initRecyclerView(){
        accountView = findViewById(R.id.sec_accout_list);
        //设置禁止增加Item时重绘重测Rec的尺寸,优化使用----即写死RecleyView的尺寸,item不可动态扩充或减少他的尺寸
//        accountView.setHasFixedSize(true);
        LinearLayoutManager layout = new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false);
        accountView.setLayoutManager(layout);
        adapet = new AccoutAdapet(accountList);
        accountView.setAdapter(adapet);

        accountView.addOnItemTouchListener(new RecyclerViewClickListener(context, accountView, new RecyclerViewClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
            }

            @Override
            public void onItemLongClick(View view, int position) {
                adapet.setLongChilk(true);
                adapet.notifyDataSetChanged();
            }
        }));
        
    }


    private void setMyAppTitle()
    {
        ActionBar actionBar= getSupportActionBar();
        if(actionBar!=null){
            actionBar.hide();
        }

        MyAppTitle mNewAppTitle = (MyAppTitle) this.findViewById(R.id.myNewAppTitle);

        mNewAppTitle.initViewsVisible(true, false, false, false);

        mNewAppTitle.setAppTitle("我的账户");
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
    protected void onResume() {
        if(accountList.size()!=GlobalUtil.getInstance().tDatabaseHeiper.readRealBuget().size()){
            adapet.setmRecordData(GlobalUtil.getInstance().tDatabaseHeiper.readRealBuget());
            adapet.notifyDataSetChanged();

            accountList= GlobalUtil.getInstance().tDatabaseHeiper.readRealBuget();

            amoutAll=0;
            for(int i=0;i<accountList.size();i++){
                if(accountList.get(i).getPayWay()!=3){
                    amoutAll=amoutAll+accountList.get(i).getAmout();
                }
            }
            amouttAllView.setText(amoutAll+" ");
        }

        amoutRealView.setText(""+(amoutAll+GlobalUtil.getInstance().tDatabaseHeiper.readRealRecord()+amoutBorrow));
        super.onResume();
    }
}
