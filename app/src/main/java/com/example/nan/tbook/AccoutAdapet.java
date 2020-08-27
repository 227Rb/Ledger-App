package com.example.nan.tbook;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.nan.tbook.Data.GlobalUtil;
import com.example.nan.tbook.Data.TData;

import java.util.List;

public class AccoutAdapet extends RecyclerView.Adapter<AccoutAdapet.AccoutViewHolder> {
    private Context context;
    private LayoutInflater mInflater;
    private List<TData> mRecordData;
    private  boolean isLongChilk=false;


    public void setmRecordData(List<TData> mRecordData) {
        this.mRecordData = mRecordData;
    }

    public void setLongChilk(boolean longChilk) {
        isLongChilk = longChilk;
    }

    public AccoutAdapet(List<TData> accountList) {
        mRecordData = accountList;
    }


    @Override
    public AccoutViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sec_item, parent, false);
        AccoutAdapet.AccoutViewHolder accoutViewHolder = new AccoutViewHolder(view);
        return accoutViewHolder;
    }

    @Override
    public void onBindViewHolder(AccoutViewHolder holder, final int position) {
        final TData accout = mRecordData.get(position);

        holder.payIcon.setImageResource(GlobalUtil.getInstance().findPayIcon(accout.getPayWay()));

        holder.payName.setText(GlobalUtil.getInstance().findPayName(accout.getPayWay()));

        holder.payRemark.setText(accout.getRemark());

        holder.aoumnt.setText(accout.getAmout()+"");

        if(isLongChilk){
            holder.del_botton.setVisibility(View.VISIBLE);
        }else{
            holder.del_botton.setVisibility(View.GONE);
        }

        holder.del_botton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GlobalUtil.getInstance().tDatabaseHeiper.removeRecord(mRecordData.get(position).getUuid());
                removeData(position);
            }
        });

       holder.item.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               isLongChilk=false;
               notifyDataSetChanged();
           }
       });

    }



    @Override
    public int getItemCount() {
        return mRecordData.size();
    }

    public class AccoutViewHolder extends RecyclerView.ViewHolder {

        private ImageView payIcon;
        private TextView payName;
        private TextView payRemark;
        private TextView aoumnt;
        private FloatingActionButton del_botton;
        private RelativeLayout item;

        public AccoutViewHolder(View view) {
            super(view);

            payIcon=(ImageView) view.findViewById(R.id.sec_item_icon);
            payName=(TextView)view.findViewById(R.id.sec_item_name);
            payRemark= (TextView)view.findViewById(R.id.sec_pay_ramark);
            aoumnt = (TextView) view.findViewById(R.id.sec_item_amout);
            del_botton = (FloatingActionButton)view.findViewById(R.id.third_del_buttom);
            item=(RelativeLayout)view.findViewById(R.id.sec_item);
        }
    }


    //   在list中添加数据，并通知条目加入一条
    public void addData(int position,TData data) {
        mRecordData.add(position, data);
        //添加动画
        notifyItemInserted(position);
        notifyItemChanged(0);
    }

    // 删除数据
    public void removeData(int position) {
        mRecordData.remove(position);
        //删除动画
        notifyItemRemoved(position);
        notifyDataSetChanged();
    }

    //修改数据
    public void updata(int lastposition,TData newdata){
        removeData(lastposition);
        addData(lastposition,newdata);

    }

}
