package com.example.nan.tbook.CustomView;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.nan.tbook.R;

public class MyAppTitle extends LinearLayout {
    private OnLeftButtonClickListener mLeftButtonClickListener;
    private OnRightButtonClickListener mRightButtonClickListener;
    private MyViewHolder mViewHolder;
    private View viewAppTitle;

    public MyAppTitle(Context context)
    {
        super(context);

        init();
    }

    public MyAppTitle(Context context, AttributeSet attrs)
    {
        super(context, attrs);

        init();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public MyAppTitle(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);

        init();
    }

    private void init()
    {
        LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        viewAppTitle = inflater.inflate(R.layout.custom_tittle, null);
        this.addView(viewAppTitle, layoutParams);


        mViewHolder = new MyViewHolder(this);

        mViewHolder.llLeftGoBack.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                if (mLeftButtonClickListener != null)
                {
                    mLeftButtonClickListener.onLeftButtonClick(v);
                }
            }
        });

        mViewHolder.llRight.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                if (mRightButtonClickListener != null)
                {
                    mRightButtonClickListener.OnRightButtonClick(v);
                }
            }
        });
    }

    public void initViewsVisible(boolean isLeftButtonVisile,boolean isTitleSelectVisile, boolean isRightIconVisile, boolean isRightTitleVisile)
    {
        // 左侧返回
        mViewHolder.llLeftGoBack.setVisibility(isLeftButtonVisile ? View.VISIBLE : View.INVISIBLE);


        // 右侧返回图标,文字
        if (!isRightIconVisile && !isRightTitleVisile)
        {
            mViewHolder.llRight.setVisibility(View.GONE);
        }
        else
        {
            mViewHolder.llRight.setVisibility(View.VISIBLE);
        }

        mViewHolder.ivRightComplete.setVisibility(isRightIconVisile ? View.VISIBLE : View.GONE);
        mViewHolder.tvRightComplete.setVisibility(isRightTitleVisile ? View.VISIBLE : View.INVISIBLE);

//        中间标题二选一
        if(isTitleSelectVisile){
            mViewHolder.tvCenterTitle.setVisibility(View.GONE);
            mViewHolder.titleSelect.setVisibility(View.VISIBLE);
        }else{
            mViewHolder.tvCenterTitle.setVisibility(View.VISIBLE);
            mViewHolder.titleSelect.setVisibility(View.GONE);
        }

    }

    /**
     * 设置标题
     *
     * @param title
     */
    public void setAppTitle(String title)
    {
        if (!TextUtils.isEmpty(title))
        {
            mViewHolder.tvCenterTitle.setText(title);
        }
    }

    //设置右侧文本
    public void setRightTitle(String text)
    {
        if (!TextUtils.isEmpty(text))
        {
            mViewHolder.tvRightComplete.setText(text);
        }
    }

    //设置右侧图片
    public void setRightIcon(int sourceID)
    {
        mViewHolder.ivRightComplete.setImageResource(sourceID);
    }


    //为整个标题栏设置颜色
    public void setAppBackground(int color)
    {
        viewAppTitle.setBackgroundColor(color);
    }


    //外部设置监听调用
    public void setOnLeftButtonClickListener(OnLeftButtonClickListener listen)
    {
        mLeftButtonClickListener = listen;
    }

    public void setOnRightButtonClickListener(OnRightButtonClickListener listen)
    {
        mRightButtonClickListener = listen;
    }


    //内部接口 提供给外部设置监听
    public static abstract interface OnLeftButtonClickListener
    {
        public abstract void onLeftButtonClick(View v);
    }

    public static abstract interface OnRightButtonClickListener
    {
        public abstract void OnRightButtonClick(View v);
    }

    //找到控件
    static class MyViewHolder
    {
        LinearLayout llLeftGoBack;
        TextView tvCenterTitle;
        LinearLayout llRight;
        ImageView ivRightComplete;
        TextView tvRightComplete;
        LinearLayout titleSelect;


        public MyViewHolder(View v)
        {
            llLeftGoBack = (LinearLayout) v.findViewById(R.id.llLeftGoBack);

            tvCenterTitle = (TextView) v.findViewById(R.id.tvCenterTitle);
            titleSelect = (LinearLayout)v.findViewById(R.id.title_select);

            llRight = (LinearLayout) v.findViewById(R.id.llRight);
            ivRightComplete = (ImageView) v.findViewById(R.id.ivRightComplete);
            tvRightComplete = (TextView) v.findViewById(R.id.tvRightComplete);
        }
    }

}
