package com.feisukj.cleaning.view.spinner;

import android.content.Context;
import android.graphics.drawable.Drawable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.feisukj.cleaning.R;

import java.util.List;

/**
 * ArrayList<String> ,其中的string可以换成一个通用的实体，比如一个（int）id ,一个(string) value
 * author: zuo
 * date: 2017/11/29 15:05
 */

public class MySpinner {
    private Context mContext;
    private TextView mTextView;
    private List<String> mData;
    private OnMyClickItem onClickItem;

    public MySpinner(Context context , TextView textView , List<String> data) {
        this.mContext = context;
        this.mTextView = textView;
        this.mData = data;
    }
    public interface OnMyClickItem {
        void onClickItenData(int pos);
    }
    public void setOnMyClickItem(OnMyClickItem onClickItem){
        this.onClickItem=onClickItem;
    }
    public void showPopupWindow() {
        tvSetImg(mTextView, R.drawable.ic_icon_xjj);
        View view = LayoutInflater.from(mContext).inflate(R.layout.choose_pop_clean, null);
        final PopupWindow popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setTouchable(true);
        popupWindow.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.shape_popup_view));
        popupWindow.showAsDropDown(mTextView);
        popupWindow.setOnDismissListener(new PopupDismissListener());
        RecyclerView recyclerView = view.findViewById(R.id.rv_choose_pop);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        SpinnerChooseAdapter adapter = new SpinnerChooseAdapter(mContext, new SpinnerChooseAdapter.MyItemClickListener() {
            @Override
            public void onClick(View view) {
                int tag = (int) view.getTag();
                mTextView.setText(mData.get(tag));
                popupWindow.dismiss();
                if (onClickItem!=null){
                    onClickItem.onClickItenData(tag);
                }
            }
        }, mData);
        recyclerView.setAdapter(adapter);
//        recyclerView.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL_LIST));
    }

    /**
     * 弹窗消失的时候让箭头换回来
     */
    class PopupDismissListener implements PopupWindow.OnDismissListener {
        @Override
        public void onDismiss() {
            tvSetImg(mTextView, R.drawable.ic_icon_sjj);
        }

    }

    /**
     * 设置textView右侧的图像
     *
     * @param textView
     * @param img
     */
    private void tvSetImg(TextView textView, int img) {
        Drawable nav_up = mContext.getResources().getDrawable(img);
        nav_up.setBounds(0, 0, nav_up.getMinimumWidth(), nav_up.getMinimumHeight());
        textView.setCompoundDrawables(null, null, nav_up, null);
    }
}
