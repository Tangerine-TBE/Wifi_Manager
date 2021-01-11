package com.example.module_tool.widget;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.example.module_tool.R;

public class DiyToolbar extends RelativeLayout {

    private TextView mTitle;
    private ImageView mIcon;
    private ImageView mRightIcon;
    private  RelativeLayout mBackground;
    private Activity mActivity;
    private boolean mIsShow=false;

    private OnShowOnClickListener mOnShowOnClickListener=null;

    public DiyToolbar(Context context) {
        this(context,null);
    }

    public DiyToolbar(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public DiyToolbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        intView(context);
        intEvent();

    }

    private void intEvent() {
        mRightIcon.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnShowOnClickListener != null) {
                    mOnShowOnClickListener.onShowClick();
                }
            }
        });
    }

    public void finishActivity(Activity  activity) {
        mActivity=activity;
        mIcon.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                mActivity.finish();
            }
        });
    }

    public void setTitle(String msg) {
        mTitle.setText(msg);
    }


    public void isShowRightIcon(boolean isShow) {
        mIsShow=isShow;
        mRightIcon.setVisibility(mIsShow?VISIBLE:GONE);
    }


    public void setOnShowOnClickListener(OnShowOnClickListener  listener) {
        this.mOnShowOnClickListener=listener;
    }


    public interface OnShowOnClickListener {
        void onShowClick();
    }



    private void intView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_mod_toolbar, this, true);
        mTitle=view.findViewById(R.id.toolbar_title);
        mIcon=view.findViewById(R.id.toolbar_icon);
        mRightIcon=view.findViewById(R.id.toolbar_right_icon);
        mBackground = view.findViewById(R.id.base_toolbar);
        isShowRightIcon(mIsShow);
    }



    public void setColorBackground(int Color) {
        mBackground.setBackgroundColor(Color);
    }


}
