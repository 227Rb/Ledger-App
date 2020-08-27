package com.example.nan.tbook;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.nan.tbook.Data.AccountResBean;
import com.example.nan.tbook.Data.GlobalUtil;
import java.util.List;

public class PayWayAdapet extends RecyclerView.Adapter<PayWayAdapet.PayWayViewHolder> {
    private Context mContext;
    private LayoutInflater mInflater;
    private List<AccountResBean> dataList;
    private ItemOnClickListener mIermOnClickListener;

    public void setIermOnClickListener(ItemOnClickListener iermOnClickListener) {
        mIermOnClickListener = iermOnClickListener;
    }

    public PayWayAdapet(List<AccountResBean> accountList) {
        dataList = accountList;
    }

    @Override
    public PayWayViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.third_item_new, parent, false);
        PayWayAdapet.PayWayViewHolder payWayViewHolder = new PayWayViewHolder(view);
        return payWayViewHolder;
    }

    public void setList(List<AccountResBean> dataList){
        this.dataList=dataList;
    }

    @Override
    public void onBindViewHolder(PayWayViewHolder holder, final int position) {
        final  AccountResBean res = dataList.get(position);
        holder.accountName.setText(res.title);
        holder.accountIcon.setImageResource(res.accountIcon);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIermOnClickListener.onClick(position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class PayWayViewHolder extends RecyclerView.ViewHolder {
        private TextView accountName;
        private ImageView accountIcon;

        public PayWayViewHolder(View view) {
            super(view);
            accountName = (TextView) view.findViewById(R.id.third_item_name);
            accountIcon=(ImageView)view.findViewById(R.id.third_item_icon);
        }
    }

    public interface ItemOnClickListener {
        void onClick(int position);
    }

}
