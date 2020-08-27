package com.example.nan.tbook.Data;


import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.nan.tbook.SecAtivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by Nan on 2019/5/28.
 */

public class TDatabaseHeiper extends SQLiteOpenHelper {

    public static  final  String DB_NAME="TData.db";
    private static String DB_TABLE="Records";



    //SQL语句块新建表
    private static final  String CREATE_RECORD_DB = "create table " +DB_TABLE+" ("
            + "id integer primary key autoincrement, "
            + "uuid text, "
            + "amout real, "
            + "categoy integer, "
            + "recordType integer, "
            + "payWay integer, "
            + "remark text, "
            + "time integer, "
            + "date date)";

    public TDatabaseHeiper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_RECORD_DB);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


    //增加数据
    public void addRecord(TData data){
        SQLiteDatabase db =this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("uuid",data.getUuid());
        values.put("amout",data.getAmout());
        values.put("categoy",data.getCategoy());
        values.put("recordType",data.getRecordType());
        values.put("payWay",data.getPayWay());
        values.put("remark",data.getRemark());
        values.put("time",data.getTimeSampe());
        values.put("date",data.getDate());

        db.insert(DB_TABLE,null,values);

    }

    //删除数据
    public  void removeRecord(String uuid){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DB_TABLE,"uuid = ?",new String[]{ uuid });
    }

    //更新数据(先删除,在添加)
    public void upRecord(String uuid , TData newTData){
        removeRecord(uuid);
        newTData.setUuid(uuid);
        addRecord(newTData);
    }

    //查询数据按指定日期返回所有条目
    public LinkedList<TData> readRecord(String selectdate){
        LinkedList<TData> records = new LinkedList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select DISTINCT * from "+DB_TABLE+" where date = ? and recordType != ? order by time desc",new String[]{selectdate,"5"});
        if(cursor.moveToFirst()){
            do {
                  String uuid = cursor.getString(cursor.getColumnIndex("uuid"));
                  int amout = cursor.getInt(cursor.getColumnIndex("amout"));
                  int categoy = cursor.getInt(cursor.getColumnIndex("categoy"));
                  int recordType = cursor.getInt(cursor.getColumnIndex("recordType"));
                  int payWay = cursor.getInt(cursor.getColumnIndex("payWay"));
                  String remark = cursor.getString(cursor.getColumnIndex("remark"));
                  long timeStmp = cursor.getLong(cursor.getColumnIndex("time"));
                  String date = cursor.getString(cursor.getColumnIndex("date"));

                TData tData = new TData();
                tData.setUuid(uuid);
                tData.setAmout(amout);
                tData.setCategoy(categoy);
                tData.setRecordType(recordType);
                tData.setPayWay(payWay);
                tData.setRemark(remark);
                tData.setTimeSampe(timeStmp);
                tData.setDate(date);
                records.add(tData);
            }while (cursor.moveToNext());
        }
        cursor.close();
        return records;
    }

    //查询当月所有有记录的日期 返回位数与日期对应的amout数组
    public ArrayList<Integer> getAvaliableDate(String month){
        String  star = month.substring(0,7)+"-01";
        String  end = month.substring(0,7)+"-31";
        String pos ;

        ArrayList<String> dates = new ArrayList<>();
        ArrayList<Integer> amouts = new ArrayList<Integer>();
        for(int i=0;i<=31;i++){
            amouts.add(0);
        }

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select DISTINCT * from  "+DB_TABLE+"  where date >=? and date<=? and recordType!=? order by time asc",new String[]{star,end,"5"});
        if(cursor.moveToFirst()){
            do {
                int amout;
                int recordType = cursor.getInt(cursor.getColumnIndex("recordType"));
                if(recordType==1||recordType==3){
                    amout  = -cursor.getInt(cursor.getColumnIndex("amout"));

                }else{
                    amout  = cursor.getInt(cursor.getColumnIndex("amout"));
                }

                String date = cursor.getString(cursor.getColumnIndex("date"));

                if(String.valueOf(date.charAt(8)).equals("0")){
                    pos = String.valueOf(date.charAt(9));
                }else {
                    pos = date.substring(8,date.length());
                }


                if(!date.contains(date)){
                    dates.add(date);
                    amouts.set(Integer.valueOf(pos),amout);
                }else{
                    amouts.set(Integer.valueOf(pos),amouts.get(Integer.valueOf(pos))+amout);
                }

            }while (cursor.moveToNext());
        }

        cursor.close();
        return amouts;
    }

    //返回当天的收支总额
    public double readRecordsAmout(String selectdate){

        double recordsAmout=0;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select DISTINCT * from "+DB_TABLE+" where date = ? and recordType!=? order by time asc",new String[]{selectdate,"5"});
        if(cursor.moveToFirst()){
            do {
                int amout = cursor.getInt(cursor.getColumnIndex("amout"));
                int recordType = cursor.getInt(cursor.getColumnIndex("recordType"));

                if(recordType==1||recordType==3){
                    recordsAmout =recordsAmout-amout;
            }else{
                    recordsAmout =recordsAmout+amout;
                }

            }while (cursor.moveToNext());
        }
        cursor.close();
        return recordsAmout;
    }

    public LinkedList<TData> readRealBuget(){
        LinkedList<TData> records = new LinkedList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select DISTINCT * from "+DB_TABLE+" where payWay!=? and recordType = ? order by time desc",new String[]{"0","5"});
        if(cursor.moveToFirst()){
            do {
                String uuid = cursor.getString(cursor.getColumnIndex("uuid"));
                int amout = cursor.getInt(cursor.getColumnIndex("amout"));
                int categoy = cursor.getInt(cursor.getColumnIndex("categoy"));
                int recordType = cursor.getInt(cursor.getColumnIndex("recordType"));
                int payWay = cursor.getInt(cursor.getColumnIndex("payWay"));
                String remark = cursor.getString(cursor.getColumnIndex("remark"));
                long timeStmp = cursor.getLong(cursor.getColumnIndex("time"));
                String date = cursor.getString(cursor.getColumnIndex("date"));

                TData tData = new TData();
                tData.setUuid(uuid);
                tData.setAmout(amout);
                tData.setCategoy(categoy);
                tData.setRecordType(recordType);
                tData.setPayWay(payWay);
                tData.setRemark(remark);
                tData.setTimeSampe(timeStmp);
                tData.setDate(date);
                records.add(tData);
            }while (cursor.moveToNext());
        }
        cursor.close();
        return records;
    }

    public LinkedList<TData> readAllBuget(){
        LinkedList<TData> records = new LinkedList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select DISTINCT * from "+DB_TABLE+" where recordType = ? order by time desc",new String[]{"5"});
        if(cursor.moveToFirst()){
            do {
                String uuid = cursor.getString(cursor.getColumnIndex("uuid"));
                int amout = cursor.getInt(cursor.getColumnIndex("amout"));
                int categoy = cursor.getInt(cursor.getColumnIndex("categoy"));
                int recordType = cursor.getInt(cursor.getColumnIndex("recordType"));
                int payWay = cursor.getInt(cursor.getColumnIndex("payWay"));
                String remark = cursor.getString(cursor.getColumnIndex("remark"));
                long timeStmp = cursor.getLong(cursor.getColumnIndex("time"));
                String date = cursor.getString(cursor.getColumnIndex("date"));

                TData tData = new TData();
                tData.setUuid(uuid);
                tData.setAmout(amout);
                tData.setCategoy(categoy);
                tData.setRecordType(recordType);
                tData.setPayWay(payWay);
                tData.setRemark(remark);
                tData.setTimeSampe(timeStmp);
                tData.setDate(date);
                records.add(tData);
            }while (cursor.moveToNext());
        }
        cursor.close();
        return records;
    }

    //返回花呗的总额
    public double readBorrowBuget(){
        double borrowAll=0;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select DISTINCT * from "+DB_TABLE+" where payWay=? and recordType = ? order by time desc",new String[]{"3","5"});
        if(cursor.moveToFirst()){
            do {
                int amout = cursor.getInt(cursor.getColumnIndex("amout"));
                borrowAll=borrowAll+amout;
            }while (cursor.moveToNext());
        }
        cursor.close();
        return borrowAll;
    }

    public LinkedList<TData>  readBorrowBugetObject(){
        LinkedList<TData> records = new LinkedList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select DISTINCT * from "+DB_TABLE+" where payWay=? and recordType = ? order by time desc",new String[]{"3","5"});
        if(cursor.moveToFirst()){
            do {
                int amout = cursor.getInt(cursor.getColumnIndex("amout"));
                int recordType = cursor.getInt(cursor.getColumnIndex("recordType"));
                int payWay = cursor.getInt(cursor.getColumnIndex("payWay"));
                String remark = cursor.getString(cursor.getColumnIndex("remark"));

                TData tData = new TData();
                tData.setAmout(amout);
                tData.setRecordType(recordType);
                tData.setPayWay(payWay);
                tData.setRemark(remark);
                records.add(tData);




            }while (cursor.moveToNext());
        }
        cursor.close();
        return records;
    }


    public LinkedList<TData> readBorrow(){
        LinkedList<TData> records = new LinkedList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select DISTINCT * from "+DB_TABLE+" where recordType = ? and payWay=? order by time desc",new String[]{"1","3"});
        if(cursor.moveToFirst()){
            do {
                String uuid = cursor.getString(cursor.getColumnIndex("uuid"));
                int amout = cursor.getInt(cursor.getColumnIndex("amout"));
                int categoy = cursor.getInt(cursor.getColumnIndex("categoy"));
                int recordType = cursor.getInt(cursor.getColumnIndex("recordType"));
                int payWay = cursor.getInt(cursor.getColumnIndex("payWay"));
                String remark = cursor.getString(cursor.getColumnIndex("remark"));
                long timeStmp = cursor.getLong(cursor.getColumnIndex("time"));
                String date = cursor.getString(cursor.getColumnIndex("date"));

                TData tData = new TData();
                tData.setUuid(uuid);
                tData.setAmout(amout);
                tData.setCategoy(categoy);
                tData.setRecordType(recordType);
                tData.setPayWay(payWay);
                tData.setRemark(remark);
                tData.setTimeSampe(timeStmp);
                tData.setDate(date);
                records.add(tData);
            }while (cursor.moveToNext());
        }
        cursor.close();
        return records;
    }

    //查询返回所有条目
    public double readRealRecord(){
        double count=0;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select DISTINCT * from "+DB_TABLE+" where  recordType != ? order by time desc",new String[]{"5"});
        if(cursor.moveToFirst()){
            do {

                int amout = cursor.getInt(cursor.getColumnIndex("amout"));
                int recordType = cursor.getInt(cursor.getColumnIndex("recordType"));

                if(recordType==1||recordType==3){
                    count=count-amout;
                }else{
                    count=count+amout;
                }


            }while (cursor.moveToNext());
        }
        cursor.close();
        return count;
    }

    public LinkedList<TData> readReal(){
        LinkedList<TData> records = new LinkedList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select DISTINCT * from "+DB_TABLE+" where recordType = ? and payWay=? order by time desc",new String[]{"5","3"});
        if(cursor.moveToFirst()){
            do {
                String uuid = cursor.getString(cursor.getColumnIndex("uuid"));
                int amout = cursor.getInt(cursor.getColumnIndex("amout"));
                int categoy = cursor.getInt(cursor.getColumnIndex("categoy"));
                int recordType = cursor.getInt(cursor.getColumnIndex("recordType"));
                int payWay = cursor.getInt(cursor.getColumnIndex("payWay"));
                String remark = cursor.getString(cursor.getColumnIndex("remark"));
                long timeStmp = cursor.getLong(cursor.getColumnIndex("time"));
                String date = cursor.getString(cursor.getColumnIndex("date"));

                TData tData = new TData();
                tData.setUuid(uuid);
                tData.setAmout(amout);
                tData.setCategoy(categoy);
                tData.setRecordType(recordType);
                tData.setPayWay(payWay);
                tData.setRemark(remark);
                tData.setTimeSampe(timeStmp);
                tData.setDate(date);
                records.add(tData);
            }while (cursor.moveToNext());
        }
        cursor.close();
        return records;
    }

}
