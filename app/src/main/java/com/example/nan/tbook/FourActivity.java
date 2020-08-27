package com.example.nan.tbook;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.nan.tbook.CustomView.MyAppTitle;
import com.example.nan.tbook.Data.GlobalUtil;
import com.example.nan.tbook.Data.TDatabaseHeiper;

public class FourActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_four);
        GlobalUtil.getInstance().setContext(this);

        findViewById(R.id.four_1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//               GlobalUtil.getInstance().tDatabaseHeiper.setDbTable("Records");
            }
        });

        findViewById(R.id.four_2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//
//                TDatabaseHeiper.DB_TABLE="SecRecords";
//                GlobalUtil.getInstance().tDatabaseHeiper.getWritableDatabase();
            }
        });

//        setMyAppTitle();
    }


    private void setMyAppTitle() {
        ActionBar actionBar= getSupportActionBar();
        if(actionBar!=null){
            actionBar.hide();
        }

        MyAppTitle mNewAppTitle = (MyAppTitle) this.findViewById(R.id.myNewAppTitle);
        mNewAppTitle.initViewsVisible(false, true, false, false);


    }
}
