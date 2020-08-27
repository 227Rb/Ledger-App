package com.example.nan.tbook.Data;

import android.content.Context;
import android.util.Log;
import android.view.View;

import com.example.nan.tbook.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class GlobalUtil {

    private  static GlobalUtil instance;
    public  TDatabaseHeiper tDatabaseHeiper;
    private Context context;



    //单例模式
    public static GlobalUtil getInstance() {
        if(instance == null){
            instance=new GlobalUtil();
        }
        return instance;
    }


    //设置上下文同时,新建databaseHepler对象
    public void setContext(Context context) {
        this.context = context;
        tDatabaseHeiper = new TDatabaseHeiper(context,TDatabaseHeiper.DB_NAME,null,1 );

        if(costRes.size()==0){
            for(int i = 0;i<costTitle.length;i++){
                CategoryResBean res =new CategoryResBean();
                res.id = i;
                res.title =costTitle[i];
                res.resBigIcon = costIconBig[i];
                costRes.add(res);
            }
        }

        if(earnRes.size()==0){
            for(int i = 0;i<earnTitle.length;i++){
                CategoryResBean res =new CategoryResBean();
                res.id = i;
                res.title =earnTitle[i];
                res.resBigIcon = earnResBig[i];
                earnRes.add(res);
            }
        }

        if(yinhangRes.size()==0){
            for(int i = 0;i<accountTitle.length;i++){
                AccountResBean res =new AccountResBean(i,accountTitle[i],accountIcon[i]);
                yinhangRes.add(res);
            }
        }

        if(onlineRes.size()==0){
            for(int i = 0;i<onlineTitle.length;i++){
                AccountResBean res =new AccountResBean(i,onlineTitle[i],onlineIcon[i]);
                onlineRes.add(res);
            }
        }
    }

    public Context getContext() {
        return context;
    }

    public LinkedList<CategoryResBean> costRes = new LinkedList<>();
    public LinkedList<CategoryResBean> earnRes = new LinkedList<>();


    public static String[] costTitle ={"吃喝","交通","买菜","娱乐","借出","还贷","公务垫付","服饰鞋包","美妆美容","日用品","话费","医疗","自定义"};

    public static  int[] costIconBig = {R.drawable.big_eat,R.drawable.big_bus,R.drawable.big_food,R.drawable.big_game,R.drawable.big_borrrow,R.drawable.big_repay,
            R.drawable.big_business,R.drawable.big_clothing,R.drawable.big_makeup,R.drawable.big_dailynece,
            R.drawable.big_communication,R.drawable.big_medical,R.drawable.big_diy};


    public static String[] earnTitle ={"工资","投资","奖金","还款","兼职","红包","报销","自定义"};

    public static  int[] earnResBig = {R.drawable.big_salary,R.drawable.big_invest,R.drawable.big_honner,
            R.drawable.big_refund,R.drawable.big_parttime,R.drawable.big_redpacket,R.drawable.big_apply,R.drawable.big_diy};



    public static  List<Map<String, Object>> spinnerListData(){
        List<Map<String,Object>> pay_way_Data = new ArrayList<Map<String, Object>>();
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("pay_image", R.drawable.pay_alipay);
        pay_way_Data.add(map);

        HashMap<String, Object> map1 = new HashMap<String, Object>();
        map1.put("pay_image", R.drawable.pay_weixin);
        pay_way_Data.add(map1);

        HashMap<String, Object> map2 = new HashMap<String, Object>();
        map2.put("pay_image", R.drawable.pay_huabei);
        pay_way_Data.add(map2);

        HashMap<String, Object> map3 = new HashMap<String, Object>();
        map3.put("pay_image", R.drawable.pay_card);
        pay_way_Data.add(map3);

        HashMap<String, Object> map4 = new HashMap<String, Object>();
        map4.put("pay_image", R.drawable.pay_cash);
        pay_way_Data.add(map4);

        return pay_way_Data;
    }

    public static int findPayIcon(int i){
        int srcId;
        switch (i){
            case 1:srcId=R.drawable.pay_alipay;break;
            case 2:srcId=R.drawable.pay_weixin;break;
            case 3:srcId=R.drawable.pay_huabei;break;
            case 4:srcId=R.drawable.pay_card;break;

            default:;srcId=R.drawable.pay_cash;
        }
        return srcId;
    }

    public  static String findPayName(int i ){
        String payName;
        switch (i) {
            case 1:
                payName="支付宝";
                break;
            case 2:
                payName="微信";
                break;
            case 3:
                payName="花呗";
                break;

            case 4:
                payName="银行卡";

            default:payName="现金";
        }
        return  payName;
    }


    public LinkedList<AccountResBean> yinhangRes = new LinkedList<>();

    public static String[] accountTitle ={"建设银行","招商银行","农业银行",};

    public static  int[] accountIcon = {R.drawable.banl_jianshe,R.drawable.bank_zhoashan,R.drawable.bank_nongye};

    public LinkedList<AccountResBean> onlineRes = new LinkedList<>();

    public static String[] onlineTitle ={"支付宝","微信支付","花呗","其他",};

    public static  int[] onlineIcon = {R.drawable.pay_alipay,R.drawable.pay_weixin,R.drawable.pay_huabei,R.drawable.pay_online};


}
