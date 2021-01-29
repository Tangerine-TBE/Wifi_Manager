package com.example.module_user.utils;

import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import com.example.module_base.utils.LogUtils;
import com.example.module_base.utils.SPUtil;
import com.example.module_user.R;
import com.example.module_user.domain.login.LoginBean;
import com.google.gson.Gson;
import com.tamsiree.rxkit.RxTimeTool;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class UserInfoUtil {

    public static void removeAdView(boolean hidden,FrameLayout frameLayout){
        if (SPUtil.getInstance().getInt(Constants.USER_VIP_LEVEL)>0) {
            if (!hidden) frameLayout.removeAllViews();
        }
    }

    public static Map<String, String> saveUserType(String type, String number, String pwd, String openId) {
        Map<String, String> mUserInfo = new HashMap<>();
        mUserInfo.put(Constants.USER_ID_TYPE, type);
        mUserInfo.put(Constants.USER_ACCOUNT, number);
        mUserInfo.put(Constants.USER_PWD, pwd);
        mUserInfo.put(Constants.USER_THIRDLY_OPENID, openId);
        return mUserInfo;
    }


    public static void saveUserInfo(LoginBean loginBean, Map<String, String> userInfo) {
        SPUtil.getInstance().putBoolean(Constants.USER_IS_LOGIN, true)
                .putString(Constants.USER_ID, loginBean.getData().getId() + "")
                .putInt(Constants.USER_VIP_LEVEL, loginBean.getData().getVip())
                .putString(Constants.USER_VIP_TIME, loginBean.getData().getVipexpiretime())
                .putString(Constants.USER_ID_TYPE, userInfo.get(Constants.USER_ID_TYPE))
                .putString(Constants.USER_ACCOUNT, userInfo.get(Constants.USER_ACCOUNT))
                .putString(Constants.USER_PWD, userInfo.get(Constants.USER_PWD))
                .putString(Constants.USER_THIRDLY_OPENID, userInfo.get(Constants.USER_THIRDLY_OPENID))
                .putLong(Constants.USER_LOGIN_TIME, System.currentTimeMillis());

    }


    public static void saveUserMsg(LoginBean loginBean){
        SPUtil.getInstance().putString(Constants.USER_INFO,new Gson().toJson(loginBean));
    }

    public static void deleteUserMsg(){
        SPUtil.getInstance().putString(Constants.USER_INFO,"");
    }




    public static void deleteUserInfo() {
        SPUtil.getInstance().putString(Constants.USER_ID, "")
                .putInt(Constants.USER_VIP_LEVEL, 0)
                .putString(Constants.USER_VIP_TIME, "")
                .putString(Constants.USER_ID_TYPE, "")
                .putString(Constants.USER_ACCOUNT, "")
                .putString(Constants.USER_PWD, "")
                .putString(Constants.USER_THIRDLY_OPENID, "")
                .putBoolean(Constants.USER_IS_LOGIN, false);
    }


    private static final int sLoginTime = 1;
    public static boolean loginTimeOut() {
        String vipTime = SPUtil.getInstance().getString(Constants.USER_VIP_TIME);
        boolean isLogin = SPUtil.getInstance().getBoolean(Constants.USER_IS_LOGIN, false);
        if (isLogin &!TextUtils.isEmpty(vipTime)) {
            long endTime= RxTimeTool.string2Milliseconds(vipTime);
            Date startDate = new Date(endTime);
            Date stopDate = new Date(System.currentTimeMillis());
            // 这样得到的差值是微秒级别
            long diff = stopDate.getTime() - startDate.getTime();
            long days = diff / (1000 * 60 * 60 * 24);
            LogUtils.i( "----------------loginTimeOut-------------------->"+days);
            if (days > sLoginTime) {
                deleteUserInfo();
                return true;
            }
        }
        return false;
    }

    public static boolean  isVIP() {
        return SPUtil.getInstance().getInt(Constants.USER_VIP_LEVEL,0) > 0 ? true : false;

    }



}
