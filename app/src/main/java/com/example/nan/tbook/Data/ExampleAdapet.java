package com.example.nan.tbook.Data;

import android.content.Context;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.nan.tbook.Calender.Utils;
import com.example.nan.tbook.R;
import com.robinhood.ticker.TickerUtils;
import com.robinhood.ticker.TickerView;

import java.util.List;

/**
 * Created by Nan on 2019/5/20.
 */

public class ExampleAdapet extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {
    private List<TData> mRecordData;
    private Context context;
    private  boolean isLongChilk=false;
    private final int TITLE_LIST = 1;
    private final int BODY_LIST = 2;
    int beforeDo =0;
    private upDataOnClickListener upDataOnClickListener;



//    private String SelectUUID;
//
//    public String getSelectUUID() {
//        return SelectUUID;
//    }
//
//    public void setSelectUUID(String selectUUID) {
//        SelectUUID = selectUUID;
//    }

    public ExampleAdapet(List<TData> recordList) {
        mRecordData = recordList;
    }

    @Override
    public int getItemViewType(int position) {
        if(position==0){
                return TITLE_LIST;
        }else {
            return BODY_LIST;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if(viewType==TITLE_LIST){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.record_title, parent, false);
            ExampleAdapet.TitleViewHolder tltleholder = new ExampleAdapet.TitleViewHolder(view);
            return tltleholder;
        }else if(viewType==BODY_LIST){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.record_item, parent, false);
             ExampleAdapet.BodyViewHolder bodyViewholder = new ExampleAdapet.BodyViewHolder(view);
            return bodyViewholder;
        }
        return null;
    }


    //绑定数据源
    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        final TData record = mRecordData.get(position);

        if(holder instanceof BodyViewHolder){

                ((BodyViewHolder) holder).time.setText(Utils.getHour(record.getTimeSampe()));

                ((BodyViewHolder) holder).pay_way.setImageResource(GlobalUtil.getInstance().findPayIcon(record.getPayWay()));

                //判断收入支出
                if(record.getRecordType()== 1||record.getRecordType()== 3){
                    ((BodyViewHolder) holder).aomnt.setTextColor(Color.RED);
                    ((BodyViewHolder) holder).aomnt.setText("-"+record.getAmout());
                }else if(record.getRecordType()== 2||record.getRecordType()== 4){
                    ((BodyViewHolder) holder).aomnt.setTextColor(Color.GREEN);
                    ((BodyViewHolder) holder).aomnt.setText("+"+record.getAmout());
                }


                //消费图标部分
                if(record.getRecordType()==1){
                    ((BodyViewHolder) holder).categoy.setImageResource(GlobalUtil.getInstance().costRes.get(record.getCategoy()).resBigIcon);
                }else{
                    ((BodyViewHolder) holder).categoy.setImageResource(GlobalUtil.getInstance().earnRes.get(record.getCategoy()).resBigIcon);
                }

                //remark部分
                if(record.getRemark().equals("")){
                    if(record.getRecordType()==1){
                        ((BodyViewHolder) holder).remark.setText(GlobalUtil.getInstance().costRes.get(record.getCategoy()).title);
                    }else{
                        ((BodyViewHolder) holder).remark.setText(GlobalUtil.getInstance().earnRes.get(record.getCategoy()).title);
                    }

                }else{
                    ((BodyViewHolder) holder).remark.setText(record.getRemark());
                }

                //判断显示删除按钮
                if(isLongChilk){
                    ((BodyViewHolder) holder).del_botton.setVisibility(View.VISIBLE);
                    ((BodyViewHolder) holder).updata_botton.setVisibility(View.VISIBLE);
                }else{
                    ((BodyViewHolder) holder).del_botton.setVisibility(View.GONE);
                    ((BodyViewHolder) holder).updata_botton.setVisibility(View.GONE);
                }

                //删除按钮响应
                ((BodyViewHolder) holder).del_botton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        GlobalUtil.getInstance().tDatabaseHeiper.removeRecord(mRecordData.get(position).getUuid());
                        upDataOnClickListener.onDel(position);
                        removeData(position);
                    }
                });


                //点击背景响应
                ((BodyViewHolder) holder).item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if( ((BodyViewHolder) holder).del_botton.getVisibility()==View.VISIBLE){
                            isLongChilk=false;
                            notifyDataSetChanged();
                        }else{
                        }
                        upDataOnClickListener.onCanelClick(position);
                    }
                });

                //更新按钮响应
                if(upDataOnClickListener!=null){
                    ((BodyViewHolder) holder).updata_botton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            upDataOnClickListener.onClick(position);
                        }
                    });
                }

            }






        if(holder instanceof TitleViewHolder){
            ((TitleViewHolder) holder).amount.setText(record.getAmout()+"");
        }
    }


    @Override
    public int getItemCount() {
        return  mRecordData.size();
    }

    public class BodyViewHolder extends RecyclerView.ViewHolder {
        private TextView time;
        private ImageView pay_way;
        private ImageView categoy;
        private TextView remark;
        private TextView aomnt;
        private FloatingActionButton del_botton;
        private FloatingActionButton updata_botton;
        private LinearLayout item;

       public BodyViewHolder(View view) {
            super(view);
            time = (TextView) view.findViewById(R.id.time);
            pay_way=(ImageView)view.findViewById(R.id.sec_item_icon);
            categoy=(ImageView) view.findViewById(R.id.categoy);
            remark=(TextView)view.findViewById(R.id.remark);
            aomnt= (TextView)view.findViewById(R.id.record_aomnt);
            del_botton=(FloatingActionButton) view.findViewById(R.id.del_button);
            item = (LinearLayout)view.findViewById(R.id.item);
            updata_botton=(FloatingActionButton) view.findViewById(R.id.updata_button);
        }
    }

    public class TitleViewHolder extends RecyclerView.ViewHolder {
        final private TickerView amount;



        public TitleViewHolder(View view) {
            super(view);
            amount = (TickerView) view.findViewById(R.id.amount);
            amount.setCharacterLists(TickerUtils.provideNumberList());

        }
    }

    //   在list中添加数据，并通知条目加入一条
    public void addData(int position,TData data) {
        beforeDo = mRecordData.size();

        mRecordData.add(position, data);
        //添加动画

        mRecordData.get(0).setAmout(GlobalUtil.getInstance().tDatabaseHeiper.readRecordsAmout(data.getDate()));
        notifyItemInserted(position);
        notifyItemChanged(0);

    }

    // 删除数据
    public void removeData(int position) {
        beforeDo=mRecordData.size();

        if(mRecordData.get(position).getRecordType()==1||mRecordData.get(position).getRecordType()==3){
            mRecordData.get(0).setAmout(mRecordData.get(0).getAmout()+mRecordData.get(position).getAmout());
        }else {
            mRecordData.get(0).setAmout(mRecordData.get(0).getAmout()-mRecordData.get(position).getAmout());
        }

        mRecordData.remove(position);
        //删除动画
        notifyItemRemoved(position);
        notifyDataSetChanged();

        if(beforeDo==2){
            isLongChilk=false;
            beforeDo=1;
        }
    }

    //修改数据
    public void updata(int lastposition,TData newdata){
        removeData(lastposition);
        addData(lastposition,newdata);

    }


    public interface upDataOnClickListener {
        void onClick(int position);
        void onCanelClick(int position);
        void onDel(int position);
    }

    public void setUpDataOnClickListener(upDataOnClickListener onClickListener) {
        this.upDataOnClickListener = onClickListener;
    }

    public void initBeforeDo(){
        beforeDo=mRecordData.size();
    }

    public int getbeforeDo(){
        return beforeDo;
    }

    public List<TData> getmRecordData() {
        return mRecordData;
    }

    public void setLongChilk(boolean longChilk) {
        isLongChilk = longChilk;
    }

}
