package com.example.nan.tbook.Data;

import android.util.Log;

import com.example.nan.tbook.Calender.Utils;
import com.example.nan.tbook.Calender.model.CalendarDate;

import java.io.Serializable;
import java.util.UUID;

/**
 * Created by Nan on 2019/5/28.
 */




public class TData implements Serializable {

    public enum RecordType{
        RECORD_TYPE_EXPENSE,
        RECORD_TYPE_INCOME,
        RECORD_TYPE_BORROW,
        RECORD_TYPE_REPAY,
        RECORD_TYPE_BUDGET,
        RECORD_TYPE_DEF;

        RecordType() {
        }
    }

    public enum PayWay{
        PAY_WAY_ZHIFUBAO,PAY_WAY_WEIXINZHIFU,PAY_WAY_HUABEI,PAY_WAY_CORD,PAY_WAY_DEF;
    }

    //加入默认值 防止崩溃
    private double amout =0;                                         //金额
    private RecordType recordType=RecordType.RECORD_TYPE_DEF;    //金额的类别
    private int categoy=0;                                     //消费种类
    private PayWay payWay=PayWay.PAY_WAY_DEF;                     //支付手段，后续添加更多自定义功能
    private String remark=" ";                                   //明细
    private String date="1970-01-01";                           //消费日期，默认添加纪录的时间
    private long timeSampe=0;                                    //记录时间，默认添加纪录的时间
    private String uuid;

    //默认点击添加时，使用该构造器
    public TData(){
        uuid= UUID.randomUUID().toString();
        date = Utils.getTime();
        timeSampe=System.currentTimeMillis();
        String shoppingTime = Utils.getHour(timeSampe);
        Log.d("TData",uuid+" "+date+" " );
    }

    //点击过去时间，使用该构造器
    public TData(CalendarDate lastDate){
        uuid= UUID.randomUUID().toString();
        this.date = lastDate.toString();
        timeSampe=System.currentTimeMillis();
        String shoppingTime = Utils.getHour(timeSampe);
        Log.d("TData",uuid+" "+date+" " +shoppingTime);
    }


    public double getAmout() {
        return amout;
    }

    public void setAmout(double amout) {
        this.amout = amout;
    }

    public int  getRecordType() {
        switch (this.recordType){
            case RECORD_TYPE_EXPENSE:
            {return  1;}

            case RECORD_TYPE_INCOME:
            { return  2;}

            case RECORD_TYPE_BORROW:
            { return  3;}

            case RECORD_TYPE_REPAY:
            {return  4;}


            case RECORD_TYPE_BUDGET:
            { return  5;}

            default:return 0;
        }
    }

    public void setRecordType(int recordType) {
       switch (recordType){
           case 1 :this.recordType= RecordType.RECORD_TYPE_EXPENSE;break;
           case 2 :this.recordType= RecordType.RECORD_TYPE_INCOME;break;
           case 3 :this.recordType= RecordType.RECORD_TYPE_BORROW;break;
           case 4 :this.recordType= RecordType.RECORD_TYPE_REPAY;break;
           case 5 :this.recordType= RecordType.RECORD_TYPE_BUDGET;break;
       }

    }

    public int getCategoy() {
        return categoy;
    }

    public void setCategoy(int categoy) {
        this.categoy = categoy;
    }

    public int getPayWay() {
        switch (this.payWay){
            case PAY_WAY_ZHIFUBAO:
                return 1;
            case PAY_WAY_WEIXINZHIFU:
                return 2;
            case PAY_WAY_HUABEI:
                return 3;
            case PAY_WAY_CORD:
                return 4;
            default:return 0;
        }
    }

    public void setPayWay(int payWay) {
        switch (payWay) {
            case 1:
                this.payWay = PayWay.PAY_WAY_ZHIFUBAO;
                break;
            case 2:
                this.payWay = PayWay.PAY_WAY_WEIXINZHIFU;
                break;
            case 3:
                this.payWay = PayWay.PAY_WAY_HUABEI;
                break;

            case 4:
                this.payWay = PayWay.PAY_WAY_CORD;

                default:this.payWay = PayWay.PAY_WAY_DEF;
        }
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public long getTimeSampe() {
        return timeSampe;
    }

    public void setTimeSampe(long timeSampe) {
        this.timeSampe = timeSampe;
    }
}
