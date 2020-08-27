package com.example.nan.tbook;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.example.nan.tbook.Data.CategoryResBean;
import com.example.nan.tbook.Data.GlobalUtil;
import java.util.LinkedList;

public class CategoyAdapet extends RecyclerView.Adapter<CategoyAdapet.CategoyViewHolder> {
    private Context mContext;
    private LayoutInflater mInflater;
    private LinkedList<CategoryResBean> cellList = GlobalUtil.getInstance().costRes;
    public String selected =" ";
    public int selectedID =0;
    private OnCategoryOnClickListen mOnCategoryOnClickListen;


    public void setmOnCategoryOnClickListen(OnCategoryOnClickListen mOnCategoryOnClickListen) {
        this.mOnCategoryOnClickListen = mOnCategoryOnClickListen;
    }

    public CategoyAdapet(Context context) {
        this.mContext = context;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public CategoyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.cell_catrgory,parent,false);
        CategoyViewHolder categoyViewHolder = new CategoyViewHolder(view);
        return categoyViewHolder;
    }

    @Override
    public void onBindViewHolder(final CategoyViewHolder holder, final int position) {
        final  CategoryResBean res = cellList.get(position);
        holder.categoyName.setText(res.title);
        holder.categoyImage.setImageResource(res.resBigIcon);

//        holder.item.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                selected =res.title;
//                 notifyDataSetChanged();
//
//            }
//        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selected =res.title;
                notifyDataSetChanged();
                selectedID=res.id;
                if(mOnCategoryOnClickListen!=null){
                    mOnCategoryOnClickListen.OnClick(position);
                }
            }
        });

        if(holder.categoyName.getText().toString().equals(selected)){
            holder.item.setBackgroundResource(R.drawable.bg_button);
        }else{
            holder.item.setBackgroundResource(0);
        }
    }

    @Override
    public int getItemCount() {
        return cellList.size();
    }


    public class CategoyViewHolder extends RecyclerView.ViewHolder {
        private TextView categoyName;
        private ImageView categoyImage;
        private RelativeLayout item;

        public CategoyViewHolder(View view) {
            super(view);
            categoyName = (TextView) view.findViewById(R.id.cell_name);
            categoyImage=(ImageView)view.findViewById(R.id.cell_image);
            item = (RelativeLayout) view.findViewById(R.id.cell_bg);

        }
    }

    public void setCellList(LinkedList<CategoryResBean> cellList) {
        this.cellList = cellList;
    }

    public interface OnCategoryOnClickListen{
        void OnClick(int position);
    }

}
